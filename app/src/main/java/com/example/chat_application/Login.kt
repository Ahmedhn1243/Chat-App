package com.example.chat_application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var edtEmail:EditText
//private lateinit var edtname:EditText
    private lateinit var edtpassword:EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignup: Button
    private lateinit var mAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        mAuth=FirebaseAuth.getInstance()
        edtEmail = findViewById(R.id.edt_email)
        edtpassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btn1)
        btnSignup = findViewById(R.id.btn2)

        btnSignup.setOnClickListener{

            val intent= Intent(this, Signup::class.java)
            startActivity(intent)

        }

        btnLogin.setOnClickListener{

            val email=edtEmail.text.toString()
            val password=edtpassword.text.toString()

            login(email, password)
        }


    }

    private fun login(email:String,password:String){
        //logic for logging user

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success,
                    val intent= Intent(this@Login, MainActivity::class.java)
                    finish()
                    startActivity(intent)


                } else {
                    // If sign in fails, display a message to the user.
                   Toast.makeText(this@Login, "User does not exist" , Toast.LENGTH_SHORT).show()
                }
            }
    }
}