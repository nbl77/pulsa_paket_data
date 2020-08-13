package com.java.project.BackEnd.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.java.project.BackEnd.Model.Customer;
import com.java.project.BackEnd.Model.CustomerBalance;
import com.java.project.BackEnd.Model.PaketData.Indosat;
import com.java.project.BackEnd.Model.PaketData.PaketData;
import com.java.project.BackEnd.Model.PaketData.Telkomsel;
import com.java.project.BackEnd.Model.PaketData.XL;
import com.java.project.BackEnd.Service.Auth.AuthService;
import com.java.project.BackEnd.Service.Balance.BalanceService;
import com.java.project.BackEnd.Service.Status.CekStatusService;
import com.java.project.BackEnd.Service.Transaction.TransactionService;
import com.java.project.Config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiverController {
    private static final Logger logger = LoggerFactory.getLogger(ReceiverController.class);


    private String message, RoutingKey, type;
    private String tes = "";
    JsonObject msg;
    private String[] result ={};
    AuthService authService;
    TransactionService transactionService;
    BalanceService balanceService;
    RabbitMQConfig rabbitMQConfig;
    CustomerBalance transaction;
    CekStatusService cekStatusService;

    public void checkLogin(JsonObject jsonObject) {
        if (jsonObject.has("username")) {
            if (jsonObject.get("username").isJsonNull()) {
                result = new String[]{"400", "Silahkan Login Terlebih dahulu"};
                type = "done";
            }
        }
    }

    public void startReceiver(String message, String RoutingKey) {
        this.message = message;
        this.RoutingKey = RoutingKey;
        msg = stringToJson(message);
        authService = new AuthService();
        transactionService = new TransactionService();
        balanceService = new BalanceService();
        cekStatusService = new CekStatusService();
        type = msg.get("action").getAsString();
        checkLogin(msg);
        System.out.println("[*]Receive data from client");
        System.out.println("[*]Processing data.");
        logger.info("[*]Receive data from client");
        logger.info("[*]Processing data.");
        Customer customer = null;
        switch (type) {
            case "register":
                customer = new Gson().fromJson(msg.toString(), Customer.class);
                result = authService.register(customer);
                break;
            case "login":
                customer = new Gson().fromJson(msg.toString(), Customer.class);
                result = authService.login(customer);
                break;
            case "topup":
                transaction = new Gson().fromJson(msg.toString(), CustomerBalance.class);
                try {
                    if (transaction.getType().equals("data" )|| transaction.getType().equals("pulsa")) {
                        if (transaction.getType().equals("data")) {
                            transaction = getPriceData(transaction);
                            if (transaction.getAmount().equals("0")) {
                                result = new String[]{"400", "Paket Data Tidak Tersedia"};
                                break;
                            }
                        }
                        if (transaction.getAmount() == null) {
                            result = new String[]{"400", "harap masukan jumlah yang ingin di beli"};
                        } else {
                            result = transactionService.getTransaksi(transaction);
                        }
                    } else {
                        if (transaction.getType() == null) {
                            result = new String[]{"400", "Tipe tidak boleh kosong"};
                        }else{
                            result = new String[]{"400", "Tipe tidak tersedia"};
                        }
                    }
                } catch (Exception e) {
                    result = new String[]{"400", "Format JSON salah"};
                }
                break;
            case "payment":
                if (transaction == null) {
                    result = new String[]{"400", "Silahkan lakukan request transaksi"};
                    break;
                }
                CustomerBalance transaction2 = new Gson().fromJson(msg.toString(), CustomerBalance.class);
                transaction.setPayment_method(transaction2.getPayment_method());
                transaction.setCode(transaction2.getCode());
                transaction.setPaid(transaction2.getPaid());
                result = transactionService.doPayment(transaction);
                if (result[0].equals("200")) {
                    transaction = null;
                }
                break;
            case "cek_status":
                result = showStatus(msg);
                break;
            case "cek_pulsa":
                result = showPulsa(msg);
                break;
            case "cek_data":
                result = showData(msg);
                break;
            case "pilih_paket":
                result = pilihPaket();
                break;
            case "cek_emoney":
                result = cekEmoney();
                break;
            default:
                result = new String[]{"400", "Json error"};
                break;
        }
        System.out.println("[*]Data has been processed.");
        System.out.println("[*]Send Feedback to client.");
        String tks = "";
        for (String s : result) {
            tks += s + " ,";
        }
        rabbitMQConfig.publishFeedback(tks, RoutingKey);
    }
    public ReceiverController(String message, String RoutingKey) {
        rabbitMQConfig = new RabbitMQConfig();
    }

    public String[] showStatus(JsonObject object) {
        CustomerBalance cs = new Gson().fromJson(object.toString(), CustomerBalance.class);
        return cekStatusService.getAll(cs);
    }

    public String[] cekEmoney() {
        CustomerBalance cb = new CustomerBalance();
        cb.setPayment_method("cekEmoney");
        return transactionService.HttpReq(cb);
    }
    public CustomerBalance getPriceData(CustomerBalance customerBalance) {
        Float price = Float.parseFloat(customerBalance.getAmount());
        int newPrice = 0;
        switch (customerBalance.getProvider().toLowerCase()) {
            case "indosat":
                for (PaketData in : new Indosat().getPaketDataList()) {
                    if (in.getData() == price) {
                        newPrice = in.getHarga();
                        break;
                    }
                }
                break;
            case "telkomsel":
                for (PaketData ts : new Telkomsel().getPaketDataList()) {
                    if (ts.getData() == price) {
                        newPrice = ts.getHarga();
                        break;
                    }
                }
                break;
            case "xl":
                for (PaketData xl : new XL().getPaketDataList()) {
                    if (xl.getData() == price) {
                        newPrice = xl.getHarga();
                        break;
                    }
                }

                break;
        }
        customerBalance.setPaket_data(customerBalance.getAmount());
        customerBalance.setAmount("" + newPrice);
        return customerBalance;
    }
    public String[] showPulsa(JsonObject object) {
        CustomerBalance cs = new Gson().fromJson(object.toString(), CustomerBalance.class);
        return cekStatusService.getPulsa(cs);
    }
    public String[] showData(JsonObject object) {
        CustomerBalance cs = new Gson().fromJson(object.toString(), CustomerBalance.class);
        return cekStatusService.getData(cs);
    }

    public String[] pilihPaket() {
        Telkomsel telkomsel = new Telkomsel();
        XL XL = new XL();
        Indosat indosat = new Indosat();
        String teksTsel = "{\"Telkomsel\":[";
        int i = 1;
        for (PaketData pd : telkomsel.getPaketDataList()) {
            if (i >= telkomsel.getPaketDataList().size()) {
                teksTsel += "{\"Paket Data\":\"" + pd.getData() + "GB\",\"Harga\":\"" + pd.getHarga() + "\"}";
            }else {
                teksTsel += "{\"Paket Data\":\"" + pd.getData() + "GB\",\"Harga\":\"" + pd.getHarga() + "\"},";
            }
            i++;
        }
        teksTsel += "]\",";

        String teksIsat = "\"Indosat\":[";
         i = 1;
        for (PaketData pd : indosat.getPaketDataList()) {
            if (i >= telkomsel.getPaketDataList().size()) {
                teksIsat += "{\"Paket Data\":\"" + pd.getData() + "GB\",\"Harga\":\"" + pd.getHarga() + "\"}";
            }else {
                teksIsat += "{\"Paket Data\":\"" + pd.getData() + "GB\",\"Harga\":\"" + pd.getHarga() + "\"},";
            }
            i++;
        }
        teksIsat += "]\",";

        String teksXl = "\"XL\":[";
        i = 1;
        for (PaketData pd : XL.getPaketDataList()) {
            if (i >= telkomsel.getPaketDataList().size()) {
                teksXl += "{\"Paket Data\":\"" + pd.getData() + "GB\",\"Harga\":\"" + pd.getHarga() + "\"}";
            }else {
                teksXl += "{\"Paket Data\":\"" + pd.getData() + "GB\",\"Harga\":\"" + pd.getHarga() + "\"},";
            }
            i++;
        }
        teksXl += "]\"}";
        return new String[]{"200", teksTsel+teksIsat+teksXl};
    }
    public JsonObject stringToJson(String data) {
        return new JsonParser().parse(data).getAsJsonObject();
    }
}
