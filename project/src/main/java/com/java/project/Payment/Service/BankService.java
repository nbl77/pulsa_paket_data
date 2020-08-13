package com.java.project.Payment.Service;

import com.java.project.BackEnd.Model.Payment;
import com.java.project.Payment.Model.Bank;
import com.java.project.Payment.PaymentMapper.BankMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Service;

import java.io.Reader;

@Service
public class BankService {
    private BankMapper bankMapper;
    private SqlSession session;
    private int balance = 1000000;
    private String norek = "";
    private String rand = null;
    public BankService() {
        try {
            Reader reader = Resources.getResourceAsReader("SqlConfig.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            session = sqlSessionFactory.openSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.getConfiguration().addMapper(BankMapper.class);
        bankMapper = session.getMapper(BankMapper.class);
    }
    public String[] updateBalance(Payment payment) {
        Bank mb = bankMapper.getBalance();
        session.commit();
        if (payment.getMethod_payment().equals("emoney") || payment.getMethod_payment().equals("merchant")) {
            payment.setCode(mb.getNorek());
        }
        if (payment.getCode().equals(rand)) {
            payment.setCode(mb.getNorek());
        }
        if (payment.getCode().equals(mb.getNorek())) {
            Long a = Long.parseLong(payment.getAmount()) + mb.getBalance();
            payment.setAmount("" + a);
            bankMapper.updateBalance(payment);
            session.commit();
            return new String[]{"200", "Berhasil melakukan transaksi"};
        }else {
            return new String[]{"400", "Norek tidak Sesuai"};
        }
    }

    public String[] checkBank(Payment payment) {
        Bank mb = bankMapper.getBalance();
        if (payment.getCode() != null) {
            norek = payment.getCode();
        }
        if (norek.isEmpty()) {
            return new String[]{"201", "Kirim nominal uang ke rekening berikut", mb.getNorek()};
        } else if (payment.getPaid() != null) {
            if (payment.getPaid() > Long.parseLong(payment.getAmount())) {
                return updateBalance(payment);
            }
        } else {
            return new String[]{"400", "Harap masukan uang nominal!"};
        }
        return new String[]{"400", "Uang tidak mencukupi!"};
    }

    public String[] checkVA(Payment payment) {
        try {
            Thread.sleep(1000);
            if (rand == null) {
                rand = "0123" + (int) (Math.random() * 1000000000 * 5);
            } else {
                if (payment.getPaid() != null) {
                    if (payment.getPaid() > Long.parseLong(payment.getAmount())) {
                        return updateBalance(payment);
                    } else {
                        return new String[]{"400", "Uang anda kurang!"};
                    }
                } else {
                    return new String[]{"400", "Harap masukan uang nominal!"};
                }
            }
            return new String[]{"201", "Kirim nominal uang ke rekening virtual berikut", "" + rand};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
