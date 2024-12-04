package com.example.laba1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;


public class LoginAct extends AppCompatActivity {
    private Context loginActContext;
    public static int loggedUserId;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button button;
    private String[] emails = {"exampleEmail", "ex2@google.com", "ex3@google.com", "ex4@google.com", "ex5@google.com"};
    private String[] passwords = {"examplePassword", "pass1", "pass2", "pass3", "pass4"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseHelper dbHelper = DatabaseHelper.instance();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from "+ "users");
        ContentValues cv1 = new ContentValues();
        ContentValues cv2 = new ContentValues();
        ContentValues cv3 = new ContentValues();
        ContentValues cv4 = new ContentValues();
        ContentValues cv5 = new ContentValues();
        cv1.put("login", "exampleEmail");
        cv1.put("password", "examplePassword");
        cv2.put("login", "exampleEmail2");
        cv2.put("password", "examplePassword2");
        cv3.put("login", "exampleEmail3");
        cv3.put("password", "examplePassword3");
        cv4.put("login", "exampleEmail4");
        cv4.put("password", "examplePassword4");
        cv5.put("login", "exampleEmail5");
        cv5.put("password", "examplePassword5");
        long RowId1 = db.insert("users", null, cv1);
        long RowId2 = db.insert("users", null, cv2);
        long RowId3 = db.insert("users", null, cv3);
        long RowId4 = db.insert("users", null, cv4);
        long RowId5 = db.insert("users", null, cv5);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_page);

        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        button = findViewById(R.id.button);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        button.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                button.setBackgroundColor(ContextCompat.getColor(LoginAct.this, android.R.color.holo_red_light));
            } else {
                String[] columns = new String[]{"_id", "login", "password"};
                Cursor cursor = db.query("users", columns, null, null, null,
                        null, null);
                boolean isValid = false;
                cursor.moveToFirst();
                int idColIndex = cursor.getColumnIndex("_id");
                int field1ColIndex = cursor.getColumnIndex("login");
                int field2ColIndex = cursor.getColumnIndex("password");
                do {
                    loggedUserId = cursor.getInt(idColIndex);
                    String field1 = cursor.getString(field1ColIndex);
                    String field2 = cursor.getString(field2ColIndex);
                    if(field1.equals(email) && field2.equals(password)){
                        isValid = true;
                        break;
                    }
                } while (cursor.moveToNext());

                if (isValid) {
                    button.setBackgroundColor(ContextCompat.getColor(LoginAct.this, android.R.color.holo_green_light));
                    Intent myIntent = new Intent(LoginAct.this, NavigationAct.class);
                    startActivity(myIntent);

                } else {
                    button.setBackgroundColor(ContextCompat.getColor(LoginAct.this, android.R.color.holo_red_light));

                }

                cursor.close();
            }
        });
    }
}
