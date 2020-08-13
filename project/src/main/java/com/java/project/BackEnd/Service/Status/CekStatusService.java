package com.java.project.BackEnd.Service.Status;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.java.project.BackEnd.Mapper.BalanceMapper;
import com.java.project.BackEnd.Mapper.StatusMapper;
import com.java.project.BackEnd.Model.Customer;
import com.java.project.BackEnd.Model.CustomerBalance;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.Reader;
import java.util.List;

public class CekStatusService {
    StatusMapper statusMapper;
    SqlSession session;

    public CekStatusService() {
        try {
            Reader reader = Resources.getResourceAsReader("SqlConfig.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            session = sqlSessionFactory.openSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.getConfiguration().addMapper(StatusMapper.class);
        statusMapper = session.getMapper(StatusMapper.class);
    }

    public String[] getAll(CustomerBalance customerBalance) {
        List<CustomerBalance> cb = statusMapper.getCustomer(customerBalance);
        String obj = new Gson().toJson(cb);
        return new String[]{"200", obj};
    }
    public String[] getPulsa(CustomerBalance customerBalance) {
        if (customerBalance.getPhone_number() == null) {
            return new String[]{"400", "Dimohon untuk memasukan nomor telp"};
        }
        CustomerBalance cb = statusMapper.get(customerBalance);
        if (cb == null) {
            return new String[]{"400", "Nomor belum terdaftar"};
        }
        return new String[]{"200", "Sisa Pulsa anda :" + cb.getBalance()};
    }
    public String[] getData(CustomerBalance customerBalance) {
        if (customerBalance.getPhone_number() == null) {
            return new String[]{"400", "Dimohon untuk memasukan nomor telp"};
        }
        CustomerBalance cb = statusMapper.get(customerBalance);
        if (cb == null) {
            return new String[]{"400", "Nomor belum terdaftar"};
        }
        return new String[]{"200", "Sisa Pake anda :" + cb.getPaket_data() + "GB"};
    }
}
