package com.java.project.BackEnd.Service.Auth;

import com.google.gson.JsonObject;
import com.java.project.BackEnd.Mapper.CustomerMapper;
import com.java.project.BackEnd.Model.Customer;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
@Service
public class AuthService {

    CustomerMapper customerMapper;
    SqlSession session;

    public AuthService() {
        try {
            Reader reader = Resources.getResourceAsReader("SqlConfig.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            session = sqlSessionFactory.openSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.getConfiguration().addMapper(CustomerMapper.class);
        customerMapper = session.getMapper(CustomerMapper.class);
    }

    public String[] register(Customer customer) {
        if (customer.getUsername() == null) return new String[]{"400","Username Tidak Boleh Kosong"};
        if (customer.getPassword() == null) return new String[]{"400","Password Tidak Boleh Kosong"};
        if (customerMapper.checkUser(customer).size() > 0) {
            return new String[]{"400","Username telah di gunakan!"};
        }
        String username = customer.getUsername();
        String password = customer.getPassword();
        boolean userV = Pattern.matches("(?=.*[0-9])(?=.*[a-zA-Z]).{6,}", username);
        boolean passV = Pattern.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}", password);
        if (userV && passV) {

            if (customerMapper.register(customer) > 0) {
                session.commit();
                session.close();
                return new String[]{"200", "Successfully Registered"};
            } else {
                return new String[]{"200", "Failed Registered"};
            }
        } else if (!userV) {
            return new String[]{"400", "Gagal registrasi! Username harus memiliki setidaknya 1 angka dan minimal 6 karakter!"};
        } else if (!passV) {
            return new String[]{"400", "Password harus memiliki setidaknya 1 angka, simbol,huruf besar dan kecil, minimal 6 karakter!"};
        }
        return new String[]{"400","Terjadi kesalahan internal!"};
    }

    public String[] login(Customer customer) {
        if (customer.getUsername() == null) return new String[]{"400","Username Tidak Boleh Kosong"};
        if (customer.getPassword() == null) return new String[]{"400","Password Tidak Boleh Kosong"};
        if (customerMapper.loginAuth(customer).size() > 0) {
            return new String[]{"200","Berhasil login!","Sekarang anda dapat melakukan transaksi"};
        }
        if (customerMapper.checkUser(customer).size() == 0) {
            return new String[]{"400","Akun belum terdaftar"};
        }
        return new String[]{"400","Username atau password yang anda masukan salah!"};
    }

    public Customer getIdCustomer(Customer customer) {
        return customerMapper.getCustomer(customer);
    }
}
