package com.example.androidnotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private final List<Notes> notes;
    private final MainActivity mainAct;

    NotesAdapter(List<Notes> notes, MainActivity ma) {
        this.notes = notes;
        mainAct = ma;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        itemView.setOnClickListener((View.OnClickListener) mainAct);
        itemView.setOnLongClickListener((View.OnLongClickListener) mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Notes note = notes.get(position);

        holder.title_text.setText(note.getTitle());
        String body = note.getBody();
        if (body.length() > 80) {
            String bodyFinal = body.substring(0, 80);
            bodyFinal = bodyFinal + "...";
            holder.new_typed_text.setText(bodyFinal);
        }
        else {
            holder.new_typed_text.setText(body);
        }
        holder.new_date.setText(new Date().toString());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
