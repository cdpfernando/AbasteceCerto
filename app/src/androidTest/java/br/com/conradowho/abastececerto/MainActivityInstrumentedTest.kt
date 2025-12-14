package br.com.conradowho.abastececerto

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.Intents.times
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.conradowho.abastececerto.database.DatabaseHandler
import br.com.conradowho.abastececerto.entity.Vehicle
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {


    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        androidx.test.espresso.intent.Intents.init()

        activityRule.scenario.onActivity { activity ->
            val dbHandler = DatabaseHandler.getInstance(activity)
            dbHandler.writableDatabase.execSQL("DELETE FROM ${DatabaseHandler.TABLE_NAME}")
            dbHandler.close()
        }
    }

    @After
    fun tearDown() {
        androidx.test.espresso.intent.Intents.release()
    }

    @Test
    fun calculateButton_whenFieldsAreValid_shouldOpenResultActivity() {

        //Arrange
        val alcoholPrice = "3.50"
        val gasolinePrice = "5.00"

        //Act
        onView(ViewMatchers.withId(R.id.et_alcohol)).perform(
            ViewActions.typeText(alcoholPrice),
            ViewActions.closeSoftKeyboard()
        )
        onView(ViewMatchers.withId(R.id.et_gasoline)).perform(
            ViewActions.typeText(gasolinePrice),
            ViewActions.closeSoftKeyboard()
        )
        onView(ViewMatchers.withId(R.id.btn_calculate)).perform(ViewActions.click())

        //Assert
        androidx.test.espresso.intent.Intents.intended(hasComponent(ResultActivity::class.java.name))
        org.hamcrest.CoreMatchers.allOf(
            hasComponent(ResultActivity::class.java.name),
            hasExtraWithKey(ResultActivity.EXTRA_CALCULATION_RESULT)
        )

    }

    @Test
    fun calculateButton_whenOneFieldIsEmpty_shouldNotOpenResultActivity() {
        //Arrange
        val gasolinePrice = "5.00"
        onView(ViewMatchers.withId(R.id.et_gasoline)).perform(
            ViewActions.typeText(gasolinePrice),
            ViewActions.closeSoftKeyboard()
        )

        //Act
        onView(ViewMatchers.withId(R.id.btn_calculate)).perform(ViewActions.click())

        //Assert
        androidx.test.espresso.intent.Intents.intended(
            hasComponent(ResultActivity::class.java.name),
            times(0)
        )
        onView(ViewMatchers.withId(R.id.btn_calculate)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}
