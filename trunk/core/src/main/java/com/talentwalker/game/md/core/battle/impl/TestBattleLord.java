/**
 * @Title: BattleLord.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月15日  占志灵
 */

package com.talentwalker.game.md.core.battle.impl;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.talentwalker.game.md.core.battle.BattleHero;
import com.talentwalker.game.md.core.battle.BattleLord;

/**
 * @ClassName: BattleLord
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月15日 上午10:35:18
 */

public class TestBattleLord implements BattleLord {
    private WebSocketSession session;
    private String name;

    public TestBattleLord(WebSocketSession socketSession, String name) {
        this.session = socketSession;
        this.name = name;
    }

    /**.
     * <p>Title: hasNextHero</p>
     * <p>Description: </p>
     * @return
     * @see com.talentwalker.game.md.core.battle.BattleLord#hasNextHero()
     */
    @Override
    public boolean hasNextHero() {
        // TODO Auto-generated method stub
        return false;
    }

    /**.
     * <p>Title: peekNextHero</p>
     * <p>Description: </p>
     * @return
     * @see com.talentwalker.game.md.core.battle.BattleLord#peekNextHero()
     */
    @Override
    public BattleHero peekNextHero() {
        // TODO Auto-generated method stub
        return null;
    }

    /**.
     * <p>Title: pollNextHero</p>
     * <p>Description: </p>
     * @return
     * @see com.talentwalker.game.md.core.battle.BattleLord#pollNextHero()
     */
    @Override
    public BattleHero pollNextHero() {
        // TODO Auto-generated method stub
        return null;
    }

    /**.
     * <p>Title: refreshActionSort</p>
     * <p>Description: </p>
     * @see com.talentwalker.game.md.core.battle.BattleLord#refreshActionSort()
     */
    @Override
    public void refreshActionSort() {
        // TODO Auto-generated method stub

    }

    /**.
     * <p>Title: refresh</p>
     * <p>Description: </p>
     * @see com.talentwalker.game.md.core.battle.BattleLord#refresh()
     */
    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }

    /**.
     * <p>Title: sendMessage</p>
     * <p>Description: </p>
     * @param message
     * @see com.talentwalker.game.md.core.battle.BattleLord#sendMessage(java.lang.String)
     */
    @Override
    public void sendMessage(String message) throws Exception {
        this.session.sendMessage(new TextMessage(message));
    }

    /**.
     * <p>Title: getName</p>
     * <p>Description: </p>
     * @see com.talentwalker.game.md.core.battle.BattleLord#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**.
     * <p>Title: close</p>
     * <p>Description: </p>
     * @throws IOException 
     * @see com.talentwalker.game.md.core.battle.BattleLord#close()
     */
    @Override
    public void close() throws IOException {
        session.close();
    }

}
