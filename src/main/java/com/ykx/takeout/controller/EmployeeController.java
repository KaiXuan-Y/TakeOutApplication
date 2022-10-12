package com.ykx.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ykx.takeout.common.R;
import com.ykx.takeout.entity.Employee;
import com.ykx.takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.time.LocalDateTime;

/**
 * Created on 2022/9/27.
 *
 * @author KaiXuan Yang
 */
@Slf4j
@RestController()
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        /**
         *  1. 将页面提交的password进行md5加密处理
         */
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        /**
         * 2. 根据用户名搜索数据库
         */
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        /**
         * 3. 如果没有查询到则返回登录失败结果
         */
        if (emp == null){
            return R.error("登录失败");
        }
        /**
         * 4. 密码比对，如果不一致则返回登录失败结果
         */
        if (!emp.getPassword().equals(password)){
            return R.error("登录失败");
        }
        /**
         * 5. 查看员工状态是否正常可用，还是不可用
         */
        if (emp.getStatus() == 0){
            return R.error("账号已经禁用");
        }
        /**
         * 6. 登录成功，将用户id放入session中
         */
        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);


    }

    /**
     * 员工退出登录
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){

        //清理session中保存的当前员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");

    }
    @RequestMapping
    public R<String> save(HttpServletRequest request , @RequestBody Employee employee){
        log.info("新增的员工信心：{}",employee.toString());
        //设置初始密码123456，需要md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);

        return R.success("员工添加成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page , int pageSize , String name){
        log.info("page = {} , pageSize = {} , name = {}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name),Employee::getName , name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);



    }

    /**
     * 根据用户id修改用户信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request , @RequestBody Employee employee){
        log.info(employee.toString());
        long id = Thread.currentThread().getId();
        log.info("当前线程id为：{}",id);
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据员工的id查询信息...");
        Employee employee = employeeService.getById(id);
        if (employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到员工信息");

    }



}
