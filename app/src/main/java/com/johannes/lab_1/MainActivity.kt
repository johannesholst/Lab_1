package com.johannes.lab_1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.johannes.lab_1.SignInActivity.Companion.userList
import android.widget.Toast
import com.google.gson.Gson
import com.johannes.lab_1.SignInActivity.Companion.SHARED_PREFS_NAME
import com.johannes.lab_1.SignInActivity.Companion.USER_LIST_KEY
import com.johannes.lab_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    private fun saveUserList() {
        val prefs = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(userList)
        prefs.edit().putString(USER_LIST_KEY, json).apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getIntExtra("key_user_id", -1)
        val user = userList.find { it.id == userId }

        val btnNote = binding.buttonAddNote

        val etNoteIn = binding.EditTextNote

        val btnAccount = binding.buttonAccount

        val btnAbout = binding.buttonAbout

        etNoteIn.setText(user?.notes)


        btnNote.setOnClickListener {
            val note = etNoteIn.text.toString()
            user?.notes = note
            saveUserList()
            println("User ID: ${user?.id}")
            println("User List: $userList")
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
        }

        btnAccount.setOnClickListener {
            val intentNavigate = Intent(this,LoggedInActivity::class.java).apply{
                putExtra("key_user_id", userId)
            }
            startActivity(intentNavigate)
        }

        btnAbout.setOnClickListener{
            val intentNavigate = Intent(this,AboutActivity::class.java)
            startActivity(intentNavigate)
        }

    }
}
