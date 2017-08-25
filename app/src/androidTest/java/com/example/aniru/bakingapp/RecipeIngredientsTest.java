package com.example.aniru.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by aniru on 8/23/2017.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeIngredientsTest {

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        mMainActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void verifyRecipe_listItem(){

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Find the recipes recyclerview & verify that first position is "Nutella Pie"
        // Check item at position 3 has "Some content"
        onView(withRecyclerView(R.id.rv_recipesname).atPosition(0))
                .check(matches(hasDescendant(withText("Nutella Pie"))));

        // Click on the first position
        onView(withId(R.id.rv_recipesname))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify we get to the Ingredients and Steps recuclerview
        onView(withRecyclerView(R.id.rv_ingredientsandsteps).atPosition(0))
                .check(matches(hasDescendant(withText("Recipe Ingredients"))));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Click on the first position
        onView(withId(R.id.rv_ingredientsandsteps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify we get to the Ingredients and Steps recuclerview
        onView(withRecyclerView(R.id.rv_ingredients_details).atPosition(0))
                .check(matches(hasDescendant(withText("Ingredient: Graham Cracker crumbs"))));
    }
}
