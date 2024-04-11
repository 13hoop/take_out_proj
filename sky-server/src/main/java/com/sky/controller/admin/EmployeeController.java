package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工,管理员等相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    @ApiOperation("更新用户信息")
    @PutMapping
    public Result<Employee> update(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeService.update(employeeDTO);
        return Result.success();
    }

    /**
     * 查找用户详情接口
     * @param id
     * @return
     */
    @ApiOperation("查找用户详情")
    @GetMapping("/{id}")
    public Result<Employee> findBy(@PathVariable Long id) {
        log.info("👉👉👉 find by id = {}", id);

        Employee employee = employeeService.findBy(id);
        return Result.success(employee);
    }

    /**
     * 员工锁定
     * @param status: 0为锁定, 1为正常
     * @param id: 用户id
     * @return
     */
    @ApiOperation("员工是否锁定")
    @PostMapping("/status/{status}")
    public Result<Employee> updateEmployeeLockStatusBy(@PathVariable Integer status, Long id) {
        log.info("👉👉👉 lock {} ----> [status]_({})", id, status);

        Employee employee = employeeService.lockStatus(status, id);
        return Result.success(employee);
    }

    @ApiOperation("员工列表查询, 姓名模糊查询")
    @GetMapping("/page")
    public Result<PageResult> queryPage(EmployeePageQueryDTO queryDTO) {
        PageResult pageResult = employeeService.queryList(queryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增员工接口
     * @param employeeDTO
     * @return Result
     */
    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info(" -->>> ", employeeDTO);
        Employee employee = employeeService.add(employeeDTO);
        return Result.success(employee);
    }

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation("员工登陆")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation("退出登陆")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

}
