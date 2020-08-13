package com.java.project.RestAPI.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.java.project.BackEnd.Model.Customer;
import com.java.project.BackEnd.Service.Auth.AuthService;
import com.java.project.Config.RabbitMQConfig;
import com.java.project.RestAPI.Temp.AuthTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendService {
    @Autowired
    AuthTemp authTemp;
    @Autowired
    RabbitMQConfig rabbitMQConfig;

    public String[] sendData(String data) {
        JsonObject newData = null;
        String[] result = {};
        try {
            newData = stringToJson(data);
        } catch (Exception e) {
            return new String[]{"400", "Format JSON Salah"};
        }
        if (newData.has("action")) {
            String action = newData.get("action").getAsString();
            switch (action) {
                case "register":
                    result = register(newData);
                    break;
                case "login":
                    result = login(newData);
                    break;
                case "logout":
                    result = logout(newData);
                    break;
                case "topup":
                    result = topup(newData);
                    break;
                case "payment":
                    result = topup(newData);
                    break;
                case "cek_status":
                    result = cekStatus(newData);
                    break;
                case "cek_pulsa":
                    result = cekStatus(newData);
                    break;
                case "cek_data":
                    result = cekStatus(newData);
                    break;
                case "pilih_paket":
                    result = cekStatus(newData);
                    break;
                case "cek_emoney":
                    result = cekStatus(newData);
                    break;
                default:
                    result = new String[]{"400", "Action yang anda pilih tidak ada"};
                    break;
            }
        } else {
            return new String[]{"400", "Anda harus menyertakan action pada json anda"};
        }
        return result;
    }

    public String[] cekStatus(JsonObject jsonObject) {
        if (authTemp.getUsername() == null) {
            return new String[]{"400", "Silahkan Login Terlebih Dahulu"};
        }
        jsonObject.addProperty("username",authTemp.getUsername());
        return rabbitMQConfig.publish(jsonObject.toString(), jsonObject.get("username").getAsString());
    }

    public String[] register(JsonObject jsonObject) {
        try {
            if (authTemp.getStatus().equals("active")) {
                return new String[]{"400", "Anda Harus Logout Terlebih Dahulu"};
            }
            return rabbitMQConfig.publish(jsonObject.toString(), jsonObject.get("username").getAsString());
        } catch (Exception e) {
            return new String[]{"400", "Format JSON Salah"};
        }
    }

    public String[] login(JsonObject jsonObject) {
        try {
            AuthService authService = new AuthService();
            Customer cs = new Customer();
            cs.setUsername(jsonObject.get("username").getAsString());
            Customer res = authService.getIdCustomer(cs);
            Long id_customer = res.getId_customer();
            authTemp.setUsername(jsonObject.get("username").getAsString());
            authTemp.setStatus("active");
            authTemp.setId_customer(id_customer);
            return rabbitMQConfig.publish(jsonObject.toString(), jsonObject.get("username").getAsString());
        } catch (Exception e) {
            return new String[]{"400", "Formnat JSON salah"};
        }
    }

    public String[] logout(JsonObject jsonObject) {
        if (jsonObject.has("logout")) {
            if (jsonObject.get("logout").getAsBoolean()) {
                authTemp.setStatus("logout");
                return new String[]{"200", "Berhasil Logout", "Silahkan login terlebih dahulu untuk melakukan transaksi"};
            }
        }
        return new String[]{"400", "Logout Error"};
    }
    public String[] topup(JsonObject jsonObject) {
        if (authTemp.getUsername() == null) {
            return new String[]{"400", "Silahkan Login Terlebih Dahulu"};
        }
        jsonObject.addProperty("username",authTemp.getUsername());
        return rabbitMQConfig.publish(jsonObject.toString(), jsonObject.get("username").getAsString());
    }



    public JsonObject stringToJson(String data) {
        return new JsonParser().parse(data).getAsJsonObject();
    }
}
