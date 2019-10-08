package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Unit test on Neighbour service
 */
@RunWith(JUnit4.class)
public class NeighbourServiceTest {

    private NeighbourApiService service;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void getNeighboursWithSuccess() {
        List<Neighbour> neighbours = service.getNeighbours();
        List<Neighbour> expectedNeighbours = DummyNeighbourGenerator.DUMMY_NEIGHBOURS;
        assertThat(neighbours, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedNeighbours.toArray()));
    }

    @Test
    public void deleteNeighbourWithSuccess() {
        Neighbour neighbourToDelete = service.getNeighbours().get(0);
        service.deleteNeighbour(neighbourToDelete);
        assertFalse(service.getNeighbours().contains(neighbourToDelete));
    }

    @Test
    public void getFavoritesWithSuccess()
    {
        service.getFavorites().clear();
        Neighbour neighbour = new Neighbour(1,"test", "test");
        service.getFavorites().add(neighbour);
        assertTrue(service.getFavorites().contains(neighbour));
    }

    @Test
    public void addFavNeighbourWithSuccess()
    {
        Neighbour neighbour = new Neighbour(1,"test", "test");
        service.addFavorite(neighbour);
        assertTrue(service.getFavorites().contains(neighbour));
    }

    @Test
    public void deleteFavNeighbourWithSuccess()
    {
        Neighbour neighbourToDelete = service.getNeighbours().get(0);
        List<Neighbour> liste = service.getFavorites();
        liste.clear();
        liste.add(neighbourToDelete);
        assertEquals(service.getNeighbours().get(0), neighbourToDelete);
        service.deleteNeighbour(neighbourToDelete);
        assertFalse(service.getNeighbours().contains(neighbourToDelete));
    }
}
