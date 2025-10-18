package com.example.backendintegrationapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var saveButton: Button
    private lateinit var viewButton: Button

    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameInput = findViewById(R.id.editName)
        emailInput = findViewById(R.id.editEmail)
        saveButton = findViewById(R.id.saveButton)
        viewButton = findViewById(R.id.viewButton)

        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()

            if(name.isEmpty() || email.isEmpty()){
                Toast.makeText(this,"Please enter name and email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = usersRef.push().key
            val user = User(name, email)
            userId?.let {
                usersRef.child(it).setValue(user).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(this,"Saved!", Toast.LENGTH_SHORT).show()
                        nameInput.text.clear()
                        emailInput.text.clear()
                    } else {
                        Toast.makeText(this,"Error saving!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewButton.setOnClickListener {
            startActivity(Intent(this, UserListActivity::class.java))
        }
    }
}