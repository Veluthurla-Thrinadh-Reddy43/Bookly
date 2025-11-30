package uk.ac.tees.mad.bookly.presentation.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    var book1Visible by remember { mutableStateOf(false) }
    var book2Visible by remember { mutableStateOf(false) }
    var book3Visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        book1Visible = true
        delay(300)
        book2Visible = true
        delay(300)
        book3Visible = true
        delay(800)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Bookly",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(32.dp))
            Box(contentAlignment = Alignment.BottomCenter) {
                Book(visible = book1Visible, color = Color(0xFFD97D5D), yOffset = 0.dp)
                Book(visible = book2Visible, color = Color(0xFF2E7D32), yOffset = (-20).dp)
                Book(visible = book3Visible, color = Color(0xFF512DA8), yOffset = (-40).dp)
            }
        }
    }
}

@Composable
private fun Book(visible: Boolean, color: Color, yOffset: androidx.compose.ui.unit.Dp) {
    AnimatedVisibility(
        visible = visible,
        modifier = Modifier.offset(y = yOffset),
        enter = slideInVertically(initialOffsetY = { -200 }, animationSpec = tween(durationMillis = 500))
    ) {
        Box(
            modifier = Modifier
                .size(width = 120.dp, height = 20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
        )
    }
}