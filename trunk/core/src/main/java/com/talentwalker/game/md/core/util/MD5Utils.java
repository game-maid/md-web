
package com.talentwalker.game.md.core.util;

import java.security.MessageDigest;

/**
 * @ClassName: MD5Utils
 * @Description: Description of this class
 * @author <a href="mailto:zhanzhiling@talentwalker.com">占志灵</a> 于 2016年10月18日 下午2:53:00
 */

public class MD5Utils {
    /**
     * 32位MD5加密（小写）
     * 
     * @param param 待加密字符串
     * @return 加密后的字符串
     * @throws Exception RuntimeException
     */
    public static String md5Encode(String str) {
        StringBuffer result = new StringBuffer();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes("UTF-8"));
            byte[] b = md5.digest();
            for (int i = 0; i < b.length; ++i) {
                int x = b[i] & 0xFF;
                int h = x >>> 4;
                int l = x & 0x0F;
                result.append((char) (h + ((h < 10) ? '0' : 'a' - 10)));
                result.append((char) (l + ((l < 10) ? '0' : 'a' - 10)));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result.toString();
    }
}
