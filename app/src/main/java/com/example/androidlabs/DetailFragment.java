package com.example.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class DetailFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long id;
    private int position;
    private boolean isSender;

    public void setTablet(boolean tablet) { isTablet = tablet; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(ChatRoomActivity.MESSAGE_ID );
        position = dataFromActivity.getInt(ChatRoomActivity.MESSAGE_POSITION );
        isSender = dataFromActivity.getBoolean(ChatRoomActivity.MESSAGE_ISSENT);

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_detail, container, false);

        //show the message
        TextView message = (TextView)result.findViewById(R.id.message);
        message.setText(dataFromActivity.getString(ChatRoomActivity.MESSAGE_TEXT));

        //show the id:
        TextView idView = (TextView)result.findViewById(R.id.idText);
        idView.setText("\nID = "+id);


        //show if sent or received:
        TextView sendView = (TextView)result.findViewById(R.id.isSender);
        if(isSender) sendView.setText("\nThe message is a sent message");
        else sendView.setText("\nThe message is a received message ");


        // get the delete button, and add a click listener:
        Button deleteButton = (Button)result.findViewById(R.id.fragmentDelBtn);
        deleteButton.setOnClickListener( clk -> {

            if(isTablet) { //both the list and details are on the screen:
                ChatRoomActivity parent = (ChatRoomActivity)getActivity();
                parent.deleteMessageId(id, position); //this deletes the item and updates the list


                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                MessageDetailActivity parent = (MessageDetailActivity) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra(ChatRoomActivity.MESSAGE_ID, dataFromActivity.getLong(ChatRoomActivity.MESSAGE_ID ));
                backToFragmentExample.putExtra(ChatRoomActivity.MESSAGE_POSITION, dataFromActivity.getInt(ChatRoomActivity.MESSAGE_POSITION ));

                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });



        return result;
    }

}
