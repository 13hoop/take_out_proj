package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class EmployeePageQueryDTO implements Serializable {

    //员工姓名
    @ApiModelProperty("可选 用户名查询key")
    private String name;

    //页码
    @ApiModelProperty("必须 页面")
    private int page;

    //每页显示记录数
    @ApiModelProperty("必须 没有数目")
    private int pageSize;

}
