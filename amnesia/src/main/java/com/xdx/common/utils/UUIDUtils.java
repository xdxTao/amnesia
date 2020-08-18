package com.xdx.common.utils;

import java.util.UUID;

/**
 * UUID工具类
 *
 * @author 小道仙
 * @date 2020年5月5日
 */
public class UUIDUtils {

    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
            "Z" };

    /**
     * 返回一个去-的UUID
     *
     * @author 小道仙
     * @date 2020年5月5日
     */
    public static String getUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-","");
    }

    /**
     * 生成相应数字的唯一码
     *
     * @param length
     *            生成的长度
     * @return
     * @author 小道仙
     * @date 2020年5月5日
     * @version 1.0
     */
    public static String getUUID(Integer length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 1, l = length / 8; i <= l; i++) {
            sb.append(getShortUUID(8));
        }
        sb.append(getShortUUID(length % 8));
        return sb.toString();
    }

    private static String getShortUUID(Integer length) {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < length; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

}
