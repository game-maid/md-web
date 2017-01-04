
package com.talentwalker.game.md.core.response;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: FriendInfo
 * @Description: Description of this class
 * @author <a href="mailto:zhanzhiling@talentwalker.com">占志灵</a> 于 2016年8月2日 下午3:16:43
 */

public class FriendInfo {
    /**
     * 好友列表
     */
    private List<LordInfo> friends = new ArrayList<>();
    /**
     * 请求加我为好友的列表
     */
    private List<LordInfo> requests = new ArrayList<>();
    /**
     * 我请求加好友的列表
     */
    private List<LordInfo> requesteds = new ArrayList<>();

    /**
     * @return friends
     */
    public List<LordInfo> getFriends() {
        return friends;
    }

    /**
     * @param friends 要设置的 friends
     */
    public void setFriends(List<LordInfo> friends) {
        this.friends = friends;
    }

    /**
     * @return requests
     */
    public List<LordInfo> getRequests() {
        return requests;
    }

    /**
     * @param requests 要设置的 requests
     */
    public void setRequests(List<LordInfo> requests) {
        this.requests = requests;
    }

    /**
     * @return requesteds
     */
    public List<LordInfo> getRequesteds() {
        return requesteds;
    }

    /**
     * @param requesteds 要设置的 requesteds
     */
    public void setRequesteds(List<LordInfo> requesteds) {
        this.requesteds = requesteds;
    }
}
