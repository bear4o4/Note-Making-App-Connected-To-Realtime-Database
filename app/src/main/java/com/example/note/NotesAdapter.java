package com.example.note;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<MainActivity.Note> noteList;

    public NotesAdapter(List<MainActivity.Note> noteList) {
        this.noteList = noteList;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        MainActivity.Note note = noteList.get(position);
        holder.titleTextView.setText(note.title);
        holder.descriptionTextView.setText(note.description);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView, descriptionTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            //titleTextView = itemView.findViewById(R.id.titleTextView);
            //descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
