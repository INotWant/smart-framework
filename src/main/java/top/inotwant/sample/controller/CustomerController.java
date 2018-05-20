package top.inotwant.sample.controller;

import top.inotwant.annocation.Action;
import top.inotwant.annocation.Controller;
import top.inotwant.annocation.InJect;
import top.inotwant.bean.Data;
import top.inotwant.bean.FileParam;
import top.inotwant.bean.Param;
import top.inotwant.bean.View;
import top.inotwant.helper.DatabaseHelper;
import top.inotwant.sample.model.Customer;
import top.inotwant.sample.service.CustomerService;

import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {

    @InJect
    private CustomerService customerService;

    @Action("GET:/customer")
    public View customerShow() {
        List<Customer> customerList = customerService.getCustomerList();
        View view = new View("customer.jsp");
        view.addModel("customerList", customerList);
        return view;
    }

    @Action("GET:/id")
    public Data findCustomer() {
        Customer customer = customerService.getCustomer("1");
        return new Data(customer);
    }

    @Action("GET:/customer_create")
    public View createCustomerShow() {
        return new View("customer_create.jsp");
    }

    @Action("POST:/customer_create")
    public Data createCustomer(Param param) {
        Map<String, Object> fieldMap = param.getFieldMap();
        DatabaseHelper.insertEntity(Customer.class, fieldMap);
        FileParam fileParam = param.getFile("photo");
        return new Data(customerService.createCustomer(fieldMap, fileParam));
    }

}
