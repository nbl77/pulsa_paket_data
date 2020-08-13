package com.java.project.BackEnd.Service.Transaction;

import com.java.project.BackEnd.Mapper.TransactionMapper;
import com.java.project.BackEnd.Model.Customer;
import com.java.project.BackEnd.Model.CustomerBalance;
import com.java.project.BackEnd.Model.Payment;
import com.java.project.BackEnd.Service.Auth.AuthService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.Reader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class TransactionService {
    TransactionMapper transactionMapper;
    SqlSession session;
    RestTemplate restTemplate = new RestTemplate();
    CustomerBalance customerBalance;
    public TransactionService() {
        try {
            Reader reader = Resources.getResourceAsReader("SqlConfig.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            session = sqlSessionFactory.openSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.getConfiguration().addMapper(TransactionMapper.class);
        transactionMapper = session.getMapper(TransactionMapper.class);
    }
    public String[] transaksiPulsa(CustomerBalance customerBalance) {
        int s = 1000;
        int m = s * 60;
        int h = m * 60;
        int d = h * 24;
        String[] result = null;
        customerBalance.setActive_period(new java.sql.Date(System.currentTimeMillis() + (d * 15)));
        if (customerBalance.getPhone_number() == null) return new String[]{"400","Nomor telephone tidak boleh kosong"};
        if (customerBalance.getPhone_number().length() < 12) return new String[]{"400","Nomor telephone minimal 12"};
        if (customerBalance.getProvider() == null) return new String[]{"400", "Provider tidak boleh kosong"};
        if (customerBalance.getAmount() == null) return new String[]{"400", "Nominal tidak boleh kosong"};
        if (customerBalance.getProvider() != null) {
            if (!customerBalance.getProvider().toLowerCase().equals("xl") && !customerBalance.getProvider().toLowerCase().equals("indosat") && !customerBalance.getProvider().toLowerCase().equals("telkomsel") ) {
                return new String[]{"400","Provider yang di perbolehkan hanya indosat xl dan telkomsel"};
            }
        }
        int nominal = Integer.parseInt(customerBalance.getAmount());
        long day = Long.parseLong(customerBalance.getAmount())/1000;
        if (transactionMapper.checkNumber(customerBalance).size() > 0) {
            CustomerBalance newData = transactionMapper.getPhoneNumber(customerBalance);
            Calendar cal = Calendar.getInstance();
            int oldBalance = Integer.parseInt(newData.getBalance());
            String currentBalance = "" + (oldBalance + nominal);
            Calendar cl = Calendar.getInstance();
            cl.setTime(newData.getActive_period());
            newData.setActive_period(new java.sql.Date(cl.getTime().getTime() + (d * day)));
            newData.setBalance(currentBalance);
            if (!customerBalance.getProvider().equals(newData.getProvider())) {
                return new String[]{"400", "Nomor ini telah di daftarkan pada provider " + newData.getProvider()};
            }
            if (transactionMapper.updatePulsa(newData) > 0) {
                session.commit();
                session.close();
                if (customerBalance.getPayment_method().equals("emoney")) {
                    CustomerBalance cb = new CustomerBalance();
                    cb.setPayment_method("cekEmoney");
                    String[] a = HttpReq(cb);
                    result = new String[]{"200", "Berhasil topup sebesar " + customerBalance.getAmount() + " Ke nomor " + customerBalance.getPhone_number(), a[1]};
                } else {
                    result = new String[]{"200", "Berhasil topup sebesar " + customerBalance.getAmount() + " Ke nomor " + customerBalance.getPhone_number()};
                }
            }
        } else {
            Customer cs = new Customer();
            cs.setUsername(customerBalance.getUsername());
            Customer customer = new AuthService().getIdCustomer(cs);
            customerBalance.setBalance(customerBalance.getAmount());
            customerBalance.setId_customer(customer.getId_customer());
            if (transactionMapper.addPulsa(customerBalance) > 0) {
                if (customerBalance.getPayment_method().equals("emoney")) {
                    CustomerBalance cb = new CustomerBalance();
                    cb.setPayment_method("cekEmoney");
                    String[] a = HttpReq(cb);
                    result = new String[]{"200", "Berhasil topup sebesar " + customerBalance.getAmount() + " Ke nomor " + customerBalance.getPhone_number(), a[1]};
                } else {
                    result = new String[]{"200", "Berhasil topup sebesar " + customerBalance.getAmount() + " Ke nomor " + customerBalance.getPhone_number()};
                }
                session.commit();
                session.close();
            }
        }
        if (result == null) {
            result = new String[]{"400", "Something wrong"};
        }
        return result;
    }
    public String[] transaksiData(CustomerBalance customerBalance) {
        int s = 1000;
        int m = s * 60;
        int h = m * 60;
        int d = h * 24;
        String[] result = null;
        customerBalance.setActive_period(new java.sql.Date(System.currentTimeMillis() + (d * 15)));

        long day = Long.parseLong(customerBalance.getAmount())/1000;

        float nominal = Float.parseFloat(customerBalance.getPaket_data());
        if (transactionMapper.checkNumber(customerBalance).size() > 0) {

            CustomerBalance newData = transactionMapper.getPhoneNumber(customerBalance);
            Calendar cal = Calendar.getInstance();
            float oldBalance = Float.parseFloat(newData.getPaket_data());
            String currentBalance = "" + (oldBalance + nominal);
            Calendar cl = Calendar.getInstance();
            cl.setTime(newData.getActive_period());
            newData.setActive_period(new java.sql.Date(cl.getTime().getTime() + (d * day)));
            newData.setPaket_data(currentBalance);
            if (!customerBalance.getProvider().equals(newData.getProvider())) {
                return new String[]{"400", "Nomor ini telah di daftarkan pada provider " + newData.getProvider()};
            }
            if (transactionMapper.updateData(newData) > 0) {
                session.commit();
                if (customerBalance.getPayment_method().equals("emoney")) {
                    CustomerBalance cb = new CustomerBalance();
                    cb.setPayment_method("cekEmoney");
                    String[] a = HttpReq(cb);
                    result = new String[]{"200", "Berhasil topup sebesar " + customerBalance.getPaket_data() + "GB Ke nomor " + newData.getPhone_number(), a[1]};
                } else {
                    result = new String[]{"200", "Berhasil topup sebesar " + customerBalance.getPaket_data() + "GB Ke nomor " + newData.getPhone_number()};
                }
            }
        } else {
            Customer cs = new Customer();
            cs.setUsername(customerBalance.getUsername());
            Customer customer = new AuthService().getIdCustomer(cs);
            customerBalance.setPaket_data(customerBalance.getPaket_data());
            customerBalance.setId_customer(customer.getId_customer());
            if (transactionMapper.addData(customerBalance) > 0) {
                if (customerBalance.getPayment_method().equals("emoney")) {
                    CustomerBalance cb = new CustomerBalance();
                    cb.setPayment_method("cekEmoney");
                    String[] a = HttpReq(cb);
                    result = new String[]{"200", "Berhasil topup sebesar " + customerBalance.getPaket_data() + "GB Ke nomor " + customerBalance.getPhone_number(), a[1]};
                } else {
                    result = new String[]{"200", "Berhasil topup sebesar " + customerBalance.getPaket_data() + "GB Ke nomor " + customerBalance.getPhone_number()};
                }
                session.commit();
            }
        }
        if (result == null) {
            result = new String[]{"400", "Something wrong"};
        }
        return result;
    }

    public String[] doPayment(CustomerBalance customerBalance) {
        String[] result = {};
        try {
            String type = customerBalance.getPayment_method();
            switch (type) {
                case "emoney":
                    result = transaksi(customerBalance);
                    break;
                case "merchant":
                    if (customerBalance.getPaid() == null) {
                        result = new String[]{"400", "Silahkan Masukan Jumlah uang yang di bayarkan"};
                    } else {
                        if (customerBalance.getCode() == null) {
                            result = transaksiMerchant(customerBalance);
                        } else {
                            result = transaksi(customerBalance);
                        }
                    }
                    break;
                case "bank_transfer":
                    result = transaksi(customerBalance);
                    break;
                case "virtual_account":
                    result = transaksi(customerBalance);
                    break;
                default:
                    result = new String[]{"400", "Metode pembayaran tidak tersedia"};
                    break;
            }
        } catch (Exception e) {
            return new String[]{"400", "Format JSON anda salah"};
        }

        return result;
    }

    public String[] transaksi(CustomerBalance customerBalance) {
        String[] result = HttpReq(customerBalance);
        if (result[0].toLowerCase().equals("200")) {
            if (customerBalance.getType().equals("pulsa")) {
                result = transaksiPulsa(customerBalance);
            } else if (customerBalance.getType().equals("data")) {
                result = transaksiData(customerBalance);
            }
        }
        return result;
    }

    public String[] transaksiMerchant(CustomerBalance customerBalance) {
        String[] result = HttpReq(customerBalance);
        return result;
    }

    public String[] getTransaksi(CustomerBalance customerBalance) {
        if (customerBalance.getPhone_number() == null) return new String[]{"400","Nomor telephone tidak boleh kosong"};
        if (customerBalance.getPhone_number().length() < 12) return new String[]{"400","Nomor telephone minimal 12"};
        if (customerBalance.getProvider() == null) return new String[]{"400", "Provider tidak boleh kosong"};
        if (customerBalance.getProvider() != null) {
            if (!customerBalance.getProvider().toLowerCase().equals("xl") && !customerBalance.getProvider().toLowerCase().equals("indosat") && !customerBalance.getProvider().toLowerCase().equals("telkomsel") ) {
                return new String[]{"400","Provider yang di perbolehkan hanya indosat xl dan telkomsel"};
            }
        }
        return new String[]{"200", "Silahkan pilih metode pembayaran", "Harga : " + customerBalance.getAmount()};
    }
    public String[] HttpReq(CustomerBalance customerBalance) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        Payment payment = new Payment();
        payment.setMethod_payment(customerBalance.getPayment_method());
        payment.setAmount(customerBalance.getAmount());
        Customer cs = new Customer();
        cs.setUsername(customerBalance.getUsername());
        payment.setCode(customerBalance.getCode());
        payment.setPaid(customerBalance.getPaid());
        HttpEntity<Payment> entity = new HttpEntity<>(payment, headers);
        return restTemplate.exchange("http://localhost:8080/payment", HttpMethod.POST, entity, String[].class).getBody();
    }
}
