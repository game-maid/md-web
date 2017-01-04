/**
 * @Title: UuidUtils.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月14日  占志灵
 */

package com.talentwalker.game.md.core.util;

import java.util.UUID;

/**
 * @ClassName: UuidUtils
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月14日 下午7:02:36
 */

public class UuidUtils {

    private static int id = 0;

    private static Object obj = new Object();

    /**
     * @return 返回小写uuid，去"-"
     */
    public static String getUuid() {
        String str = UUID.randomUUID().toString();
        str = str.replaceAll("-", "").toLowerCase();
        return str;
    }

    public static long getId() {
        synchronized (obj) {
            id += 1;
            return (((long) (1/* serverId */& 0xFFFF)) << 48)
                    | (((System.currentTimeMillis() / 1000) & 0x00000000FFFFFFFFl) << 16)
                    | (id & 0x0000FFFF);
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
//            System.out.println(getId());
            getId();
        }
        System.out.println(System.currentTimeMillis() - start);
    }

}
