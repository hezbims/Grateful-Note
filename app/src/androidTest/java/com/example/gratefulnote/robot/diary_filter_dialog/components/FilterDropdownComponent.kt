package com.example.gratefulnote.robot.diary_filter_dialog.components

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.gratefulnote.robot._common.IEspressoComponent

interface IFilterDropdownComponent {
    fun chooseItem(itemName : String)
}

abstract class EspressoFilterDropdownComponent : IEspressoComponent, IFilterDropdownComponent {
    override fun chooseItem(itemName : String){
        viewInteractor.perform(click())
        onView(withText(itemName))
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())
    }
}
