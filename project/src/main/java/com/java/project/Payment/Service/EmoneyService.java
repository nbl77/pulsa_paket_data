package com.java.project.Payment.Service;

import com.java.project.BackEnd.Model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmoneyService {
    @Autowired
    BankService bankService;
    int balance = 1000000;

    public String[] checkTransaction(Payment payment) {
        if (balance >= Integer.parseInt(payment.getAmount())) {
            balance = balance - Integer.parseInt(payment.getAmount());
            System.out.println(payment);
            return bankService.updateBalance(payment);
        } else {
            return new String[]{"400", "Saldo E-Money Tidak Mencukupi"};
        }
    }

    public String[] checkEmoney(Payment payment) {
        return new String[]{"200", "Saldo E-Money : " + this.balance};
    }
}
