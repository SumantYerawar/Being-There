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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class teacherList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner Teacherlist;
    String item;
    ArrayList listTeacher = new ArrayList<>();

    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);

        Teacherlist=findViewById(R.id.spinnerTeacher);
        Teacherlist.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("TE-A");
        categories.add("TE-B");
        categories.add("TE-C");
        ArrayAdapter<String> teachadapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,categories);
        teachadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Teacherlist.setAdapter(teachadapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void showTeacherList(View view){
        AlphaAnimation buttonClick = new AlphaAnimation(2F, 0.9F);
        view.startAnimation(buttonClick);

        listTeacher.clear();
        DatabaseReference studRef=reference.child("Teacher");
        studRef.orderByChild("classes").equalTo(item).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    listTeacher.add(dsp.child("tname").getValue().toString());
                }
                display(listTeacher);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void display(ArrayList<String> listTeacher){
        ListView teacherlist=findViewById(R.id.TeacherListView);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listTeacher);
        teacherlist.setAdapter(aa);
    }
}
