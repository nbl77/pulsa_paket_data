package com.java.project.BackEnd.Mapper;

import com.java.project.BackEnd.Model.CustomerBalance;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface BalanceMapper {
    String getBalance = "SELECT*FROM customer_balance LEFT JOIN customer ON customer_balance.id_customer=customer.id_customer WHERE customer_balance.phone_number=#{phone_number}";
    @Select(getBalance)
    @Results({
            @Result(column = "id_customer", property = "id_customer"),
            @Result(column = "balance", property = "balance"),
            @Result(column = "active_period", property = "active_period"),
            @Result(column = "provider", property = "provider"),
            @Result(column = "phone_number", property = "phone_number")
    })
    CustomerBalance getBalance(CustomerBalance customerBalance);
}
