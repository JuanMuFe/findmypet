package cat.proven.findmypet.findmypet;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import model.PetClass;
import model.PetModel;


/**
 * Created by Alumne on 30/04/2016.
 */
public class PetAdministrationActivity extends AppCompatActivity {
    Button sendButton,imageButton;
    EditText name, race,description;
    String picturePath="";

    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_administration_layout);

        sendButton = (Button)findViewById(R.id.sendButton);

        name = (EditText)findViewById(R.id.name);
        race = (EditText)findViewById(R.id.race);
        description = (EditText)findViewById(R.id.description);


        imageButton = (Button)findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                petAdministration(name.getText().toString(), race.getText().toString(),description.getText().toString(),picturePath);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();



            // String picturePath contains the path of selected Image
        }

    }

    private void petAdministration(String name,String race,String description,String image)
    {
        PetClass p = new PetClass(1,name,race,description,image);
        PetModel pModel = new PetModel();
        //Call to ado to delete or modify depends on what button is clicked

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
