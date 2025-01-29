package com.example.gratefulnote.steps.common

import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.gratefulnote.common.domain.ITimeProvider
import com.example.gratefulnote.database.DiaryDao
import dagger.hilt.android.testing.HiltAndroidTest
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.And
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.`is`
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltAndroidTest
class DatabaseAssertionSteps {
    @Inject
    lateinit var diaryDao: DiaryDao
    @Inject
    lateinit var timeProvider: ITimeProvider

    @And("diary titled {string} will not be exist in the database")
    fun diaryWillNotExistInTheDatabase(diaryTitle: String) = runTest {
        assertThat(diaryDao.getDiaries(SimpleSQLiteQuery(
            "SELECT * FROM positive_emotion_table " +
                "WHERE what = '$diaryTitle'"
        )), `is`(empty()))
    }

    @And("there is {string} diaries in database")
    fun assertTotalDiaries(total: String) = runBlocking {
        val totalInt = total.toInt()
        assertThat(diaryDao.getDiaries(SimpleSQLiteQuery(
            "SELECT * FROM positive_emotion_table "
        )), hasSize(totalInt))
    }

    @And("there is exactly one diary with:")
    fun assertDiaries(dataTable: DataTable) = runBlocking {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").apply {
            timeZone = timeProvider.getCurrentTimezone()
        }
        dataTable.asMaps().forEach { rawDiary ->
            val expectedTitle = rawDiary["title"]
            val expectedDescription = rawDiary["desc"]
            val expectedTag = rawDiary["tag"]
            val expectedIsFavorite = rawDiary["isFavorite"].toBoolean()
            val expectedCreatedAt = rawDiary["createdAt"]
            val expectedUpdatedAt = rawDiary["updatedAt"]

            assertThat(diaryDao.getDiaries(SimpleSQLiteQuery(
                "SELECT * FROM positive_emotion_table "
            )).filter { diary ->
                val createdAtStr = diary.createdAt.toTestDateString(formatter)
                val updatedAtStr = diary.updatedAt.toTestDateString(formatter)

                diary.what == expectedTitle &&
                diary.why == expectedDescription &&
                diary.type == expectedTag &&
                diary.isFavorite == expectedIsFavorite &&
                createdAtStr == expectedCreatedAt &&
                updatedAtStr == expectedUpdatedAt
            }, hasSize(1))
        }
    }

    private fun Long.toTestDateString(format: DateFormat) : String {
        return with(Calendar.getInstance()){
            timeZone = timeProvider.getCurrentTimezone()
            timeInMillis = this@toTestDateString
            format.format(this.time)
        }
    }
}