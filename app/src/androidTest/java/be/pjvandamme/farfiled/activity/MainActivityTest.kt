package be.pjvandamme.farfiled.activity

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import be.pjvandamme.farfiled.R
import kotlinx.android.synthetic.main.fragment_relations_list.view.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    companion object {
        @BeforeClass
        @JvmStatic
        fun clearDatabase() {
            Timber.i("About to delete \"farfiled_database\"")
            InstrumentationRegistry.getInstrumentation().targetContext.deleteDatabase("farfiled_database")
            Timber.i("\"farfiled_database\" deleted.")
        }
    }

    @Test
    fun addRandomRelation_addsFaceToRecycler(){
        Timber.i("Starting test addRandomRelation_addsFaceToRecycler")
        clickGenerateRandomRelationsButton()
        // TODO: implement the rest of the test
    }

    fun clickGenerateRandomRelationsButton(){
        Timber.i("About to retrieve generateRandomRelationsButton")
        val appCompatButton = onView(
            allOf(
                withId(R.id.generateRandomRelationsButton), withText(R.string.generateRandomText),
                childAtPosition(
                    childAtPosition(withId(android.R.id.content),0),
                    0),
                isDisplayed()
            )
        )
        Timber.i("About to perform click on generateRandomRelationsButton")
        appCompatButton.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

}