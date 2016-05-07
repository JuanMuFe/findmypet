package cat.proven.findmypet.findmypet;

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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.AnnouncementClass;


public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    ListView announcementsView;
    List<AnnouncementClass> announcements = new ArrayList<AnnouncementClass>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        SharedPreferences userDetails = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        String userName = userDetails.getString("userName", "");
        int idProfile = userDetails.getInt("profile",0);

        switch(idProfile)
        {
            case 1:
                //admin area

                break;
            case 2:
                //user area
                new HttpRequestTask().execute();
                break;
            default:
                finish();
            break;
        }




       /* announcements.add(new AnnouncementClass("He encontrado una fantastica protectora de animales!", "01/05/2016", 2));
        announcements.add(new AnnouncementClass("He visto un perro abandonado.", "03/04/2016", 3));
        announcements.add(new AnnouncementClass("Esta mañana hace un precioso dia para pasear a tu animal!", "18/01/2015", 3));*/

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



            String urlString = "http://192.168.27.27:8080/RestFulFindMyPet/restful/announcement/getAnnouncements/";

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

                JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();

                announcements =  new Gson().fromJson(jsonObject.get("annoucements"), new TypeToken<List<List<String>>>() {}.getType());


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
}
