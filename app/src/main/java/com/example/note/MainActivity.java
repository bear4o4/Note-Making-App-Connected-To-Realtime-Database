package com.example.note;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText;
    private Button saveButton;
    private FirebaseDatabase database;
    private DatabaseReference notesRef;
    private RecyclerView notesRecyclerView;
    private FirebaseRecyclerAdapter<Note, NoteViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);
        notesRecyclerView = findViewById(R.id.notesRecyclerView);

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance();
        notesRef = database.getReference("notes");

        // Set up RecyclerView
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configure FirebaseRecyclerAdapter to retrieve notes
        FirebaseRecyclerOptions<Note> options = new FirebaseRecyclerOptions.Builder<Note>()
                .setQuery(notesRef, Note.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Note, NoteViewHolder>(options) {
            @Override
            protected void onBindViewHolder(NoteViewHolder holder, int position, Note model) {
                holder.bind(model);
            }

            @Override
            public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = getLayoutInflater().inflate(R.layout.item_note, parent, false);
                return new NoteViewHolder(view);
            }
        };

        // Set the adapter to the RecyclerView
        notesRecyclerView.setAdapter(adapter);

        // Save note to Firebase when the save button is clicked
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                if (!title.isEmpty() && !description.isEmpty()) {
                    // Create a new note with a unique ID
                    String noteId = notesRef.push().getKey();
                    Note newNote = new Note(noteId, title, description, System.currentTimeMillis());

                    // Save note to Firebase Realtime Database
                    if (noteId != null) {
                        notesRef.child(noteId).setValue(newNote)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(MainActivity.this, "Note Saved!", Toast.LENGTH_SHORT).show();
                                    titleEditText.setText("");
                                    descriptionEditText.setText("");
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(MainActivity.this, "Error Saving Note", Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening for changes in the database
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening for changes in the database
        adapter.stopListening();
    }

    // ViewHolder for notes
    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, descriptionTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.noteTitle);
            descriptionTextView = itemView.findViewById(R.id.noteDescription);
        }

        public void bind(Note note) {
            titleTextView.setText(note.title);
            descriptionTextView.setText(note.description);
        }
    }

    public static class Note {
        public String id;
        public String title;
        public String description;
        public long dateTime;

        public Note() {
            // Default constructor required for Firebase Realtime Database
        }

        public Note(String id, String title, String description, long dateTime) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.dateTime = dateTime;
        }
    }
}
