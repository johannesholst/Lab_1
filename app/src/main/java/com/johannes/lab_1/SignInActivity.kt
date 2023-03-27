package com.johannes.lab_1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.johannes.lab_1.databinding.ActivitySignInBinding
import android.content.Context
import androidx.activity.OnBackPressedCallback

class SignInActivity : AppCompatActivity() {



    companion object {
        const val SHARED_PREFS_NAME = "MyPrefs"
        const val USER_LIST_KEY = "userList"

        val userList = arrayListOf<User>(
            User(1,"Username", "Password","Note" ),
            User(2,"admin","admin","Admin Note"),
            User(3,"Hans","Hans123","Hans note" )
        )
    }


    private fun saveUserList(name: String, pass: String, rememberMe: Boolean) {
        val prefs = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(userList)
        prefs.edit().putString(USER_LIST_KEY, json).apply()

        if (rememberMe) {
            prefs.edit().putString("username", name).apply()
            prefs.edit().putString("password", pass).apply()
        } else {
            prefs.edit().remove("username").apply()
            prefs.edit().remove("password").apply()
        }
    }



    private lateinit var backPressedCallback: OnBackPressedCallback
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)


        val prefs = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(USER_LIST_KEY, null)
        if (json != null) {
            userList.clear()
            userList.addAll(Gson().fromJson(json, Array<User>::class.java))
        }


        val tvPass = binding.textViewPassword
        val tvUser = binding.textViewUsername
        val btnLogin = binding.buttonSignIn
        val chkRememberMe = binding.switchRemember



        val savedUsername = prefs.getString("username", null)
        val savedPassword = prefs.getString("password", null)
        if (savedUsername != null && savedPassword != null) {
            val user = userList.find { it.name == savedUsername && it.pass == savedPassword }
            if (user != null) {
                val intentNavigate = Intent(this, MainActivity::class.java).apply {
                    putExtra("key_user_id", user.id)
                }
                startActivity(intentNavigate)
                finish()
                return
            }
        }

        btnLogin.setOnClickListener {
            val name = tvUser.text.toString().trim()
            val pass = tvPass.text.toString().trim()
            val rememberMe = chkRememberMe.isChecked

            if (name.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Username and password cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                val user = userList.find { it.name == name && it.pass == pass }

                if (user != null) {
                    Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show()
                    val intentNavigate = Intent(this, MainActivity::class.java).apply {
                        putExtra("key_user_id", user.id)
                    }
                    startActivity(intentNavigate)
                    saveUserList(name, pass, rememberMe)
                    finish()
                } else {
                    val userNameExists = userList.any { it.name == name }
                    if (userNameExists) {
                        Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show()
                    } else {
                        val nextUserId = userList.maxByOrNull { it.id }?.id?.plus(1) ?: 1
                        val newUser = User(id = nextUserId, name, pass)
                        Toast.makeText(this, "Creating user", Toast.LENGTH_SHORT).show()
                        userList.add(newUser)
                        val intentNavigate = Intent(this, MainActivity::class.java).apply {
                            putExtra("key_user_id", newUser.id)
                        }
                        startActivity(intentNavigate)
                    }
                }
                println(userList)
            }
        }
    }
}

