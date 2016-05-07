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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
                setContentView(R.layout.register_layout);
            }
        });
    }

    private void redirectAfterLogin(UserClass user)
    {
        messageBox("Este usuario SI existe!");
        SharedPreferences.Editor editor = sharedpreferences.edit();
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

            String urlResult = urlString + u.getUserName() + "/" + passwordEncrypted;
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

                JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();

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
        protected void onPostExecute(UserClass user) {
            if(user!=null)
            {
                redirectAfterLogin(user);
            }else messageBox("Error introducing username or password");

        }


    }

}