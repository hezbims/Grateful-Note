package com.example.gratefulnote.robot.main_home.components

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.gratefulnote.R
import com.example.gratefulnote.robot._common.action.pagingScrollUntilPosition
import com.example.gratefulnote.robot._common.assertion.itemMatchAtIndex
import com.example.gratefulnote.robot._common.interactor.base.EspressoInteractor
import com.example.gratefulnote.robot._common.node_interaction.WaitViewUntil
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

class MainMenuDiaryList(matcher: Matcher<View>) : EspressoInteractor(matcher) {
    fun getDiaryCardWithTitle(title: String) : DiaryCard {
        return DiaryCard(allOf(
            withId(R.id.diary_card),
            ViewMatchers.hasDescendant(withText(title)))
        )
    }
//    fun scrollToItemWithTitle(){
//        onView(matcher).perform(scrollTo<DiaryPreviewViewHolder>(
//            withText("what-1")
//        ))
//    }
    fun waitUntilItemCountAtLeast(itemCount: Int){
        onView(viewMatcher).perform(WaitViewUntil{ view ->
            if (view is RecyclerView){
                return@WaitViewUntil (view.adapter?.itemCount ?: 0) >= itemCount
            }
            false
        })
    }

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

    fun assertTagAtIndex(index: Int, tag: String){
        onView(viewMatcher).check(itemMatchAtIndex(
            allOf(withId(R.id.tipe), withText(tag)),
            index
        ))
    }
}