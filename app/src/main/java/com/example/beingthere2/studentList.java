package com.example.beingthere2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class studentList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner studlist;
    String item;
    ListView studentlist;
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    ArrayList listStudent = new ArrayList<>();
    ArrayList studentname = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        studentlist=findViewById(R.id.studentListView);

        studlist=findViewById(R.id.spinnerStud);
        studlist.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("TE-A");
        categories.add("TE-B");
        categories.add("TE-C");
        ArrayAdapter<String> studadapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,categories);
        studadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        studlist.setAdapter(studadapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
     item=parent.getItemAtPosition(position).toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void showStudent(View view){
        AlphaAnimation buttonClick = new AlphaAnimation(2F, 0.9F);
        view.startAnimation(buttonClick);

        listStudent.clear();
        studentname.clear();
        DatabaseReference studRef=reference.child("Student");
        studRef.orderByChild("classname").equalTo(item).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    listStudent.add(dsp.child("rollno").getValue().toString());
                    studentname.add(dsp.child("sname").getValue().toString());
                }
                display(listStudent,studentname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void display(ArrayList<String> listStudent,ArrayList<String> studentname){
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, studentname);
        studentlist.setAdapter(aa);
    }
}
