package com.example.gratefulnote.test_scenario.filter_test.test_data

data class FilterInstruction(
    val year: String?,

    val month: String?,

    /**
     * memutuskan apakah harus mentoogle switch dari only favorite
     */
    val toogleSwitchOnlyFavorite: Boolean,

    /**
     * memutuskan apakah harus mentoogle switch dari sorted latest
     */
    val toogleSwitchSortedLatest: Boolean,

    val diaryType : String?,

    val expectedTitleAtIndex : Map<Int, String>
){
    companion object {
        fun testData() : Iterable<Any>{
            return listOf(
                FilterInstruction(
                    year = null,
                    month = null,
                    toogleSwitchOnlyFavorite = false,
                    toogleSwitchSortedLatest = false,
                    diaryType = null,
                    expectedTitleAtIndex = mapOf(
                        0 to "Belajar Banyak Hal Baru",
                        1 to "Makan Siang Gratis",
                        2 to "Bermain Game",
                        3 to "Senang Bermain Bola",
                        4 to "Selamat Sampai Tujuan",
                        5 to "Masa Depan",
                    )
                )
            )
        }
    }
}
