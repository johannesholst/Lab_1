package com.johannes.lab_1


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.google.gson.Gson
import com.johannes.lab_1.SignInActivity.Companion.SHARED_PREFS_NAME
import com.johannes.lab_1.SignInActivity.Companion.USER_LIST_KEY
import com.johannes.lab_1.SignInActivity.Companion.userList
import com.johannes.lab_1.databinding.ActivityLoggedInBinding

class LoggedInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoggedInBinding

    private fun clearRememberState() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("username")
        editor.remove("password")
        editor.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoggedInBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val users = userList
        val userId = intent.getIntExtra("key_user_id", -1)
        val user = userList.find { it.id == userId }

        val displayUsername: TextView = binding.textViewName
        val displayPassword: TextView = binding.textViewPass
        val btnDelete = binding.buttonDeleteUser
        val btnCheckList = binding.checkUserList
        val sSeePass = binding.SwitchSeePass
        val btnLogout = binding.buttonLogout

        displayUsername.text = user?.name
        displayPassword.text = "********"

        sSeePass.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                displayPassword.text = user?.pass
            } else {
                displayPassword.text = "********"
            }
        }
        println("these are the existing users $users")


        btnLogout.setOnClickListener {
            clearRememberState()
            // Start the SignInActivity to allow the user to sign in again
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnDelete.setOnClickListener {
            val intentRemove = Intent(this, SignInActivity::class.java).apply {
                putExtra("key_user_id", userId)
            }

            users.remove(user)
            val sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(USER_LIST_KEY, Gson().toJson(users))
            editor.apply()
            startActivity(intentRemove)
        }

        btnCheckList.setOnClickListener {
            println("Current user $user")
            val intentNavigate = Intent(this,MainActivity::class.java).apply{
                putExtra("key_user_id", userId)
            }
            startActivity(intentNavigate)
        }

    }
}

