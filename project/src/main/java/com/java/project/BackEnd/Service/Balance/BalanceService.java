package com.java.project.BackEnd.Service.Balance;

import com.java.project.BackEnd.Mapper.BalanceMapper;
import com.java.project.BackEnd.Mapper.TransactionMapper;
import com.java.project.BackEnd.Model.Customer;
import com.java.project.BackEnd.Model.CustomerBalance;
import com.java.project.BackEnd.Service.Auth.AuthService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.Reader;

public class BalanceService {
    BalanceMapper balanceMapper;
    SqlSession session;
    public BalanceService() {
        try {
            Reader reader = Resources.getResourceAsReader("SqlConfig.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            session = sqlSessionFactory.openSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.getConfiguration().addMapper(BalanceMapper.class);
        balanceMapper = session.getMapper(BalanceMapper.class);
    }
    public String[] cekPulsa(CustomerBalance customerBalance) {
        CustomerBalance cs = getBalance(customerBalance);
        String pulsa = cs.getBalance();
        if (pulsa == null) {
            pulsa = "0";
        }
        int pls = Integer.parseInt(pulsa);
        if (pls < 3000) {
            return new String[]{"200", "Sisa Pulsa anda saat ini : " + pls,"Disarankan Untuk mengisi pulsa"};
        }
        return new String[]{"200", "Sisa Pulsa anda saat ini : " + pls};
    }
    public String[] cekData(CustomerBalance customerBalance) {
        CustomerBalance cs = getBalance(customerBalance);
        return new String[]{"200", "Sisa Paket anda saat ini : " + cs.getPaket_data() + "GB."};
    }
    public String[] cekMasaAktiv(CustomerBalance customerBalance) {
        CustomerBalance cs = getBalance(customerBalance);
        return new String[]{"200", "Masa aktiv anda sampai dengan : " + cs.getActive_period()};
    }

    public String[] cekStatus(CustomerBalance customerBalance) {
        CustomerBalance cs = getBalance(customerBalance);
        return new String[]{"200", "{\"No Telp\":\"" + cs.getPhone_number() + "\",\"Pulsa \":\"" + cs.getBalance() + "\",\"Paket Data\":\"" + cs.getPaket_data() + "GB\",\"Masa Aktif\":\"" + cs.getActive_period() + "\"}"};
    }

    public CustomerBalance getBalance(CustomerBalance customerBalance) {
        return balanceMapper.getBalance(customerBalance);
    }
}
