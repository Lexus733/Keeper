package com.example.admin.keeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GeneralActivity extends AppCompatActivity {

    private final int TEXT_RESULT = 1;
    public static final String NAME = "NAME";
    public static final String TEXT = "TEXT";
    private static final int CAMERA_RESULT = 2;
    private static final int GALLERY_RESULT = 3;


    private static List<ListItems> listItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    public android.support.v7.view.ActionMode mActionMode;
    public android.support.v7.view.ActionMode.Callback mActionModeCallback;
    private RecyclerItemAdapter recyclerItemAdapter;
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        setInitialData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_split:
                if (recyclerView.getLayoutManager() == linearLayoutManager)
                    recyclerView.setLayoutManager(gridLayoutManager);
                else {
                    recyclerView.setLayoutManager(linearLayoutManager);
                }
                break;
        }
        return true;
    }


    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerItemAdapter = new RecyclerItemAdapter(listItems, this);
        recyclerView.setAdapter(recyclerItemAdapter);
    }


    private void setInitialData() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

        initRecyclerView();
        recyclerItemAdapter.initActionMode();

        recyclerView.setLayoutManager(linearLayoutManager);

        initPhotoDialog();
    }

    private void initPhotoDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Photo adding")
                .setMessage("Choose option")
                .setPositiveButton("Create new", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_RESULT);
                    }
                })
                .setNegativeButton("Took from gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

                        photoPickerIntent.setType("image/*");

                        startActivityForResult(photoPickerIntent, GALLERY_RESULT);

                    }
                });
        builder.create();
    }

    public void OnClick(View view){
        switch (view.getId()){
            case R.id.add_task_text:
                Intent textIntent = new Intent(this, TextActivity.class);
                startActivityForResult(textIntent, TEXT_RESULT);
                break;
            case R.id.add_task_photo:
                builder.show();

                break;
            case R.id.add_task_list:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

        switch (requestCode){
            case TEXT_RESULT:
                if(resultCode==RESULT_OK){
                    Bundle extras = data.getExtras();
                    if(extras != null){
                        String name = data.getStringExtra(NAME);
                        String text = data.getStringExtra(TEXT);
                        save(name, RecyclerItemAdapter.TYPE_ITEM_TEXT, text);

                    }
                }
                else{
                    Toast.makeText(this, R.string.save_canceled, Toast.LENGTH_SHORT).show();
                }
                break;
            case CAMERA_RESULT:
                try {

                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    // чего-то тут не хватает
                }
                catch (NullPointerException ex){
                    Toast.makeText(this, "Photo don't created", Toast.LENGTH_SHORT).show();
                }
                break;
            case GALLERY_RESULT:
                Uri imageUri;
                InputStream imageStream;
                try {
                    imageUri = data.getData();
                    imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    // чего-то тут не хватает
                }
                catch (FileNotFoundException e) {
                    Toast.makeText(this, "Picture don't took",
                            Toast.LENGTH_SHORT).show();
                }
                catch (NullPointerException ex){
                    Toast.makeText(this, "Picture don't took",
                            Toast.LENGTH_SHORT).show();
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public void retrieve() {

        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(this);

        dataBaseAdapter.openDB();

        listItems.clear();

        Cursor cursor = dataBaseAdapter.getAllTasks();


        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int type = cursor.getInt(2); //Зачем, если не используем
            String content = cursor.getString(3);

            ListItems taskListItem = new Text(id, name, content);

            recyclerItemAdapter.notifyDataSetChanged();
            listItems.add(taskListItem);
        }

    }

    public void save(String name, int type, String content)
    {
        DataBaseAdapter dataBaseAdapter=new DataBaseAdapter(this);


        dataBaseAdapter.openDB();


        long result=dataBaseAdapter.add(name, type, content);

        if(result>0)
        {
            Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(this, "Added error!", Toast.LENGTH_SHORT).show();
        }

        dataBaseAdapter.close(); // Зачем вы каждый раз пересоздаете класс для доступа к БД?
        // Этот объект нам нужен часто. Вы же при этом постоянно забиваете память новыми объектами
        // данного класса и у вас чаще запускается GC, а это снижает производительнось.
        // Сделайте его сингтоном и живите спокойно)

        retrieve();

    }

    public void delete(int id)
    {
        DataBaseAdapter dataBaseAdapter = new DataBaseAdapter(this);

        dataBaseAdapter.openDB();

        long result=dataBaseAdapter.Delete(id);

        if(result>0)
        {
            Toast.makeText(this, "Task deleted!", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(this, "Deleted error!", Toast.LENGTH_SHORT).show();
        }

        dataBaseAdapter.close();

        retrieve();

    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieve();
    }
}
