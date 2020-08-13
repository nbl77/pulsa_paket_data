package com.java.project.Payment.PaymentMapper;

import com.java.project.BackEnd.Model.Payment;
import com.java.project.Payment.Model.Bank;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface BankMapper {

    String balance = "SELECT*FROM bank";
    String updateBalance = "UPDATE bank SET balance=#{amount} WHERE norek=#{code}";

    @Select(balance)
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "balance", property = "balance"),
            @Result(column = "marketplace", property = "marketplace"),
            @Result(column = "norek", property = "norek"),
    })
    Bank getBalance();

    @Update(updateBalance)
    int updateBalance(Payment payment);
}
