package cat.proven.findmypet.findmypet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import model.OwnerClass;
import model.UserClass;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.acl.Owner;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by juuua on 16/05/2016.
 */
public class BasicDataActivity extends AppCompatActivity {
    EditText name, firstname, surname, nif, birthdate, phone_number, address, username, email, oldPassword, newPassword;
    Button modifyButton;
    SharedPreferences sharedPref;
    OwnerClass owner,savedOwner = null;
    UserClass user,savedUser = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_data_layout);

        name = (EditText)findViewById(R.id.editText1);
        firstname = (EditText)findViewById(R.id.editText2);
        surname = (EditText)findViewById(R.id.editText3);
        nif = (EditText)findViewById(R.id.editText4);
        birthdate = (EditText)findViewById(R.id.editText5);
        phone_number = (EditText)findViewById(R.id.editText6);
        address = (EditText)findViewById(R.id.editText7);
        username = (EditText)findViewById(R.id.editText8);
        email = (EditText)findViewById(R.id.editText9);
        oldPassword = (EditText)findViewById(R.id.editText10);
        newPassword = (EditText)findViewById(R.id.editText11);
        modifyButton = (Button)findViewById(R.id.modifyButton);

        loadUserBasicData();

        modifyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String oldPass = encrypt(oldPassword.getText().toString());
                String newPass = encrypt(newPassword.getText().toString());
                if(oldPass.equals(user.getPassword())){
                    owner = new OwnerClass(savedOwner.getIdUser(), name.getText().toString(), firstname.getText().toString(), surname.getText().toString(), nif.getText().toString(), birthdate.getText().toString(), phone_number.getText().toString(), address.getText().toString(), owner.getIdCityProvince());
                    if(newPassword.getText().toString()!= ""){
                        user = new UserClass(2, username.getText().toString(), newPass, email.getText().toString(), 1);
                    }else{
                        user = new UserClass(2, username.getText().toString(), oldPass, email.getText().toString(), 1);
                    }

                    new modifyBasicData().execute(owner,user);
                }else{ messageBox("The old password isn't valid"); }
            }
        });
    }

    public void loadUserBasicData(){
        SharedPreferences userDetails = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        int id = userDetails.getInt("id",0);
        new loadBasicData().execute(id);
    }

    private String encrypt(String password)
    {
        MessageDigest md=null;
        String passwordmd5="";

        try {
            md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            passwordmd5 = sb.toString();

        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return passwordmd5;
    }

    private class loadBasicData extends AsyncTask<Integer, Void, Map<String, Object>> {

        @Override
        protected Map<String, Object> doInBackground(Integer... params) {

            String urlString = "http://provenapps.cat:8080/RestFulFindMyPet/restful/users/getOwner/";

            URL url = null;

            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            String response = "";
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            try {
                conn.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, Integer> postDataParams = new HashMap<String, Integer>();
            postDataParams.put("id_user", params[0]);

            OutputStream os = null;
            try {
                os = conn.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            int responseCode= 0;
            try {
                responseCode = conn.getResponseCode();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br= null;
                try {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
            else {
                response="";

            }

            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
            owner = new Gson().fromJson(jsonObject.get("owner"), OwnerClass.class);
            user = new Gson().fromJson(jsonObject.get("user"), UserClass.class);

            Map<String, Object> mapping = new HashMap<>();
            mapping.put("owner", owner);
            mapping.put("user", user);
            return mapping;
        }
        private String getPostDataString(HashMap<String, Integer> params) throws UnsupportedEncodingException{
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, Integer> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(Map<String, Object> mapping) {
            savedOwner = (OwnerClass)mapping.get("owner");
            savedUser = (UserClass)mapping.get("user");

            name.setText(savedOwner.getName());
            firstname.setText(savedOwner.getFirstname());
            surname.setText(savedOwner.getSurname());
            nif.setText(savedOwner.getNif());
            birthdate.setText(savedOwner.getBirthdate());
            phone_number.setText(savedOwner.getPhoneNumber());
            address.setText(savedOwner.getAddress());
            username.setText(savedUser.getUserName());
            email.setText(savedUser.getEmail());
        }
    }

    private class modifyBasicData extends AsyncTask< Object, Void, Integer> {

        @Override
        protected Integer doInBackground(Object... params) {

            String urlString = "http://provenapps.cat:8080/RestFulFindMyPet/restful/users/modifyOwner/";
            OwnerClass o =(OwnerClass)params[0];
            UserClass u = (UserClass)params[1];

            URL url = null;

            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            String response = "";
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            try {
                conn.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String, String> postDataParams = new HashMap<String, String>();
            postDataParams.put("id_user", String.valueOf(o.getIdUser()));
            postDataParams.put("name", o.getName());
            postDataParams.put("firstname", o.getFirstname());
            postDataParams.put("surname", o.getSurname());
            postDataParams.put("nif", o.getNif());
            postDataParams.put("birthdate", o.getBirthdate());
            postDataParams.put("phone_number", o.getPhoneNumber());
            postDataParams.put("address", o.getAddress());
            postDataParams.put("id_city_province", String.valueOf(o.getIdCityProvince()));
            postDataParams.put("id_profile", String.valueOf(u.getIdProfile()));
            postDataParams.put("user_name", u.getUserName());
            postDataParams.put("pswd", u.getPassword());
            postDataParams.put("email", u.getEmail());
            postDataParams.put("active", String.valueOf(u.getActive()));


            OutputStream os = null;
            try {
                os = conn.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            int responseCode= 0;
            try {
                responseCode = conn.getResponseCode();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br= null;
                try {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
            else {
                response="";
            }

            return Integer.valueOf(response);
        }
        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(Integer resultQ) {
            redirectToProfile(resultQ);
        }
    }

    public void redirectToProfile(int res){
        if(res > 0){ messageBox("Data modified correctly"); }
        else{ messageBox("An error occurred while data is modified"); }
        Intent in = new Intent(BasicDataActivity.this,ProfileActivity.class);
        startActivity(in);
        this.finish();
    }

    public Activity getActivity() {
        return this;
    }

    private void messageBox(String mensaje){
        Toast.makeText(this.getApplicationContext(),mensaje, Toast.LENGTH_SHORT).show();
    }
}
