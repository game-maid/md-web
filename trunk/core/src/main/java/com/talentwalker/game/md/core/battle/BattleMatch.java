/**
 * @Title: BattleTimer.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月23日  占志灵
 */

package com.talentwalker.game.md.core.battle;

import java.util.ArrayDeque;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import com.talentwalker.game.md.core.battle.impl.TestBattleLord;

/**
 * @ClassName: BattleTimer
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月23日 下午4:07:20
 */

public class BattleMatch {
    private static final Logger log = LoggerFactory.getLogger(BattleMatch.class);
    private static final ArrayDeque<WebSocketSession> sessions = new ArrayDeque<WebSocketSession>();
    private static Timer gameTimer = null;
    private static final long TICK_DELAY = 100;

    static {
        startTimer();
    }

    public static synchronized void add(WebSocketSession webSocketSession) {
        sessions.offer(webSocketSession);
    }

    public static synchronized void remove(WebSocketSession webSocketSession) {
        sessions.remove(webSocketSession);
    }

    public static synchronized void match() {
        if (sessions.size() >= 2) {
            WebSocketSession session1 = sessions.poll();
            WebSocketSession session2 = sessions.poll();
            TestBattleLord lord1 = new TestBattleLord(session1, "昆哥");
            TestBattleLord lord2 = new TestBattleLord(session2, "平爷");
            Battle battle = BattleFactory.createBattle(lord1, lord2);
            session1.getAttributes().put("lord", lord1);
            session2.getAttributes().put("lord", lord2);
            session1.getAttributes().put("battle", battle);
            session2.getAttributes().put("battle", battle);
            battle.start();
        }
    }

    public static void startTimer() {
        gameTimer = new Timer("BattleMatchTimer");
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    match();
                } catch (Throwable ex) {
                    log.error("Caught to prevent timer from shutting down", ex);
                }
            }
        }, TICK_DELAY, TICK_DELAY);
    }
}
