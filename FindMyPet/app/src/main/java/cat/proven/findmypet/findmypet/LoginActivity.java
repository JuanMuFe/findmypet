package cat.proven.findmypet.findmypet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import model.UserClass;
import model.UserModel;

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
            setContentView(R.layout.register_layout);
            }
        });
    }



    public void cridaActivityRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void messageBox(String mensaje){
        Toast.makeText(this.getApplicationContext(),mensaje, Toast.LENGTH_SHORT).show();
    }

    private class HttpRequestTask extends AsyncTask<String, String, UserClass> {

        UserClass usr = null;

        @Override
        protected UserClass doInBackground(String... params) {

            UserClass u = new UserClass(params[0], params[1]);

            String urlString = "http://localhost:8080/RestFulFindMyPet/restful/users/login/";

            String urlResult = urlString + u.getUserName() + "/" + u.getPassword();
            URL url = null;


            //http://localhost:8080/RestFulFindMyPet/restful/users/login/admin/admin


            try {
                url = new URL(urlResult);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            //L'objecte HttpUrlConnection ens permet manipular una connexió HTTP.


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
            return usr;
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
        protected void onPostExecute(UserClass usr) {
            if (usr != null) {
                messageBox("entra");
            } else messageBox("no");

        }


    }

    }

    /*
    get

        String urlString = "http://10.0.2.2:8080/RestFulFindMyPet/restful/users/login/";

          //  String urlResult = urlString + user.getUserName()+"/"+user.getPassword();
            URL url = null;


            //http://localhost:8080/RestFulFindMyPet/restful/users/login/admin/admin


            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            //L'objecte HttpUrlConnection ens permet manipular una connexió HTTP.


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
            return usr;
     */



