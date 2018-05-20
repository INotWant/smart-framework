package top.inotwant.sample.service;

import top.inotwant.annocation.Service;
import top.inotwant.annocation.Transaction;
import top.inotwant.bean.FileParam;
import top.inotwant.helper.DatabaseHelper;
import top.inotwant.helper.UploadHelper;
import top.inotwant.sample.model.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    /**
     * 获取所有客户
     */
    public List<Customer> getCustomerList() {
        return DatabaseHelper.queryEntityList(Customer.class, "select * from customer");
    }

    /**
     * 根据 id 获取客户
     */
    public Customer getCustomer(String id) {
        return DatabaseHelper.queryEntity(Customer.class, "select * from customer where id=?", id);
    }

    /**
     * 创建用户
     */
    @Transaction
    public boolean createCustomer(Map<String, Object> fieldMap, FileParam fileParam) {
        boolean result = DatabaseHelper.insertEntity(Customer.class, fieldMap);
        if (result) {
            UploadHelper.uploadFile("/tmp/upload/", fileParam);
        }
        return result;
    }

    /**
     * 修改用户信息
     */
    @Transaction
    public boolean updateCustomer(Customer customer) {
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("name", customer.getName());
        fieldMap.put("contact", customer.getContact());
        fieldMap.put("telephone", customer.getTelephone());
        fieldMap.put("email", customer.getEmail());
        fieldMap.put("remark", customer.getRemark());
        return DatabaseHelper.updateEntity(Customer.class, customer.getId(), fieldMap);
    }

    /**
     * 根据 id 删除用户
     */
    @Transaction
    public boolean deleteCustomer(long id) {
        return DatabaseHelper.deleteEntity(Customer.class, id);
    }

    /**
     * 根据 “客户名称” 进行模型查询
     */
    public List<Customer> findCustomers(String name) {
        // TODO
        return null;
    }

}
