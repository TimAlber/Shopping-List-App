/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package de.example.android.floatingactionbuttonbasic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import de.example.android.common.activities.SampleActivityBase;
import de.example.android.common.logger.Log;

import com.example.android.floatingactionbuttonbasic.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p>
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class MainActivity extends SampleActivityBase {

    public static final String TAG = "MainActivity";
    String[] shoppinitems = {"Thisn"};
    ArrayList<String> shoppinitems2 = new ArrayList<String>();
    int x;

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;
    int n;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            File file = new File(getApplicationContext().getFilesDir(), "ItemList.txt");
            if (file.exists()) {
                shoppinitems = getFileContent().split(";", -1); // Read File and davide it up in a List of Strings.
                for (String nitm : shoppinitems) {
                    if (!nitm.isEmpty()) { // Make it so that emty List entry's dont appear in the Listview
                        shoppinitems2.add(nitm);
                    }
                }
            }

        }
        catch (IOException e) {
            Toast.makeText(this, "Exception: "+e, Toast.LENGTH_SHORT ).show();
        }


        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, shoppinitems2);
        ListView listView = (ListView) findViewById(R.id.shopping_item_list);
        listView.setAdapter(adapter);
        // Make Listview dispaly the List of Strings


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int x;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                File file = new File(getApplicationContext().getFilesDir(), "ItemList.txt");
                FileOutputStream outputStream;
                try {
                    outputStream = openFileOutput("ItemList.txt", getApplicationContext().MODE_PRIVATE);

                    for (String Item : shoppinitems) {
                        if (Item != shoppinitems[position]) {
                            Item += ";";
                            try {
                                outputStream.write(Item.getBytes()); // Write Item and Semicolon to File excepet the one Clicked on
                            } catch (IOException e) {
                                Log.d("", "" + e);
                            }

                        }
                    }
                }
                catch(FileNotFoundException e){
                    Log.d("",""+e);
                }

                startActivity(new Intent(getApplicationContext(), MainActivity.class)); // Start Main Activity again so that the Listview is updated
            }
        });

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FloatingActionButtonBasicFragment fragment = new FloatingActionButtonBasicFragment(); // Some stuff so that the Floating Action Button works
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }


    private String getFileContent() throws IOException { // Function to read File to String
        File itemfile = new File(getApplicationContext().getFilesDir(), "ItemList.txt");
        try {

            FileInputStream fileInputStream = new FileInputStream(itemfile);
            StringBuilder sb;
            sb = new StringBuilder();
            while (fileInputStream.available() > 0) {
                sb.append((char) fileInputStream.read());
            }
            String fileContent;
            if (sb != null) {
                fileContent = sb.toString();
                return fileContent;
            }
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            File filen = new File(getApplicationContext().getFilesDir(), "ItemList.txt");
            PrintWriter writer = new PrintWriter(filen);
            writer.print("");
            writer.close();
            Toast.makeText(this, "File Not Found", Toast.LENGTH_SHORT ).show();
        } catch (Exception e) {
            Toast.makeText(this, "Execption: "+e, Toast.LENGTH_SHORT ).show();
        }
        return null;
    }
}
