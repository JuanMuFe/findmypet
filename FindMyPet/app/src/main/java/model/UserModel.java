package model;

import android.provider.ContactsContract;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.cedarsoftware.util.io.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import persistence.UserADO;

/**
 * Created by Alumne on 30/04/2016.
 */
public class UserModel {

    private String baseUrl = "http://localhost:8080/RestFulFindMyPet/restful/users";

    public int login(UserClass u){

        String urlLogin = baseUrl + "/login";
        String userName = u.getUserName();
        String password = u.getPassword();
        String urlResult = urlLogin + "/"+userName+ "/"+password;
        URL url = null;

        //http://localhost:8080/RestFulFindMyPet/restful/users/login/admin/admin

        try {
            url = new URL(urlResult);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //L'objecte HttpUrlConnection ens permet manipular una connexió HTTP.
        UserClass usr=null;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            String response = getResponseBody(con);
            com.google.gson.JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
            usr = new Gson().fromJson(jsonObject.get("user"), UserClass.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(usr != null){
            return 1;
        }else {
            return 0;
        }
    }


    private String getResponseBody(HttpURLConnection con) throws IOException {
        BufferedReader br;

        if (con.getResponseCode() >= 400) {
            //Si el codi de resposta és superior a 400 s'ha produit un error i cal llegir
            //el missatge d'ErrorStream().
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));

        } else {
            //Si el codi és inferior a 400 llavors obtenim una resposta correcte del servidor.
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        return sb.toString();
    }


    public int register(UserClass u, OwnerClass o){

        //llamara al webservice, que a su vez llamara al ado
        UserADO uado = new UserADO();
        int result = uado.register(u,o);

        return result;

    }

    public List<OwnerClass> searchUser(UserClass u, OwnerClass o)
    {
        UserADO uado = new UserADO();
        List<OwnerClass> listUser = uado.searchUser(u,o);

        return listUser;
    }
}
