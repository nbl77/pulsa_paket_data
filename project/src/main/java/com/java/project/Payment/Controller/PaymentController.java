package com.java.project.Payment.Controller;

import com.java.project.BackEnd.Model.Payment;
import com.java.project.Payment.Service.BankService;
import com.java.project.Payment.Service.EmoneyService;
import com.java.project.Payment.Service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    @Autowired
    EmoneyService emoneyService;
    @Autowired
    BankService bankService;
    @Autowired
    MerchantService merchantService;

    @PostMapping("/payment")
    public String[] getPayment(@RequestBody Payment payment) {
        String method = payment.getMethod_payment();
        String[] result = {};
        switch (method) {
            case "emoney":
                result = emoneyService.checkTransaction(payment);
                break;
            case "cekEmoney":
                result = emoneyService.checkEmoney(payment);
                break;
            case "merchant":
                result = merchantService.checkTransaction(payment);
                break;
            case "bank_transfer":
                result = bankService.checkBank(payment);
                break;
            case "virtual_account":
                result = bankService.checkVA(payment);
                break;
            default:
                result = new String[]{"400", "Metode pembayaran tidak tersedia"};
                break;
        }
        return result;
    }
}
