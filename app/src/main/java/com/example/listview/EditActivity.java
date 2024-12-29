package com.example.listview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditActivity extends AppCompatActivity {

    EditText editName, editEmail, editAge;
    Button btnSave;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editAge = findViewById(R.id.editAge);
        btnSave = findViewById(R.id.btnSave);

        // Get the User object from the intent
        User user = (User) getIntent().getSerializableExtra("user");
        Log.d("EditActivity", "Received Intent Extra - user: " + (user != null ? user.getName() : "null"));

        if (user != null) {
            Log.d("EditActivity", "User retrieved successfully: " + user.getName());
            Log.d("EditActivity", "Original data - Name: " + user.getName() + ", Email: " + user.getEmail() + ", Age: " + user.getAge());

            // Initialize Firebase
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getId());

            // Set existing data to EditText fields
            editName.setText(user.getName());
            editEmail.setText(user.getEmail());
            editAge.setText(user.getAge());

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Log.d("EditActivity", "Save button clicked");

                        // Retrieve data from EditText fields
                        String newName = editName.getText().toString();
                        String newEmail = editEmail.getText().toString();
                        String newAge = editAge.getText().toString();

                        Log.d("EditActivity", "New data - Name: " + newName + ", Email: " + newEmail + ", Age: " + newAge);

                        // Update the user data in Firebase
                        user.setName(newName);
                        user.setEmail(newEmail);
                        user.setAge(newAge);

                        databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("EditActivity", "Update task completed");
                                if (task.isSuccessful()) {
                                    Toast.makeText(EditActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    String errorMessage = "Failed to update user. Error: " + task.getException().getMessage();
                                    Log.e("EditActivity", errorMessage);
                                    Toast.makeText(EditActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("EditActivity", "Exception in onClick: " + e.getMessage());
                    }
                }
            });
        } else {
            Log.e("EditActivity", "User data not found");
            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
