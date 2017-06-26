package de.example.android.floatingactionbuttonbasic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.floatingactionbuttonbasic.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddItem extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
    }

    public void Submit(View v){
        final EditText edit =  (EditText) findViewById(R.id.ItemName);
        String Itemname = (String) edit.getText().toString();
        //Get String vom EditText

        try {
            Itemname += ";";
            //Add Semicolon to String
            Itemname += getFileContent();
            //Add File content to String
        }
        catch (IOException e) {
            Toast.makeText(this, "Exception: "+e, Toast.LENGTH_SHORT ).show();
        }


        File file = new File(getApplicationContext().getFilesDir(), "ItemList.txt");
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput("ItemList.txt", getApplicationContext().MODE_PRIVATE);
            outputStream.write(Itemname.getBytes()); // Write String to File
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, MainActivity.class)); // Start Listview Activity

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
            Toast.makeText(this, "File Not Found", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Execption: "+e, Toast.LENGTH_SHORT ).show();
        }
        return null;
    }
}
