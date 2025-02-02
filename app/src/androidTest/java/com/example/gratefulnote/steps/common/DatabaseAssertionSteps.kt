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
        dataTable.asMaps().forEach { rawDiary ->
            val expectedTitle : String? = rawDiary["title"]
            val expectedDescription : String? = rawDiary["desc"]
            val expectedTag : String? = rawDiary["tag"]
            val expectedIsFavorite : Int? = rawDiary["isFavorite"]?.let {
                when(it){
                    "true" -> 1
                    "false" -> 0
                    else -> throw RuntimeException("isFavorite salah tulis")
                }
            }
            val expectedCreatedAt : String? = rawDiary["createdAt"]
            val expectedUpdatedAt : String? = rawDiary["updatedAt"]

            val query = StringBuilder("SELECT * FROM positive_emotion_table WHERE 1 = 1 ")
            if (expectedTitle != null)
                query.append("AND what = '$expectedTitle' ")
            if (expectedDescription != null)
                query.append("AND why = '$expectedDescription' ")
            if (expectedTag != null)
                query.append("AND type = '$expectedTag' ")
            if (expectedIsFavorite != null)
                query.append("AND is_favorite = $expectedIsFavorite ")
            if (expectedCreatedAt != null)
                query.append("AND strftime('%Y-%m-%dT%H:%M:%S', created_at / 1000, 'unixepoch', '+0 hours') = '$expectedCreatedAt' ")
            if (expectedUpdatedAt != null)
                query.append("AND strftime('%Y-%m-%dT%H:%M:%S', updated_at / 1000, 'unixepoch', '+0 hours') = '$expectedUpdatedAt' ")

            assertThat(diaryDao.getDiaries(SimpleSQLiteQuery(
                query.toString()
            )), hasSize(1))
        }
    }
}