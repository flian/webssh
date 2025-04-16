package org.lotus.carp.webssh.navicat.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h3>webssh</h3>
 * <p></p>
 *
 * @author : foy
 * @date : 2024-05-08 14:55
 **/
@Configuration
@Order(Integer.MIN_VALUE)
@Slf4j
public class QuercusConfig {
    @Bean
    public ViewResolver resolver(){
        QuercusViewResolver quercusViewResolver = new QuercusViewResolver();
        //quercusViewResolver.setPrefix("php");
        //quercusViewResolver.setSuffix(".php");
        quercusViewResolver.setViewNames("*.php");
        quercusViewResolver.setOrder(0);
        return quercusViewResolver;
    }

    @Bean
    @ConditionalOnMissingBean({MongoClient.class})
    @ConditionalOnProperty(name = "webssh.init.mongoclient.onstartup",matchIfMissing = true)
    @Order(value = 123988)
    public MongoClient mongo(ObjectProvider<MongoClientSettingsBuilderCustomizer> builderCustomizers, MongoClientSettings settings) {
        MongoClient result = null;
        //add here to avoid exception on startup when web ssh sever has no mongodb on localhost
        try{
            result = (new MongoClientFactory(builderCustomizers.orderedStream().collect(Collectors.toList()))).createMongoClient(settings);
        }catch (Exception e){
            log.error("error while create mongoClient.",e);
        }
        return result;
    }
}
