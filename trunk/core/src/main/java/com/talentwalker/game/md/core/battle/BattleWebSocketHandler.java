/**
 * @Title: WebsocketHandler.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月2日  占志灵
 */

package com.talentwalker.game.md.core.battle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @ClassName: WebsocketHandler
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月2日 下午8:35:46
 */

public class BattleWebSocketHandler extends TextWebSocketHandler {
    private static Logger log = LoggerFactory.getLogger(BattleWebSocketHandler.class);

    /**.
     * <p>Title: afterConnectionEstablished</p>
     * <p>Description: </p>
     * @param session
     * @throws Exception
     * @see org.springframework.web.socket.handler.AbstractWebSocketHandler#afterConnectionEstablished(org.springframework.web.socket.WebSocketSession)
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        BattleMatch.add(session);
    }

    /**.
     * <p>Title: afterConnectionClosed</p>
     * <p>Description: </p>
     * @param session
     * @param status
     * @throws Exception
     * @see org.springframework.web.socket.handler.AbstractWebSocketHandler#afterConnectionClosed(org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.CloseStatus)
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        BattleMatch.remove(session);
    }

    /**.
     * <p>Title: handleTextMessage</p>
     * <p>Description: </p>
     * @param session
     * @param message
     * @throws Exception
     * @see org.springframework.web.socket.handler.AbstractWebSocketHandler#handleTextMessage(org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.TextMessage)
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Battle battle = (Battle) session.getAttributes().get("battle");
        BattleLord lord = (BattleLord) session.getAttributes().get("lord");
        log.info("【" + lord.getName() + "】放了一个大招");
        battle.insert("【" + lord.getName() + "】放了一个大招");
    }
}
