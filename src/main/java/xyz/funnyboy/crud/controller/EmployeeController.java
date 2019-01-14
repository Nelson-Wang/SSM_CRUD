package xyz.funnyboy.crud.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.funnyboy.crud.model.Employee;
import xyz.funnyboy.crud.service.EmployeeService;

import java.util.List;

/**
 * @author Uxiahnan OR 14027
 * @version Dragon1.0
 * @createTime 2019年01月14日19时20分
 * @desciption This is a program.
 * @since Java10
 */
@Controller()
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/emps",method = RequestMethod.GET)
    public String getEmps(@RequestParam(value = "pg", defaultValue = "1") Integer pg, Model model) {
        PageHelper.startPage(pg, 5);
        List<Employee> employees = employeeService.getAll();
        PageInfo pageInfo = new PageInfo(employees, 5);
        model.addAttribute("pageInfo", pageInfo);
        return "list";
    }
}
