package com.example.beingthere2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addteacher extends AppCompatActivity {

    EditText Tname,Tid,subject,tpassword,classes;
    String tname,tid,sub,classname,tpass;
    DatabaseReference databaseTeacher;
    TextView tescherView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addteacher);

        databaseTeacher = FirebaseDatabase.getInstance().getReference("Teacher");

        Tname = findViewById(R.id.editText1);
        Tid = findViewById(R.id.editText3);
        subject = findViewById(R.id.editText4);
        classes = findViewById(R.id.year);
        tpassword = findViewById(R.id.editText5);

        tescherView=findViewById(R.id.Teacherlist);
        tescherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3=new Intent(addteacher.this,teacherList.class);
                startActivity(intent3);
            }
        });
    }

    public void addTeacher(View view){
        AlphaAnimation buttonClick = new AlphaAnimation(2F, 0.9F);
        view.startAnimation(buttonClick);

        tname = Tname.getText().toString();
        tid = Tid.getText().toString();
        sub = subject.getText().toString().toUpperCase();
        classname = classes.getText().toString();
        tpass = tpassword.getText().toString();

        if (!(TextUtils.isEmpty(Tid.getText().toString()))) {
            // String id = databaseTeacher.push().getKey();
            Teacher teacher =new Teacher(tname ,tid ,sub ,classname,tpass);
            databaseTeacher.child(tid).setValue(teacher);
            Toast.makeText(getApplicationContext(),"Teacher added successfully", Toast.LENGTH_LONG).show();
            finish();

        }else {
            Toast.makeText(getApplicationContext(),"fields cannot be empty", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
