package com.example.admin.keeper;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class TextActivity extends AppCompatActivity {

    EditText taskTitle;
    EditText taskText;
    private String title;
    private String text;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_task);
        setInitialData();
    }

    private void setInitialData() {
        taskTitle = (EditText) findViewById(R.id.taskTitle);
        taskText = (EditText) findViewById(R.id.taskText);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_text, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_task_text_save:
                saveText();
                finish();
                break;
            case R.id.action_task_text_share:
                saveText();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));

                break;
            case R.id.action_task_text_translate:
                translation();
                break;
        }
        return true;
    }

    private void translation() {
        getText();
        new AsyncRequest().execute();
    }

    private void saveText() {
        Intent data = new Intent();
        getText();
        data.putExtra(GeneralActivity.NAME, title);
        data.putExtra(GeneralActivity.TEXT, text);
        setResult(RESULT_OK, data);
    }

    private void getText() {
        title = taskTitle.getText().toString();
        text = taskText.getText().toString();
    }

    class AsyncRequest extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... voids){
            try {
                String translatedTitle = Translator.translate("en-ru", title);
                String translatedText = Translator.translate("en-ru", text);
                return new String[]{translatedTitle, translatedText};
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if (strings != null) {
                taskTitle.setText(strings[0]);
                taskText.setText(strings[1]);
                Toast.makeText(TextActivity.this, "Translate completed!",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(TextActivity.this, "Translate error!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
