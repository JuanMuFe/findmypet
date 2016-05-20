package cat.proven.findmypet.findmypet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import model.AnnouncementClass;
import model.NotificationClass;
import model.OwnerClass;
import model.UserClass;


public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    ListView announcementsView;
    List<AnnouncementClass> announcements = new ArrayList<AnnouncementClass>();
    List<NotificationClass> notifications = new ArrayList<NotificationClass>();
    private Activity activity;
    private Context context = this;
    public int id_user;
    public String announcementDescription;
    Button addAnnouncementButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

addAnnouncementButton = (Button)findViewById(R.id.addAnnouncementButton);
        SharedPreferences userDetails = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        String userName = userDetails.getString("userName", "");
        int idProfile = userDetails.getInt("profile",2);
        int id = userDetails.getInt("id",4);

        new getNotifications().execute(String.valueOf(id));
   addAnnouncementButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createModal();
            }
        });


        switch(idProfile)
        {
            case 1:
                //admin area

                break;
            case 2:
                //user area
                new HttpRequestTask().execute();



                    announcementsView = (ListView) findViewById(R.id.announcementsList);
                    CustomAnnouncementList adapter = new CustomAnnouncementList(MainActivity.this, (ArrayList<AnnouncementClass>) announcements);
                    announcementsView.setAdapter(adapter);

                    announcementsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                           final int id_user_post = announcements.get(position).getIdUser();
                        createAndShowModal(id_user_post);
                        }

                    });

                break;
            default:

                finish();
            break;
        }

    }
    public void createModal(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_announcement_layout);
        dialog.setTitle("Add Announcement");

        final EditText writed = (EditText)dialog.findViewById(R.id.editTextDialog);

        Button dialogOkButton = (Button) dialog.findViewById(R.id.buttonOKDialog);
        Button dialogCancelButton = (Button) dialog.findViewById(R.id.buttonCloseDialog);

        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                announcementDescription = writed.getText().toString();
                if(announcementDescription != ""){
                    new addAnnouncement().execute(announcementDescription, String.valueOf(id_user));
                }
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    public void createAndShowModal(final int id_user_post) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_announcement_layout);
        dialog.setTitle("Add Announcement Response");

        ImageView image = (ImageView)dialog.findViewById(R.id.imageDialog);
        final EditText writed = (EditText)dialog.findViewById(R.id.editTextDialog);

        Button dialogOkButton = (Button) dialog.findViewById(R.id.buttonOKDialog);
        Button dialogCancelButton = (Button) dialog.findViewById(R.id.buttonCloseDialog);

        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                announcementDescription = writed.getText().toString();
                if(announcementDescription != ""){
                    new addResponseAnnouncement().execute(announcementDescription,String.valueOf(id_user_post), String.valueOf(id_user));
                }
                dialog.dismiss();

            }
        });

        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, Menu.FIRST,   Menu.NONE, "Profile").setIcon(R.drawable.profile);
        menu.add(0, Menu.FIRST+1, Menu.NONE, "Report pet").setIcon(R.drawable.report);
        menu.add(0, Menu.FIRST+2, Menu.NONE, "Settings").setIcon(R.drawable.setting);
        menu.add(0, Menu.FIRST+3, Menu.NONE, "Logout").setIcon(R.drawable.logout);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent =null;
        switch(item.getItemId()){
            case 1:
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                this.finish();
                break;

            case 2:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                this.finish();
                break;

            case 3:
                messageBox("Settings");
                break;

            case 4:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                logout();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;

            default : break;
        }
        return true;
    }

    public void logout(){
        sharedpreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void messageBox(String mensaje){
        Toast.makeText(this.getApplicationContext(),mensaje, Toast.LENGTH_SHORT).show();
    }

    public Activity getActivity() {
        return activity;
    }

    private class HttpRequestTask extends AsyncTask<String, String, List<AnnouncementClass>> {

        @Override
        protected List<AnnouncementClass> doInBackground(String... params) {

            String urlString = "http://provenapps.cat:8080/RestFulFindMyPet/restful/announcements/getAnnouncements/";

            URL url = null;

 //http://localhost:8080/RestFulFindMyPet/restful/users/login/admin/admin
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
                jArray = jsonObject.getAsJsonArray("announcements");
                AnnouncementClass ann=null;

                for (int i = 0; i < jArray.size(); i++) {
                    JsonElement q = jArray.get(i);
                    ann = new Gson().fromJson(q, AnnouncementClass.class);
                    announcements.add(ann);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return announcements;
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
        protected void onPostExecute(List<AnnouncementClass> announcements) {


        }


    }

    public void createNotification(List<NotificationClass> notifications) {
        int i=0;

        for(NotificationClass n :notifications)
        {
            // Prepare intent which is triggered if the
            // notification is selected
            Intent intent = new Intent(this, NotificationReceiverActivity.class);
            intent.putExtra("idreport", String.valueOf(n.getId_report()));
            PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

            // Build notification
            // Actions are just fake
            Notification noti = new Notification.Builder(this)
                    .setContentTitle("New notification from findmypet")
                    .setContentText(n.getDescription()).setSmallIcon(R.drawable.ic_cast_disabled_light)
                    .setContentIntent(pIntent)
                    .addAction(R.drawable.ic_cast_disabled_light, "Call", pIntent)
                    .addAction(R.drawable.ic_cast_disabled_light, "More", pIntent)
                    .addAction(R.drawable.ic_cast_disabled_light, "And more", pIntent).build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // hide the notification after its selected
            noti.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(i, noti);
            i++;
        }


    }

    private class getNotifications extends AsyncTask<String, String, List<NotificationClass>> {

        @Override
        protected List<NotificationClass> doInBackground(String... params) {

            String urlString = "http://provenapps.cat:8080/RestFulFindMyPet/restful/notifications/getUserNotifications/";
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
            postDataParams.put("id_user", String.valueOf(params[0]));

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
            jArray = jsonObject.getAsJsonArray("userNotifications");
            NotificationClass noti=null;

            if(jArray.size()>0) {
                for (int i = 0; i < jArray.size(); i++) {
                    JsonElement q = jArray.get(i);
                    noti = new Gson().fromJson(q, NotificationClass.class);
                    notifications.add(noti);
                }
            }


            return notifications;
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
        protected void onPostExecute(List<NotificationClass> notifications) {

            if (notifications.size() > 0) {
                createNotification(notifications);

            }
        }

    }


private class addAnnouncement extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            String urlString = "http://provenapps.cat:8080/RestFulFindMyPet/restful/announcements/add/";
            String description = params[0];
            String id_user = params[1];

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
            postDataParams.put("description", description);
            postDataParams.put("id_user", id_user);

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
            if(resultQ > 0){
                messageBox("Announcement added correctly.");
            }else{ messageBox("Some error occured adding the announcement."); }


        }
    }

  private class addResponseAnnouncement extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            String urlString = "http://provenapps.cat:8080/RestFulFindMyPet/restful/announcements/add/";
            String description = params[0];
            String id_user_post = params[1];
            String id_user = params[2];

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
            postDataParams.put("description", description);
            postDataParams.put("id_user", id_user);
            postDataParams.put("id_foreign_user", id_user_post);


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
            if(resultQ > 0){
                messageBox("Announcement response added correctly.");
            }else{ messageBox("Some error occured adding the response."); }


        }
    }

}