package com.example.task_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task_app.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fabBtn;

    // FIREBASE
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    // RECYCLER
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        toolbar = findViewById(R.id.toolbar_home);

        System.out.println(toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Task App");

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        final String uId = mUser.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("TODO").child(uId);
        mDatabase.keepSynced(true);

        // RECYCLER

        recyclerView = findViewById(R.id.recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager);

        fabBtn = findViewById(R.id.fab_btn);

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder myDailog = new AlertDialog.Builder(HomeActivity.this);

                LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);

                View myView = inflater.inflate(R.layout.custominputfield,null);

                myDailog.setView(myView);

                final AlertDialog dailog = myDailog.create();

                final EditText title = myView.findViewById(R.id.edt_title);
                final EditText note = myView.findViewById(R.id.edt_note);

                Button btnsave = myView.findViewById(R.id.save_btn);

                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String mTitle = title.getText().toString().trim();
                        String mNote = note.getText().toString().trim();

                        if(TextUtils.isEmpty(mTitle))
                        {
                            title.setError("Required Field..");
                            return;
                        }
                        if(TextUtils.isEmpty(mNote))
                        {
                            note.setError("Required Field..");
                            return;
                        }

                        String id = mDatabase.push().getKey();

                        String datee = DateFormat.getDateInstance().format(new Date());

                        Data data = new Data(mTitle,mNote,datee,id);

                        mDatabase.child(id).setValue(data);

                        Toast.makeText(getApplicationContext(),"Data Inserted",Toast.LENGTH_SHORT).show();

                        dailog.dismiss();

                    }
                });

                dailog.show();

            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>
                (
                        Data.class,
                        R.layout.item_data,
                        MyViewHolder.class,
                        mDatabase
                )
        {
            @Override
            protected void populateViewHolder(MyViewHolder myViewHolder, Data model, int i) {

                myViewHolder.setTitle(model.getTitle());
                myViewHolder.setNote(model.getNote());
                myViewHolder.setDate(model.getDate());

            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        View myView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
        }

        public void setTitle(String title)
        {
            TextView mTitle = myView.findViewById(R.id.title);
            mTitle.setText(title);
        }

        public void setNote(String note)
        {
            TextView mNote = myView.findViewById(R.id.note);
            mNote.setText(note);
        }

        public void setDate(String date)
        {
            TextView mDate = myView.findViewById(R.id.date);
            mDate.setText(date);
        }
    }
}
