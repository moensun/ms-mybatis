package com.moensun.mybatis.bigpagehelper;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bane.Shi.
 * Copyright Vortex
 * User: Bane.Shi
 * Date: 2017/8/30
 * Time: 11:58
 */
public class BigPageInfo<T> implements Serializable {
    public BigPageInfo(List<T> list) {
        if(list instanceof BigPage){
            BigPage page = (BigPage) list;
        }
    }
}
