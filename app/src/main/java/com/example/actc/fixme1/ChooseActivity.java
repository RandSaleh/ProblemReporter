package com.example.actc.fixme1;

import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.actc.fixme1.MainActivity.COFFE_MACHIEN;
import static com.example.actc.fixme1.MainActivity.COOLING_AIR_CONDITION;
import static com.example.actc.fixme1.MainActivity.ELECTRICAN;
import static com.example.actc.fixme1.MainActivity.MECHANICAL;
import static com.example.actc.fixme1.MainActivity.NETWORK;
import static com.example.actc.fixme1.MainActivity.PLUMBER;
import static com.example.actc.fixme1.MainActivity.POLICE;
import static com.google.android.gms.common.util.CollectionUtils.mapOf;

public class ChooseActivity extends AppCompatActivity {
    @BindView
            (R.id.lst)
    ListView chooseUser;

    @BindView
            (R.id.departmentName)
    Spinner SpinnerDeptName;

    ArrayList<String> emails = new ArrayList<String>();
    ArrayList<String> keys;
    ArrayList<String> specialistType = new ArrayList<String>();
    ArrayList<String> readySpecialistType = new ArrayList<String>();
    ArrayAdapter arrayAdapterForListOfSpecialist;
    String SelectedItemFromSpecialist = "";
    String message = "";
    String imageUrl = "";
    String ImageName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        if (intent.hasExtra(getString(R.string.message))) {
            message = intent.getStringExtra((getString(R.string.message)));

        }


        if (intent.hasExtra(getString(R.string.imageUrl))) {
            imageUrl = intent.getStringExtra((getString(R.string.imageUrl)));
            System.out.println("r2" + imageUrl);
        }


        if (intent.hasExtra(getString(R.string.ImageName))) {
            ImageName = intent.getStringExtra(getString(R.string.ImageName));

        }

        if (specialistType.isEmpty()) {
            specialistType.add(MECHANICAL);
            specialistType.add(POLICE);
            specialistType.add(COOLING_AIR_CONDITION);
            specialistType.add(COFFE_MACHIEN);
            specialistType.add(NETWORK);
            specialistType.add(PLUMBER);
            specialistType.add(ELECTRICAN);
        }
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, specialistType);
        SpinnerDeptName.setAdapter(arrayAdapter2);
        SpinnerDeptName.setSelection(-1);


        SpinnerDeptName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //--
                emails = new ArrayList<String>();
                keys = new ArrayList<String>();
                //---

                SelectedItemFromSpecialist = specialistType.get(i);

                pupulateData();

                arrayAdapterForListOfSpecialist = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, emails);
                chooseUser.setAdapter(arrayAdapterForListOfSpecialist);
                arrayAdapterForListOfSpecialist.notifyDataSetChanged();

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        chooseUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Map<String, String> snapMap = mapOf("from", FirebaseAuth.getInstance().getCurrentUser().getEmail(), "to", emails.get(position), "imageUrl", imageUrl, "ImageName", ImageName, "message", message);
                FirebaseDatabase.getInstance().getReference().child("users").child(keys.get(position)).child("snaps").push().setValue(snapMap);

                Intent intent = new Intent(getApplicationContext(), SnapActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


                Toast.makeText(ChooseActivity.this, emails.get(position), Toast.LENGTH_SHORT).show();

            }
        });


        //---


    }

    public void pupulateData() {


        FirebaseDatabase.getInstance().getReference().child(getString(R.string.users)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String email = (String) dataSnapshot.child(getString(R.string.email)).getValue();
                String tempUserSpicialist = (String) dataSnapshot.child(getString(R.string.SpecialistType)).getValue();

                if (tempUserSpicialist.equals(SelectedItemFromSpecialist)) {
                    emails.add(email);
                    keys.add(dataSnapshot.getKey());


                }

                arrayAdapterForListOfSpecialist.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
