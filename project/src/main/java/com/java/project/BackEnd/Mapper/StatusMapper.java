package com.java.project.BackEnd.Mapper;

import com.java.project.BackEnd.Model.Customer;
import com.java.project.BackEnd.Model.CustomerBalance;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StatusMapper {
    String checkUser = "SELECT*FROM customer LEFT JOIN customer_balance ON customer.id_customer=customer_balance.id_customer WHERE customer.username=#{username}";
    @Select(checkUser)
    @Results({
            @Result(column = "username", property = "username"),
            @Result(column = "balance", property = "balance"),
            @Result(column = "paket_data", property = "paket_data"),
            @Result(column = "active_period", property = "active_period"),
            @Result(column = "provider", property = "provider"),
            @Result(column = "phone_number", property = "phone_number")
    })
    List<CustomerBalance> getCustomer(CustomerBalance customerBalance);

    String checkPls = "SELECT*FROM customer_balance WHERE phone_number=#{phone_number}";
    @Select(checkPls)
    @Results({
            @Result(column = "balance", property = "balance"),
            @Result(column = "paket_data", property = "paket_data"),
            @Result(column = "active_period", property = "active_period"),
            @Result(column = "provider", property = "provider"),
            @Result(column = "phone_number", property = "phone_number")
    })
    CustomerBalance get(CustomerBalance customerBalance);
}
