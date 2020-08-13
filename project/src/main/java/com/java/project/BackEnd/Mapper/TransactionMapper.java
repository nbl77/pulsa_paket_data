package com.java.project.BackEnd.Mapper;

import com.java.project.BackEnd.Model.Customer;
import com.java.project.BackEnd.Model.CustomerBalance;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TransactionMapper {
    String addPulsa = "INSERT INTO customer_balance(id_customer, balance, active_period, provider, phone_number) VALUES(#{id_customer}, #{balance}, #{active_period}, #{provider}, #{phone_number})";
    String updatePulsa = "UPDATE customer_balance SET balance=#{balance}, active_period=#{active_period} WHERE phone_number=#{phone_number}";
    String addData = "INSERT INTO customer_balance(id_customer, paket_data, active_period, provider, phone_number) VALUES(#{id_customer}, #{paket_data}, #{active_period}, #{provider}, #{phone_number})";
    String updateData = "UPDATE customer_balance SET paket_data=#{paket_data}, active_period=#{active_period} WHERE phone_number=#{phone_number}";
    String updateBalanceBank = "UPDATE bank SET balance=#{balance} WHERE norek=#{norek}";
    String checkNumber = "SELECT*FROM customer_balance WHERE phone_number=#{phone_number}";

    @Insert(addPulsa)
    int addPulsa(CustomerBalance customerBalance);

    @Update(updatePulsa)
    int updatePulsa(CustomerBalance customerBalance);

    @Insert(addData)
    int addData(CustomerBalance customerBalance);

    @Update(updateData)
    int updateData(CustomerBalance customerBalance);

    @Select(checkNumber)
    @Results({
            @Result(column = "phone_number", property = "phone_number")
    })
    List<CustomerBalance> checkNumber(CustomerBalance customerBalance);

    @Select(checkNumber)
    @Results({
            @Result(column = "id_customer", property = "id_customer"),
            @Result(column = "balance", property = "balance"),
            @Result(column = "active_period", property = "active_period"),
            @Result(column = "provider", property = "provider"),
            @Result(column = "phone_number", property = "phone_number")
    })
    CustomerBalance getPhoneNumber(CustomerBalance customerBalance);

    @Update(updateBalanceBank)
    int updataBankBalance(CustomerBalance customerBalance);
}
