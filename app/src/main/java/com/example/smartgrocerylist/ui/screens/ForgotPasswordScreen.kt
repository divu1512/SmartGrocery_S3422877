package com.example.smartgrocerylist.ui.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : ComponentActivity() {
    private val auth = FirebaseAuth.getInstance() // Firebase Authentication instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForgotPasswordScreen(onResetPassword = { email -> resetPassword(email) })
        }
    }

    // Sends a password reset email to the user
    private fun resetPassword(email: String) {
        // Validate the email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(onResetPassword: (String) -> Unit) {
    var email by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Card with rounded corners and shadow for elevation effect
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title with a large font size and modern font weight
                Text(
                    text = "Forgot Your Password?",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0288D1) // A light blue color
                    ),
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                // Subtitle text with a smaller font and neutral color
                Text(
                    text = "Enter your registered email to reset your password.",
                    style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                    modifier = Modifier.padding(bottom = 32.dp),
                    textAlign = TextAlign.Center
                )

                // Email input field with rounded corners
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color(0xFF0288D1), // Blue when focused
                        unfocusedIndicatorColor = Color.Gray
                    )
                )

                // Reset Password button with a more modern and larger style
                Button(
                    onClick = { onResetPassword(email) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
                ) {
                    Text(
                        text = "Reset Password",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}














//package com.example.smartgrocerylist.ui.screens
//
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.google.firebase.auth.FirebaseAuth
//
//class ForgotPasswordActivity : ComponentActivity() {
//    private val auth = FirebaseAuth.getInstance() // Firebase Authentication instance
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // Set the Forgot Password screen using Jetpack Compose
//        setContent {
//            ForgotPasswordScreen(onResetPassword = { email -> resetPassword(email) })
//        }
//    }
//    // Sends a password reset email to the user
//    private fun resetPassword(email: String) {
//        // Validate the email format
//        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        auth.sendPasswordResetEmail(email)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
//}
//
//@Composable
//fun ForgotPasswordScreen(onResetPassword: (String) -> Unit) {
//    var email by remember { mutableStateOf("") }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Card(
//            modifier = Modifier
//                .fillMaxWidth(0.9f)
//                .padding(16.dp),
//            shape = RoundedCornerShape(16.dp),
//            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(24.dp)
//                    .fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = "Forgot Password",
//                    style = TextStyle(
//                        fontSize = 24.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF1565C0)
//                    ),
//                    textAlign = TextAlign.Center
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(
//                    text = "Enter your registered email to reset your password.",
//                    style = TextStyle(fontSize = 16.sp, color = Color.Gray),
//                    textAlign = TextAlign.Center
//                )
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                TextField(
//                    value = email,
//                    onValueChange = { email = it },
//                    label = { Text("Email Address") },
//                    modifier = Modifier.fillMaxWidth(),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//                    shape = RoundedCornerShape(12.dp)
//                )
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                Button(
//                    onClick = { onResetPassword(email) },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(50.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0))
//                ) {
//                    Text(
//                        text = "Reset Password",
//                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
//                        color = Color.White
//                    )
//                }
//            }
//        }
//    }
//}