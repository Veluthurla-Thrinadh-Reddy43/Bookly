package uk.ac.tees.mad.bookly.presentation.forgot_password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.ui.theme.BooklyTheme

@Composable
fun ForgotPasswordRoot(
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    ForgotPasswordScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ForgotPasswordScreen(
    state: ForgotPasswordState,
    onAction: (ForgotPasswordAction) -> Unit,
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
                Text(
                    text = "Forgot Password",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { onAction(ForgotPasswordAction.OnEmailChanged(it)) },
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

                if (state.error != null) {
                    Text(
                        text = state.error!!.toUserFriendlyString(),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                if (state.successMessage != null) {
                    Text(
                        text = state.successMessage,
                        color = Color(0xFF00C853), // A green success color
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onAction(ForgotPasswordAction.SendPasswordResetEmail) },
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
                        Text("Send Password Reset Email", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

private fun DataError.Firebase.toUserFriendlyString(): String {
    return when (this) {
        DataError.Firebase.NETWORK -> "No internet connection."
        DataError.Firebase.INVALID_ARGUMENT -> "Please enter a valid email."
        else -> "An unknown error occurred."
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    BooklyTheme {
        ForgotPasswordScreen(
            state = ForgotPasswordState(),
            onAction = {}
        )
    }
}
