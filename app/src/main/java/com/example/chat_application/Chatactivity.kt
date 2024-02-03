package com.example.chat_application


//import android com.example.chat_application
import android.os.Bundle

import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Chatactivity : AppCompatActivity() {


    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox:EditText
    private lateinit var sentButton:ImageView
    private lateinit var messageAdaptor: MessageAdaptor
    private final lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef:DatabaseReference

    var senderRoom:String?=null
    var receiverRoom:String?=null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatactivity)


        val name =intent.getStringExtra("name")
        val receiveruid =intent.getStringExtra("uid")
        val senderUid= FirebaseAuth.getInstance().currentUser?.uid

        mDbRef=FirebaseDatabase.getInstance().getReference()

        senderRoom =  receiveruid + senderUid
        receiverRoom= senderUid + receiveruid


        supportActionBar?.title=name


        chatRecyclerView = findViewById(R.id.chatRecyclerview)
        messageBox=findViewById(R.id.messagebox)
        sentButton=findViewById(R.id.sentButton)
        messageList= ArrayList()


        messageAdaptor= MessageAdaptor(this,messageList)



        chatRecyclerView.layoutManager=LinearLayoutManager(this)
        chatRecyclerView.adapter= messageAdaptor


//        logic for adding data to recyclerview
        mDbRef.child("chats").child(senderRoom!!).child("message")
            .addValueEventListener(object:ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for (postSnapshot in snapshot.children){

                        val message=postSnapshot.getValue(Message::class.java)

                        messageList.add(message!!)
                    }

                    messageAdaptor.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })


// adding message to databse
        sentButton.setOnClickListener{
            val message = messageBox.text.toString()
            val messageObject = Message(message , senderUid)

            mDbRef .child("chats").child(senderRoom!!).child("message").push()
                .setValue(messageObject).addOnSuccessListener {

                    mDbRef .child("chats").child(receiverRoom!!).child("message").push()
                        .setValue(messageObject)

                }

            messageBox.setText("")
        }

    }
}