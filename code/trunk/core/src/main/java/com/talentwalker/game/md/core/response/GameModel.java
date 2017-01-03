/**
 * @Title: GameModelMap.java
 * @Copyright (C) 2016 太能沃可
 * @Description:
 * @Revision History:
 * @Revision 1.0 2016年6月1日  占志灵
 */

package com.talentwalker.game.md.core.response;

import java.util.Map;

import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @ClassName: GameModelMap
 * @Description: Description of this class
 * @author <a href="mailto:XXX@talentwalker.com">占志灵</a> 于 2016年6月1日 上午10:25:43
 */

public class GameModel {

    private ModelMap model;

    /** Indicates whether or not this instance has been cleared with a call to {@link #clear()} */
    private boolean cleared = false;

    public GameModel() {
    }

    /**
     * Return the underlying {@code ModelMap} instance (never {@code null}).
     */
    public ModelMap getModelMap() {
        if (this.model == null) {
            this.model = new ModelMap();
        }
        return this.model;
    }

    /**
     * Return the model map. Never returns {@code null}.
     * To be called by application code for modifying the model.
     */
    public Map<String, Object> getModel() {
        return getModelMap();
    }

    /**
     * Add an attribute to the model.
     * @param attributeName name of the object to add to the model
     * @param attributeValue object to add to the model (never {@code null})
     * @see ModelMap#addAttribute(String, Object)
     * @see #getModelMap()
     */
    public GameModel addObject(String attributeName, Object attributeValue) {
        getModelMap().addAttribute(attributeName, attributeValue);
        return this;
    }

    /**
     * Add an attribute to the model using parameter name generation.
     * @param attributeValue the object to add to the model (never {@code null})
     * @see ModelMap#addAttribute(Object)
     * @see #getModelMap()
     */
    public GameModel addObject(Object attributeValue) {
        getModelMap().addAttribute(attributeValue);
        return this;
    }

    /**
     * Add all attributes contained in the provided Map to the model.
     * @param modelMap a Map of attributeName -> attributeValue pairs
     * @see ModelMap#addAllAttributes(Map)
     * @see #getModelMap()
     */
    public GameModel addAllObjects(Map<String, ?> modelMap) {
        getModelMap().addAllAttributes(modelMap);
        return this;
    }

    /**
     * Clear the state of this ModelAndView object.
     * The object will be empty afterwards.
     * <p>Can be used to suppress rendering of a given ModelAndView object
     * in the {@code postHandle} method of a HandlerInterceptor.
     * @see #isEmpty()
     * @see HandlerInterceptor#postHandle
     */
    public void clear() {
        this.model = null;
        this.cleared = true;
    }

    /**
     * Return whether this ModelAndView object is empty,
     * i.e. whether it does not hold any view and does not contain a model.
     */
    public boolean isEmpty() {
        return (CollectionUtils.isEmpty(this.model));
    }

    /**
     * Return whether this ModelAndView object is empty as a result of a call to {@link #clear}
     * i.e. whether it does not hold any view and does not contain a model.
     * <p>Returns {@code false} if any additional state was added to the instance
     * <strong>after</strong> the call to {@link #clear}.
     * @see #clear()
     */
    public boolean wasCleared() {
        return (this.cleared && isEmpty());
    }

    /**
     * Return diagnostic information about this model and view.
     */
    @Override
    public String toString() {
        return this.model.toString();
    }

    public Object getModel(String key) {
        return getModelMap().get(key);
    }

}
