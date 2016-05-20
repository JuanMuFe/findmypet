package cat.proven.findmypet.findmypet;

import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.android.gms.identity.intents.Address;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import model.AnnouncementClass;
import model.OwnerClass;
import model.ReportClass;
import model.UserClass;


/**
 * Created by Alumne on 14/05/2016.
 */
public class ReportsActivity extends AppCompatActivity {
    ListView reportsList;
    OwnerClass owner = null;
    ReportClass report = null;
    ArrayList<ReportClass> reports = new ArrayList<>();
    ArrayList<String> locations = new ArrayList<>();
    SharedPreferences sharedpreferences;
    Geocoder geocoder;
    List<android.location.Address> addresses;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports_layout);

        reportsList = (ListView)findViewById(R.id.reportsListView);
        sharedpreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        int id_owner = sharedpreferences.getInt("id_owner", -1);

        loadUserReports(id_owner);

    }

    public void loadUserReports(int id_owner){
        if(id_owner > -1){
            new loadOwnerReports().execute(id_owner);
        }
    }

    private class loadOwnerReports extends AsyncTask<Integer, Void, ArrayList<ReportClass>> {

        @Override
        protected ArrayList<ReportClass> doInBackground(Integer... params) {

            String urlString = "http://provenapps.cat:8080/RestFulFindMyPet/restful/reports/searchOwnerReports/";

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
            postDataParams.put("id_owner", String.valueOf(params[0]));

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

            JsonArray jArray= null;
            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
            jArray = jsonObject.getAsJsonArray("ownerReports");

            for(int i = 0; i< jArray.size(); i++){
                JsonElement q = jArray.get(i);
                report = new Gson().fromJson(q, ReportClass.class);
                reports.add(report);
            }

            return reports;
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
                result.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(ArrayList<ReportClass> reports) {
            fillOwnerReportsList(reports);
        }
    }

    public void fillOwnerReportsList(ArrayList<ReportClass> ownerReports){
        geocoder = new Geocoder(this, Locale.getDefault());
        //reportsList
        for(int i=0; i<ownerReports.size(); i++){
            String latLon = ownerReports.get(i).getLocation();
            double lat = Double.parseDouble(latLon.split(",")[0]);
            double lon = Double.parseDouble(latLon.split(",")[1]);
            try {
                addresses = geocoder.getFromLocation(lat, lon, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();

            locations.add(address+" ,"+ city +" - "+postalCode +" - "+ country);
        }

        CustomReportList adapter = new CustomReportList(ReportsActivity.this, (ArrayList<ReportClass>) ownerReports, (ArrayList<String>) locations);
        reportsList.setAdapter(adapter);
    }

}
