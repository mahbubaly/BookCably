package com.bookcably;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;

public class UserListActivityAdminPage extends AppCompatActivity {
    private ListView listViewUsers;

    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_admin_page);

        listViewUsers = findViewById(R.id.listOfUsers);
        Button deleteBtn = findViewById(R.id.btn_delete);
        Button UpdateBtn = findViewById(R.id.btn_update);


        databaseHelper = new DatabaseHelper(this);

        displayAllUsers();

        deleteBtn.setOnClickListener(v->{
//            //Toast.makeText(UserListActivityAdminPage.this, "Deleted", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(UserListActivityAdminPage.this,DeleteCarsByNameAdmin.class);
//            startActivity(intent);

        });

        UpdateBtn.setOnClickListener(v->{
//            Intent intent = new Intent(UserListActivityAdminPage.this,DeleteCarsByNameAdmin.class);
//            startActivity(intent);
        });

    }



    private void displayAllUsers(){
        Cursor cursor = databaseHelper.getAllUsers();
        if (cursor != null) {
            Log.d("Cursor User Data", "Number of rows in cursor: " + cursor.getCount());
            Toast.makeText(UserListActivityAdminPage.this, "Showing user list...", Toast.LENGTH_SHORT).show();


            UserAdapter adapter = new UserAdapter(this,cursor,0);

            try {
                listViewUsers.setAdapter(adapter);
            } catch (Exception e){
                Toast.makeText(UserListActivityAdminPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Log.e("Cursor Data", "Cursor is null");
        }
    }
    }
