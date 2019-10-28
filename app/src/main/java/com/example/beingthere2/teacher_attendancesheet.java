package com.example.beingthere2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Person;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class teacher_attendancesheet extends AppCompatActivity{

    ListView listView;
    String teacher_id,item,Lecture;

    String[] parts;
    ArrayList<String> changeData=new ArrayList<>();

    EditText date;
    TextView showRollno;
    ArrayList Userlist = new ArrayList<>();
    ArrayList Studentlist = new ArrayList<>();

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbAttendance;
    DatabaseReference dbStudent;
    String required_date,batch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendancesheet);

        TextView teacheryear=findViewById(R.id.teacherYear);
        showRollno=findViewById(R.id.showRollno);
        showRollno.setVisibility(View.INVISIBLE);

        listView = findViewById(R.id.list);
        date = findViewById(R.id.date);

        Bundle bundle1 = getIntent().getExtras();
        teacher_id = bundle1.getString("tid");
        item=bundle1.getString("year");
        Lecture=bundle1.getString("subject");

        teacheryear.setText(item+" "+Lecture);

        if(Lecture.equals("DBMSL") || Lecture.equals("CNL") || Lecture.equals("SDLL")){
            Toast.makeText(this, Lecture, Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Enter the Batch");
            final LayoutInflater inflater = this.getLayoutInflater();
            View add_menu_layout = inflater.inflate(R.layout.selectbatch, null);
            final EditText batchSelect=(EditText)add_menu_layout.findViewById(R.id.batchSslect);
            alertDialog.setView(add_menu_layout);

            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    batch=batchSelect.getText().toString();
                }
            });
            alertDialog.show();
        }
    }

    public void viewlist(View view) {
        AlphaAnimation buttonClick = new AlphaAnimation(2F, 0.9F);
        view.startAnimation(buttonClick);

        showRollno.setVisibility(View.VISIBLE);
        Userlist.clear();
        dbStudent = ref.child("Student");
        if(Lecture.equals("DBMSL") || Lecture.equals("CNL") || Lecture.equals("SDLL")){

            dbStudent.orderByChild("sbatch").equalTo(batch).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        Userlist.add(dsp.child("rollno").getValue().toString());
                    }
                    display_list(Userlist);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            dbStudent.orderByChild("classname").equalTo(item).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        Userlist.add(dsp.child("rollno").getValue().toString());
                    }
                    display_list(Userlist);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void display_list(final ArrayList userlist) {

        Studentlist.clear();
        required_date = date.getText().toString();
        dbAttendance = ref.child("attendance").child(item);

        for (Object rollno : userlist) {
            dbAttendance.child(required_date).child(rollno.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dsp : dataSnapshot.getChildren()) {
                        String p1 = dsp.getValue().toString();
                        if((p1.equals("A/"+teacher_id))||(p1.equals("P/"+teacher_id))){
                            Studentlist.add(dataSnapshot.getKey() + "        " + p1.substring(0,1) +"        "+dsp.getKey());
                        }
                    }
                    Student_list(Studentlist);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void Student_list(ArrayList studentlist){
        Collections.sort(Studentlist);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, studentlist);
        listView.setAdapter(adapter);

        dbAttendance = ref.child("attendance").child(item).child(required_date);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String data=(String) parent.getItemAtPosition(position);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(teacher_attendancesheet.this);
                alertDialog.setTitle("Update Attendance");
                final LayoutInflater inflater = teacher_attendancesheet.this.getLayoutInflater();
                View add_menu_layout = inflater.inflate(R.layout.update_attendance, null);
                final EditText newAtten=(EditText)add_menu_layout.findViewById(R.id.updateAttend);
                alertDialog.setView(add_menu_layout);

                alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        parts=data.split("        ");
                        changeData= new ArrayList<>(Arrays.asList(parts));
                        if(TextUtils.isEmpty(newAtten.getText().toString())){
                            Toast.makeText(teacher_attendancesheet.this, "Enter the Text", Toast.LENGTH_SHORT).show();
                        }else{
                            dbAttendance.child(changeData.get(0)).child(changeData.get(2)).setValue(newAtten.getText().toString()+"/"+teacher_id);
                            Toast.makeText(teacher_attendancesheet.this, "Attendance Updated Successfully", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}