/**
 * @Title: Condition.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月17日 闫昆
 */

package com.talentwalker.game.md.core.repository.support;

import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.talentwalker.game.md.core.util.GameExceptionUtils;

/**
 * @ClassName: Condition
 * @Description: 查询条件
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月17日 下午12:14:07
 */

public class Condition {

    /**
     * EQ：等于；LIKE：模糊查询；LT：小于；LTE：小于等于；GT：大于；GTE：大于等于
     */
    public enum Operator {
        EQ, LIKE, LT, GT, LTE, GTE;
    }

    public enum PropertyType {
        S(String.class), I(Integer.class), L(Long.class), D(Date.class),;

        private Class<?> clazz;

        private PropertyType(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getValue() {
            return clazz;
        }
    }

    private Object value;

    private Operator operator;

    private Condition(String match, String value) {
        String operator = StringUtils.substring(match, 0, match.length() - 1);
        String propertyType = StringUtils.substring(match, match.length() - 1);
        try {
            this.operator = Enum.valueOf(Operator.class, operator);
        } catch (Exception e) {
            GameExceptionUtils.throwException("Unknow operator type [" + operator + "]");
        }

        Class<?> clazz = null;
        try {
            clazz = Enum.valueOf(PropertyType.class, propertyType).getValue();
        } catch (Exception e) {
            GameExceptionUtils.throwException("Unknow property type [" + propertyType + "]");
        }

        try {
            // 日期类型工具类不支持，自己处理
            if ("D".equals(propertyType)) {
                this.value = DateUtils.parseDate(value, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
            } else if ("L".equals(propertyType)) {
                // 如果是日期字符串转换成long，自己处理
                if (StringUtils.isNumeric(value)) {
                    this.value = ConvertUtils.convert(value, clazz);
                } else {
                    this.value = DateUtils.parseDate(value, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd").getTime();
                }
            } else {
                this.value = ConvertUtils.convert(value, clazz);
            }
        } catch (Exception e) {
            GameExceptionUtils.throwException("Convert value faild:" + e.getMessage());
        }
    }

    public static Condition newCondition(String operator, String value) {
        return new Condition(operator, value);
    }

    /**
     * @return value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value 要设置的 value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * @return operator
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * @param operator 要设置的 operator
     */
    public void setOperator(Operator operator) {
        this.operator = operator;
    }

}
