package cat.proven.findmypet.findmypet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import model.UserClass;
import model.UserModel;

/**
 * Created by Alumne on 30/04/2016.
 */
public class LoginActivity extends AppCompatActivity {
    Button loginButton, registerButton;
    EditText usuariText, passwordText;
    SharedPreferences sharedpreferences;
    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        loginButton = (Button)findViewById(R.id.loginButton);
        registerButton= (Button)findViewById(R.id.registerButton);
        usuariText = (EditText)findViewById(R.id.usuariEditText);
        passwordText = (EditText)findViewById(R.id.passwordEditText);
        sharedpreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loginUser(usuariText.getText().toString(), passwordText.getText().toString());

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            //iniciar la actividad para enseñar el formulario de registro
                cridaActivityRegister();
            setContentView(R.layout.register_layout);
            }
        });
    }

    public void loginUser(String username, String password){
        UserClass u = new UserClass(username, password);
        UserModel umodel = new UserModel();
        int res = umodel.login(u);

        if(res == 1){
            messageBox("Este usuario SI existe!");
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("userName", usuariText.getText().toString());
            editor.commit();

            in = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(in);
        }else{
            messageBox("Este usuario NO existe!");
        }
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
