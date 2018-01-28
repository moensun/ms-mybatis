package com.moensun.mybatis.bigpagehelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Bane.Shi.
 * Copyright Vortex
 * User: Bane.Shi
 * Date: 2017/8/30
 * Time: 11:58
 */
public class BigPageInfo<T> implements Serializable {

    //当前页
    private int pageNum;
    //每页的数量
    private int pageSize;
    //当前页的数量
    private int size;

    //由于startRow和endRow不常用，这里说个具体的用法
    //可以在页面中"显示startRow到endRow 共size条数据"

    //当前页面第一个元素在数据库中的行号
    private int startRow;
    //当前页面最后一个元素在数据库中的行号
    private int endRow;

    //结果集
    private List<T> list;

    //前一页
    private int prePage;
    //下一页
    private int nextPage;

    //是否为第一页
    private boolean isFirstPage = false;
    //是否为最后一页
    private boolean isLastPage = false;
    //是否有前一页
    private boolean hasPreviousPage = false;
    //是否有下一页
    private boolean hasNextPage = false;
    //导航页码数
    private int navigatePages;
    //所有导航页号
    private int[] navigatePageNumArray;
    //导航条上的第一页
    private int navigateFirstPage;
    //导航条上的最后一页
    private int navigateLastPage;

    private boolean hasMore = false;

    public BigPageInfo(List<T> list) {
        if(list instanceof BigPage){
            BigPage page = (BigPage) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();

            this.navigatePages = page.getNavSize();

            this.size = page.size();
            //由于结果是>startRow的，所以实际的需要+1
            if (this.size == 0) {
                this.startRow = 0;
                this.endRow = 0;
            } else {
                this.startRow = page.getStartRow() + 1;
                //计算实际的endRow（最后一页的时候特殊）
                this.endRow = this.startRow - 1 + this.size;
            }
        }else if (list instanceof Collection) {
            this.pageNum = 1;
            this.pageSize = list.size();
            this.size = list.size();
            this.startRow = 0;
            this.endRow = list.isEmpty() ? 0 : list.size() - 1;
        }

        if(list instanceof Collection){
            resList(list);
            calcnavigatePageNumArray(list);
        }

    }

    private void resList(List<T> list){
        if(this.pageSize >= list.size()){
            this.list = list;
        }else {
            List<T> res = new ArrayList<>();
            for (int i = 0 ;i< this.pageSize;i++){
                res.add(list.get(i));
            }
            this.list = res;
        }
    }

    private void calcnavigatePageNumArray(List<T> list){
        int listSize = list.size();
        int pageCount = listSize/this.pageSize + ((listSize%this.pageSize>0)?1:0);
        int queryPages = (this.pageNum<=(this.pageSize/2+this.pageSize%2))?(this.pageSize-this.pageNum+2):(this.pageSize/2+this.pageSize%2+1);
        if(pageCount >= queryPages){
            this.hasMore = true;
        }

        if( this.navigatePages/2 >= this.pageNum){
            this.navigateFirstPage = 1;
        }else {
            this.navigateFirstPage = (this.pageNum - this.navigatePages/2);
        }

        //分页显示的最后页数，分查询页数有超出和无超出两种情况
       // this.navigateLastPage = this.pageNum - 1 + (this.hasMore?(this.navigatePages/2+this.navigatePages%2):pageCount);
        this.navigateLastPage = this.pageNum - 1 + (this.hasMore?(queryPages-1):pageCount);

        if( pageCount < (this.navigatePages/2+this.navigatePages%2) ){
            this.navigateFirstPage = this.navigateFirstPage - (this.navigatePages/2+this.navigatePages%2 - pageCount);
            if(this.navigateFirstPage < 1){
                this.navigateFirstPage = 1;
            }
        }

        int navCount = this.navigateLastPage-this.navigateFirstPage;

        if(this.pageNum>1){
            this.hasPreviousPage = true;
        }
        if(pageCount>1){
            this.hasNextPage = true;
        }

        this.navigatePageNumArray = new int[navCount+1];
        for (int i = 0;i <= navCount;i++){
            this.navigatePageNumArray[i] = this.navigateFirstPage+i;
        }
    }

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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean firstPage) {
        isFirstPage = firstPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int[] getNavigatePageNumArray() {
        return navigatePageNumArray;
    }

    public void setNavigatePageNumArray(int[] navigatePageNumArray) {
        this.navigatePageNumArray = navigatePageNumArray;
    }

    public int getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public void setNavigateFirstPage(int navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public int getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateLastPage(int navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
