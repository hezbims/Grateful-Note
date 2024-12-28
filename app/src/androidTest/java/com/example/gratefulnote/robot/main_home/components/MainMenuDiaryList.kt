package com.example.gratefulnote.robot.main_home.components

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.gratefulnote.R
import com.example.gratefulnote.mainMenu.presentation.component.DiaryPreviewViewHolder
import com.example.gratefulnote.robot._common.EspressoInteractor
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

class MainMenuDiaryList(matcher: Matcher<View>) : EspressoInteractor(matcher) {
    fun getDiaryCardWithTitle(title: String) : DiaryCard {
        return DiaryCard(allOf(withId(R.id.diary_card), withText(title)))
    }
//    fun scrollToItemWithTitle(){
//        onView(matcher).perform(scrollTo<DiaryPreviewViewHolder>(
//            withText("what-1")
//        ))
//    }

    fun scrollToIndex(itemIndex : Int) {
        onView(viewMatcher)
            .perform(scrollToPosition<DiaryPreviewViewHolder>(itemIndex))
    }
}