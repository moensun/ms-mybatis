/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.moensun.mybatis.bigpagehelper.dialect;

import com.moensun.mybatis.bigpagehelper.BigPage;
import com.moensun.mybatis.bigpagehelper.BigPageHelper;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Properties;

/**
 * 针对 PageHelper 的实现
 *
 * @author liuzh
 * @since 2016-12-04 14:32
 */
public abstract class AbstractHelperDialect extends AbstractDialect {

    /**
     * 获取分页参数
     *
     * @param <T>
     * @return
     */
    public <T> BigPage<T> getLocalPage() {
        return BigPageHelper.getLocalPage();
    }

    @Override
    public final boolean skip(MappedStatement ms, Object parameterObject, RowBounds rowBounds) {
        //该方法不会被调用
        return true;
    }


    @Override
    public Object processParameterObject(MappedStatement ms, Object parameterObject, BoundSql boundSql, CacheKey pageKey) {
        return parameterObject;
    }

    @Override
    public boolean beforePage(MappedStatement ms, Object parameterObject, RowBounds rowBounds) {
        BigPage page = getLocalPage();
        if (page.getPageSize() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public String getPageSql(MappedStatement ms, BoundSql boundSql, Object parameterObject, RowBounds rowBounds, CacheKey pageKey) {
        String sql = boundSql.getSql();
        BigPage page = getLocalPage();
        return getPageSql(sql, page, pageKey);
    }

    /**
     * 单独处理分页部分
     *
     * @param sql
     * @param page
     * @param pageKey
     * @return
     */
    public abstract String getPageSql(String sql, BigPage page, CacheKey pageKey);

    @Override
    public Object afterPage(List pageList, Object parameterObject, RowBounds rowBounds) {
        BigPage page = getLocalPage();
        if (page == null) {
            return pageList;
        }
        page.addAll(pageList);
        return page;
    }

    @Override
    public void afterAll() {

    }

    @Override
    public void setProperties(Properties properties) {

    }
}
