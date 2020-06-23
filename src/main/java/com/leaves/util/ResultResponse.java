package com.leaves.util;

import lombok.Data;

/**
 * Resultful风格返回参数  单数据
 *
 * @author
 * @create 2019/10/6 18:21
 * @since 1.0.0
 */
@Data
public class ResultResponse {
    /**
     * 状态码:默认为0,
     */
    private int code;
    /**
     * 状态
     */
    private boolean status=false;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 返回数据
     */
    private Object obje;

    public void setCode(int code) {
        if (code == 0) {
            this.status = true;
        }
        this.code = code;
    }
}