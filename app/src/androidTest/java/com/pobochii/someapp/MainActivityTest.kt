package com.pobochii.someapp

import androidx.navigation.findNavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivity_isRecyclerViewDisplayed_isSearchItemDisplayed() {
        onView(withId(R.id.action_search)).check(matches(isDisplayed()))

        val recyclerView = onView(
            allOf(
                withId(R.id.users_list),
                withParent(withParent(withId(R.id.nav_host))),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))
    }

    @Test
    fun mainActivity_isStartDestinationShowed() {
        activityScenarioRule.scenario.onActivity {
            val navController = it.findNavController(R.id.nav_host)
            assertThat(navController.currentDestination?.id, equalTo(R.id.users_dest))
        }
    }
}
