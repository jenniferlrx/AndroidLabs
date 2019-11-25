package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
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

public class ChatRoomActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "ChatRoom_Activity";
    public static final String MESSAGE_TEXT = "TEXT";
    public static final String MESSAGE_ID = "ID";

    public static final String MESSAGE_POSITION = "POSITION";
    public static final String MESSAGE_ISSENT = "ISSENT";
    public static final int MESSAGE_DETAIL_ACTIVITY = 345;
    public static SQLiteDatabase db;

    ArrayList<Message> messages = new ArrayList<Message>();

    BaseAdapter myAdapter;
    static MyDatabaseOpenHelper dbOpener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //get a database:
        dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();

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
        boolean isTablet = findViewById(R.id.fragmentLocation) != null;


        listView.setOnItemClickListener( (list, item, position, id) -> {

            Bundle dataToPass = new Bundle();
            dataToPass.putString(MESSAGE_TEXT, messages.get(position).getText() );
            dataToPass.putInt(MESSAGE_POSITION, position);
            dataToPass.putLong(MESSAGE_ID, messages.get(position).getId());
            dataToPass.putBoolean(MESSAGE_ISSENT,messages.get(position).IsSender());

            if(isTablet)
            {
                DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(ChatRoomActivity.this, MessageDetailActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, MESSAGE_DETAIL_ACTIVITY); //make the transition
            }
        });


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

    public void deleteMessageId(long id, int position)
    {
        Log.i("Delete this message:" , " id="+id);
        messages.remove(position);
        db.delete(MyDatabaseOpenHelper.TABLE_NAME, MyDatabaseOpenHelper.COL_ID+"="+id, null );
        myAdapter.notifyDataSetChanged();
    }



    //This function only gets called on the phone. The tablet never goes to a new activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MESSAGE_DETAIL_ACTIVITY)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra(MESSAGE_ID, 0);
                int position = data.getIntExtra(MESSAGE_POSITION, 0);
                deleteMessageId(id, position);
            }
        }
    }



    class MyListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int i) {
            return messages.get(i);
        }

        @Override
        public long getItemId(int i) {
            return messages.get(i).getId();
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

