package com.java.project.BackEnd.Mapper;

import com.java.project.BackEnd.Model.Customer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CustomerMapper {
    String register = "INSERT INTO customer (fullname, username, password) VALUES(#{fullname}, #{username}, #{password});";
    String checkUser = "SELECT*FROM customer WHERE username=#{username}";
    String loginAuth = "SELECT*FROM customer WHERE username=#{username} AND password=#{password}";

    @Insert(register)
    int register(Customer customer);

    @Select(checkUser)
    @Results({
            @Result(column = "username", property = "username")
    })
    List<Customer> checkUser(Customer customer);

    @Select(loginAuth)
    @Results({
            @Result(column = "username", property = "username"),
            @Result(column = "password", property = "password")
    })
    List<Customer> loginAuth(Customer customer);

    @Select(checkUser)
    @Results({
            @Result(column = "id_customer",property = "id_customer"),
            @Result(column = "username", property = "username"),
            @Result(column = "password", property = "password")
    })
    Customer getCustomer(Customer customer);
}
