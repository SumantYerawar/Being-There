package com.example.beingthere2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addstudent extends AppCompatActivity {

    EditText Sname,Sid,spassword,division,batch,year,Rollno;
    String sname,sid,classname,spass,rollno,sdiv,sbatch,syear;
    Spinner classes;
    DatabaseReference databaseStudent;
    TextView studlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstudent);

        databaseStudent = FirebaseDatabase.getInstance().getReference("Student");

        Sname =  (EditText) findViewById(R.id.editText1);
        Sid =  (EditText) findViewById(R.id.editText3);
        classes = (Spinner) findViewById(R.id.spinner3);
        spassword = (EditText) findViewById(R.id.editText4);
        batch=findViewById(R.id.batch);
        Rollno=findViewById(R.id.rollno);

        studlist=findViewById(R.id.studlist);

        studlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2= new Intent(addstudent.this, studentList.class);
                startActivity(intent2);
            }
        });
    }

    public void addStudent(View view){
        AlphaAnimation buttonClick = new AlphaAnimation(2F, 0.9F);
        view.startAnimation(buttonClick);

        if (!(TextUtils.isEmpty(Sid.getText().toString()))) {
            //String id = databaseStudent.push().getKey();
            sname = Sname.getText().toString();
            sid = Sid.getText().toString();
            classname = classes.getSelectedItem().toString().toUpperCase();
            spass = spassword.getText().toString();
            sbatch=batch.getText().toString().toUpperCase();
            rollno=Rollno.getText().toString();

            //sid=classname.concat("-").concat(syear).concat("-").concat(sdiv).concat("-").concat(uniqueId);
            final Student student = new Student(sname,classname,sbatch,rollno,sid,spass);
            databaseStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(sid).exists()){
                        Toast.makeText(getApplicationContext(), "Username Already Present", Toast.LENGTH_SHORT).show();
                    }else{
                        databaseStudent.child(sid).setValue(student);
                        Toast.makeText(getApplicationContext(), "student added successfully" , Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(addstudent.this,adminActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            Toast.makeText(getApplicationContext(),"fields cannot be empty", Toast.LENGTH_LONG).show();
        }
    }

   /* public void removeStudent(View view){
        if (!TextUtils.isEmpty(Sid.getText().toString())) {
            uniqueId = Sid.getText().toString();
            databaseStudent.child(sid).child(uniqueId).setValue(null);
            Toast.makeText(getApplicationContext(),"Student removed successfully", Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(getApplicationContext(),"id cannot be empty", Toast.LENGTH_LONG).show();
        }
    }*/
}
