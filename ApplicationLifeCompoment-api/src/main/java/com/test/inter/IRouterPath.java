package com.test.inter;

import com.test.bean.RouterBean;

import java.util.Map;

public interface IRouterPath {
    /**
     *
     * @return
     */
    public Map<String,RouterBean> loadMate();
}
