package com.leaves.service;

import com.leaves.entity.WbeParameter;

public interface WbeParameterService {


    /**
     * 修改
     *
     * @param wbeParameter
     * @return
     */
    boolean editById(WbeParameter wbeParameter);

    /**
     * 得到网站设置数据
     *
     * @return
     */
    WbeParameter getWbeParameter();


}
