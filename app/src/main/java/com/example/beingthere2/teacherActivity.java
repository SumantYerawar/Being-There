package com.example.beingthere2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class teacherActivity extends AppCompatActivity {

    DatePickerDialog picker;

    String year1,subject,name;
    String date;
    String message;
    private static long back_pressed;
    TextView showDate;
    String[] year;
    String[] subject1;
    ArrayList<String> selectLecture;
    ArrayList<String> selectyear1;
    String selectyear,selectLec;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbteacher;
    DatabaseReference password1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        showDate=findViewById(R.id.showDate);
        showDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                final int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(teacherActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                showDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        //to get username from login page
        Bundle bundle1 = getIntent().getExtras();
        message = bundle1.getString("message");
        final TextView txtView = findViewById(R.id.textView1);


        dbteacher=reference.child("Teacher");
        dbteacher.child(message).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                year1=dataSnapshot.child("classes").getValue().toString();
                subject=dataSnapshot.child("subject").getValue().toString();
                name=dataSnapshot.child("tname").getValue().toString();
                txtView.setText("Welcome "+name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void takeAttendanceButton(View view){
        AlphaAnimation buttonClick = new AlphaAnimation(2F, 0.9F);
        view.startAnimation(buttonClick);

        date=showDate.getText().toString();

        year=year1.split(",");
        subject1=subject.split(",");
        selectyear1=new ArrayList<>(Arrays.asList(year));
        selectLecture=new ArrayList<>(Arrays.asList(subject1));

        if(date.equals("Select Date")){
            Toast.makeText(this, "Date is not Selected", Toast.LENGTH_SHORT).show();
        }else{
            if(selectLecture.size()>=2){
                //Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Fill the Details");
                final LayoutInflater inflater = this.getLayoutInflater();
                View add_menu_layout = inflater.inflate(R.layout.select_div_year, null);
                final EditText yearSelect= add_menu_layout.findViewById(R.id.selectYear);
                final EditText lecSelect= add_menu_layout.findViewById(R.id.selectLec);
                alertDialog.setView(add_menu_layout);

                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectyear=yearSelect.getText().toString();
                        selectLec=lecSelect.getText().toString();

                        if(selectLecture.contains(selectLec) && selectyear1.contains(selectyear)) {
                            Bundle basket= new Bundle();
                            basket.putString("tid", message);
                            basket.putString("year",selectyear);
                            basket.putString("subject",selectLec);
                            basket.putString("date",date);

                            Intent intent = new Intent(teacherActivity.this, takeAttendance.class);
                            intent.putExtras(basket);
                            startActivity(intent);
                        }else {
                            Toast.makeText(teacherActivity.this, "Enter valid details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertDialog.show();
            }else{
                Bundle basket= new Bundle();
                basket.putString("tid", message);
                basket.putString("year",year1);
                basket.putString("subject",subject);
                basket.putString("date",date);

                Intent intent = new Intent(this, takeAttendance.class);
                intent.putExtras(basket);
                startActivity(intent);
            }
        }
    }

    public void  previous_records(View view){
        AlphaAnimation buttonClick = new AlphaAnimation(2F, 0.9F);
        view.startAnimation(buttonClick);

        year=year1.split(",");
        subject1=subject.split(",");
        selectyear1=new ArrayList<>(Arrays.asList(year));
        selectLecture=new ArrayList<>(Arrays.asList(subject1));

        if(selectLecture.size()>=2){

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Enter the Batch");
            final LayoutInflater inflater = this.getLayoutInflater();
            View add_menu_layout = inflater.inflate(R.layout.select_div_year, null);
            final EditText yearSelect= add_menu_layout.findViewById(R.id.selectYear);
            final EditText lecSelect= add_menu_layout.findViewById(R.id.selectLec);
            alertDialog.setView(add_menu_layout);

            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectyear=yearSelect.getText().toString();
                    selectLec=lecSelect.getText().toString();

                    if(selectLecture.contains(selectLec) && selectyear1.contains(selectyear)) {
                        Bundle basket1= new Bundle();
                        basket1.putString("tid", message);
                        basket1.putString("year",selectyear);
                        basket1.putString("subject",selectLec);
                        basket1.putString("date",date);

                        Intent intent1 = new Intent(teacherActivity.this, teacher_attendancesheet.class);
                        intent1.putExtras(basket1);
                        startActivity(intent1);
                    }else {
                        Toast.makeText(teacherActivity.this, "Enter valid details", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alertDialog.show();
        }else{
            Bundle basket1= new Bundle();
            basket1.putString("tid", message);
            basket1.putString("year",year1);
            basket1.putString("subject",subject);

            Intent intent1 = new Intent(this, teacher_attendancesheet.class);
            intent1.putExtras(basket1);
            startActivity(intent1);
        }
    }

    public void Change_pass(View view){
        AlphaAnimation buttonClick = new AlphaAnimation(2F, 0.9F);
        view.startAnimation(buttonClick);

        password1=reference.child("Teacher");

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Set your new password");
        final LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.changepassword, null);
        final EditText password= add_menu_layout.findViewById(R.id.newpassword);
        alertDialog.setView(add_menu_layout);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(password.getText().toString()))
                {
                    password1.child(message).child("tpass").setValue(password.getText().toString());
                    Toast.makeText(teacherActivity.this, "Successfully Changed", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(teacherActivity.this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void logoutTeacher(View view) {
        AlphaAnimation buttonClick = new AlphaAnimation(2F, 0.9F);
        view.startAnimation(buttonClick);

        Intent logoutTeacher=new Intent(teacherActivity.this,loginActivity.class);
        logoutTeacher.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logoutTeacher);
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
