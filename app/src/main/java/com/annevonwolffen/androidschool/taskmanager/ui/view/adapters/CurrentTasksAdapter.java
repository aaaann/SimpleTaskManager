package com.annevonwolffen.androidschool.taskmanager.ui.view.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CurrentTasksAdapter extends RecyclerView.Adapter {


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class CurentTaskiewHolder extends RecyclerView.ViewHolder {


        public CurentTaskiewHolder(@NonNull View itemView) {
            super(itemView);
        }

        
    }
}
