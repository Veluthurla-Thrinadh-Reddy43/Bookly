package uk.ac.tees.mad.bookly.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.ui.theme.BooklyTheme

@Composable
fun LoginRoot(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onForgotPasswordClicked: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isLoginSuccess) {
        if (state.isLoginSuccess) {
            onLoginSuccess()
        }
    }

    LoginScreen(
        state = state,
        onAction = viewModel::onAction,
        onForgotPasswordClicked = onForgotPasswordClicked
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    onForgotPasswordClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    TabButton(
                        text = "Login",
                        isSelected = state.selectedTab == 0,
                        onClick = { onAction(LoginAction.OnTabSelected(0)) }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    TabButton(
                        text = "Sign Up",
                        isSelected = state.selectedTab == 1,
                        onClick = { onAction(LoginAction.OnTabSelected(1)) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { onAction(LoginAction.OnEmailChanged(it)) },
                    label = { Text("Email") },
                    placeholder = { Text("your.email@example.com") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    isError = state.error != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color(0xFFD97D5D),
                        unfocusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
                        focusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.password,
                    onValueChange = { onAction(LoginAction.OnPasswordChanged(it)) },
                    label = { Text("Password") },
                    placeholder = { Text("Enter your password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    isError = state.error != null,
                    trailingIcon = {
                        IconButton(onClick = { onAction(LoginAction.TogglePasswordVisibility) }) {
                            Icon(
                                imageVector = if (state.isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (state.isPasswordVisible) "Hide password" else "Show password",
                                tint = Color.Gray
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color(0xFFD97D5D),
                        unfocusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
                        focusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                if (state.selectedTab == 1) {
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.confirmPassword,
                        onValueChange = { onAction(LoginAction.OnConfirmPasswordChanged(it)) },
                        label = { Text("Confirm Password") },
                        placeholder = { Text("Confirm your password") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (state.isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = state.error != null,
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = { onAction(LoginAction.ToggleConfirmPasswordVisibility) }) {
                                Icon(
                                    imageVector = if (state.isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (state.isConfirmPasswordVisible) "Hide password" else "Show password",
                                    tint = Color.Gray
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.LightGray,
                            focusedBorderColor = Color(0xFFD97D5D),
                            unfocusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
                            focusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                if (state.error != null) {
                    Text(
                        text = state.error!!.toUserFriendlyString(),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (state.selectedTab == 0) {
                    TextButton(
                        onClick = onForgotPasswordClicked,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Forgot Password?", color = Color(0xFFD97D5D))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (state.selectedTab == 0) {
                            onAction(LoginAction.Login)
                        } else {
                            onAction(LoginAction.SignUp)
                        }
                    },
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD97D5D),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            color = Color.White
                        )
                    } else {
                        Text(if (state.selectedTab == 0) "Login" else "Sign Up", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun TabButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFFD97D5D) else Color.Transparent
    val contentColor = if (isSelected) Color.White else Color.Black

    Button(
        onClick = onClick,
        modifier = Modifier
            .width(120.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

private fun DataError.Firebase.toUserFriendlyString(): String {
    return when (this) {
        DataError.Firebase.UNAUTHENTICATED -> "Invalid email or password."
        DataError.Firebase.NETWORK -> "No internet connection."
        DataError.Firebase.ALREADY_EXISTS -> "An account with this email already exists."
        DataError.Firebase.INVALID_ARGUMENT -> "Please enter a valid email and password."
        else -> "An unknown error occurred."
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    BooklyTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {},
            onForgotPasswordClicked = {}
        )
    }
}
