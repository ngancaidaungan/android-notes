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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NoteEdit extends Activity {

	protected Button save_btn;
	protected Button cancel_btn;
	protected EditText edit_txt;	
	
    private final OnClickListener save_click = new OnClickListener() {
		public void onClick(View v) {
			//db.addNote();
			finish();
		}
    };
    
    private final OnClickListener cancel_click = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
    };
	
	/**
	 *  Called when the activity is first created.
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);
        save_btn = (Button) findViewById(R.id.save_note);
        save_btn.setOnClickListener(save_click);
        cancel_btn = (Button) findViewById(R.id.cancel_note);
        cancel_btn.setOnClickListener(cancel_click);
        edit_txt = (EditText) findViewById(R.id.edit_note);
    }
}
