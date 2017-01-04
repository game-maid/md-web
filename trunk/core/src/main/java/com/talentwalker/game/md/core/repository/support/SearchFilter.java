/**
 * @Title: SearchFilter.java
 * @Copyright (C) 2016 Ykun
 * @Description:
 * @Revision 1.0 2016年6月13日 闫昆
 */

package com.talentwalker.game.md.core.repository.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;

import com.talentwalker.game.md.core.util.GameExceptionUtils;
import com.talentwalker.game.md.core.util.ServletUtils;

/**
 * @ClassName: SearchFilter
 * @Description: 扩展分页条件查询，request中key命名规则：search_property_operator
 *  例如查询username = "yankun" ----》  search_username_eq
 *  operator 支持 eq、lt、lte、gt、gte
 *  目前仅支持字符串相等的自动处理和数字类型的大小判断
 * @author <a href="mailto:yy.ykun@qq.com">闫昆</a> 于 2016年6月13日 下午5:08:12
 */

public class SearchFilter {

    private static final String PREFIX_SEARCH = "search";
    private static final String SPLIT = "_";
    private static final String PAGE_LIMIT = "limit";
    private static final String PAGE_OFFSET = "offset";
    private static final String PAGE_SORT = "sort";
    private static final String PAGE_ORDER = "order";

    private List<Criteria> criteriaList = new ArrayList<Criteria>();

    private Pageable pageable;

    private Map<String, List<Condition>> conditionMap = new HashMap<String, List<Condition>>();

    private SearchFilter(final HttpServletRequest request, final Sort sort) {
        buildPageRequest(request, sort);
        buildCriteria(request);
    }

    public static SearchFilter newSearchFilter() {
        return new SearchFilter(ServletUtils.getRequest(), null);
    }

    public static SearchFilter newSearchFilter(final Sort sort) {
        return new SearchFilter(ServletUtils.getRequest(), sort);
    }

    /**
     * @return criteriaList
     */
    public List<Criteria> getCriteriaList() {
        return criteriaList;
    }

    /**
     * @return pageable
     */
    public Pageable getPageable() {
        return pageable;
    }

    public void addCriteria(Criteria criteria) {
        this.criteriaList.add(criteria);
    }

    private void addCriteria(String property, List<Condition> list) {
        Criteria criteria = new Criteria(property);
        for (Condition condition : list) {
            switch (condition.getOperator()) {
            case EQ:
                criteria.is(condition.getValue());
                break;
            case LT:
                criteria.lt(condition.getValue());
                break;
            case LTE:
                criteria.lte(condition.getValue());
                break;
            case GT:
                criteria.gt(condition.getValue());
                break;
            case GTE:
                criteria.gte(condition.getValue());
                break;
            case LIKE:
                criteria.regex(condition.getValue().toString());
                break;
            }
        }
        this.criteriaList.add(criteria);
    }

    private void buildPageRequest(HttpServletRequest request, Sort sort) {
        int offset = Integer.parseInt(request.getParameter(PAGE_OFFSET));
        int size = Integer.parseInt(request.getParameter(PAGE_LIMIT));
        int page = offset / size;
        String sortName = request.getParameter(PAGE_SORT);
        if (!StringUtils.isEmpty(sortName)) {
            String order = request.getParameter(PAGE_ORDER);
            if ("asc".equals(order)) {
                sort = new Sort(Direction.ASC, sortName);
            } else {
                sort = new Sort(Direction.DESC, sortName);
            }
        }
        if (sort != null) {
            this.pageable = new PageRequest(page, size, sort);
        } else {
            this.pageable = new PageRequest(page, size);
        }

    }

    private void buildCriteria(HttpServletRequest request) {
        Map<String, Object> params = ServletUtils.getParametersStartWith(request, PREFIX_SEARCH + SPLIT);
        Iterator<String> it = params.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            String[] k = StringUtils.split(key, SPLIT);
            if (k.length != 2) {
                GameExceptionUtils
                        .throwException("Search key must format: search_property_operator; eg: search_username_eq!");
            }
            String property = k[0];
            String operator = k[1];
            Object value = params.get(key);
            if (null == value) {
                continue;
            }
            if (value instanceof String) {
                if (StringUtils.isNotEmpty(value.toString())) {
                    this.addCondition(property, Condition.newCondition(operator, value.toString()));
                }
            }
        }
        buildCriteria();
    }

    private void buildCriteria() {
        Iterator<String> it = this.conditionMap.keySet().iterator();
        while (it.hasNext()) {
            String property = it.next();
            List<Condition> list = this.conditionMap.get(property);
            this.addCriteria(property, list);
        }
    }

    private void addCondition(String property, Condition condition) {
        List<Condition> list = new ArrayList<Condition>();
        if (this.conditionMap.containsKey(property)) {
            list = this.conditionMap.get(property);
        }
        list.add(condition);
        this.conditionMap.put(property, list);
    }

}
