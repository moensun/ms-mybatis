package com.moensun.mybatis.bigpagehelper;

import com.moensun.mybatis.bigpagehelper.dialect.AbstractHelperDialect;
import com.moensun.mybatis.bigpagehelper.page.BigPageAutoDialect;
import com.moensun.mybatis.bigpagehelper.page.BigPageParams;
import com.moensun.mybatis.bigpagehelper.util.StringUtil;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Properties;

/**
 * Created by Bane.Shi.
 * Copyright Vortex
 * User: Bane.Shi
 * Date: 2017/8/30
 * Time: 11:15
 */
public class BigPageHelper extends BigPageMethod implements Dialect {

    private BigPageParams pageParams;
    private BigPageAutoDialect autoDialect;

    @Override
    public boolean skip(MappedStatement ms, Object parameterObject, RowBounds rowBounds) {
        BigPage page = pageParams.getPage(parameterObject, rowBounds);
        if (page == null) {
            return true;
        } else {
            autoDialect.initDelegateDialect(ms);
            return false;
        }
    }

    @Override
    public Object processParameterObject(MappedStatement ms, Object parameterObject, BoundSql boundSql, CacheKey pageKey) {
        return autoDialect.getDelegate().processParameterObject(ms, parameterObject, boundSql, pageKey);
    }

    @Override
    public boolean beforePage(MappedStatement ms, Object parameterObject, RowBounds rowBounds) {
        return autoDialect.getDelegate().beforePage(ms, parameterObject, rowBounds);
    }

    @Override
    public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds, CacheKey pageKey) {
        return autoDialect.getDelegate().getPageSql(ms, boundSql, parameterObject, rowBounds, pageKey);
    }

    public String getPageSql(String sql, BigPage page, RowBounds rowBounds, CacheKey pageKey) {
        return autoDialect.getDelegate().getPageSql(sql, page, pageKey);
    }

    @Override
    public Object afterPage(List pageList, Object parameterObject, RowBounds rowBounds) {
        AbstractHelperDialect delegate = autoDialect.getDelegate();
        if(delegate != null){
            return delegate.afterPage(pageList, parameterObject, rowBounds);
        }
        return pageList;
    }

    @Override
    public void afterAll() {
        //这个方法即使不分页也会被执行，所以要判断 null
        AbstractHelperDialect delegate = autoDialect.getDelegate();
        if (delegate != null) {
            delegate.afterAll();
            autoDialect.clearDelegate();
        }
        clearPage();
    }

    @Override
    public void setProperties(Properties properties) {
        pageParams = new BigPageParams();
        autoDialect = new BigPageAutoDialect();
        pageParams.setProperties(properties);
        autoDialect.setProperties(properties);
    }
}
