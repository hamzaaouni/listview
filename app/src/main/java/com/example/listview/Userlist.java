package com.example.listview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Userlist extends AppCompatActivity implements Myadapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ArrayList<User> list;
    private DatabaseReference databaseReference;
    private Myadapter adapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Userlist.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);

        recyclerView = findViewById(R.id.recycleview);
        list = new ArrayList<>();
        adapter = new Myadapter(this, list, this);

        initializeRecyclerView();
        setupFirebaseListener();
    }

    private void initializeRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupFirebaseListener() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        user.setId(dataSnapshot.getKey());
                        list.add(user);
                    }
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                Log.d("Userlist", "Data retrieved successfully. Count: " + list.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Userlist", "Data retrieval cancelled. Error: " + error.getMessage());
                // You can display a Toast or handle the error in other ways
            }
        });
    }

    @Override
    public void onDeleteClick(int position) {
        if (position >= 0 && position < list.size()) {
            User selectedUser = list.get(position);
            DatabaseReference userReference = databaseReference.child(selectedUser.getId());

            userReference.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Userlist.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Userlist.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("Userlist", "Invalid position for deletion: " + position);
        }
    }

    @Override
    public void onEditClick(int position) {
        User selectedUser = list.get(position);
        Log.d("Userlist", "Edit clicked for user: " + (selectedUser != null ? selectedUser.getName() : "null"));

        if (selectedUser != null) {
            // Existing code...
        } else {
            Log.e("Userlist", "User data is null");
            Toast.makeText(Userlist.this, "User data is null", Toast.LENGTH_SHORT).show();
        }
    }

}