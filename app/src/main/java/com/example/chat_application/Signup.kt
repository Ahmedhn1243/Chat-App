package com.example.chat_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Signup : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtname:EditText
    private lateinit var edtpassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        supportActionBar?.hide()

        edtEmail = findViewById(R.id.edt_email)
        edtname = findViewById(R.id.edt_name)
        edtpassword = findViewById(R.id.edt_password)
//        btnLogin = findViewById(R.id.btn1)
        btnSignup = findViewById(R.id.btn2)
        mAuth=FirebaseAuth.getInstance()

        btnSignup.setOnClickListener{

            val name=edtname.text.toString()
            val email= edtEmail.text.toString()
            val password =edtpassword.text.toString()

            signup(name,email,password)
        }



    }

    private fun signup(name:String , email:String , password:String){

        // logic for creating user from firebase documentation

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, jump to home

                    addUserToDatabse(name, email, mAuth.currentUser?.uid!!)

                    val intent= Intent(this@Signup, MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@Signup, "some error occured " , Toast.LENGTH_SHORT ).show()

                }
            }




    }

    private fun addUserToDatabse(name: String, email: String,uid:String){

        mDbRef=FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user" ).child(uid).setValue(User(name,email,uid))


    }
}