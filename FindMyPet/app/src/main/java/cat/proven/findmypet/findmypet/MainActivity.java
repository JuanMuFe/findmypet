package cat.proven.findmypet.findmypet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        SharedPreferences userDetails = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        String userName = userDetails.getString("userName", "");
        int idProfile = userDetails.getInt("profile",0);
        int id = userDetails.getInt("id",0);

        new getNotifications().execute(String.valueOf(id));

        switch(idProfile)
        {
            case 1:
                //admin area

                break;
            case 2:
                //user area
                new HttpRequestTask().execute();

                announcementsView = (ListView)findViewById(R.id.announcementsList);
                CustomAnnouncementList adapter = new CustomAnnouncementList(MainActivity.this, (ArrayList<AnnouncementClass>) announcements);
                announcementsView.setAdapter(adapter);

                announcementsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        messageBox("Has clickado en "+ announcements.get(position).getIdUser());
                    }

                });
                break;
            default:
                finish();
            break;
        }




       /* announcements.add(new AnnouncementClass("He encontrado una fantastica protectora de animales!", "01/05/2016", 2));
        announcements.add(new AnnouncementClass("He visto un perro abandonado.", "03/04/2016", 3));
        announcements.add(new AnnouncementClass("Esta mañana hace un precioso dia para pasear a tu animal!", "18/01/2015", 3));*/



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, Menu.FIRST,   Menu.NONE, "Profile").setIcon(getResources().getDrawable(R.drawable.profile));
        menu.add(0, Menu.FIRST+1, Menu.NONE, "Report pet").setIcon(R.drawable.report);
        menu.add(0, Menu.FIRST+2, Menu.NONE, "Settings").setIcon(R.drawable.setting);
        menu.add(0, Menu.FIRST+3, Menu.NONE, "Logout").setIcon(R.drawable.logout);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case 1:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                this.finish();
                break;

            case 2:
                messageBox("Report");
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

    private class HttpRequestTask extends AsyncTask<String, String, List<AnnouncementClass>> {

        @Override
        protected List<AnnouncementClass> doInBackground(String... params) {

            String urlString = "http://provenapps.cat:8080/RestFulFindMyPet/restful/announcements/getAnnouncements/";

            URL url = null;

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

        for(NotificationClass n :notifications)
        {
            // Prepare intent which is triggered if the
            // notification is selected
            Intent intent = new Intent(this, NotificationReceiverActivity.class);
            intent.putExtra("id_report", n.getId_report());
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

            notificationManager.notify(0, noti);
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
            jArray = jsonObject.getAsJsonArray("owners");
            NotificationClass noti=null;

            for (int i = 0; i < jArray.size(); i++) {
                JsonElement q = jArray.get(i);
                noti = new Gson().fromJson(q, NotificationClass.class);
                notifications.add(noti);
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

            createNotification(notifications);
        }


    }
}
