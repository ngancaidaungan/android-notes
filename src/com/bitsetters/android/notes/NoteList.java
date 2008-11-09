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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NoteList extends ListActivity {

	public static final String TAG = "NoteList";
	public static final int ADD_INDEX = Menu.FIRST;
	public static final int SETTINGS_INDEX = Menu.FIRST + 1;
	public static final int HELP_INDEX = Menu.FIRST + 2;
	
	private List<NoteEntry> rows;
    private DBHelper dbHelper=null;

	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	if (dbHelper==null) {
			dbHelper = new DBHelper(this);
		}
        
        setContentView(R.layout.note_list);
        fillData();
    }
    
    @Override
    protected void onPause() {
		super.onPause();
		
		Log.d(TAG,"onPause()");
		if (dbHelper != null) {
			dbHelper = null;
		}
    }
    
    @Override
    public void onStop() {
		super.onStop();
		
		Log.d(TAG,"onStop()");
		if (dbHelper != null) {
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
		
        fillData();
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case ADD_INDEX:
		    addNote();
		    break;
		case HELP_INDEX:
		    Intent help = new Intent(this, Help.class);
		    startActivity(help);
			break;
		}
		return super.onOptionsItemSelected(item);
    }

   private String get_description(NoteEntry ent) {
	   String first = ent.note.split("\\n")[0];
	   if (first.length() > 30) {
		   first = first.substring(0, 27) + "...";
	   }
	   return first;
   }
   
   
    
   private void fillData() {
	   
	  rows = dbHelper.fetchAllRows();
	   
	  List<String> items = new ArrayList<String>();

	  for(NoteEntry ent : rows) {
		  items.add(get_description(ent));
	  }
	   
	   ArrayAdapter<String> entries = 
		    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		setListAdapter(entries);
   }
    
   protected void addNote() {
	   Intent i = new Intent(this, NoteEdit.class);
	   i.putExtra("id", -1);
	   startActivity(i);
   }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
	
		menu.add(0,ADD_INDEX, 0, R.string.add)
			.setIcon(android.R.drawable.ic_menu_add)
			.setShortcut('1', 'a');
		menu.add(0, SETTINGS_INDEX, 0, R.string.settings)
			.setShortcut('2','s')
			.setIcon(android.R.drawable.ic_menu_preferences);
		menu.add(0, HELP_INDEX, 0, R.string.help)
			.setShortcut('9', 'h')
			.setIcon(android.R.drawable.ic_menu_help);

		return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);

    	Intent i = new Intent(this, NoteEdit.class);
    	i.putExtra("id", rows.get(position).id);
    	startActivity(i);
    }

}