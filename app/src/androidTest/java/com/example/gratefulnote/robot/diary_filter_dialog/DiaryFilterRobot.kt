package com.example.gratefulnote.robot.diary_filter_dialog

import com.example.gratefulnote.robot.diary_filter_dialog.components.ApplyFilterButton
import com.example.gratefulnote.robot.diary_filter_dialog.components.IApplyFilterButton
import com.example.gratefulnote.robot.diary_filter_dialog.components.IMonthDropdown
import com.example.gratefulnote.robot.diary_filter_dialog.components.IOnlyFavoriteFilterSwitch
import com.example.gratefulnote.robot.diary_filter_dialog.components.ISortByLatestSwitch
import com.example.gratefulnote.robot.diary_filter_dialog.components.ITypeOfEmotionDropdown
import com.example.gratefulnote.robot.diary_filter_dialog.components.IYearDropdown
import com.example.gratefulnote.robot.diary_filter_dialog.components.MonthDropdown
import com.example.gratefulnote.robot.diary_filter_dialog.components.OnlyFavoriteFilterSwitch
import com.example.gratefulnote.robot.diary_filter_dialog.components.SortByLatestSwitch
import com.example.gratefulnote.robot.diary_filter_dialog.components.TypeOfEmotionDropdown
import com.example.gratefulnote.robot.diary_filter_dialog.components.YearDropdown
import com.example.gratefulnote.test_scenario.filter_test.test_data.FilterInstruction

class DiaryFilterRobot {
    // region Switch Components
    private val onlyFavoriteFilterSwitch : IOnlyFavoriteFilterSwitch = OnlyFavoriteFilterSwitch()
    private val sortByLatestSwitch : ISortByLatestSwitch = SortByLatestSwitch()
    // endregion

    // region Dropdowns
    private val yearDropdown : IYearDropdown = YearDropdown()
    private val monthDropdown : IMonthDropdown = MonthDropdown()
    private val typeOfEmotionDropdown : ITypeOfEmotionDropdown = TypeOfEmotionDropdown()
    // endregion

    private val applyFilterButton : IApplyFilterButton = ApplyFilterButton()

    fun applyFilterInstruction(filterInstruction : FilterInstruction){
        filterInstruction.apply {
            if (toogleSwitchOnlyFavorite)
                onlyFavoriteFilterSwitch.toogle()
            if (toogleSwitchSortedLatest)
                sortByLatestSwitch.toogle()

            if (diaryType != null)
                typeOfEmotionDropdown.chooseTypeOfEmotion(diaryType)
            if (month != null)
                monthDropdown.chooseMonth(month)
            if (year != null)
                yearDropdown.chooseYear(year)

            applyFilterButton.tap()
        }
    }
}