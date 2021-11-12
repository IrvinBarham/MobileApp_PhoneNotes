package com.example.androidnotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, View.OnLongClickListener {
    private RecyclerView recyclerView;
    private final List<Notes> notesList = new ArrayList<>();
    private NotesAdapter nAdapter;
    private String name;
    private int NAME_REQUEST = 123;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF0066D3));

        recyclerView = findViewById(R.id.RecyclerView);
        nAdapter = new NotesAdapter(notesList, this);
        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notesList.addAll(loadFile());
        updateRecyclerView();

        ApplicationInfo applicationInfo = getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        if (stringId == 0) {
            name = applicationInfo.nonLocalizedLabel.toString();
        } else {
            name = getString(stringId);
        }

        String updatedName = name + "(" + String.format("%d", count) + ")";
        getSupportActionBar().setTitle(updatedName);

        String title;
        String body;
        for (int i = 0; i < 4; i++) {
            title = "NOTE: " + String.format(" %d", i);
            body = "This was pretty random!" + String.format(" %d", i);
            addNote(title, body);
        }
    }

    public void addNote(String title, String body) {
        Notes note = new Notes(title, body);

        notesList.add(0, note);
        nAdapter.notifyDataSetChanged();

        count++;
        String updatedName = name + " (" + String.format("%d", count) + ")";
        getSupportActionBar().setTitle(updatedName);
    }

    public void removeNote(int index) {
        if (!notesList.isEmpty()) {
            notesList.remove(index);
            nAdapter.notifyDataSetChanged();

            count--;
            String updatedName = name + " (" + String.format("%d", count) + ")";
            getSupportActionBar().setTitle(updatedName);
        }
    }

    public void updateRecyclerView() {
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.filename), Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(notesList);
            printWriter.close();
            fos.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intentEdit = new Intent(this, EditActivity.class);
        int pos = recyclerView.getChildLayoutPosition(v);
        Notes note = notesList.get(pos);
        intentEdit.putExtra("title", note.getTitle());
        intentEdit.putExtra("typed", note.getBody());
        intentEdit.putExtra("NoteIndexToDelete", pos);

        startActivityForResult(intentEdit, NAME_REQUEST);
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Notes note = notesList.get(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                removeNote(pos);
                Toast.makeText(v.getContext(), note.getTitle() + " is now deleted", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });

        builder.setTitle("Delete Note?");
        builder.setMessage(String.format("Title: %s", note.getTitle()));

        AlertDialog dialog = builder.create();
        dialog.show();

        return false;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Back Button Used", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void openNewActivity(int n) {
        Intent intentAbout = new Intent(this, AboutActivity.class);
        Intent intentEdit = new Intent(this, EditActivity.class);

        if (n == 1) {
            startActivity(intentAbout);
        } else if (n == 2) {
            startActivityForResult(intentEdit, NAME_REQUEST);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu1) {
            Toast.makeText(this, "Launching About Activity", Toast.LENGTH_SHORT).show();
            openNewActivity(1);
            return true;
        } else if (item.getItemId() == R.id.menu2) {
            Toast.makeText(this, "Launching Edit Activity", Toast.LENGTH_SHORT).show();
            openNewActivity(2);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String title = "";
        String typed = "";
        if (requestCode == NAME_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.hasExtra("TITLE")) {
                    title = data.getStringExtra("TITLE");
                }
                if (data != null && data.hasExtra("BODY")) {
                    typed = data.getStringExtra("BODY");
                }
                if (data != null && data.hasExtra("NoteToDelete")) {
                    int index = data.getIntExtra("NoteToDelete", -1);
                    if (index != -1) {
                        removeNote(index);
                    }
                }
                addNote(title, typed);
                updateRecyclerView();
            }
        }
    }

    private ArrayList<Notes> loadFile() {

        ArrayList<Notes> nList = new ArrayList<>();
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.filename));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String bodyText = jsonObject.getString("typed");
                Notes note = new Notes(name, bodyText);
                nList.add(note);
            }

        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nList;
    }
}

