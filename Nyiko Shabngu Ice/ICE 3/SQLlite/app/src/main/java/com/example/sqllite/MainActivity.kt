package com.example.sqllite

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.sqllite.databinding.ActivityMainBinding


import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.google.android.material.textfield.TextInputEditText

import com.example.sqllite.databinding.DialogUpdateUserBinding


class MainActivity : AppCompatActivity(), UserAdapter.UserClickListener  {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var userAdapter: UserAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize database helper
        dbHelper = DatabaseHelper(this)

        // Setup RecyclerView
        setupRecyclerView()

        // Add user button click listener
        binding.buttonAdd.setOnClickListener {
            addNewUser()
        }

        // View all users button click listener
        binding.buttonView.setOnClickListener {
            refreshUsersList()
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter(emptyList(), this)
        binding.recyclerViewUsers.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
            setHasFixedSize(true)
        }
        refreshUsersList()
    }

    private fun addNewUser() {
        val name = binding.editTextName.text.toString()
        val email = binding.editTextEmail.text.toString()

        if (validateInput(name, email)) {
            val user = DatabaseHelper.User(name = name, email = email)
            val id = dbHelper.insertUser(user)
            if (id != -1L) {
                showSnackbar("User added successfully!")
                clearInputFields()
                refreshUsersList()
            } else {
                showSnackbar("Error adding user")
            }
        } else {
            showSnackbar("Please fill all fields")
        }
    }

    private fun validateInput(name: String, email: String): Boolean {
        return name.isNotEmpty() && email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun clearInputFields() {
        binding.editTextName.text?.clear()
        binding.editTextEmail.text?.clear()
    }

    private fun refreshUsersList() {
        val users = dbHelper.getAllUsers()
        if (users.isEmpty()) {
            binding.tvNoUsers.visibility = android.view.View.VISIBLE
            binding.recyclerViewUsers.visibility = android.view.View.GONE
        } else {
            binding.tvNoUsers.visibility = android.view.View.GONE
            binding.recyclerViewUsers.visibility = android.view.View.VISIBLE
            userAdapter.updateUsers(users)
        }
    }

    // UserClickListener Implementation
    override fun onUserClick(user: DatabaseHelper.User) {
        showUpdateDeleteDialog(user)
    }

    override fun onUserLongClick(user: DatabaseHelper.User) {
        showDeleteConfirmationDialog(user)
    }

    private fun showUpdateDeleteDialog(user: DatabaseHelper.User) {
        MaterialAlertDialogBuilder(this)
            .setTitle("User Options")
            .setItems(arrayOf("Update", "Delete")) { _, which ->
                when (which) {
                    0 -> showUpdateDialog(user)
                    1 -> showDeleteConfirmationDialog(user)
                }
            }
            .show()
    }

    private fun showUpdateDialog(user: DatabaseHelper.User) {
        val dialogBinding = DialogUpdateUserBinding.inflate(layoutInflater)

        // Pre-fill the current values
        dialogBinding.editTextUpdateName.setText(user.name)
        dialogBinding.editTextUpdateEmail.setText(user.email)

        MaterialAlertDialogBuilder(this)
            .setTitle("Update User")
            .setView(dialogBinding.root)
            .setPositiveButton("Update") { _, _ ->
                handleUserUpdate(user, dialogBinding)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun handleUserUpdate(user: DatabaseHelper.User, dialogBinding: DialogUpdateUserBinding) {
        val newName = dialogBinding.editTextUpdateName.text.toString()
        val newEmail = dialogBinding.editTextUpdateEmail.text.toString()

        if (validateInput(newName, newEmail)) {
            val updatedUser = user.copy(name = newName, email = newEmail)
            val rowsAffected = dbHelper.updateUser(updatedUser)

            if (rowsAffected > 0) {
                showSnackbar("User updated successfully!")
                refreshUsersList()
            } else {
                showSnackbar("Error updating user")
            }
        } else {
            showSnackbar("Please enter valid information")
        }
    }

    private fun showDeleteConfirmationDialog(user: DatabaseHelper.User) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete User")
            .setMessage("Are you sure you want to delete ${user.name}?")
            .setPositiveButton("Delete") { _, _ ->
                deleteUser(user)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteUser(user: DatabaseHelper.User) {
        val rowsAffected = dbHelper.deleteUser(user.id)
        if (rowsAffected > 0) {
            showSnackbar("User deleted successfully!")
            refreshUsersList()
        } else {
            showSnackbar("Error deleting user")
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}