package uk.ac.tees.mad.bookly.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.bookly.data.AuthRepositoryImpl
import uk.ac.tees.mad.bookly.data.BookRemoteDataSourceImpl
import uk.ac.tees.mad.bookly.data.BookRepositoryImpl
import uk.ac.tees.mad.bookly.data.BookSearchRepositoryImpl
import uk.ac.tees.mad.bookly.data.SearchHistoryRepositoryImpl
import uk.ac.tees.mad.bookly.domain.AuthRepository
import uk.ac.tees.mad.bookly.domain.BookRemoteDataSource
import uk.ac.tees.mad.bookly.domain.BookRepository
import uk.ac.tees.mad.bookly.domain.BookSearchRepository
import uk.ac.tees.mad.bookly.domain.SearchHistoryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindBookRemoteDataSource(impl: BookRemoteDataSourceImpl): BookRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindSearchHistoryRepository(impl: SearchHistoryRepositoryImpl): SearchHistoryRepository

    @Binds
    @Singleton
    abstract fun bindBookRepository(impl: BookRepositoryImpl): BookRepository

    @Binds
    @Singleton
    abstract fun bindBookSearchRepository(impl: BookSearchRepositoryImpl): BookSearchRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @Provides
        @Singleton
        fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()
    }
}