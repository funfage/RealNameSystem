package com.real.name.common.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by jim.z.hu on 2018/7/11.
 */
@Getter
@Setter
public class APIRequestBase implements Serializable {
    /**
     * 当前页码
     */
    private Integer pageIndex;

    /**
     * 每页显示数量
     */
    private Integer pageSize;
}
