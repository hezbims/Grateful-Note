package com.example.gratefulnote.test_scenario.filter_test.test_data

data class FilterInstruction(
    val year: DropdownItemIndex? = null,

    val month: DropdownItemIndex? = null,

    /**
     * memutuskan apakah harus mentoogle switch dari only favorite
     */
    val toogleSwitchOnlyFavorite: Boolean = false,

    /**
     * memutuskan apakah harus mentoogle switch dari sorted latest
     */
    val toogleSwitchSortedLatest: Boolean = false,

    val diaryType : DropdownItemIndex? = null,

    val expectedTitleAtIndex : Map<Int, String>
){
    companion object {
        fun testData() : Iterable<Any>{
            return listOf(
                FilterInstruction(
                    expectedTitleAtIndex = mapOf(
                        0 to "Belajar Banyak Hal Baru",
                        1 to "Makan Siang Gratis",
                        2 to "Bermain Game",
                        3 to "Senang Bermain Bola",
                        4 to "Selamat Sampai Tujuan",
                        5 to "Masa Depan",
                    )
                ),
                FilterInstruction(
                    year = DropdownItemIndex.Y2021,
                    toogleSwitchSortedLatest = true,
                    expectedTitleAtIndex = mapOf(
                        0 to "Bermain Game",
                        1 to "Makan Siang Gratis",
                        2 to "Belajar Banyak Hal Baru",
                    )
                ),
                FilterInstruction(
                    month = DropdownItemIndex.NOV,
                    toogleSwitchOnlyFavorite = true,
                    expectedTitleAtIndex = mapOf(
                        0 to "Selamat Sampai Tujuan",
                    )
                ),
            )
        }
    }
}