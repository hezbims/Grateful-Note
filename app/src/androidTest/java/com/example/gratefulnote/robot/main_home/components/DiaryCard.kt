package com.example.gratefulnote.robot.main_home.components

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers
import com.example.gratefulnote.R
import com.example.gratefulnote.robot._common.interactor.base.EspressoInteractor
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

class DiaryCard(matcher: Matcher<View>) : EspressoInteractor(matcher) {
    val editIcon = EspressoInteractor(allOf(
        ViewMatchers.isDescendantOfA(this.viewMatcher),
        ViewMatchers.withId(R.id.edit_diary_icon))
    )

    val deleteIcon = EspressoInteractor(allOf(
        ViewMatchers.isDescendantOfA(this.viewMatcher),
        ViewMatchers.withId(R.id.delete_diary_icon)
    ))

    val favoriteIcon = EspressoInteractor(allOf(
        ViewMatchers.isDescendantOfA(this.viewMatcher),
        ViewMatchers.withId(R.id.favorite_symbol)
    ))
}