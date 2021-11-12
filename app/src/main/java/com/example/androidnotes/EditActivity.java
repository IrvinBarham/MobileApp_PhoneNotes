package com.example.androidnotes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    private EditText typed_text;
    private EditText title;
    private String title_text;
    private String typed;
    private int NAME_REQUEST = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF0066D3));

       title = findViewById(R.id.title);
       typed_text = findViewById(R.id.typedtext);
        typed_text.setMovementMethod(new ScrollingMovementMethod());

        if (getIntent().hasExtra("title")) {
           title_text = getIntent().getStringExtra("title");
           title.setText(title_text);
        }
        if (getIntent().hasExtra("body")) {
            typed = getIntent().getStringExtra("body");
            typed_text.setText(typed);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_activity, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        String titleText = title.getText().toString();
        String body = typed_text.getText().toString();

        if ((titleText == title_text && body == typed)){
            finishActivity(NAME_REQUEST);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String titleText = title.getText().toString();
                String body = typed_text.getText().toString();

                if (titleText.trim().isEmpty()) {
                    Toast.makeText(EditActivity.this, "Enter a Title", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("TITLE", titleText);
                if (!body.trim().isEmpty()) {
                    intent.putExtra("BODY", body);
                }
                if (getIntent().hasExtra("NoteIndexToDelete")){
                    int index = getIntent().getIntExtra("NoteIndexToDelete", -1);
                    intent.putExtra("NoteToDelete", index);
                }
                setResult(Activity.RESULT_OK, intent);

                Toast.makeText(EditActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        builder.setMessage("Would you like to save?");
        builder.setTitle("You have unsaved changes");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            String titleText = title.getText().toString();
            String body = typed_text.getText().toString();

            if ((titleText == title_text) && (body == typed)){
                finishActivity(NAME_REQUEST);
            }
            if (titleText.trim().isEmpty()) {
                Toast.makeText(this, "Enter a Title", Toast.LENGTH_SHORT).show();
                return false;
            }
            Intent intent = new Intent();
            intent.putExtra("TITLE", titleText);

            if (!body.trim().isEmpty()) {
                intent.putExtra("BODY", body);
            }
            if (getIntent().hasExtra("NoteIndexToDelete")){
                int index = getIntent().getIntExtra("NoteIndexToDelete", -1);
                intent.putExtra("NoteToDelete", index);
            }
            setResult(Activity.RESULT_OK, intent);

            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
