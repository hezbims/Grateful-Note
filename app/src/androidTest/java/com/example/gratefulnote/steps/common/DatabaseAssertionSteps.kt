package com.example.gratefulnote.steps.common

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.gratefulnote.database.DiaryDao
import dagger.hilt.android.testing.HiltAndroidTest
import io.cucumber.java.en.And
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.`is`
import javax.inject.Inject

@HiltAndroidTest
class DatabaseAssertionSteps {
    @Inject
    lateinit var diaryDao: DiaryDao

    @And("^diary titled '(.*)' will not be exist in the database$")
    fun diaryWillNotExistInTheDatabase(diaryTitle: String) = runTest {
        assertThat(diaryDao.getDiaries(SimpleSQLiteQuery(
            "SELECT * FROM positive_emotion_table " +
                "WHERE what = '$diaryTitle'"
        )), `is`(empty()))
    }
}