package com.example.actc.fixme1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.gms.common.util.CollectionUtils.mapOf;

public class MainActivity extends AppCompatActivity {
    @BindView
            (R.id.btnGo)Button btnGo;
    @BindView
            (R.id.goSignIn)TextView goSignIn;
    @BindView
            (R.id.inputName) TextView txtName;
    @BindView
            (R.id.inputPassword)  TextView txtPassword;
    @BindView
            (R.id.spinner) Spinner spinnerSpecialist;
    @BindView
            (R.id.checkBox) CheckBox ifSpecialist;

    private FirebaseAuth mAuth;
    Boolean valIfSpecialist;
    ArrayList<String> listSpecialistType = new ArrayList<String>();
    public static String ELECTRICAN = "Electrician";
    public static String PLUMBER = "Plumber";
    public static String NETWORK = "network";
    public static String COFFE_MACHIEN = "Coffee Machine";
    public static String COOLING_AIR_CONDITION = "Cooling and Air Conditioning";
    public static String MECHANICAL = "Mechanical";
    public static String POLICE = "police";
    public static String SelectedSpecialist = "";
    String email = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        mAuth = FirebaseAuth.getInstance();


        //----- read the checkbox ----
        valIfSpecialist = ifSpecialist.isChecked();

        ifSpecialist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true) {
                    spinnerSpecialist.setVisibility(View.VISIBLE);
                    if (listSpecialistType.isEmpty()) {
                        listSpecialistType.add(MECHANICAL);
                        listSpecialistType.add(POLICE);
                        listSpecialistType.add(COOLING_AIR_CONDITION);
                        listSpecialistType.add(COFFE_MACHIEN);
                        listSpecialistType.add(NETWORK);
                        listSpecialistType.add(PLUMBER);
                        listSpecialistType.add(ELECTRICAN);
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, listSpecialistType);
                    spinnerSpecialist.setAdapter(arrayAdapter);


                } else {

                    spinnerSpecialist.setVisibility(View.INVISIBLE);

                }

            }
        });


        goSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });


        /// make adapter to fill in the Spinner


//set the spinners adapter to the previously created one.
        //   spinnerSpecialist.setAdapter();


        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //TODO(1):READ THE VALUE FORM THE CHECKED BOX AND FROM THE SPINNER

                valIfSpecialist = ifSpecialist.isChecked();
                if (valIfSpecialist == true)
                    SelectedSpecialist = spinnerSpecialist.getSelectedItem().toString();
                else
                    SelectedSpecialist = "";

/////sign in
                email = txtName.getText().toString();
                password = txtPassword.getText().toString();

                if (!email.isEmpty()) {
                    if (!password.isEmpty())


                        mAuth.signInWithEmailAndPassword(txtName.getText().toString(), txtPassword.getText().toString())
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(MainActivity.this, R.string.The_User_Existsd, Toast.LENGTH_SHORT).show();
                                                    // Sign in success, update UI with the signed-in user's information
                                                } else {

                                                    mAuth.createUserWithEmailAndPassword(txtName.getText().toString(), txtPassword.getText().toString())
                                                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    if (task.isSuccessful()) {
                                                                        // Sign in success, update UI with the signed-in user's information
                                                                        Register(task.getResult().getUser().getUid().toString());


                                                                        login();

                                                                    } else {

                                                                        Toast.makeText(getApplicationContext(), R.string.loginfalied, Toast.LENGTH_LONG).show();

                                                                    }

                                                                }
                                                            });
                                                }

                                            }
                                        }


                                );


                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter The Requiered Data", Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    //---
    public void login() {

        Intent intent = new Intent(this, SnapActivity.class);
        startActivity(intent);
    }

    //--

    public void Register(String UserID) {
        Map<String, String> snapMap = mapOf("email", txtName.getText().toString(), "Password", txtPassword.getText().toString(), "ifSpecialist", valIfSpecialist.toString(), "SpecialistType", SelectedSpecialist);

        FirebaseDatabase.getInstance().getReference().child("users").
                child(UserID).setValue(snapMap);

    }
  /*  public  void Register (){
        Map<String,String> snapMap = mapOf("Email",txtName.getText().toString(),"Password",txtPassword.getText().toString(),"ifSpecialist",valIfSpecialist.toString(),"SpecialistType",spinnerSpecialist.getSelectedItem().toString());

        FirebaseDatabase.getInstance().getReference().child("users").
                child(UUID.randomUUID().toString()).setValue(snapMap);

    }*/

}
