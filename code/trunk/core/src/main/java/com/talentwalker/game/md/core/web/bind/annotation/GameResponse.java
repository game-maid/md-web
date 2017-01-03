/**
 * @Title: GameResponse.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年5月12日 闫昆
 */

package com.talentwalker.game.md.core.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: GameResponse
 * @Description: 自定义注解，返回值交给GameRequestResponseBodyMethodProcessor处理
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年5月12日 下午9:10:23
 */

@Target({ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GameResponse {

}
