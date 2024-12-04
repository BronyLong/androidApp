package com.example.laba1;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_db_first";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_PASSWORD = "password";

    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_RECIPE_ID = "_id";
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_RECIPE_INGRIDIENTS = "ingridients";
    public static final String COLUMN_RECIPE_DIFFICULTY = "difficulty";
    public static final String COLUMN_RECIPE_TIME = "time";
    public static final String COLUMN_CALORIE = "calorie";

    public static final String TABLE_FAVORITE_RECIPES = "favorite_recipes";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_RECIPE_ID_FAV = "recipe_id";

    private static DatabaseHelper mInstance;

    public static DatabaseHelper instance()
    {
        return mInstance;
    }

    public static void createInstance(Application application)
    {
        mInstance = new DatabaseHelper(application);
    }

    public static void destroyInstance()
    {
        if (null != mInstance)
        {
            mInstance.close();

            mInstance = null;
        }
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_RECIPES);

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_LOGIN + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_RECIPE_NAME + " TEXT,"
                + COLUMN_RECIPE_INGRIDIENTS + " TEXT,"
                + COLUMN_RECIPE_DIFFICULTY + " INTEGER,"
                + COLUMN_RECIPE_TIME + " INTEGER, "
                + COLUMN_CALORIE + " INTEGER " + ")";
        db.execSQL(CREATE_RECIPES_TABLE);

        String CREATE_FAVORITE_RECIPES_TABLE = "CREATE TABLE " + TABLE_FAVORITE_RECIPES + "("
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_RECIPE_ID_FAV + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "),"
                + "FOREIGN KEY(" + COLUMN_RECIPE_ID_FAV + ") REFERENCES " + TABLE_RECIPES + "(" + COLUMN_RECIPE_ID + ")" + ")";
        db.execSQL(CREATE_FAVORITE_RECIPES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE_RECIPES);
        onCreate(db);
    }
}
