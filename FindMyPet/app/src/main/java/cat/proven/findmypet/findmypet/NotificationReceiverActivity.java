package cat.proven.findmypet.findmypet;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import model.NotificationClass;

public class NotificationReceiverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String id = b.getString("idreport");
        new modifyActive().execute(id);

    }

    private class modifyActive extends AsyncTask<String, String, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            String urlString = "http://provenapps.cat:8080/RestFulFindMyPet/restful/notifications/modifyReaded";
            URL url=  null;
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
                conn.setRequestProperty("Accept-Encoding", "");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            conn.setDoInput(true);
            conn.setDoOutput(true);


            HashMap<String, String> postDataParams = new HashMap<String, String>();
            postDataParams.put("id_report", String.valueOf(params[0]));

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


        @Override
        protected void onPostExecute(Integer result) {

            if(result>0)
            {
                redirect();
            }
        }


    }

        private void redirect()
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
}
