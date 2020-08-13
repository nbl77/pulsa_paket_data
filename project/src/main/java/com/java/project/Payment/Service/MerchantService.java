package com.java.project.Payment.Service;

import com.java.project.BackEnd.Model.Payment;
import com.java.project.Payment.Model.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantService {
    String code = null;
    @Autowired
    BankService bankService;

    public String[] checkTransaction(Payment payment) {
        try {
            Thread.sleep(1000);
            if (payment.getPaid() >= Long.parseLong(payment.getAmount())) {
                if (code == null) {
                    code = "" + System.currentTimeMillis();
                    if (payment.getPaid() - Long.parseLong(payment.getAmount()) > 0) {
                        return new String[]{"201", "Kirimkan Bukti Pembayaran berikut : " + code, "Uang kembalian : " + (payment.getPaid() - Long.parseLong(payment.getAmount()))};
                    } else {
                        return new String[]{"201", "Kirimkan Bukti Pembayaran berikut : " + code};
                    }
                } else {
                    if (payment.getCode().equals(code)) {
                        code = "" + System.currentTimeMillis();
                        return bankService.updateBalance(payment);
                    } else {
                        return new String[]{"400", "Kode Yang anda masukan salah"};
                    }
                }
            } else {
                return new String[]{"400", "Uang anda tidak cukup"};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
