package uk.ac.tees.mad.bookly.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import uk.ac.tees.mad.bookly.R
import uk.ac.tees.mad.bookly.domain.Book
import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.ui.theme.BooklyTheme

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = hiltViewModel(),
    onBookClick: (String) -> Unit,
    onNotificationsClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onAction = {
            if (it is HomeAction.OnBookClicked) {
                onBookClick(it.bookId)
            } else if (it is HomeAction.OnNotificationsClicked) {
                onNotificationsClick()
            } else {
                viewModel.onAction(it)
            }
        }
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { onAction(HomeAction.OnSearchQueryChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search by title, author, or ISBN") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = { Icon(painterResource(id = R.drawable.ic_filter), contentDescription = "Filter") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onAction(HomeAction.OnSearch)
                keyboardController?.hide()
            }),
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display the info message banner independently if it exists
        state.infoMessage?.let {
            InfoBanner(text = it)
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            // Show an error banner if there's an error
            InfoBanner(text = state.error.toUserFriendlyString(), isError = true)
        } else if (state.books.isNotEmpty()) {
            // Show the list of books if it's not empty
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(state.books, key = { it.id }) { book ->
                    BookItem(book = book, onClick = { onAction(HomeAction.OnBookClicked(book.id)) })
                }
            }
        } else if (state.recentSearches.isNotEmpty()) {
            // Show recent searches only if there are no books to show
            RecentSearches(searches = state.recentSearches, onAction = onAction)
        } else {
            // Optional: A placeholder for when there is nothing to show at all
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Search for books to get started.")
            }
        }
    }
}

@Composable
fun RecentSearches(searches: List<String>, onAction: (HomeAction) -> Unit) {
    Column {
        Text("Recent Searches", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(searches) { query ->
                Text(
                    text = query,
                    modifier = Modifier.clickable { onAction(HomeAction.OnSearchQueryChanged(query)) }
                )
            }
        }
    }
}

@Composable
fun InfoBanner(text: String, isError: Boolean = false) {
    val backgroundColor = if (isError) MaterialTheme.colorScheme.errorContainer else Color(0xFFE3F2FD)
    val contentColor = if (isError) MaterialTheme.colorScheme.onErrorContainer else Color(0xFF0D47A1)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Info, contentDescription = "Info", tint = contentColor)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = contentColor)
    }
}

@Composable
fun BookItem(book: Book, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .height(IntrinsicSize.Max) // Use Max to allow the row to grow to the tallest child
        ) {
            AsyncImage(
                model = book.thumbnail?.replace("http://", "https://"),
                contentDescription = book.title,
                placeholder = painterResource(id = R.drawable.book_placeholder),
                error = painterResource(id = R.drawable.book_placeholder),
                contentScale = ContentScale.Crop, // Use Crop to fill the bounds
                modifier = Modifier
                    .width(90.dp) // A slightly wider image
                    .fillMaxHeight() // Fill the height of the row
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(book.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(book.authors.joinToString(", "), style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(book.description ?: "", maxLines = 3, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

fun DataError.Remote.toUserFriendlyString(): String {
    return when (this) {
        DataError.Remote.NO_INTERNET -> "No internet connection. Please check your connection and try again."
        DataError.Remote.SERVER_ERROR -> "A server error occurred. Please try again later."
        DataError.Remote.REQUEST_TIMEOUT -> "The request timed out. Please try again."
        else -> "An unknown error occurred."
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    BooklyTheme {
        val books = listOf(
            Book(
                id = "1",
                title = "The Silent Patient",
                authors = listOf("Alex Michaelides"),
                description = "A shocking psychological thriller...",
                thumbnail = null,
                smallThumbnail = null,
                publishDate = "2019",
                rating = 4.1
            ),
            Book(
                id = "2",
                title = "Where the Crawdads Sing",
                authors = listOf("Delia Owens"),
                description = "A coming-of-age story...",
                thumbnail = null,
                smallThumbnail = null,
                publishDate = "2018",
                rating = 4.8
            )
        )
        HomeScreen(
            state = HomeState(books = books, infoMessage = "Results from your last search are displayed."),
            onAction = {}
        )
    }
}
