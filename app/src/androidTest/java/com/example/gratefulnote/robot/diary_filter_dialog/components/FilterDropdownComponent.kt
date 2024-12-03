package com.example.gratefulnote.robot.diary_filter_dialog.components

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.RootMatchers
import com.example.gratefulnote.robot._common.IEspressoComponent
import com.example.gratefulnote.test_scenario.filter_test.test_data.DropdownItemIndex
import org.hamcrest.Matchers.anything

interface IFilterDropdownComponent {
    fun chooseItem(itemIndex: Int)
    fun chooseItem(item : DropdownItemIndex)
}

abstract class EspressoFilterDropdownComponent : IEspressoComponent, IFilterDropdownComponent {
    override fun chooseItem(itemIndex: Int) {
        viewInteractor.perform(click())
        onData(anything())
            .inRoot(RootMatchers.isPlatformPopup())
            .atPosition(itemIndex)
            .perform(click())
    }

    override fun chooseItem(item: DropdownItemIndex) {
        chooseItem(item.index)
    }
}
