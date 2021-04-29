package cyrille.mbia.gestiondesprsences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import cyrille.mbia.gestiondesprsences.realm.Class_Names;
import io.realm.Realm;
import io.realm.RealmAsyncTask;

public class insert_class extends AppCompatActivity {

    Button create_button;
    EditText _className;
    EditText _subjectName;

    Realm realm;
    RealmAsyncTask transaction;

    private  String position_bg = "0";

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_class);

        Toolbar toolbar = findViewById(R.id.toolbar_insert_class);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        create_button = findViewById(R.id.button_createClass);
        _className = findViewById(R.id.className_createClass);
        _subjectName = findViewById(R.id.subjectName_createClass);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        final RadioRealButton button1 = (RadioRealButton) findViewById(R.id.button1);
        final RadioRealButton button2 = (RadioRealButton) findViewById(R.id.button2);
        final RadioRealButton button3 = (RadioRealButton) findViewById(R.id.button3);
        final RadioRealButton button4 = (RadioRealButton) findViewById(R.id.button4);
        final RadioRealButton button5 = (RadioRealButton) findViewById(R.id.button5);
        final RadioRealButton button6 = (RadioRealButton) findViewById(R.id.button6);

        RadioRealButtonGroup group = (RadioRealButtonGroup) findViewById(R.id.group);
        group.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                position_bg = String.valueOf(position);
            }
        });

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValid()) {

                    final ProgressDialog progressDialog = new ProgressDialog(insert_class.this);
                    progressDialog.setMessage("Creating class..");
                    progressDialog.show();

                    transaction = realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Class_Names class_name = realm.createObject(Class_Names.class);
                            String id = _className.getText().toString() + _subjectName.getText().toString();
                            class_name.setId(id);
                            class_name.setName_class(_className.getText().toString());
                            class_name.setName_subject(_subjectName.getText().toString());
                            class_name.setPosition_bg(position_bg);
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            progressDialog.dismiss();
                            Toast.makeText(insert_class.this, "Créee", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            progressDialog.dismiss();
                            Toast.makeText(insert_class.this, "Erreur!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(insert_class.this, "Remplir toutes les informations", Toast.LENGTH_SHORT).show();
                }

                //-------

            }
        });
    }

    public boolean isValid(){

        return !_className.getText().toString().isEmpty() && !_subjectName.getText().toString().isEmpty();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}