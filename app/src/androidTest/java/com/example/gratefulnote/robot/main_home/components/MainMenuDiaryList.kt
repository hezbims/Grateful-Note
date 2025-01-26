package com.example.gratefulnote.robot.main_home.components

import android.view.View
import androidx.compose.ui.test.hasAnyDescendant
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.gratefulnote.R
import com.example.gratefulnote.robot._common.action.pagingScrollUntilPosition
import com.example.gratefulnote.robot._common.assertion.itemMatchAtIndex
import com.example.gratefulnote.robot._common.interactor.base.EspressoInteractor
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

class MainMenuDiaryList(matcher: Matcher<View>) : EspressoInteractor(matcher) {
    fun getDiaryCardWithTitle(title: String) : DiaryCard {
        return DiaryCard(allOf(
            ViewMatchers.withId(R.id.diary_card),
            ViewMatchers.hasDescendant(withText(title)))
        )
    }
//    fun scrollToItemWithTitle(){
//        onView(matcher).perform(scrollTo<DiaryPreviewViewHolder>(
//            withText("what-1")
//        ))
//    }

    fun scrollToIndex(itemIndex : Int) {
        onView(viewMatcher)
            .perform(pagingScrollUntilPosition(itemIndex))
    }

    fun assertTitleAtIndex(title: String, index: Int){
        onView(viewMatcher).check(itemMatchAtIndex(
            allOf(withId(R.id.title), withText(title)),
            index
        ))
    }
}