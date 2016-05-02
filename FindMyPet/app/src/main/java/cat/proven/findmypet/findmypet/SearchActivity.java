package cat.proven.findmypet.findmypet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        UserClass u = new UserClass();
        u.setUserName(text);
        u.setEmail(text);
        OwnerClass o = new OwnerClass();
        o.setName(text);

        UserModel uModel = new UserModel();
        List<OwnerClass> ownerList = uModel.searchUser(u,o);
    }


    public void cridaActivityRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void messageBox(String mensaje){
        Toast.makeText(this.getApplicationContext(),mensaje, Toast.LENGTH_SHORT).show();
    }


}
