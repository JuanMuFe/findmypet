package cat.proven.findmypet.findmypet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import android.support.design.widget.TabLayout;

/**
 * Created by Alumne on 06/05/2016.
 */
public class ProfileActivity extends AppCompatActivity{
    ImageView profileImg;
    TextView name;
    SharedPreferences sharedpreferences;
    Button dataButton, reportsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        profileImg = (ImageView) findViewById(R.id.profileImg);
        profileImg.setImageResource(R.drawable.user);

        name = (TextView) findViewById(R.id.name);
        name.setText("Juan Muñoz Fernandez");


        dataButton = (Button)findViewById(R.id.button);
        reportsButton = (Button)findViewById(R.id.button2);

        //modify user and owner (user)
        dataButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadBasicDataLayout();
            }
        });

        //own reports of user
        reportsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadReportsLayout();
            }
        });


    }

    public void loadBasicDataLayout(){

    }

    public void loadReportsLayout(){

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
                messageBox("Wall");
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
