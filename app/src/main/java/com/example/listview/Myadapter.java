package com.example.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Myadapter extends RecyclerView.Adapter<Myadapter.MyViewHolder> {

    private Context context;
    private ArrayList<User> list;
    private OnItemClickListener listener;

    public Myadapter(Context context, ArrayList<User> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.userentry, parent, false);
        return new MyViewHolder(v);
    }

    @Override

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = list.get(position);

        if (user != null) {
            holder.name.setText(user.getName());
            holder.email.setText(user.getEmail());
            holder.age.setText(user.getAge());

            // Set click listener for delete button
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(adapterPosition);
                        }
                    }
                }
            });

            // Set click listener for edit button (if needed)
            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION && listener != null) {
                        listener.onEditClick(adapterPosition);
                    }
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, age;
        Button deleteButton, editButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textname);
            email = itemView.findViewById(R.id.textemail);
            age = itemView.findViewById(R.id.textage);

            // Initialize delete and edit buttons
            deleteButton = itemView.findViewById(R.id.btnDelete);
            editButton = itemView.findViewById(R.id.btnEdit);
        }
    }

    // Interface for handling button clicks
    public interface OnItemClickListener {
        void onDeleteClick(int position);
        void onEditClick(int position);
    }
}
