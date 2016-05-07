package cat.proven.findmypet.findmypet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import model.OwnerClass;
import model.UserClass;
import model.UserModel;

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
                //hacer lo que sea
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

    private class HttpRequestTask extends AsyncTask<String, String, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            UserClass u = new UserClass(2,params[0], params[1],params[2],1);
            OwnerClass o = new OwnerClass(params[3],params[4],params[5],params[6],params[7],params[8],params[8],Integer.parseInt(params[10]));


            String urlString = "http://192.168.27.27:8080/RestFulFindMyPet/restful/users/register/";

            String urlResult = urlString + u.getUserName() + "/" + u.getPassword() +"/"+ u.getEmail()+"/"+o.getName()+"/"+o.getFirstname()+"/"+o.getSurname()+"/"+o.getNif()+"/"+o.getBirthdate()+"/"+o.getPhoneNumber()+"/"+o.getAddress()+"/"+o.getIdCityProvince();
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

                result = new Gson().fromJson(jsonObject.get("register"), Integer.class);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
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
            if(result==1)
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
