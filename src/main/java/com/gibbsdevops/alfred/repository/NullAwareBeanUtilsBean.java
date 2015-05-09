package com.gibbsdevops.alfred.repository;

import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;

public class NullAwareBeanUtilsBean extends BeanUtilsBean {

    @Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if (value == null) return;
        if ("id".equals(name) || "version".equals(name)) return;
        super.copyProperty(dest, name, value);
    }

}
