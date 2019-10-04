package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.List;


/**
 * Neighbour API client
 */
public interface NeighbourApiService {

    /**
     * Get all my Neighbours
     * @return {@link List}
     */
    List<Neighbour> getNeighbours();

    /**
     * Deletes a neighbour
     * @param neighbour
     */
    void deleteNeighbour(Neighbour neighbour);


    /**
     * Get all the Favorites
     * @return {@link List}
     */
    List<Neighbour> getFavorites();


    /**
     * Add a Favorite
     * @param neighbour
     */
    void addFavorite(Neighbour neighbour);

    /**
     * Deletes a neighbour in the favorite list
     * @param neighbour
     */
    void deleteFavorite(Neighbour neighbour);


}
