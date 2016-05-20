package cat.proven.findmypet.findmypet;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import model.AnnouncementClass;
import model.OwnerClass;
import model.UserClass;
//import android.support.design.widget.TabLayout;

/**
 * Created by Alumne on 06/05/2016.
 */
public class ProfileActivity extends AppCompatActivity{
    ImageView profileImg;
    TextView name;
    SharedPreferences sharedpreferences;
    Button dataButton, reportsButton;
    ListView responseAnnouncements;
    List<AnnouncementClass> announcements = new ArrayList<>();
    AnnouncementClass announ = null;
    String imagePath="", ba1 ="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        SharedPreferences userDetails = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        String userName = userDetails.getString("userName", "");
        profileImg = (ImageView) findViewById(R.id.profileImg);
        profileImg.setImageResource(R.drawable.user);

        name = (TextView) findViewById(R.id.name);
        name.setText("Welcome "+userName.substring(0,1).toUpperCase() + userName.substring(1)+"!");

        loadResponseReports();
        dataButton = (Button)findViewById(R.id.modifyButton);
        reportsButton = (Button)findViewById(R.id.button2);

        //modify user and owner (user)
        dataButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadBasicDataLayout();
            }
        });

        profileImg.setOnHoverListener(new View.OnHoverListener() {

            @Override
            public boolean onHover(View v, MotionEvent event) {
                profileImg.setImageAlpha(80);
                return true;
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openGallery(1);
            }
        });
        //own reports of user
        reportsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadReportsLayout();
            }
        });

 }

    public void openGallery(int req_code){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select image to modify "), req_code);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                profileImg.setImageBitmap(image);
                profileImg.setImageAlpha(249);
                imagePath = selectedImageUri.getPath();
                //new modifyProfilePhoto().execute(image);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImage(){
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba, 0);
        // Upload image to server

    }

    public void loadBasicDataLayout(){
        Intent in = new Intent(ProfileActivity.this,BasicDataActivity.class);
        startActivity(in);
        this.finish();
    }

    public void loadReportsLayout(){
    Intent in = new Intent(ProfileActivity.this,ReportsActivity.class);
        startActivity(in);
        this.finish();
    }

    public void loadResponseReports(){
        SharedPreferences userDetails = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        int id = userDetails.getInt("id",0);
        new loadResponseReports().execute(id);

        responseAnnouncements = (ListView)findViewById(R.id.resAnnouncementsList);
        CustomAnnouncementList adapter = new CustomAnnouncementList(ProfileActivity.this, (ArrayList<AnnouncementClass>) announcements);
        responseAnnouncements.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, Menu.FIRST, Menu.NONE, "Wall").setIcon(R.drawable.profile);
        menu.add(0, Menu.FIRST + 1, Menu.NONE, "Report pet").setIcon(R.drawable.report);
        menu.add(0, Menu.FIRST + 2, Menu.NONE, "Settings").setIcon(R.drawable.setting);
        menu.add(0, Menu.FIRST + 3, Menu.NONE, "Logout").setIcon(R.drawable.logout);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                 Intent intent = new Intent(this, MainActivity.class);
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

            default:
                break;
        }
        return true;
    }

private class loadResponseReports extends AsyncTask<Integer, Void, List<AnnouncementClass>> {

        @Override
        protected List<AnnouncementClass> doInBackground(Integer... params) {

            String urlString = "http://provenapps.cat:8080/RestFulFindMyPet/restful/announcements/getResponseAnnouncements/";

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

            HashMap<String, Integer> postDataParams = new HashMap<String, Integer>();
            postDataParams.put("id_user", params[0]);

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
                } catch (IOException e2) {
                    e2.printStackTrace();
                }

            }
            else {
                response="";

            }

            JsonArray jArray= null;
            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
            jArray = jsonObject.getAsJsonArray("responseAnnouncements");

            for(int i = 0; i< jArray.size(); i++){
                JsonElement q = jArray.get(i);
                announ = new Gson().fromJson(q, AnnouncementClass.class);
                announcements.add(announ);
            }

            return announcements;
        }
        private String getPostDataString(HashMap<String, Integer> params) throws UnsupportedEncodingException{
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, Integer> entry : params.entrySet()){
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
        protected void onPostExecute(List<AnnouncementClass> announcements) {

        }
    }

    private class modifyProfilePhoto extends AsyncTask<Bitmap, Void, Integer> {

        @Override
        protected Integer doInBackground(Bitmap... params) {
            String urlString = "http://provenapps.cat:8080/RestFulFindMyPet/restful/users/modifyUserImage/";

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
            /*postDataParams.put("id_user", String.valueOf(o.getIdUser()));
            postDataParams.put("name", o.getName());
            postDataParams.put("firstname", o.getFirstname());
            postDataParams.put("surname", o.getSurname());*/


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

        }
    }

    /*responseAnnouncements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
        int position, long id) {
            messageBox("Has clickado en "+ announcements.get(position).getIdUser());
        }

    });*/
    private void messageBox(String mensaje) {
        Toast.makeText(this.getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void logout() {
        sharedpreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
}
