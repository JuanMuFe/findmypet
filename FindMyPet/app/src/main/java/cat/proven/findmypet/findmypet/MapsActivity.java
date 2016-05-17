package cat.proven.findmypet.findmypet;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import model.PetClass;
import model.ReportClass;


public class MapsActivity extends FragmentActivity  {
    String[] dades;
    GoogleMap googleMap;
    List<PetClass> pets = new ArrayList<>();
    int id=0;
    int idProfile=0;
    String userName="";
    private Spinner spinner;
    int flag=0;
    int id_report=0;
    String[] arrayToModify={""};
    Map<MarkerOptions, ReportClass> markerReport = new HashMap<MarkerOptions, ReportClass>();
    boolean flagToModify=false;
    String newposition="";
    int idPet=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //getting shared preferences parameters
        SharedPreferences userDetails = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        userName = userDetails.getString("userName", "");
        idProfile = userDetails.getInt("profile",0);
        id = userDetails.getInt("id",4);

        //load spinner
        spinner = (Spinner) findViewById(R.id.petsSpinner);

        new HttpRequestTask().execute();

      spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        flag=flag+1;
                        if (flag>1) {
                            PetClass pet = (PetClass) ((Spinner) findViewById(R.id.petsSpinner)).getSelectedItem();
                            new getPetById().execute(String.valueOf(pet.getId()));
                        }
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(getApplicationContext(), "Res Seleccionat",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting a reference to the map
        googleMap = supportMapFragment.getMap();

        new getAllPets().execute();

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                LatLng position = marker.getPosition();
                modalToModify(position);
            }
        });

        // Setting a click event handler for the map
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + "," + latLng.longitude);

                // Clears the previously touched position
                googleMap.clear();

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);

                if(!flagToModify) {
                  /*  postDataParams.put("id_owner", params[0]);
                    postDataParams.put("id_pet", params[1]);
                    postDataParams.put("location", params[2]);

                    postDataParams.put("id_user", params[4]);*/
                    int id_owner = pets.get(1).getIdOwner();
                    String location = latLng.latitude + "," + latLng.longitude;
                    int idUser = id;
                    modalToAdd(id_owner,location, String.valueOf(idUser));

                }

                if(flagToModify)
                {
                    newposition = latLng.latitude + "," + latLng.longitude;
                    new modifyReport().execute(arrayToModify);
                    flagToModify=false;
                }



            }
        });


    }

    private void controlSpinner(List<PetClass> pets)
    {

        ArrayAdapter<PetClass> adaptador =
                new ArrayAdapter<PetClass>(this,
                        android.R.layout.simple_spinner_item, pets);
        // si volem li podem canviar el tipus de layout per visualitzar el spinner
        adaptador.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        // Li afegimn les opcions al spinner
        spinner.setAdapter(adaptador);

    }

    private void setAllMarkers(List<ReportClass> petsReports) {

        List<MarkerOptions> markers = new ArrayList<>();


        if(petsReports.size()>0)
        {
            for (ReportClass report : petsReports) {

                MarkerOptions markerOptions = new MarkerOptions();

                String[] latlong = report.getLocation().split(",");
                double latitude = Double.parseDouble(latlong[0]);
                double longitude = Double.parseDouble(latlong[1]);
                LatLng latLng = new LatLng(latitude, longitude);
                // Setting the position for the marker
                markerOptions.position(latLng);
                //marker.report.getId();


                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(report.getPet().getName());
                markers.add(markerOptions);
                // Clears the previously touched position
                //googleMap.clear();

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions);

                markerReport.put(markerOptions, report);
            }
        }

       LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MarkerOptions marker : markers) {
            builder.include(marker.getPosition());
        }
       LatLngBounds bounds = builder.build();
        int padding = 100; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.moveCamera(cu);


    }

    private class HttpRequestTask extends AsyncTask<String, String, List<PetClass>> {

        @Override
        protected List<PetClass> doInBackground(String... params) {

            URL url = null;
            //http://localhost:8080/RestFulFindMyPet/restful/users/login/
            // String urlString = "http://192.168.27.27:8080/RestFulFindMyPet/restful/pets/searchOwnerPet";
            String urlString = "http://provenapps.cat:8080/RestFulFindMyPet/restful/pets/searchOwnerPet";
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
            postDataParams.put("id_user", String.valueOf(id));


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

            int responseCode = 0;
            try {
                responseCode = conn.getResponseCode();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            } else {
                response = "";

            }

            JsonArray jArray = null;
            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
            jArray = jsonObject.getAsJsonArray("ownerPets");
            PetClass pet = null;

            for (int i = 0; i < jArray.size(); i++) {
                JsonElement q = jArray.get(i);
                pet = new Gson().fromJson(q, PetClass.class);
                pets.add(pet);
            }


            return pets;
        }

        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
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
        protected void onPostExecute(List<PetClass> pets) {

            controlSpinner(pets);

        }


    }
    private class getAllPets extends AsyncTask<String, String, List<ReportClass>> {

        @Override
        protected List<ReportClass> doInBackground(String... params) {
            List<ReportClass> petsReports = new ArrayList<>();
            URL url = null;
            String urlString = "http://provenapps.cat:8080/RestFulFindMyPet/restful/pets/getAll";


            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection con = null;

            try {
                con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");

                con.connect();

                String response = getResponseBody(con);
                JsonArray jArray = null;
                JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                jArray = jsonObject.getAsJsonArray("reports");
                ReportClass report = null;

                for (int i = 0; i < jArray.size(); i++) {

                    JsonElement q = jArray.get(i);
                    report = new Gson().fromJson(q, ReportClass.class);
                    petsReports.add(report);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return petsReports;
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
        protected void onPostExecute(List<ReportClass> reports) {


            setAllMarkers(reports);

        }
    }
        private class getPetById extends AsyncTask<String, String, ReportClass> {

            @Override
            protected ReportClass doInBackground(String... params) {

                URL url = null;

                String urlString = "http://provenapps.cat:8080/RestFulFindMyPet/restful/pets/getPetById";
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
                postDataParams.put("id_pet", params[0]);


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

                JsonArray jArray = null;
                JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();

                ReportClass report=null;

                report = new Gson().fromJson(jsonObject.get("report"), ReportClass.class);

                return report;
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
            protected void onPostExecute(ReportClass report) {

                List<ReportClass> reports = new ArrayList<>();
                reports.add(report);
                setAllMarkers(reports);

            }

    }

    private class insertPet extends AsyncTask<String, String, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            URL url = null;

            String urlString = "http://provenapps.cat:8080/RestFulFindMyPet/restful/reports/add";
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
            postDataParams.put("id_owner", params[0]);
            postDataParams.put("id_pet", params[1]);
            postDataParams.put("location", params[2]);
            postDataParams.put("id_user", params[3]);

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

            return Integer.parseInt(response);
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



        }



    }

    private void modalToModify(final LatLng position)
    {
        final String[] extra = {""};
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
        alertDialog.setTitle("Modify report");
        alertDialog.setMessage("Enter information");

        final EditText input = new EditText(MapsActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

       // alertDialog.setIcon(R.drawable.key);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        extra[0] = input.getText().toString();
                        double latitude = position.latitude;
                        double longitude = position.longitude;
                        String location = latitude+","+longitude;

                        arrayToModify= cleanMapToModify(extra[0],location);


                       flagToModify=true;

                        messageBox("Introduce the position where you have found a pet");

                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    private void modalToAdd(final int id_owner, final String location, final String idUser)
    {

        final String[] extra = {""};
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);
        alertDialog.setTitle("Add report");
        alertDialog.setMessage("Select pet");

        final ArrayAdapter<PetClass> adp = new ArrayAdapter<PetClass>(MapsActivity.this,
                android.R.layout.simple_spinner_item, pets);


        final Spinner sp = new Spinner(MapsActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sp.setAdapter(adp);

        alertDialog.setView(sp);

        // alertDialog.setIcon(R.drawable.key);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        PetClass pet = (PetClass) sp.getSelectedItem();
                        idPet = pet.getId();
                        new insertPet().execute(String.valueOf(id_owner),String.valueOf(idPet),location,String.valueOf(idUser));

                        messageBox("Inserted");
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.create().show();








    }

    private void messageBox(String mensaje){
        Toast.makeText(this.getApplicationContext(),mensaje, Toast.LENGTH_SHORT).show();
    }

    private class modifyReport extends AsyncTask<String[], String, Integer> {

        @Override
        protected Integer doInBackground(String[]... params) {





            //id (id report),location, extra,id_user,id_notification,id_notification(old)


            URL url = null;



            String urlString = "http://provenapps.cat:8080/RestFulFindMyPet/restful/reports/modify";
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
            postDataParams.put("id", String.valueOf(params[0][0]));
            postDataParams.put("location", newposition);
            postDataParams.put("extra", params[0][2]);
            postDataParams.put("id_notification", params[0][4]);
          //  postDataParams.put("id_user", params[0][3]);

           // postDataParams.put("old_id_notification", params[0][5]);

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

            return Integer.parseInt(response);
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

            if(result==1)
            {
                finish();
                startActivity(getIntent());
            }

        }



    }

    private String[] cleanMapToModify(String extra, String location)
    {

        int id_user = id;
        int id_notification_old = 4;
        int id_notification = 3;


        String location2="";
        for (Map.Entry<MarkerOptions, ReportClass> entry : markerReport.entrySet()) {
            LatLng key = entry.getKey().getPosition();
            location2=key.latitude+","+key.longitude;
            if(location.equals(location2)) {
                id_report = entry.getValue().getId();

            }
        }
        //id (id report),location, extra,id_user,id_notification,id_notification(old)
        String[] array = {String.valueOf(id_report),location,extra, String.valueOf(id_user), String.valueOf(id_notification), String.valueOf(id_notification_old)};

        googleMap.clear();


        return array;
    }
}





