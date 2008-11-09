/* $Id$
 * 
 * Copyright 2008 Steven Osborn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bitsetters.android.notes;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * DBHelper class.  
 * 
 * The overall theme of this class was borrowed from the Notepad
 * example Open Handset Alliance website.  It's essentially a very
 * primitive database layer.
 * 
 * @author Steven Osborn - http://steven.bitsetters.com
 */
public class DBHelper {

    private static final String DATABASE_NAME = "notes";
    private static final String TABLE_DBVERSION = "dbversion";
    private static final String TABLE_NOTES = "notes";
    private static final int DATABASE_VERSION = 1;
    private static String TAG = "DBHelper";
    Context myCtx;

    private static final String DBVERSION_CREATE = 
    	"create table " + TABLE_DBVERSION + " ("
    		+ "version integer not null);";

    private static final String NOTES_CREATE =
        "create table " + TABLE_NOTES + " ("
    	    + "id integer primary key autoincrement, "
            + "note text, "
            + "lastedit text);";

    private static final String NOTES_DROP =
    	"drop table " + TABLE_NOTES + ";";

    private SQLiteDatabase db;

    /**
     * 
     * @param ctx
     */
    public DBHelper(Context ctx) {
    	myCtx = ctx;
		try {
			db = myCtx.openOrCreateDatabase(DATABASE_NAME, 0,null);

			// Check for the existence of the DBVERSION table
			// If it doesn't exist than create the overall data,
			// otherwise double check the version
			Cursor c =
				db.query("sqlite_master", new String[] { "name" },
						"type='table' and name='"+TABLE_DBVERSION+"'", null, null, null, null);
			int numRows = c.getCount();
			if (numRows < 1) {
				CreateDatabase(db);
			} else {
				int version=0;
				Cursor vc = db.query(true, TABLE_DBVERSION, new String[] {"version"},
						null, null, null, null, null,null);
				if(vc.getCount() > 0) {
				    vc.moveToFirst();
				    version=vc.getInt(0);
				}
				vc.close();
				if (version!=DATABASE_VERSION) {
					Log.e(TAG,"database version mismatch");
				}
			}
			c.close();
			

		} catch (SQLException e) {
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		} finally {
			db.close();
		}
    }

    private void CreateDatabase(SQLiteDatabase db)
    {
		try {
			db.execSQL(DBVERSION_CREATE);
			ContentValues args = new ContentValues();
			args.put("version", DATABASE_VERSION);
			db.insert(TABLE_DBVERSION, null, args);

			db.execSQL(NOTES_CREATE);
		} catch (SQLException e) {
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		} 
    }
    
    public void deleteDatabase()
    {
        try {
			db = myCtx.openOrCreateDatabase(DATABASE_NAME, 0,null);
			db.execSQL(NOTES_DROP);
			db.execSQL(NOTES_CREATE);
        } catch (SQLException e) {
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		} finally {
			db.close();
		}    	
    }
    
    /**
     * Close database connection
     */
    public void close() {
    	try {
    		db.close();
	    } catch (SQLException e) {
	    	Log.d(TAG,"close exception: " + e.getLocalizedMessage());
	    }
    }

////////// Notes Functions ////////////////
	
	
	/**
	 * 
	 * @param entry
	 */
	public void addNote(NoteEntry entry) {
	    ContentValues initialValues = new ContentValues();
	    initialValues.put("note", entry.note);
	
	    try {
			db = myCtx.openOrCreateDatabase(DATABASE_NAME, 0,null);
	        db.insert(TABLE_NOTES, null, initialValues);
		} catch (SQLException e) {
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		} finally {
			db.close();
		}
	}
	
	/**
	 * 
	 * @param Id
	 */
	public void deleteNote(long Id) {
	    try {
			db = myCtx.openOrCreateDatabase(DATABASE_NAME, 0,null);
			db.delete(TABLE_NOTES, "id=" + Id, null);
		} catch (SQLException e) {
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		} finally {
			db.close();
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<NoteEntry> fetchAllRows(){
	    ArrayList<NoteEntry> ret = new ArrayList<NoteEntry>();
	    try {
			db = myCtx.openOrCreateDatabase(DATABASE_NAME, 0,null);
	        Cursor c;
	        c = db.query(TABLE_NOTES, new String[] {
                "id", "note"},
                null, null, null, null, null);
	        int numRows = c.getCount();
	        c.moveToFirst();
	        for (int i = 0; i < numRows; ++i) {
	            NoteEntry row = new NoteEntry();
	            row.id = c.getInt(0);
	            row.note = c.getString(1);
	            
	            ret.add(row);
	            c.moveToNext();
	        }
	        c.close();
		} catch (SQLException e) {
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		} finally {
			db.close();
		}
	    return ret;
	}
	
	/**
	 * 
	 * @param Id
	 * @return
	 */
	public NoteEntry fetchNote(int Id) {
	    NoteEntry row = new NoteEntry();
	    try {
			db = myCtx.openOrCreateDatabase(DATABASE_NAME, 0,null);
	        Cursor c =
	            db.query(true, TABLE_NOTES, new String[] {
	                "id", "note", "lastedit"}, "id=" + Id, null, null, null, null, null);
	        if (c.getCount() > 0) {
	            c.moveToFirst();
	            row.id = c.getInt(0);
	            row.note = c.getString(1);
	        } else {
	            row.id = -1;
	        }
	        c.close();
		} catch (SQLException e) {
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		} finally {
			db.close();
		}
	    return row;
	}
	
	/**
	 * 
	 * @param Id
	 * @param entry
	 */
	public void updateNote(long Id, NoteEntry entry) {
	    ContentValues args = new ContentValues();
	    args.put("note", entry.note);
	    
	    try {
			db = myCtx.openOrCreateDatabase(DATABASE_NAME, 0,null);
			db.update(TABLE_NOTES, args, "id=" + Id, null);
		} catch (SQLException e) {
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		} finally {
			db.close();
		}
	}
}

