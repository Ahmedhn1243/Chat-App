package com.example.chat_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userlist:ArrayList<User>
    private lateinit var adaptor: UserAdaptor
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDbRef:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mAuth=FirebaseAuth.getInstance()
        mDbRef=FirebaseDatabase.getInstance().getReference()
        userlist=ArrayList()
        adaptor = UserAdaptor(this,userlist)

        userRecyclerView=findViewById(R.id.userRecyclerview)

        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter =adaptor

        mDbRef.child("user").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userlist.clear()

                for(postSnapshot in snapshot.children){
                    val currentUser=postSnapshot.getValue(User::class.java)

                    if(mAuth.currentUser?.uid!=currentUser?.uid){

                        userlist.add(currentUser!!)
                    }

                }


                adaptor.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater .inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId==R.id.logout){

            mAuth.signOut()
            val intent=Intent(this@MainActivity, Login::class.java)
            startActivity(intent)

            return true
        }

        return true

    }
}