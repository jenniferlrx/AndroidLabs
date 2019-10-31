package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatRoomActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "ChatRoom_Activity";

    ArrayList<Message> messages = new ArrayList<Message>();

    BaseAdapter myAdapter;
    static MyDatabaseOpenHelper dbOpener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //get a database:
        dbOpener = new MyDatabaseOpenHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        //query all the results from the database:
        String [] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_ISSENDER, MyDatabaseOpenHelper.COL_MESSAGE};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        printCursor(results);
        results.moveToFirst();
        results.moveToPrevious();
        //find the column indices:
        int isSenderColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ISSENDER);
        int messageColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            boolean isSender = results.getInt(isSenderColumnIndex)>0;
            String message = results.getString(messageColIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            messages.add(new Message(message, isSender, id));
        }
        ListView listView = findViewById(R.id.chat_list);
        listView.setAdapter(myAdapter = new MyListAdapter());

        Button receive = findViewById(R.id.btn_receive);
        Button send = findViewById(R.id.btn_send);
        EditText newText = findViewById(R.id.newText);

        receive.setOnClickListener(v->{
            String text = newText.getText().toString();
            int isSender = 0;
            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string name in the MESSAGE column:
            newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, text);
            //put string email in the ISSENDER column:
            newRowValues.put(MyDatabaseOpenHelper.COL_ISSENDER, isSender);
            //insert in the database:
            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

            //now you have the newId, you can create the Contact object
            Message newMessage = new Message(text, isSender>0, newId);

            //add the new contact to the list:
            messages.add(newMessage);
            //update the listView:
            myAdapter.notifyDataSetChanged();

            //clear the EditText fields:
            newText.setText("");
            myAdapter.notifyDataSetChanged();
        });

        send.setOnClickListener(v->{
            String text = newText.getText().toString();
            int isSender = 1;
            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string name in the MESSAGE column:
            newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, text);
            //put string email in the ISSENDER column:
            newRowValues.put(MyDatabaseOpenHelper.COL_ISSENDER, isSender);
            //insert in the database:
            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

            //now you have the newId, you can create the Contact object
            Message newMessage = new Message(text, isSender>0, newId);

            //add the new contact to the list:
            messages.add(newMessage);
            //update the listView:
            myAdapter.notifyDataSetChanged();

            //clear the EditText fields:
            newText.setText("");
            myAdapter.notifyDataSetChanged();
        });

    }

    static void printCursor(Cursor c){
        c.moveToFirst();
        c.moveToPrevious();
        int columnCounts = c.getColumnCount();
        String [] columnNames = c.getColumnNames();
        String colNames="";

        Log.d(ACTIVITY_NAME, "DATABASE VERSION NUMBER: "+ dbOpener.VERSION_NUM);

        Log.d(ACTIVITY_NAME, "NUMBER OF COLUMNS: "+ columnCounts);


        for (int i=0; i<columnCounts; i++){
            colNames +=(" "+columnNames[i]);
        }
        Log.d(ACTIVITY_NAME, "NAME OF COLUMNS: "+colNames);

        Log.d(ACTIVITY_NAME, "NUMBER OF RESULTS: "+ c.getCount());

        int isSenderColumnIndex = c.getColumnIndex(MyDatabaseOpenHelper.COL_ISSENDER);
        int messageColIndex = c.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE);
        int idColIndex = c.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        Log.d(ACTIVITY_NAME, "ROWS OF COLUMNS");
        while(c.moveToNext())
        {
            boolean isSender = c.getInt(isSenderColumnIndex)>0;
            String message = c.getString(messageColIndex);
            long id = c.getLong(idColIndex);

            //add the new Contact to the array list:
            Log.d(ACTIVITY_NAME, "ID: "+id + "| isSender: "+isSender + "| text: " +message);
        }


    }

    class MyListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int i) {
            return messages.get(i).getText();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View oldView, ViewGroup viewGroup) {
            View thisView = oldView;


                if(messages.get(i).IsSender())
                    thisView = getLayoutInflater().inflate(R.layout.table_row_send_layout, null);
                else
                    thisView = getLayoutInflater().inflate(R.layout.table_row_receive_layout, null);


            TextView message = thisView.findViewById(R.id.row);
            message.setText(messages.get(i).getText());
            return thisView;
        }
    }
}

