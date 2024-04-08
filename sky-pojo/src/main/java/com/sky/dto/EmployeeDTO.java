package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("管理端 员工信息")
public class EmployeeDTO implements Serializable {

    private Long id;

    @ApiModelProperty("必须 用户名")
    private String username;

    @ApiModelProperty("必须 员工姓名")
    private String name;

    @ApiModelProperty("必须 电话")
    private String phone;

    @ApiModelProperty("必须 性别")
    private String sex;

    @ApiModelProperty("必须 身份证号码")
    private String idNumber;

}
