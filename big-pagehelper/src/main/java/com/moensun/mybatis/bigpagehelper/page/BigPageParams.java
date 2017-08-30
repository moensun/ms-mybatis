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

package com.moensun.mybatis.bigpagehelper.page;

import com.moensun.mybatis.bigpagehelper.BigPage;
import com.moensun.mybatis.bigpagehelper.BigPageHelper;
import com.moensun.mybatis.bigpagehelper.util.BigPageObjectUtil;
import com.moensun.mybatis.bigpagehelper.util.StringUtil;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

/**
 * Page 参数信息
 *
 * @author liuzh
 */
public class BigPageParams {
    //RowBounds参数offset作为PageNum使用 - 默认不使用
    protected boolean offsetAsPageNum = false;
    //RowBounds是否进行count查询 - 默认不查询
    protected int navSize = 5;
    //当设置为true的时候，如果pagesize设置为0（或RowBounds的limit=0），就不执行分页，返回全部结果
    protected boolean pageSizeZero = false;
    //分页合理化
    protected boolean reasonable = false;
    //是否支持接口参数来传递分页参数，默认false
    protected boolean supportMethodsArguments = false;
    //默认count(0)
    protected String countColumn = "0";

    /**
     * 获取分页参数
     *
     * @param parameterObject
     * @param rowBounds
     * @return
     */
    public BigPage getPage(Object parameterObject, RowBounds rowBounds) {
        BigPage page = BigPageHelper.getLocalPage();
        if (page == null) {
            if (rowBounds != RowBounds.DEFAULT) {
                if (offsetAsPageNum) {
                    page = new BigPage(rowBounds.getOffset(), rowBounds.getLimit(),navSize );
                } else {

                }
            } else if(supportMethodsArguments){
                try {
                    page = BigPageObjectUtil.getPageFromObject(parameterObject, false);
                } catch (Exception e) {
                    return null;
                }
            }
            if(page == null){
                return null;
            }
            BigPageHelper.setLocalPage(page);
        }

        return page;
    }

    public void setProperties(Properties properties) {
        //offset作为PageNum使用
        String offsetAsPageNum = properties.getProperty("offsetAsPageNum");
        this.offsetAsPageNum = Boolean.parseBoolean(offsetAsPageNum);
        //RowBounds方式是否做count查询
        String rowBoundsWithCount = properties.getProperty("rowBoundsWithCount");

        //当设置为true的时候，如果pagesize设置为0（或RowBounds的limit=0），就不执行分页
        String pageSizeZero = properties.getProperty("pageSizeZero");
        this.pageSizeZero = Boolean.parseBoolean(pageSizeZero);
        //分页合理化，true开启，如果分页参数不合理会自动修正。默认false不启用
        String reasonable = properties.getProperty("reasonable");
        this.reasonable = Boolean.parseBoolean(reasonable);
        //是否支持接口参数来传递分页参数，默认false
        String supportMethodsArguments = properties.getProperty("supportMethodsArguments");
        this.supportMethodsArguments = Boolean.parseBoolean(supportMethodsArguments);
        //默认count列
        String countColumn = properties.getProperty("countColumn");
        if(StringUtil.isNotEmpty(countColumn)){
            this.countColumn = countColumn;
        }
        //当offsetAsPageNum=false的时候，不能
        //参数映射
        BigPageObjectUtil.setParams(properties.getProperty("params"));
    }

    public boolean isOffsetAsPageNum() {
        return offsetAsPageNum;
    }


    public boolean isPageSizeZero() {
        return pageSizeZero;
    }

    public boolean isReasonable() {
        return reasonable;
    }

    public boolean isSupportMethodsArguments() {
        return supportMethodsArguments;
    }

    public String getCountColumn() {
        return countColumn;
    }
}
