package com.example.it320project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SpacesLibrary.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "SpaceInfo";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "Space_title";
    private static final String COLUMN_LOCATION = "Space_location";
    private static final String COLUMN_CATEGORY = "Space_category";
    private static final String COLUMN_PRICE = "Space_price";
    private static final String COLUMN_CAPACITY = "Space_capacity";
    private static final String COLUMN_DATE = "Space_date";
    private static final String COLUMN_DESCRIPTION = "Space_description";
    private static final String COLUMN_PHOTO = "Space_photo";
    private Context context;

    //extra
    MyDatabaseHelper helper;
    SQLiteDatabase db;
    List<Space> spaceList = new ArrayList<>();


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    //create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query= "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_PRICE + " INTEGER, " +
                COLUMN_CAPACITY + " INTEGER, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, "+
                COLUMN_PHOTO + " BLOB) ";;
        db.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add the photo column using ALTER TABLE
            String query = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_PHOTO + " BLOB";
            db.execSQL(query);
        }
    }
    //add space to the database
    public boolean addOne(Space spaceModel){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_NAME, spaceModel.getName());
        cv.put(COLUMN_LOCATION, spaceModel.getLocation());
        cv.put(COLUMN_CATEGORY, spaceModel.getCategory());
        cv.put(COLUMN_PRICE, spaceModel.getPrice());
        cv.put(COLUMN_CAPACITY, spaceModel.getCapacity());
        cv.put(COLUMN_DESCRIPTION, spaceModel.getDescription());
        cv.put(COLUMN_PHOTO, spaceModel.getPhoto());

        long insert= db.insert(TABLE_NAME, null, cv);
        if(insert == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean deleteOne(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRows = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[] {String.valueOf(id)});
        db.close();
        return (affectedRows > 0);
    }

    public List<Space> getAllSpaces(){
        List<Space> resultList= new ArrayList<>();
        //get data from database
        String queryString="SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do {
                int spaceId= cursor.getInt(0);
                String spaceName=cursor.getString(1);
                String spaceLocation= cursor.getString(2);
                String spaceCategory= cursor.getString(3);
                int spacePrice= cursor.getInt(4);
                int spaceCapacity= cursor.getInt(5);
                String spaceDesc= cursor.getString(6);
                byte[] photoData=cursor.getBlob(7);

                //creating an objects from the database search and then store them
                Space spaceModel=new Space(spaceId, spaceName, spaceLocation, spaceCategory, spacePrice,
                        spaceCapacity, spaceDesc, photoData);
                spaceList.add(spaceModel);

            } while(cursor.moveToNext());


        } else{

        }
        cursor.close();
        db.close();
        return spaceList;
    }

    public byte[] getPhotoData(int spaceId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = { COLUMN_PHOTO };
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(spaceId) };
        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        byte[] photoData = null;
        if (cursor.moveToFirst()) {
            photoData = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PHOTO));
        }
        cursor.close();
        return photoData;
    }
}