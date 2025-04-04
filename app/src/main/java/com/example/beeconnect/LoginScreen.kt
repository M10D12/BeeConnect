package com.example.beeconnect

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun LoginScreen(
    navController: NavController,
    auth: FirebaseAuth? = FirebaseAuth.getInstance(),
    db: FirebaseFirestore? = FirebaseFirestore.getInstance()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8B42B)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(30.dp),
            elevation = 20.dp,
            modifier = Modifier.padding(30.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF8B42B)
                )

                Spacer(modifier = Modifier.height(40.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "User Icon")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(30.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock Icon")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(30.dp),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        if (username.isNotBlank() && password.isNotBlank()) {
                            auth?.signInWithEmailAndPassword(username, password)
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(context, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()

                                        val userId = auth.currentUser?.uid
                                        if (userId != null) {
                                            db?.collection("users")?.document(userId)
                                                ?.get()
                                                ?.addOnSuccessListener { document ->
                                                    if (document != null && document.exists()) {
                                                        val name = document.getString("name")
                                                        val email = document.getString("email")
                                                        Toast.makeText(context, "Nome: $name, Email: $email", Toast.LENGTH_LONG).show()
                                                        navController.navigate("home")
                                                    }
                                                }
                                                ?.addOnFailureListener {
                                                    Toast.makeText(context, "Erro ao acessar dados do usuário", Toast.LENGTH_LONG).show()
                                                }
                                        }
                                    } else {
                                        val error = task.exception?.message ?: "Erro desconhecido"
                                        Toast.makeText(context, "Erro: $error", Toast.LENGTH_LONG).show()
                                    }
                                }
                        } else {
                            Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFf8b42b))
                ) {
                    Text("Login", color = Color.White, fontSize = 16.sp)
                }

                TextButton(onClick = {
                    navController.navigate("register")
                }) {
                    Text("Não tem conta? Registar", color = Color(0xFFf8b42b))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    val navController = rememberNavController()
    LoginScreen(navController = navController, auth = null, db = null)
}
