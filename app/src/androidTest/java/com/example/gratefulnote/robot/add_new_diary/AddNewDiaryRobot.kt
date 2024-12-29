package com.example.gratefulnote.robot.add_new_diary

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.gratefulnote.R
import com.example.gratefulnote.robot._common.interactor.base.EspressoInteractor
import com.example.gratefulnote.robot._common.interactor.component.EspressoTextFieldInteractor
import com.example.gratefulnote.robot.add_new_diary.component.TagDropdown

class AddNewDiaryRobot {
    val confirmSaveDialog = ConfirmSaveNewDiaryRobot()

    val titleTextField = EspressoTextFieldInteractor(withId(R.id.what_value))
    val descTextField = EspressoTextFieldInteractor(withId(R.id.why_value))
    val tagDropdown = TagDropdown(withId(R.id.emotion_type_spinner))
    val saveButton = EspressoInteractor(withId(R.id.save))


    fun enterWhyText(value : String): AddNewDiaryRobot {
        onView(withId(R.id.why_value))
            .perform(typeText(value))
        return this
    }

    fun enterWhatText(value: String): AddNewDiaryRobot {
        onView(withId(R.id.what_value))
            .perform(typeText(value))
        return this
    }

    fun chooseSpinnerItemWithText(value : String): AddNewDiaryRobot {
        onView(withId(R.id.emotion_type_spinner))
            .perform(ViewActions.click())
        onView(withText(value))
            .perform(ViewActions.click())
        return this
    }

    fun confirmSave(): AddNewDiaryRobot {
        onView(withId(R.id.save))
            .perform(ViewActions.click())

        onView(withText("YA"))
            .perform(ViewActions.click())
        return this
    }

    fun fillFormAndSave(
        spinnerValue : String,
        whatValue : String,
        whyValue : String
    ): AddNewDiaryRobot {
        chooseSpinnerItemWithText(spinnerValue)
        enterWhatText(whatValue)
        enterWhyText(whyValue)
        confirmSave()
        return this
    }
}