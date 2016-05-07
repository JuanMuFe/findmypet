package cat.proven.findmypet.findmypet;

import android.content.Intent;
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
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.acl.Owner;
import java.util.List;

import model.OwnerClass;
import model.UserClass;
import model.UserModel;

/**
 * Created by Alumne on 30/04/2016.
 */
public class SearchActivity extends AppCompatActivity {
    Button searchButton;
    EditText usuariEditText;
    List<Owner> owners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);

        searchButton = (Button)findViewById(R.id.searchButton);

        usuariEditText= (EditText)findViewById(R.id.usuariEditText);


        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchUser(usuariEditText.getText().toString());
            }
        });

    }

    private void searchUser(String text) {
        new HttpRequestTask().execute(text);
    }

    public void cridaActivityRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void messageBox(String mensaje){
        Toast.makeText(this.getApplicationContext(),mensaje, Toast.LENGTH_SHORT).show();
    }

    private class HttpRequestTask extends AsyncTask<String, String, List<Owner>> {

        UserClass usr = null;

        @Override
        protected List<Owner> doInBackground(String... params) {


            String urlString = "http://192.168.27.27:8080/RestFulFindMyPet/restful/users/search/";

            String urlResult = urlString + params[0];
            URL url = null;

            //http://192.168.27.27:8080/RestFulFindMyPet/restful/users/search/admin

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

                owners = new Gson().fromJson(jsonObject.get("owners"),  new TypeToken<List<List<String>>>() {}.getType());


            } catch (IOException e) {
                e.printStackTrace();
            }
            return owners;
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
        protected void onPostExecute(List<Owner> owners) {
            if(owners.size()>0)
            {
                //rellena la lista Juan, por favor
            }else messageBox("User not found");

        }


    }
}
