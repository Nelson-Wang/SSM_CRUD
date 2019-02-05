package xyz.funnyboy.crud.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import xyz.funnyboy.crud.model.Employee;
import xyz.funnyboy.crud.model.Msg;
import xyz.funnyboy.crud.service.EmployeeService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Uxiahnan OR 14027
 * @version Dragon1.0
 * @createTime 2019年01月14日19时20分
 * @desciption This is a program.
 * @since Java10
 */
@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/emp/{empIds}", method = RequestMethod.DELETE)
    @ResponseBody
    public Msg delEmp(@PathVariable("empIds") String empIds) {
        boolean result;
        if (!empIds.contains("-")) {
            Integer empId = Integer.parseInt(empIds);
            result = employeeService.delEmpById(empId) == 1;
        } else {
            result = employeeService.delEmps(empIds) != 0;
        }
        if (result)
            return Msg.success();
        else
            return Msg.failed();
    }

    @RequestMapping(value = "/emp/{empId}", method = RequestMethod.PUT)
    @ResponseBody
    public Msg updateEmp(@Valid Employee employee, BindingResult result) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                map.put(error.getField(), error.getDefaultMessage());
            }
            return Msg.failed().add("error", map);
        } else {
            employeeService.updateEmp(employee);
            return Msg.success().add("success", employee);
        }
    }

    @RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id) {
        Employee employee = employeeService.getEmp(id);
        if (employee == null) {
            return Msg.failed().add("msg", "无此员工记录");
        }
        return Msg.success().add("emp", employee);
    }

    @RequestMapping(value = "/validateuser", method = RequestMethod.POST)
    @ResponseBody
    public Msg validateUser(@RequestParam("empName") String empName) {
        String regName = "^[a-z0-9_-]{6,16}$|^[\\u2E80-\\u9FFF]{2,5}$";
        if (!empName.matches(regName)) {
            return Msg.failed().add("msg", "用户名不合法");
        }
        boolean success = employeeService.validateUserUsable(empName);
        if (success) {
            return Msg.success();
        } else {
            return Msg.failed().add("msg", "用户名已存在");
        }
    }

    @RequestMapping(value = "/emps", method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                map.put(error.getField(), error.getDefaultMessage());
            }
            return Msg.failed().add("result", map);
        } else {
            employeeService.insertEmp(employee);
            return Msg.success();
        }
    }

    @RequestMapping(value = "/emps", method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pg", defaultValue = "1") Integer pg) {
        PageHelper.startPage(pg, 5);
        List<Employee> employees = employeeService.getAll();
        PageInfo pageInfo = new PageInfo(employees, 5);
        return Msg.success().add("pageInfo", pageInfo);
    }
}
