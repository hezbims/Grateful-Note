package com.example.gratefulnote.test_scenario.filter_test.test_data

import com.example.gratefulnote.database.GratefulNoteDatabase
import com.example.gratefulnote.database.PositiveEmotion
import java.time.Month
import java.util.Calendar


/**
 * Menyediakan 6 buah diary item unique
 *
 * | No | type       | Title                     | Description                                           | Tanggal     | isFavorite |
 * | :- | :----------|---------------------------| :----------------------                               | --------    | ---------- |
 * |  1 |`Joy`       | `Senang Bermain Bola`     | `Seneng aja cuy`                                      | 22/11/2020  | `false`    |
 * |  2 |`Gratitude` | `Selamat Sampai Tujuan`   | `Alhamdulillah selamat tadi pagi`                     | 20/11/2020  | `true`     |
 * |  3 |`Hope`      | `Masa Depan`              | `Masa depanku cerah`                                  | 20/09/2020  | `true`     |
 * |  4 |`Gratitude` | `Makan Siang Gratis`      | `Alhamdulillah perut kenyang dapat makan gratis tadi` | 12/08/2021  | `false`    |
 * |  5 |`Joy`       | `Bermain Game`            | `Seru banget main call of duty`                       | 13/07/2021  | `false`    |
 * |  6 |`Interest`  | `Belajar Banyak Hal Baru` | `Aku tertarik banyak belajar hal baru`                | 14/09/2021  | `true`     |
 */
suspend fun GratefulNoteDatabase.prepareWithFilterTestData(){
    val dao = this.positiveEmotionDao
    val listDate = listOf(
        Calendar.getInstance().apply { set(Calendar.YEAR, 2020); set(Calendar.MONTH, Month.NOVEMBER.value); set(Calendar.DAY_OF_MONTH, 22) },
        Calendar.getInstance().apply { set(Calendar.YEAR, 2020); set(Calendar.MONTH, Month.NOVEMBER.value); set(Calendar.DAY_OF_MONTH, 20) },
        Calendar.getInstance().apply { set(Calendar.YEAR, 2020); set(Calendar.MONTH, Month.SEPTEMBER.value); set(Calendar.DAY_OF_MONTH, 20) },
        Calendar.getInstance().apply { set(Calendar.YEAR, 2021); set(Calendar.MONTH, Month.AUGUST.value); set(Calendar.DAY_OF_MONTH, 12) },
        Calendar.getInstance().apply { set(Calendar.YEAR, 2021); set(Calendar.MONTH, Month.JULY.value); set(Calendar.DAY_OF_MONTH, 13) },
        Calendar.getInstance().apply { set(Calendar.YEAR, 2021); set(Calendar.MONTH, Month.SEPTEMBER.value); set(Calendar.DAY_OF_MONTH, 14) },
    )

    dao.insertAll(listOf(
        PositiveEmotion(
            type = "Joy",
            what = "Senang Bermain Bola",
            why = "Seneng aja cuy",
            month = listDate[0].get(Calendar.MONTH),
            year = listDate[0].get(Calendar.YEAR),
            day = listDate[0].get(Calendar.DAY_OF_MONTH),
            isFavorite = false,
            createdAt = listDate[0].timeInMillis,
            updatedAt = listDate[0].timeInMillis,
        ),
        PositiveEmotion(
            type = "Gratitude",
            what = "Selamat Sampai Tujuan",
            why = "Alhamdulillah selamat tadi pagi",
            month = listDate[1].get(Calendar.MONTH),
            year = listDate[1].get(Calendar.YEAR),
            day = listDate[1].get(Calendar.DAY_OF_MONTH),
            isFavorite = true,
            createdAt = listDate[1].timeInMillis,
            updatedAt = listDate[1].timeInMillis,
        ),
        PositiveEmotion(
            type = "Hope",
            what = "Masa Depan",
            why = "Masa depanku cerah",
            month = listDate[2].get(Calendar.MONTH),
            year = listDate[2].get(Calendar.YEAR),
            day = listDate[2].get(Calendar.DAY_OF_MONTH),
            isFavorite = true,
            createdAt = listDate[2].timeInMillis,
            updatedAt = listDate[2].timeInMillis,
        ),
        PositiveEmotion(
            type = "Gratitude",
            what = "Makan Siang Gratis",
            why = "Alhamdulillah perut kenyang dapat makan gratis tadi",
            month = listDate[3].get(Calendar.MONTH),
            year = listDate[3].get(Calendar.YEAR),
            day = listDate[3].get(Calendar.DAY_OF_MONTH),
            isFavorite = false,
            createdAt = listDate[3].timeInMillis,
            updatedAt = listDate[3].timeInMillis,
        ),
        PositiveEmotion(
            type = "Joy",
            what = "Bermain Game",
            why = "Seru banget main call of duty",
            month = listDate[4].get(Calendar.MONTH),
            year = listDate[4].get(Calendar.YEAR),
            day = listDate[4].get(Calendar.DAY_OF_MONTH),
            isFavorite = false,
            createdAt = listDate[4].timeInMillis,
            updatedAt = listDate[4].timeInMillis,
        ),
        PositiveEmotion(
            type = "Interest",
            what = "Belajar Banyak Hal Baru",
            why = "Aku tertarik banyak belajar hal baru",
            month = listDate[5].get(Calendar.MONTH),
            year = listDate[5].get(Calendar.YEAR),
            day = listDate[5].get(Calendar.DAY_OF_MONTH),
            isFavorite = true,
            createdAt = listDate[5].timeInMillis,
            updatedAt = listDate[5].timeInMillis,
        ),

    ))
}