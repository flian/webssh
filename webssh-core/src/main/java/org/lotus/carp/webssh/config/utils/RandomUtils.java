package org.lotus.carp.webssh.config.utils;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <h3>webssh</h3>
 * <p>random uitls</p>
 *
 * @author : foy
 * @date : 2024-02-29 17:45
 **/
public class RandomUtils {

    private static Random random = new SecureRandom();

    public static void main(String[] args) {
        Long saltLong = System.currentTimeMillis();
        String str = getMD5("加盐MD5", String.valueOf(saltLong));
        System.out.println(str);
        System.out.println(getPassWord(str));
    }

    //用于加密的字符
    private static final char md5String[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N'
            , 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c'
            , 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r'
            , 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    public static final char[] chars = {'!', '@', ')', '(', '&', '~', '$', '#', '*', '?', '+', '-'};

    /**
     * 单次MD5加密函数
     *
     * @param pwd
     * @return java.lang.String
     * @throws
     * @Author 李
     * @MethodName MD5
     * @Description //单次MD5加密函数
     * @Date 15:51 2020/9/25
     **/
    public static String getMD5(String pwd) {
        try {
            //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
            byte[] btInput = pwd.getBytes();
            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(btInput);
            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            byte[] md = mdInst.digest();

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 4];
            int k = 0;
            for (int i = 0; i < j; i++) {   //  i = 0
                byte byte0 = md[i];  //95
                str[k++] = md5String[byte0 >>> 4 & 0x3d];    //    5
                str[k++] = md5String[byte0 & 0x3d];   //   z
                int n = k + 30;
                str[n++] = md5String[byte0 >>> 3 & 0x3d];
                str[n++] = md5String[byte0 >>> 2 & 0x3d];
            }
            //返回经过加密后的字符串
            return new String(str);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     *
     * @Author 李
     * @MethodName saltMD5
     * @Description //加盐MD5
     * @Date 15:51 2020/9/25
     * @param str
     * @param salt
     * @return java.lang.String
     * @exception
     **/
    public static String getMD5(String password, String salt) {
        return getMD5(getMD5(password) + getMD5(salt));
    }

    public static String getPassWord(String passWord){
        return getPassWord(passWord,12);
    }
    /**
     * 功能描述：随机密码生成器
     *
     * @param passWord
     * @return java.lang.String
     * @throws
     * @Author 李
     * @MethodName getPassWord
     * @Date 10:02 2020/11/17
     **/
    public static String getPassWord(String passWord,int length) {
        String prefix = passWord.substring(0, 6);
        String suffix = passWord.substring(passWord.length() - 6);
        passWord = prefix + suffix;
        char[] charPassWord = passWord.toCharArray();
        List<Integer> intKeys = new ArrayList<>(16);
        List<String> letterKeys = new ArrayList<>(16);
        int intKey = 0;
        int letterKey = 0;
        for (int i = 0; i < charPassWord.length; i++) {
            intKeys.add(i, null);
            letterKeys.add(i, null);
            if (charPassWord[i] < '9' && charPassWord[i] >= '0') {
                intKeys.add(i, Integer.parseInt(String.valueOf(charPassWord[i])));
                intKey++;
            }
            if ((charPassWord[i] <= 'Z' && charPassWord[i] >= 'A') ||
                    (charPassWord[i] <= 'z' && charPassWord[i] >= 'a')) {
                letterKeys.add(i, String.valueOf(charPassWord[i]));
                letterKey++;
            }
        }
        intKey = getRandom(intKey);
        letterKey = getRandom(letterKey);
        int ik = 0;
        int il = 0;
        for (int i = 0; i < charPassWord.length; i++) {
            //抽选部分数字转为特殊字符
            if (intKeys != null && intKeys.size() > 0 && ik <= intKey) {
                if (intKeys.get(i) != null && Integer.parseInt(String.valueOf(charPassWord[i])) == intKeys.get(i)) {
                    ik++;
                    charPassWord[i] = getHash(i);
                }
            }
            //抽选部分字母大小写转换
            if (letterKeys != null && letterKeys.size() > 0 && il < letterKey) {
                if (letterKeys.get(i) != null && String.valueOf(charPassWord[i]).equals(letterKeys.get(i))) {
                    il++;
                    if (charPassWord[i] >= 'a' && charPassWord[i] <= 'z') {
                        charPassWord[i] -= 32;
                    } else {
                        charPassWord[i] += 32;
                    }
                    if (letterKey - intKey >= 4 && i >= 6) {//当数字过少时，抽选部分字母转成特殊字符
                        charPassWord[i / 2] = getHash(i);
                    }
                }
            }
        }
        charPassWord = shuffle(charPassWord);//洗牌算法
        String result = String.valueOf(charPassWord);
        return result;
    }

    public static int getRandom(int key) {
        if (key == 0) {
            return 0;
        }
        key = new Random().nextInt(key);
        return key;
    }

    public static char getHash(int c) {
        return chars[c];
    }

    public static char[] shuffle(char[] c) {//洗牌算法
        for (int i = 0; i < c.length; i++) {
            int j = random.nextInt(c.length - 1);
            if (c[i] != c[j] && !String.valueOf(c[i]).equals(String.valueOf(c[j]))) {
                char tem = c[i];
                c[i] = c[j];
                c[j] = tem;
            }
        }
        return c;
    }


}
