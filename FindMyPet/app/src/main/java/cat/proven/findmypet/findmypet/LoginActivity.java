package cat.proven.findmypet.findmypet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

import model.UserClass;

/**
 * Created by Alumne on 30/04/2016.
 */
public class LoginActivity extends AppCompatActivity {
    Button loginButton, registerButton;
    EditText usuariText, passwordText;
    SharedPreferences sharedpreferences;
    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);


        loginButton = (Button)findViewById(R.id.loginButton);
        registerButton= (Button)findViewById(R.id.registerButton);
        usuariText = (EditText)findViewById(R.id.usuariEditText);
        passwordText = (EditText)findViewById(R.id.passwordEditText);
        sharedpreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new HttpRequestTask().execute(usuariText.getText().toString(),passwordText.getText().toString());

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //iniciar la actividad para enseñar el formulario de registro
                cridaActivityRegister();

            }
        });
    }

    private void redirectAfterLogin(UserClass user)
    {
        messageBox("Este usuario SI existe!");
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("id", user.getId());
        editor.putString("userName", user.getUserName());
        editor.putInt("profile", user.getIdProfile());
        editor.commit();

        in = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(in);
        this.finish();
    }



    public void cridaActivityRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        this.finish();
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


    private class HttpRequestTask extends AsyncTask<String, String, UserClass> {

        UserClass usr = null;

        @Override
        protected UserClass doInBackground(String... params) {

            UserClass u = new UserClass(params[0], params[1]);

            String urlString = "http://192.168.27.27:8080/RestFulFindMyPet/restful/users/login/";

            String passwordEncrypted = encrypt(u.getPassword());
            u.setPassword(passwordEncrypted);

            //String urlResult = urlString + u.getUserName() + "/" + passwordEncrypted;
            URL url = null;
            //http://localhost:8080/RestFulFindMyPet/restful/users/login/

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
            postDataParams.put("username", u.getUserName());
            postDataParams.put("password",u.getPassword());

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

            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
            usr = new Gson().fromJson(jsonObject.get("user"), UserClass.class);

            return usr;
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
        protected void onPostExecute(UserClass user) {
            if(user!=null)
            {
                redirectAfterLogin(user);
            }else messageBox("Error introducing username or password");

        }


    }

}