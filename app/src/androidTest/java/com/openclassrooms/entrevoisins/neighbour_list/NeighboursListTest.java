
package com.openclassrooms.entrevoisins.neighbour_list;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.DummyNeighbourApiService;
import com.openclassrooms.entrevoisins.service.DummyNeighbourGenerator;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.ViewPagerActions.scrollRight;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsNull.notNullValue;



/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static int ITEMS_COUNT = 12;
    private static int POSITION = 0;

    private ListNeighbourActivity mActivity;
    private List<Neighbour> mNeighbourList = DummyNeighbourGenerator.DUMMY_NEIGHBOURS;
    private NeighbourApiService mApiService = DI.getNeighbourApiService();
    private List<Neighbour> mFavList = mApiService.getFavorites();

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.list_neighbours))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(ViewMatchers.withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(ViewMatchers.withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(ViewMatchers.withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT-1));
    }

    /**
     * When we click on a Neighbour, display the Activity Vue
     */

    @Test
    public void myNeighboursList_clickAction_shouldDisplayVue()
    {
        //Given : Click on the item
        onView(withId(R.id.list_neighbours)).
                perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //Then : Go to the Vue Activity
        onView(withId(R.id.activity_vue)).check(matches(isDisplayed()));
    }


    /**
     * When we click on a Neighbour, give the good Neighbour's name
     */
    @Test
    public void activityVue_loadNameText_shouldBeTheGoodOne()
    {
        //Given : A Neighbour for the test
        Neighbour neighbour = mNeighbourList.get(POSITION);
        //When : Click on it
        onView(withId(R.id.list_neighbours)).
                perform(RecyclerViewActions.actionOnItemAtPosition(POSITION, click()));
        //Then : Have the good name on ActivityVue
        onView(withId(R.id.vue_name_photo_txt)).check(matches(withText(neighbour.getName())));
    }

    /**
     * When we delete an item on the favorites list, the item is no more shown
     */
    @Test
    public void myFavoritesList_deleteAction_shouldRemoveItem()
    {
        //Given : Add 2 Neighbours in the Favorite List and check the List's size
        mApiService.addFavorite(mNeighbourList.get(0));
        mApiService.addFavorite(mNeighbourList.get(1));
        onView(ViewMatchers.withId(R.id.fav_list_neighbours)).check(withItemCount(2));

        //Scroll to the favorite page in the container
        onView(withId(R.id.container)).perform(scrollRight());

        // When perform a click on a delete icon for the 2nd item
        onView(ViewMatchers.withId(R.id.fav_list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 1
        onView(ViewMatchers.withId(R.id.fav_list_neighbours)).check(withItemCount(2-1));
    }

    /**
     * The FavList's size == number of Favorites Neighbours
     */
    @Test
    public void myFavoritesList_haveOnlyFavorites()
    {
        //Given : The Favorite List is empty
        onView(withId(R.id.container)).perform(scrollRight());
        mFavList.clear();
        onView(ViewMatchers.withId(R.id.fav_list_neighbours)).check(withItemCount(0));

        //When : Add 5 Neighbours in the Favorite List and check the List's size
        for(int i = 0; i < 5; i++)
        {
            mApiService.addFavorite(mNeighbourList.get(i));
        }

        //Then : The FavList have 5 Neighbours
        onView(ViewMatchers.withId(R.id.fav_list_neighbours)).check(withItemCount(5));
    }

}







