package com.example.beingthere2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class loginActivity extends AppCompatActivity {

    EditText username,password;
    String item,dbpassword;
    String userid,pass;
    Button button;
    ProgressDialog mDialog;
    Bundle basket;
    DatabaseReference ref;
    private static long back_pressed;
    RadioButton admin,teacher,student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        admin=findViewById(R.id.adminbutton);
        teacher=findViewById(R.id.teacherbutton);
        student=findViewById(R.id.studentbutton);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
    }

    public void loginFunction(View view){
        AlphaAnimation buttonClick = new AlphaAnimation(2F, 0.9F);
        view.startAnimation(buttonClick);

        if(admin.isChecked()){
            //item=admin.getText().toString();
            item="Admin";
        }else if(teacher.isChecked()){
            //item=teacher.getText().toString();
            item="Teacher";
        }else if(student.isChecked()) {
            //item=student.getText().toString();
            item = "Student";
        }

        userid = username.getText().toString();
        pass = password.getText().toString();
        mDialog=new ProgressDialog(loginActivity.this);
        basket = new Bundle();
        basket.putString("message", userid);

        ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbuser = ref.child(item).child(userid);

        if(userid.isEmpty()) {
            username.setError("Please enter your username");
            username.requestFocus();
        }else if(pass.isEmpty()){
            password.setError("Provide your password");
            password.requestFocus();
        }else {
            mDialog.setMessage("Please Wait...");
            mDialog.setTitle("Loading");
            mDialog.show();
            dbuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String dbchild = null;
                    try {
                        if (item == "Admin") {
                            mDialog.dismiss();
                            dbpassword = dataSnapshot.getValue(String.class);
                            verify(dbpassword);
                        } else {
                            mDialog.dismiss();
                            if (item == "Student") {
                                dbchild = "spass";
                            }
                            if (item == "Teacher") {
                                dbchild = "tpass";
                            }
                            dbpassword = dataSnapshot.child(dbchild).getValue(String.class);
                            verify(dbpassword);
                        }
                    } catch (Exception e) {
                        Toast.makeText(loginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void verify(String dbpassword) {
        try {
            if (userid.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Username cannot be empty", Toast.LENGTH_LONG).show();
            } else if (item == "Teacher" && pass.equalsIgnoreCase(this.dbpassword)) {

                mDialog.dismiss();
                Intent intent = new Intent(loginActivity.this, teacherActivity.class);
                intent.putExtras(basket);
                startActivity(intent);

            } else if (item == "Admin" && pass.equalsIgnoreCase(loginActivity.this.dbpassword)) {
                //  if (userid.equalsIgnoreCase("admin") && pass.equals("admin")) {
                mDialog.dismiss();
                Intent intent = new Intent(loginActivity.this, adminActivity.class);
                intent.putExtras(basket);
                startActivity(intent);
                //  }
            } else if (item == "Student" && pass.equalsIgnoreCase(this.dbpassword)) {
                mDialog.dismiss();
                Intent intent = new Intent(loginActivity.this, studentActivity.class);
                intent.putExtras(basket);
                startActivity(intent);
            } else if (!pass.equalsIgnoreCase(this.dbpassword)) {
                Toast.makeText(getApplicationContext(), "UserId or Password is Incorrect", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(loginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            finish();
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }
        else {
            Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }
}