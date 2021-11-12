package com.example.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title_text;
    public TextView new_typed_text;
    public TextView new_date;

    MyViewHolder(View view) {
        super(view);
        title_text = view.findViewById(R.id.title);
        new_typed_text = view.findViewById(R.id.typedtext);
        new_date = view.findViewById(R.id.date);
    }
}
