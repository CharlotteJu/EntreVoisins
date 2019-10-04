package com.openclassrooms.entrevoisins.service;

import android.util.Log;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.ArrayList;
import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyNeighbourApiService implements  NeighbourApiService {

    private List<Neighbour> neighbours = DummyNeighbourGenerator.generateNeighbours();
    private List<Neighbour> favorites= new ArrayList<>();


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNeighbour(Neighbour neighbour) {
        neighbours.remove(neighbour);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getFavorites() {
        return favorites;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFavorite(Neighbour neighbour) {
        if (!favorites.contains(neighbour))
        {
            neighbour.setFavorite(true);
            favorites.add(neighbour);
        }
        else
        {
            Log.e("ERROR", "On essaie de d'ajouter un favoris qui est déjà dans la liste des favoris");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFavorite(Neighbour neighbour) {

        if (favorites.contains(neighbour))
        {
            neighbour.setFavorite(false);
            favorites.remove(neighbour);
        }
        else
        {
            Log.e("ERROR", "On essaie de supprimer un favoris qui n'est pas dans la liste des favoris");
        }

    }

}


