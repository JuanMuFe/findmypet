package cat.proven.findmypet.findmypet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;




import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import model.OwnerClass;
import model.UserClass;

/**
 * Created by Alumne on 30/04/2016.
 */
public class RegisterActivity extends AppCompatActivity {
    EditText name, firstname, surname, nif, birthdate, phone, address, username, email, password;
    Spinner country;
    Button continueButton;
    int result = 0;
    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        name = (EditText)findViewById(R.id.nameEditText);
        firstname = (EditText)findViewById(R.id.firstnameEditText);
        surname = (EditText)findViewById(R.id.surnameEditText);
        nif = (EditText)findViewById(R.id.nifEditText);
        birthdate = (EditText)findViewById(R.id.birthdateEditText);
        phone = (EditText)findViewById(R.id.phoneEditText);
        address = (EditText)findViewById(R.id.addressEditText);
        username = (EditText)findViewById(R.id.usernameEditText);
        email = (EditText)findViewById(R.id.emailEditText);
        password = (EditText)findViewById(R.id.passwordEditText);
        continueButton = (Button)findViewById(R.id.continueButton);


        continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                register(username.getText().toString(),password.getText().toString(),email.getText().toString(),name.getText().toString(),firstname.getText().toString(),surname.getText().toString(),nif.getText().toString(),birthdate.getText().toString(),phone.getText().toString(),address.getText().toString(),1);
            }
        });


    }

    private void register(String username, String password, String email, String name, String firstname, String surname, String nif, String birthdate, String phone, String address,int idCityProvince) {


        new HttpRequestTask().execute(String.valueOf(2), username, password, email,String.valueOf(1),name,  firstname,  surname,  nif,  birthdate,  phone, address, String.valueOf(idCityProvince));


    }

    private void messageBox(String mensaje){
        Toast.makeText(this.getApplicationContext(),mensaje, Toast.LENGTH_SHORT).show();
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

    private class HttpRequestTask extends AsyncTask<String, String, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            UserClass u = new UserClass(Integer.parseInt(params[0]),params[1], params[2],params[3],1);
            OwnerClass o = new OwnerClass(params[5],params[6],params[7],params[8],params[9],params[10],params[11],2);


//            String urlString = "http://192.168.27.27:8080/RestFulFindMyPet/restful/users/register/";

            String passEncrypted = encrypt(u.getPassword());
            u.setPassword(passEncrypted);

           // String urlResult = urlString + u.getUserName() + "/" + passEncrypted +"/"+ u.getEmail()+"/"+o.getName()+"/"+o.getFirstname()+"/"+o.getSurname()+"/"+o.getNif()+"/"+o.getBirthdate()+"/"+o.getPhoneNumber()+"/"+o.getAddress()+"/"+o.getIdCityProvince();
            String response = "";
            URL url = null;
            try {
                url = new URL("http://192.168.27.27:8080/RestFulFindMyPet/restful/users/register/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
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
            postDataParams.put("username", u.getUserName());
            postDataParams.put("pswd",u.getPassword());
            postDataParams.put("email",u.getEmail());
            postDataParams.put("name",o.getName());
            postDataParams.put("firstname",o.getFirstname());
            postDataParams.put("surname",o.getSurname());
            postDataParams.put("nif",o.getNif());
            postDataParams.put("birthdate",o.getBirthdate());
            postDataParams.put("phone_number",o.getPhoneNumber());
            postDataParams.put("address",o.getAddress());
            postDataParams.put("id_city_province", String.valueOf(o.getIdCityProvince()));



            OutputStream os = null;
            try {
                os = conn.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
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
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();
        }


        @Override
        protected void onPostExecute(Integer resultQ) {
            if(resultQ==1)
            {
                messageBox("User registered correctly");
                redirectAfterRegister();
            }else messageBox("Error inserting user");

        }


    }

    private void redirectAfterRegister() {

            in = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(in);
            this.finish();
    }
}
