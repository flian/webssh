package org.lotus.carp.webssh.config.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.lotus.carp.webssh.config.exception.BusinessException;
import org.lotus.carp.webssh.config.service.WebSshLoginService;
import org.lotus.carp.webssh.config.service.vo.PropertiesConfigUser;
import org.lotus.carp.webssh.config.service.vo.WebSshLoginResultVo;
import org.lotus.carp.webssh.config.service.vo.WebSshLoginVo;
import org.lotus.carp.webssh.config.websocket.config.WebSshConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <h3>javaWebSSH</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-01-31 11:13
 **/
@Slf4j
public class DefaultWebSshLoginServiceImpl implements WebSshLoginService, InitializingBean {


    @Resource
    private WebSshConfig webSshConfig;

    private List<PropertiesConfigUser> allowedUsers = new ArrayList<>();

    /**
     * cache token and login object.
     */
    Cache<Object, Object> tokenCache;

    @Override
    public WebSshLoginResultVo doWebSshLogin(WebSshLoginVo webSshLoginVo) {
        if (ObjectUtils.isEmpty(allowedUsers)) {
            log.info("there is no config user.., please consider config webSsh.allowedUsers.");
            return null;
        }
        if (ObjectUtils.isEmpty(webSshLoginVo.getUsername())) {
            log.info("please input username");
            throw new BusinessException("username is needed.");
        }
        Optional<PropertiesConfigUser> matchOne = allowedUsers.stream().filter(u -> u.getUsername().equals(webSshLoginVo.getUsername())).findFirst();
        if (matchOne.isPresent()) {
            PropertiesConfigUser configUser = matchOne.get();
            //password not set or password ok.
            if (ObjectUtils.isEmpty(configUser.getPassword())
                    || configUser.getPassword().equals(webSshLoginVo.getPassword())) {
                if (isIpValidForGivenUser(webSshLoginVo.getRequestIp(), configUser.getAllowedIps())) {
                    //user match.
                    return genOneTokenAndCache(webSshLoginVo);
                }
            }
        }
        return null;
    }

    private boolean isIpValidForGivenUser(String userIpAddr, String allowedIps) {
        if (ObjectUtils.isEmpty(userIpAddr)) {
            return true;
        }
        if (ObjectUtils.isEmpty(allowedIps) || webSshConfig.getAllowAnyIpChar().equals(allowedIps)) {
            return true;
        }
        if (allowedIps.contains(userIpAddr)) {
            return true;
        }
        return false;
    }

    private WebSshLoginResultVo genOneTokenAndCache(WebSshLoginVo user) {
        WebSshLoginResultVo result = new WebSshLoginResultVo();
        result.setUserName(user.getUsername());
        result.setToken(genTokenForUser(null));
        Date expirationDate = genExpirationDate();
        long expirationTms = expirationDate.getTime();
        result.setExpired(formatNormalStr(expirationDate));
        result.setExpirationTms(expirationTms);
        result.setUserIpAddr(user.getRequestIp());
        setCache(result.getToken(), result, webSshConfig.getTokenExpiration());
        return result;
    }

    public void setCache(String token, Object cacheObject, long cacheTimeInHour) {
        tokenCache.put(token, cacheObject);
    }

    public <T> T getCacheObj(String token) {
        return (T) tokenCache.getIfPresent(token);
    }

    private Date genExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, webSshConfig.getTokenExpiration());
        return calendar.getTime();
    }

    private String formatNormalStr(Date date) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sd.format(date);
    }

    @Override
    public Boolean isTokenValid(String token) {
        if(!webSshConfig.isShouldVerifyToken()){
            log.info("ignore token verify.");
            return true;
        }
        if(ObjectUtils.isEmpty(token)){
            return false;
        }
        WebSshLoginResultVo cacheObject = getCacheObj(token);
        return null == cacheObject ? Boolean.FALSE : Boolean.TRUE;
    }


    private void setAllowedUsers(String usersConfig) {
        if (!ObjectUtils.isEmpty(usersConfig)) {
            String[] users = usersConfig.split(webSshConfig.getUserDelimiter());
            if (!ObjectUtils.isEmpty(users)) {
                Arrays.stream(users).forEach(u -> {
                    String[] fields = u.split(webSshConfig.getUserFieldDelimiter());
                    if (!ObjectUtils.isEmpty(fields)) {
                        PropertiesConfigUser user = new PropertiesConfigUser();
                        user.setUsername(fields[0]);
                        if (fields.length >= 2) {
                            user.setPassword(fields[1]);
                        }
                        if (fields.length >= 3) {
                            user.setAllowedIps(fields[2]);
                        }
                        allowedUsers.add(user);
                    }
                });
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setAllowedUsers(webSshConfig.getAllowedUsers());
        tokenCache = CacheBuilder.newBuilder().maximumSize(10000).expireAfterAccess(webSshConfig.getTokenExpiration(), TimeUnit.HOURS).build();
    }
}
