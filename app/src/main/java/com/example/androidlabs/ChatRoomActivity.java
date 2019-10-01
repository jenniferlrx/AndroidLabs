package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    ArrayList<Message> messages = new ArrayList<Message>(
            Arrays.asList(new Message("Hello", true),
                    new Message("Hi", false),
                    new Message("How are you", true),
                    new Message("I'm fine",false)));

    BaseAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ListView listView = findViewById(R.id.chat_list);
        listView.setAdapter(myAdapter = new MyListAdapter());

        Button receive = findViewById(R.id.btn_receive);
        Button send = findViewById(R.id.btn_send);
        EditText newText = findViewById(R.id.newText);

        receive.setOnClickListener(v->{
            messages.add(new Message(newText.getText().toString(),false));
            newText.setText("");
            myAdapter.notifyDataSetChanged();
        });

        send.setOnClickListener(v->{
            messages.add(new Message(newText.getText().toString(),true));
            newText.setText("");
            myAdapter.notifyDataSetChanged();
        });

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

