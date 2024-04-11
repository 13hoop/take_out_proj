package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 密码比对
        // 最好前端(取得明文密码 进行md5)加密, 然后传递加密后的pwd, 进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 密码不要返回
        employee.setPhone("****");

        //3、返回实体对象
        return employee;
    }

    @Override
    public Employee add(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        // copy字段值
        BeanUtils.copyProperties(employeeDTO, employee);

        // 设置初始化密码
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        // 设置默认状态
        employee.setStatus(StatusConstant.ENABLE);

        // 设置时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 设置创建人 -- 即当前登陆的用户
        // TODO: 留待 -- 从ThreadLocal中去取
        Long currentId = BaseContext.getCurrentId();
        employee.setCreateUser(currentId);
        employee.setUpdateUser(currentId);

        employeeMapper.insert(employee);

        // 密码不要返回
        employee.setPhone("****");
        return employee;
    }

    /**
     * 员工分页查询, 姓名查询列表
     * @param queryDTO
     * @return
     */
    @Override
    public PageResult queryList(EmployeePageQueryDTO queryDTO) {

        log.info(" --> query by {}", queryDTO.toString());

        // 开始分页查询
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());

        Page<Employee> page = employeeMapper.find(queryDTO);

        long total = page.getTotal();

        List<Employee> list = page.getResult();

        PageResult result = new PageResult(total, list);
        return result;
    }

    @Override
    public Employee lockStatus(Integer status, Long id) {

        log.info("  👉👉👉 lock {} ----> {}", id, status);

        // 查询出对应的员工
        Employee employee = employeeMapper.findById(id);

        // 调整员工的status
        employee.setStatus(status);
        employee.setUpdateTime(LocalDateTime.now());
        // 更新者的id, 当前登陆用户id
        Long currentId = BaseContext.getCurrentId();
        employee.setUpdateUser(currentId);

        // 更新员工
        employeeMapper.update(employee);
        // 密码不要返回
        employee.setPhone("****");
        return employee;
    }

    @Override
    public Employee findBy(Long id) {
        return employeeMapper.findById(id);
    }

    @Override
    public Employee update(EmployeeDTO employeeDTO) {

        Employee employee = Employee.builder().build();

        // copy 信息
        BeanUtils.copyProperties(employeeDTO, employee);

        // 更新信息
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.update(employee);

        // 密码不要返回
        employee.setPhone("****");
        return employee;
    }


}
