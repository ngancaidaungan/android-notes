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

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class NoteEdit extends Activity {

	public static final String TAG = "NoteEdit";

	public static final int DELETE_INDEX = Menu.FIRST;
	
	private NoteEntry note;
	protected EditText edit_txt;	
	
    private DBHelper dbHelper=null;

    public static class NoteHeader extends TextView {
		private Paint mLine;

    	public NoteHeader(Context context, AttributeSet attrs) {
			super(context, attrs);
			
			setPadding(20, 0, 0, 0);
			
			setHeight(20);
			mLine = new Paint();
			mLine.setStyle(Paint.Style.STROKE);
			mLine.setARGB(100, 255, 0, 0);
    	}
       		
    	   @Override
           protected void onDraw(Canvas canvas) {               
               Paint paint = mLine;

               canvas.drawLine(getLeft(), getHeight() -1, getRight(), getHeight()  -1, paint);
               canvas.drawLine(10, getTop(), 10, getBottom(), paint);
               canvas.drawLine(15, getTop(), 15, getBottom(), paint);
 
               super.onDraw(canvas);
           }    	
    }

    
    /**
     * A custom EditText that draws lines between each line of text that is displayed.
     */
    public static class LinedEditText extends EditText {
        private Rect mRect;
        private Paint mPaint;
        private Paint mVert;

        
        // we need this constructor for LayoutInflater
        public LinedEditText(Context context, AttributeSet attrs) {
            super(context, attrs);

            mRect = new Rect();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setARGB(100, 0, 0, 255);
            mVert = new Paint();
            mVert.setStyle(Paint.Style.STROKE);
            mVert.setARGB(100, 255, 0, 0);
            setPadding(20, 0, 0, 0);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int count = getLineCount();
            
            Rect r = mRect;
            Paint paint = mPaint;

//            int height = getHeight();
//            int line_height = getLineHeight();
//            int count = height / line_height;
//            
//            for (int i=1; i< count; i++) {
//            	int posY = i*line_height;
//            	canvas.drawLine(getLeft(), posY + 1, getRight(), posY + 1, paint);
//            }
//            
//        	canvas.drawLine(10, getTop(), 10, getBottom(), mVert);
//        	canvas.drawLine(15, getTop(), 15, getBottom(), mVert);

            
            for (int i = 0; i < count; i++) {
                int baseline = getLineBounds(i, r);

                canvas.drawLine(r.left - 20, baseline + 1, r.right, baseline + 1, paint);
                canvas.drawLine(10, r.top, 10, r.bottom, mVert);
                canvas.drawLine(15, r.top, 15, r.bottom, mVert);
            }

            super.onDraw(canvas);
        }
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
	
		menu.add(0,DELETE_INDEX, 0, R.string.delete)
			.setIcon(android.R.drawable.ic_menu_delete)
			.setShortcut('1', 'd');

		return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case DELETE_INDEX:
		    deleteNote();
		    finish();
		    break;
		}
		return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
    	menu.findItem(DELETE_INDEX).setEnabled(note.id >= 0);
    	return super.onMenuOpened(featureId, menu);
    }
    
    private void deleteNote() {
    	dbHelper.deleteNote(note.id);
    }
	
    private void save() {
		note.note = edit_txt.getText().toString();
		if (note.id >= 0) {
			// Editing an existing note
			dbHelper.updateNote(note.id, note);
		}
		else {
			// Creating a note
			dbHelper.addNote(note);
		}
    }
	    
    @Override
    protected void onPause() {
		super.onPause();
		
		Log.d(TAG,"onPause()");
		if (dbHelper != null) {
			save();
			dbHelper = null;
		}
    }
    
    @Override
    public void onStop() {
		super.onStop();
		Log.d(TAG,"onStop()");
		if (dbHelper != null) {
			save();
			dbHelper=null;
		}
    }
    
    @Override
    protected void onResume() {
		super.onResume();
		
		Log.d(TAG,"onResume()");

		if (dbHelper == null) {
		    dbHelper = new DBHelper(this);
		}
    }

	/**
	 *  Called when the activity is first created.
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.note_edit);
        edit_txt = (EditText) findViewById(R.id.edit_note);
        edit_txt.setLongClickable(true);
        

        if (dbHelper==null) {
			dbHelper = new DBHelper(this);
		}
    	
        int id = getIntent().getIntExtra("id", -1);
        
        if (id >= 0) {
	        note = dbHelper.fetchNote(id);
	        edit_txt.setText(note.note);
        }
        else {
        	note = new NoteEntry();
        	note.id = -1;
        }
    }
    
}
