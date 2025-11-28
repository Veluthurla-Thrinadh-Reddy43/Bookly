package uk.ac.tees.mad.bookly.presentation.book_detail


import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import uk.ac.tees.mad.bookly.R // Add placeholder drawables
import uk.ac.tees.mad.bookly.domain.Book
import uk.ac.tees.mad.bookly.domain.util.DataError
import uk.ac.tees.mad.bookly.ui.theme.BooklyTheme

@Composable
fun BookDetailRoot(
    viewModel: BookDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookDetailScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    state: BookDetailState,
    onAction: (BookDetailAction) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.book?.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(BookDetailAction.OnAddToListClicked) },
                shape = CircleShape,
                containerColor = Color(0xFFD97D5D) // Always custom orange color
            ) {
                Crossfade(targetState = state.isInReadingList, label = "Add-Check-Icon") { isInList ->
                    if (isInList) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "In Reading List",
                            tint = Color.White
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add to List",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.book != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                BookHeader(book = state.book, isOffline = state.isOffline)

                Column(Modifier.padding(16.dp)) {
                    BookInfoSection(book = state.book)
                    Spacer(modifier = Modifier.height(24.dp))
                    state.book.description?.let {
                        SummarySection(summary = it)
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    SimilarBooksSection(
                        books = state.similarBooks,
                        onBookClick = { onAction(BookDetailAction.OnSimilarBookClicked(it)) }
                    )
                }
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.error.toString(), color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun BookHeader(book: Book, isOffline: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp) // Increased height
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        AsyncImage(
            model = book.thumbnail?.replace("http://", "https://"),
            contentDescription = book.title,
            placeholder = painterResource(id = R.drawable.book_placeholder),
            error = painterResource(id = R.drawable.book_placeholder),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        if (isOffline) {
            Chip(
                label = "Offline",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun BookInfoSection(book: Book) {
    Column {
        Text(
            text = book.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(text = book.authors.joinToString(", "), style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = book.publishDate ?: "N/A",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            if (book.rating != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107), // Yellow star color
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = book.rating.toString(),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun SummarySection(summary: String) {
    Column {
        Text(
            text = "Summary",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = summary,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun SimilarBooksSection(books: List<Book>, onBookClick: (String) -> Unit) {
    Column {
        Text(
            text = "Similar Books",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(books, key = { it.id }) { book ->
                SimilarBookItem(book = book, onClick = { onBookClick(book.id) })
            }
        }
    }
}

@Composable
fun SimilarBookItem(book: Book, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = book.thumbnail?.replace("http://", "https://"),
            contentDescription = book.title,
            placeholder = painterResource(id = R.drawable.book_placeholder),
            error = painterResource(id = R.drawable.book_placeholder),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = book.title,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = book.authors.joinToString(", "),
            fontSize = 12.sp,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun Chip(label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    BooklyTheme {
        val book = Book(
            id = "1",
            title = "The Silent Patient",
            authors = listOf("Alex Michaelides"),
            description = "Alicia Berenson’s life is seemingly perfect...",
            thumbnail = "",
            smallThumbnail = null,
            publishDate = "February 5, 2019",
            rating = 4.2
        )
        BookDetailScreen(
            state = BookDetailState(
                book = book,
                similarBooks = listOf(
                    Book("101", "Verity", listOf("Colleen Hoover"), null, null, null, null, null),
                    Book("102", "The Guest List", listOf("Lucy Fokley"), null, null, null, null, null)
                ),
                isOffline = true
            ),
            onAction = {}
        )
    }
}