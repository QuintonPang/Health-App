package com.quintonpyx.healthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtName: EditText
    private lateinit var btnSignUp: Button
    private lateinit var btnLogin: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        edtName = findViewById(R.id.edt_name)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnSignUp = findViewById(R.id.btn_signup)
        btnLogin = findViewById(R.id.btn_login)

        mAuth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if ((name.isEmpty()) or (email.isEmpty()) or (password.isEmpty())) {
                Toast.makeText(this, "Please fill up all fields to sign up.", Toast.LENGTH_LONG)
                    .show()
            } else {
                signUp(name, email, password)
            }
        }

        btnLogin.setOnClickListener{
            // redirect
            val intent = Intent(this@SignUp,Login::class.java)
            startActivity(intent)
        }
    }

    private fun signUp(name:String, email:String, password:String){
        val newEmail = email.trim()
        // referenced from google firebase android password authentication documentation
        mAuth.createUserWithEmailAndPassword(newEmail, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success")
//                    val user = auth.currentUser
//                    updateUI(user)
                    addUserToDatabase(name,newEmail, mAuth.currentUser?.uid!!)
                    val intent = Intent(this@SignUp,MainActivity::class.java)
//                    Toast.makeText(this@SignUp, "Signed up successfully, please log in.", Toast.LENGTH_LONG).show()
                    finish()
                    startActivity(intent)

                } else {

                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                    Toast.makeText(baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
//                    Toast.makeText(this@SignUp, "Some error occured.", Toast.LENGTH_SHORT).show()

                    Toast.makeText(this@SignUp, task.exception?.message, Toast.LENGTH_LONG).show()
                    Log.e("SIGN UP ERROR: ",task.exception.toString())
                }
            }



    }
    private fun addUserToDatabase(name:String, email:String, uid:String){
        mDbRef = FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid).setValue(User(name,email,uid,null))
    }
}