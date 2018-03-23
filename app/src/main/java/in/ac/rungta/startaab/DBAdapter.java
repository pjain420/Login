package in.ac.rungta.startaab;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created alertBy George on 01-Feb-18.
 */


public class DBAdapter extends SQLiteOpenHelper {
    @SuppressLint("SdCardPath")
    public static final String DATABASE_NAME = "Prachi.db";  //exampleDB.db -   DB Name
    public static final String TAG = "Prachi_DATA_SOURCE";  // DB
    private static DBAdapter mInstance = null;
    private final Context context;
    private SQLiteDatabase db;

    String seq_str;

    public DBAdapter(Context ctx) {
        super(ctx, DATABASE_NAME, null, 1);
        this.context = ctx;
    }

    public static DBAdapter getInstance(Context ctx) {
        /**
         * use the application context as suggested alertBy CommonsWare. this will
         * ensure that you dont accidentally leak an Activitys context (see this
         * article for more information:
         * http://developer.android.com/resources/articles
         * /avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new DBAdapter(ctx.getApplicationContext());
            try {
                mInstance.createDataBase();
                mInstance.openDataBase();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return mInstance;
    }

    private boolean checkDataBase() {
        File dbFile = new File(context.getFilesDir() + "/" + DATABASE_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {
        try {
            InputStream input;
            String outPutFileName;
            File sd = Environment.getExternalStorageDirectory();
            File locDB = new File(sd.getPath(), "/");
            File locDstDB = new File(context.getFilesDir().getPath(), "/");
            File currentDB = new File(locDstDB, DATABASE_NAME);
            File backUpDB = new File(locDB, "Prachi_back");						//DB BACKUP
            String backupDBPath = sd.getPath() + "/" + "Prachi_back";			//DB BACKUP
            if (backUpDB.exists()) {
                @SuppressWarnings("resource")
                FileChannel src = new FileInputStream(backUpDB).getChannel();
                @SuppressWarnings("resource")
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            } else {
                input = context.getAssets().open(DATABASE_NAME);
                outPutFileName = context.getFilesDir().getPath() + "/"
                        + DATABASE_NAME;
                OutputStream output = new FileOutputStream(outPutFileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                input.close();
            }
        } catch (IOException e) {
            Log.v("error", e.toString());
        }
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            // do nothing - database already exist
        } else {
            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    public void openDataBase() throws SQLException, IOException {
        String fullDbPath = context.getFilesDir().getPath() + "/"
                + DATABASE_NAME;
        db = SQLiteDatabase.openDatabase(fullDbPath, null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    public SQLiteDatabase getDB() {
        try {
            openDataBase();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            /* TODO Auto-generated catch block */
            e.printStackTrace();
        }
        return db;
    }

    /******************************
     * closes the database
     ******************************/
    public synchronized void close() {
        if (db != null)
            db.close();
        super.close();
        mInstance = null;
    }

    public synchronized void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    public synchronized void onUpgrade(SQLiteDatabase db, int oldVersion,
                                       int newVersion) {
        // TODO Auto-generated method stub

    }
    public boolean CheckEmailID(String email)
    {
        Cursor cursor=db.rawQuery("select Email_ID From User_TB where Email_ID='"+email+"'", null);
        if(cursor.getCount()>0){
            return true;
        }
        return false;
    }

    public boolean CheckIDnPass(String email, String pass)
    {
        Cursor cursor=db.rawQuery("select * From User_TB where Email_ID='"+email+"' and Password='"+pass+"'",null);

        if(cursor.getCount()>0){
            return true;
        }
        return false;
    }
    public long insertIntoLogin(String name,String email,String pass)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put("Name",name);
        contentValues.put("Email_ID",email);
        contentValues.put("Password",pass);

        long rowInserted=db.insert("User_TB",null,contentValues);
        return rowInserted;
    }

}
