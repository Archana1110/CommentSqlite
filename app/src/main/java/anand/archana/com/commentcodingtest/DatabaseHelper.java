package anand.archana.com.commentcodingtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;



public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "comments_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create  table
        db.execSQL(CommentModelClass.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + CommentModelClass.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertComment(String commt) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(CommentModelClass.COLUMN_COMMENT, commt);

        // insert row
        long id = db.insert(CommentModelClass.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public CommentModelClass getComment(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(CommentModelClass.TABLE_NAME,
                new String[]{CommentModelClass.COLUMN_ID, CommentModelClass.COLUMN_COMMENT,CommentModelClass.COLUMN_TIMESTAMP},
                CommentModelClass.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare  object
        CommentModelClass commnt = new CommentModelClass(
                cursor.getInt(cursor.getColumnIndex(CommentModelClass.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(CommentModelClass.COLUMN_COMMENT)),
                cursor.getString(cursor.getColumnIndex(CommentModelClass.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return commnt;
    }

    public List<CommentModelClass> getAllComments() {
        List<CommentModelClass> Commnt = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + CommentModelClass.TABLE_NAME + " ORDER BY " +
                CommentModelClass.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CommentModelClass commet = new CommentModelClass();
                commet.setId(cursor.getInt(cursor.getColumnIndex(CommentModelClass.COLUMN_ID)));
                commet.setComment(cursor.getString(cursor.getColumnIndex(CommentModelClass.COLUMN_COMMENT)));
                commet.setTimestamp(cursor.getString(cursor.getColumnIndex(CommentModelClass.COLUMN_TIMESTAMP)));

                Commnt.add(commet);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return list
        return Commnt ;
    }

    public int getCommentsCount() {
        String countQuery = "SELECT  * FROM " + CommentModelClass.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public int updateCommet(CommentModelClass  commt) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CommentModelClass.COLUMN_COMMENT, commt.getComment());

        // updating row
        return db.update(CommentModelClass.TABLE_NAME, values, CommentModelClass.COLUMN_ID + " = ?",
                new String[]{String.valueOf(commt.getId())});
    }

    public void deleteCommnt(CommentModelClass Commt) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CommentModelClass.TABLE_NAME, CommentModelClass.COLUMN_ID + " = ?",
                new String[]{String.valueOf(Commt.getId())});
        db.close();
    }
}

