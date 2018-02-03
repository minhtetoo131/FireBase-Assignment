package com.minhtetoo.firebase_assignment.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.minhtetoo.firebase_assignment.R;
import com.minhtetoo.firebase_assignment.adapters.ChatListAdapter;
import com.minhtetoo.firebase_assignment.data.MessageVo;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by min on 1/31/2018.
 */

public class ChatActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 101;
    private static final String OTHER_USERA_NAME = "chatmate name";
    String room_type_1;
    String room_type_2;
    String uid,receiveruid;
    String chatemateName;

    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    StorageReference StorageRef;

    @BindView(R.id.rv_chat)RecyclerView rvChat;
    ChatListAdapter mChatListAdapter;
    List<MessageVo> messages;

    @BindView(R.id.et_message)EditText etMessage;
    @BindView(R.id.btn_send_pic)ImageView btnSendPic;
    @BindView(R.id.btn_send_text)ImageView btnsendText;



    private static final String OTHER_USERA_ID = "other user id";
    private static final String MY_UID = "my user id";

    public static Intent newIntent(Context context ,String otherUID ,String otherUserName ){
        Intent intent = new Intent(context,ChatActivity.class);
        intent.putExtra(OTHER_USERA_ID ,otherUID);
        intent.putExtra(OTHER_USERA_NAME ,otherUserName);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this, this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = currentUser.getUid();


        Intent intent = getIntent();
        receiveruid = intent.getStringExtra(OTHER_USERA_ID);
        chatemateName = intent.getStringExtra(OTHER_USERA_NAME);

        toolbar.setTitle(chatemateName);


        rvChat.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        mChatListAdapter = new ChatListAdapter(getApplicationContext(),uid);
        rvChat.setAdapter(mChatListAdapter);
        messages = new ArrayList<>();


        room_type_1 = uid + "_" + receiveruid;
        room_type_2 = receiveruid + "_" + uid;

        StorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance()
                .getReference();

        databaseReference.child("chats").child(room_type_1)
                .getRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshots) {
                        messages.clear();
                        for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()){
                            MessageVo messageVo = dataSnapshot.getValue(MessageVo.class);
                            messages.add(messageVo);


                        }
                        mChatListAdapter.setNewData(messages);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        btnsendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = etMessage.getText().toString().trim();
                createMessageAndWriteDatabase(messageText,null,uid);

                etMessage.setText("");
            }
        });

        btnSendPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent , PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            if(resultCode == RESULT_OK  && data != null){
                Uri imageUri = data.getData();
                Log.d("tag", imageUri.toString());
//                File file = new File(getPath(imageUri));

                uploadFile(imageUri.toString());



            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    public void uploadFile(String fileToUpload) {
        Uri file = Uri.parse(fileToUpload);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference pathToUpload = storage.getReferenceFromUrl("gs://fir-assignment-bdf65.appspot.com/Images");

        StorageReference uploadingFile = pathToUpload.child(file.getLastPathSegment());
        UploadTask uploadTask = uploadingFile.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.toString();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri uploadedImageUrl = taskSnapshot.getDownloadUrl();

                createMessageAndWriteDatabase(null,uploadedImageUrl.toString(),uid);
            }
        });

    }

    private void createMessageAndWriteDatabase(String sendMessage, String sendPicUrl, String senderId){
        MessageVo newMessage = new MessageVo(sendMessage,sendPicUrl,senderId);

        FirebaseDatabase.getInstance()
                .getReference()
                .child("chats")
                .child(room_type_1)
                .push()
                .setValue(newMessage);

        FirebaseDatabase.getInstance()
                .getReference()
                .child("chats")
                .child(room_type_2)
                .push()
                .setValue(newMessage);
    }
}
