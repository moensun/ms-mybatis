package com.moensun.mybatis.bigpagehelper;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Bane.Shi.
 * Copyright Vortex
 * User: Bane.Shi
 * Date: 2017/8/30
 * Time: 11:17
 */
public class BigPage<E> extends ArrayList<E> implements Closeable {

    /**
     * 页码，从1开始
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;
    /**
     * 导航页码数
     */
    private int navSize;
    /**
     * 起始行
     */
    private int startRow;

    private int querySize;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getNavSize() {
        return navSize;
    }

    public void setNavSize(int navSize) {
        this.navSize = navSize;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getQuerySize() {
        return querySize;
    }

    public void setQuerySize(int querySize) {
        this.querySize = querySize;
    }

    public BigPage(int pageNum, int pageSize) {
        this(pageNum,pageSize,5);
    }

    public BigPage(int pageNum, int pageSize, int navSize) {
        super(0);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.navSize = navSize;
        calculateStartRowAndSelectSize();
    }

    /**
     * 计算起始条数，以及需要查询的条数
     */
    private void calculateStartRowAndSelectSize(){
        this.startRow = this.pageNum > 0 ? (this.pageNum - 1) * this.pageSize : 0;
        int queryPages = (this.pageNum <= (this.navSize/2 + this.pageSize%2) )?(this.navSize-pageNum+1) : (this.navSize/2+this.navSize%2);
        if(queryPages < 2){ queryPages =2;}
        this.querySize = queryPages * this.pageSize;
    }

    @Override
    public void close() throws IOException {
        BigPageHelper.clearPage();
    }
}
