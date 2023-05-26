package com.example.it320project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SpacesLibrary.db";
    private static final int DATABASE_VERSION = 3;
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
    private static final String COLUMN_STATUS = "Space_status";

    private static final String TABLE_NAME_RENTED = "RentedSpaces";
    private static final String COLUMN_ID_RENTED = "_id";
    private static final String COLUMN_SPACE_ID_RENTED = "Space_id";


    private Context context;


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
                COLUMN_PHOTO + " BLOB, " +
                COLUMN_STATUS + " INTEGER DEFAULT 0)";
        db.execSQL(query);


        String queryRented = "CREATE TABLE " + TABLE_NAME_RENTED +
                " (" + COLUMN_ID_RENTED + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SPACE_ID_RENTED + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_SPACE_ID_RENTED + ") REFERENCES " + TABLE_NAME + "(" + COLUMN_ID + "))";

        db.execSQL(queryRented);

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
        cv.put(COLUMN_STATUS, spaceModel.getStatus());

        long insert= db.insert(TABLE_NAME, null, cv);
        if(insert == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean insertIntoRentedSpaces(int spaceId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SPACE_ID_RENTED, spaceId);

        long insertedRows= db.insert(TABLE_NAME_RENTED, null, values);
       // db.close();
        return (insertedRows > 0);
    }

    public boolean deleteOne(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRows = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[] {String.valueOf(id)});
       // db.close();
        return (affectedRows > 0);
    }

    public void returnSpace(int spaceId) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {

            updateSpaceStatus(spaceId, 0);

            ContentValues values = new ContentValues();
            values.put(COLUMN_STATUS, 0);  // Updated status

            int affectedRows=db.update(TABLE_NAME, values, "_id=?", new String[]{String.valueOf(spaceId)});
            Log.d("Affected rows", String.valueOf(affectedRows));

            int deletedRows= db.delete(TABLE_NAME_RENTED, "Space_id=?", new String[]{String.valueOf(spaceId)});
            Log.d("Deleted rows", String.valueOf(deletedRows));

            db.setTransactionSuccessful();

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean updateSpaceStatus(int spaceId, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);

        int affectedRows = db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(spaceId)});
       // db.close();
        return (affectedRows > 0);
    }

    public List<Space> getRentedSpaces() {

        List<Space> rentedSpaces = new ArrayList<>();

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);

        String query = "SELECT * FROM " + TABLE_NAME_RENTED +
                " INNER JOIN " + TABLE_NAME +
                " ON " + TABLE_NAME_RENTED + "." + COLUMN_SPACE_ID_RENTED +
                " = " + TABLE_NAME + "." + COLUMN_ID;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        while(c.moveToNext()) {


            int idIndex = c.getColumnIndex(COLUMN_ID);
            if (idIndex == -1) {
                return null;
            }


            int nameIndex = c.getColumnIndex(COLUMN_NAME);
            if (nameIndex == -1) {
                return null;
            }


            int locationIndex = c.getColumnIndex(COLUMN_LOCATION);
            if (locationIndex == -1) {
                return null;
            }


            int categoryIndex = c.getColumnIndex(COLUMN_CATEGORY);
            if (categoryIndex == -1) {
                return null;
            }



            int priceIndex = c.getColumnIndex(COLUMN_PRICE);
            if (priceIndex == -1) {
                return null;
            }


            int capacityIndex = c.getColumnIndex(COLUMN_CAPACITY);
            if (capacityIndex == -1) {
                return null;
            }


            int descriptionIndex = c.getColumnIndex(COLUMN_DESCRIPTION);
            if (descriptionIndex == -1) {
                return null;
            }


            int photoIndex1 = c.getColumnIndex(COLUMN_PHOTO);
            if (photoIndex1 == -1) {
                return null;
            }
            int photoIndex = c.getColumnIndex(COLUMN_PHOTO);
            if (photoIndex == -1) {
                return null;
            }
            byte[] photoData = c.getBlob(photoIndex);

            Space space = new Space(
                    c.getInt(idIndex),
                    c.getString(nameIndex),
                    c.getString(locationIndex),
                    c.getString(categoryIndex),
                    c.getDouble(priceIndex),
                    c.getInt(capacityIndex),
                    c.getString(descriptionIndex),
                    c.getBlob(photoIndex)
            );



            rentedSpaces.add(space);
        }

        c.close();
        //db.close();

        return rentedSpaces;
    }

    public List<Space> getAllSpaces(){
        List<Space> resultList= new ArrayList<>();
        //get data from database
       try{ String queryString="SELECT * FROM " + TABLE_NAME;
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
       // db.close();
           }
       catch(SQLiteBlobTooBigException e){
           Toast.makeText(context, "Failed to run the query", Toast.LENGTH_SHORT).show();

       }
        return spaceList;
    }

    public List<Space> getAvailableSpaces() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Space> spaceList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " = 0";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
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
            } while (cursor.moveToNext());
        }

        cursor.close();
        //db.close();

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

    public String getDescription(int spaceId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = { COLUMN_DESCRIPTION };
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
        String description = null;
        if (cursor.moveToFirst()) {
            description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
        }
        cursor.close();
        return description;
    }

    public List<Space> searchSpaces(String name) {
        List<Space> returnList = new ArrayList<>();
        // get data from database
        String queryString = "Select * from " + TABLE_NAME + " WHERE " +
                COLUMN_NAME + " LIKE '%" + name + "%'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            // loop through cursor results
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
                returnList.add(spaceModel);
            } while (cursor.moveToNext());
        }
        //close
        cursor.close();
        //db.close();
        return returnList;
    }


}

