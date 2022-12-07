package com.example.aip

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.aip.buy.DisplayFarmActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestDetailsClass {

    private lateinit var scenario: ActivityScenario<DisplayFarmActivity>

    @Before
    fun setUp() {
        scenario = launch(DisplayFarmActivity::class.java)
    }

    @Test
    fun checkVisibility() {
        Espresso.onView(withId(R.id.details))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}