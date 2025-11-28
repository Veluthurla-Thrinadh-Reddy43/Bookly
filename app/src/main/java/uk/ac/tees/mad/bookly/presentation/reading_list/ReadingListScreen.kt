package uk.ac.tees.mad.bookly.presentation.reading_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import uk.ac.tees.mad.bookly.R
import uk.ac.tees.mad.bookly.ui.theme.BooklyTheme

@Composable
fun ReadingListRoot(
    viewModel: ReadingListViewModel = hiltViewModel(),
    onBookClick: (String) -> Unit,
    onNotificationsClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ReadingListScreen(
        state = state,
        onAction = {
            when (it) {
                is ReadingListAction.OnBookClicked -> onBookClick(it.bookId)
                is ReadingListAction.OnNotificationsClicked -> onNotificationsClick()
                else -> viewModel.onAction(it)
            }
        }
    )
}

@Composable
fun ReadingListScreen(
    state: ReadingListState,
    onAction: (ReadingListAction) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ReadingJourneySection(
                total = state.totalBooks,
                finished = state.finishedBooks,
                inProgress = state.inProgressBooks
            )
        }

        item {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
        }

        if (state.isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else {
            items(state.books, key = { it.id }) { book ->
                ReadingListItem(book = book, onAction = onAction)
            }
        }
    }
}


@Composable
fun ReadingJourneySection(total: Int, finished: Int, inProgress: Int) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Your Reading Journey",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatsItem(count = total, label = "Total Books")
                StatsItem(count = finished, label = "Finished")
                StatsItem(count = inProgress, label = "In Progress")
            }
        }
    }
}

@Composable
fun StatsItem(count: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count.toString(),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ReadingListItem(book: ReadingListBook, onAction: (ReadingListAction) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .clickable { onAction(ReadingListAction.OnBookClicked(book.id)) }
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = book.imageUrl,
                contentDescription = book.title,
                placeholder = painterResource(id = R.drawable.book_placeholder),
                error = painterResource(id = R.drawable.book_placeholder),
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )
                Text(text = book.author, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                StatusChip(status = book.status)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { onAction(ReadingListAction.OnEditClicked(book.id)) }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray)
                }
                IconButton(onClick = { onAction(ReadingListAction.OnDeleteClicked(book.id)) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: ReadingStatus) {
    val backgroundColor = when (status) {
        ReadingStatus.InProgress -> MaterialTheme.colorScheme.secondary
        ReadingStatus.Finished -> Color(0xFF4CAF50) // A pleasant green color
    }
    val icon = when (status) {
        ReadingStatus.InProgress -> Icons.Default.MenuBook
        ReadingStatus.Finished -> Icons.Default.CheckCircle
    }

    Row(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = status.name,
            tint = Color.White,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = status.name,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    BooklyTheme {
        val sampleBooks = listOf(
            ReadingListBook("1", "The Alchemist", "Paulo Coelho", "", ReadingStatus.InProgress),
            ReadingListBook("2", "Pride and Prejudice", "Jane Austen", "", ReadingStatus.Finished)
        )
        ReadingListScreen(
            state = ReadingListState(
                totalBooks = 6,
                finishedBooks = 3,
                inProgressBooks = 3,
                books = sampleBooks
            ),
            onAction = {}
        )
    }
}