package uk.ac.tees.mad.bookly.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import uk.ac.tees.mad.bookly.R // Make sure you have a placeholder drawable
import uk.ac.tees.mad.bookly.ui.theme.BooklyTheme

val accentColor = Color(0xFFD97D5D)
val dangerColor = Color(0xFFE57373)
val lightGrayColor = Color(0xFFF5F5F5)

@Composable
fun ProfileRoot(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileScreen(
        state = state,
        onAction = {
            if (it == ProfileAction.OnLogoutClicked) {
                onLogout()
            } else {
                viewModel.onAction(it)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = lightGrayColor
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(lightGrayColor)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            UserInfoSection(
                name = state.userName,
                email = state.userEmail,
                imageUrl = state.profileImageUrl,
                onEditClick = { onAction(ProfileAction.OnEditProfileClicked) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            ReadingStatsSection(
                booksSaved = state.booksSaved,
                booksCompleted = state.booksCompleted,
                totalHours = state.totalReadingTimeHours
            )
            Spacer(modifier = Modifier.height(24.dp))
            DailyRemindersSection(
                remindersEnabled = state.areRemindersEnabled,
                reminderTime = state.reminderTime.toString(),
                onToggle = { onAction(ProfileAction.OnToggleReminders(it)) },
                onTimeClick = { /* TODO: Show time picker */ }
            )
            Spacer(modifier = Modifier.height(24.dp))
            AccountManagementSection(
                onLogoutClick = { onAction(ProfileAction.OnLogoutClicked) },
                onDeleteAccountClick = { onAction(ProfileAction.OnDeleteAccountClicked) }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Composable
private fun Section(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        content = content
    )
}

@Composable
fun UserInfoSection(name: String, email: String, imageUrl: String, onEditClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = imageUrl,
            fallback = painterResource(id = R.drawable.book_placeholder),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = name, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(text = email, color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onEditClick,
            modifier = Modifier.padding(horizontal = 32.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = accentColor),
            border = BorderStroke(1.dp, accentColor)
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Icon", Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Edit Profile")
        }
    }
}

@Composable
fun ReadingStatsSection(booksSaved: Int, booksCompleted: Int, totalHours: Int) {
    Section {
        Text(
            text = "Your Reading Stats",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatBox(modifier = Modifier.weight(1f), count = booksSaved, label = "Books Saved")
            StatBox(modifier = Modifier.weight(1f), count = booksCompleted, label = "Books Completed")
        }
        Spacer(modifier = Modifier.height(16.dp))
        StatBox(modifier = Modifier.fillMaxWidth(), count = totalHours, label = "Total Reading Time", unit = "hours")
    }
}

@Composable
fun StatBox(modifier: Modifier = Modifier, count: Int, label: String, unit: String? = null) {
    Box(
        modifier = modifier.border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                Text(
                    text = count.toString(),
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
                if (unit != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = unit,
                        color = accentColor,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.Bottom)
                    )
                }
            }
            Text(text = label, color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun DailyRemindersSection(
    remindersEnabled: Boolean,
    reminderTime: String,
    onToggle: (Boolean) -> Unit,
    onTimeClick: () -> Unit
) {
    Section {
        Text(
            text = "Daily Reading Reminders",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Enable Reminders")
            Switch(
                checked = remindersEnabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = accentColor,
                    checkedTrackColor = accentColor.copy(alpha = 0.5f),
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = onTimeClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = remindersEnabled,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, if (remindersEnabled) accentColor else Color.Gray)
        ) {
            Text(reminderTime, fontSize = 18.sp, color = if(remindersEnabled) accentColor else Color.Gray)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Get notified daily to keep up with your reading goals.",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun AccountManagementSection(onLogoutClick: () -> Unit, onDeleteAccountClick: () -> Unit) {
    Section {
        Text(
            text = "Account Management",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onLogoutClick)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                Spacer(modifier = Modifier.width(16.dp))
                Text("Logout")
            }
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
        }
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onDeleteAccountClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = dangerColor)
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Icon", Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Delete Account")
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
private fun Preview() {
    BooklyTheme {
        ProfileScreen(
            state = ProfileState(
                userName = "Aria Sharma",
                userEmail = "aria.sharma@bookly.com",
                booksSaved = 124,
                booksCompleted = 87,
                totalReadingTimeHours = 340
            ),
            onAction = {}
        )
    }
}
