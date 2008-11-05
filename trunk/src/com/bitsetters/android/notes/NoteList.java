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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class NoteList extends ListActivity {

	public static final String TAG = "NoteList";
	public static final int EDIT_INDEX = Menu.FIRST;
	public static final int ADD_INDEX = Menu.FIRST + 1;
	public static final int HELP_INDEX = Menu.FIRST + 2;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case ADD_INDEX:
		    //addNote();
		    break;
		case HELP_INDEX:
		    Intent help = new Intent(this, Help.class);
		    startActivity(help);
			break;
		}
		return super.onOptionsItemSelected(item);
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
	
		menu.add(0,ADD_INDEX, 0, R.string.add)
			.setIcon(android.R.drawable.ic_menu_add)
			.setShortcut('1', 'a');
		menu.add(0, HELP_INDEX, 0, R.string.help)
			.setShortcut('9', 'h')
			.setIcon(android.R.drawable.ic_menu_help);

		return super.onCreateOptionsMenu(menu);
    }

}