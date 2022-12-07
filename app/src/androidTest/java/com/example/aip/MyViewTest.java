package com.example.aip;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.aip.MainActivity;
import com.example.aip.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MyViewTest {

    @Rule
    public ActivityScenarioRule<Registration> activityScenarioRule =
            new ActivityScenarioRule<>(Registration.class);

    @Test
    public void checkVisibility() {
        onView(withId(R.id.registration))
                .check(matches(isDisplayed()));
    }

//    @Test
//    public void checkTextSize() {
//        onView(withId(R.id.textView5))
//                .check(matches(isDisplayed()));
//    }
}
