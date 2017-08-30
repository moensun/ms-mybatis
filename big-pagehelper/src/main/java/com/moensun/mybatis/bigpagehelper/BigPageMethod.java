package com.moensun.mybatis.bigpagehelper;

/**
 * Created by Bane.Shi.
 * Copyright Vortex
 * User: Bane.Shi
 * Date: 2017/8/30
 * Time: 11:16
 */
public abstract class BigPageMethod {
    protected static final ThreadLocal<BigPage> LOCAL_BIG_PAGE = new ThreadLocal<BigPage>();

    /**
     * 设置 Page 参数
     *
     * @param page
     */
    public static void setLocalPage(BigPage page) {
        LOCAL_BIG_PAGE.set(page);
    }

    /**
     * 获取 Page 参数
     *
     * @return
     */
    public static <T> BigPage<T> getLocalPage() {
        return LOCAL_BIG_PAGE.get();
    }

    /**
     * 移除本地变量
     */
    public static void clearPage() {
        LOCAL_BIG_PAGE.remove();
    }

    public static <E> BigPage<E> startPage(int pageNum, int pageSize,int navSize) {
        BigPage<E> page = new BigPage<E>(pageNum, pageSize, navSize);
        setLocalPage(page);
        return page;
    }


}
