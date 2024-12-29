package com.example.gratefulnote.robot.add_new_diary

import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.gratefulnote.robot._common.interactor.base.EspressoInteractor

class ConfirmSaveNewDiaryRobot {
    val confirmButton = EspressoInteractor(withText("YA"))
}