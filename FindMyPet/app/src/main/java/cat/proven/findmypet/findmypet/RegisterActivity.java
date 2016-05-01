package cat.proven.findmypet.findmypet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Alumne on 30/04/2016.
 */
public class RegisterActivity extends AppCompatActivity {
    EditText name, firstname, surname, nif, birthdate, phone, address, username, email, password;
    Spinner country;
    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        name = (EditText)findViewById(R.id.nameEditText);
        firstname = (EditText)findViewById(R.id.firstnameEditText);
        surname = (EditText)findViewById(R.id.surnameEditText);
        nif = (EditText)findViewById(R.id.nifEditText);
        birthdate = (EditText)findViewById(R.id.birthdateEditText);
        phone = (EditText)findViewById(R.id.phoneEditText);
        address = (EditText)findViewById(R.id.addressEditText);
        username = (EditText)findViewById(R.id.usernameEditText);
        email = (EditText)findViewById(R.id.emailEditText);
        password = (EditText)findViewById(R.id.passwordEditText);
        continueButton = (Button)findViewById(R.id.continueButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //hacer lo que sea
            }
        });
    }
}
