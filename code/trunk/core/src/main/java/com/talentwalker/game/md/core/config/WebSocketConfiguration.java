/**
 * @Title: WebSocketConfiguration.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月2日  占志灵
 */

package com.talentwalker.game.md.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.talentwalker.game.md.core.battle.BattleWebSocketHandler;

/**
 * @ClassName: WebSocketConfiguration
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月2日 下午8:34:18
 */
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    /**.
     * <p>Title: registerWebSocketHandlers</p>
     * <p>Description: </p>
     * @param registry
     * @see org.springframework.web.socket.config.annotation.WebSocketConfigurer#registerWebSocketHandlers(org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry)
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(battleWebSocketHandler(), "/battle").withSockJS();
    }

    @Bean
    public WebSocketHandler battleWebSocketHandler() {
        return new BattleWebSocketHandler();
    }
}
