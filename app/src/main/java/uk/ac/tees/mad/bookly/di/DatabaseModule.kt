package uk.ac.tees.mad.bookly.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.bookly.data.local.BookDao
import uk.ac.tees.mad.bookly.data.local.BooklyDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideBooklyDatabase(@ApplicationContext context: Context): BooklyDatabase {
        return Room.databaseBuilder(
            context,
            BooklyDatabase::class.java,
            "bookly.db"
        )
        .fallbackToDestructiveMigration() // This will prevent the crash
        .build()
    }

    @Provides
    @Singleton
    fun provideBookDao(database: BooklyDatabase): BookDao = database.bookDao()
}
