package component.learnprogramming.com.arc_component.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import component.learnprogramming.com.arc_component.adapter.NoteAdapter;
import component.learnprogramming.com.arc_component.R;
import component.learnprogramming.com.arc_component.controller.NoteViewModel;
import component.learnprogramming.com.arc_component.model.Note;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private NoteViewModel noteViewModel;
    private RecyclerView mrec;
    private NoteAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mrec = findViewById(R.id.rl);
        mrec.setLayoutManager(new LinearLayoutManager(this));
        mrec.setHasFixedSize(true);
        adp = new NoteAdapter();
        mrec.setAdapter(adp);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, (notes) -> {
            Toast.makeText(MainActivity.this, "Data Changed" + notes.size(), Toast.LENGTH_SHORT).show();
            adp.setNotes(notes);

        });


        findViewById(R.id.add)
                .setOnClickListener(view ->
                        startActivityForResult(
                                new Intent(MainActivity.this,
                                        AddNoteActivity.class), 1));

        new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adp.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        })
                .attachToRecyclerView(mrec);


        adp.setOnItemClickListener(note ->
            startActivityForResult(new Intent(this, AddNoteActivity.class)
                            .putExtra(AddNoteActivity.EXTRA_TITLE, note.getTitle())
                            .putExtra(AddNoteActivity.EXTRA_DESCRIPTION, note.getDescription())
                            .putExtra(AddNoteActivity.EXTRA_PRIORITY, note.getPriority())
                            .putExtra(AddNoteActivity.EXTRA_ID, note.getId()),
                    MainActivity.EDIT_NOTE_REQUEST));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1);
            noteViewModel.insert(new Note(title, description, priority));
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } else if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddNoteActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1);

            Note note =new Note(title, description, priority);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }  else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
