package com.openclassrooms.entrevoisins.ui.neighbour_list;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteFavEvent;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.DummyNeighbourApiService;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_details.NeighbourVueActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavListFragment extends Fragment implements MyNeighbourRecyclerViewAdapter.onItemListener{

    private List<Neighbour> mFavNeighbours;
    private RecyclerView mRecyclerView;
    private MyNeighbourRecyclerViewAdapter mAdapter;
    private NeighbourApiService mApiService;


    public FavListFragment() {
        // Required empty public constructor
    }


    public static FavListFragment newInstance ()
    {
        FavListFragment frag = new FavListFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_list, container, false);
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        initList();
        return view;
    }

    @Override
    public void onResume() {
        initList();
        super.onResume();
    }

    /**
     * Init the List of neighbours
     */
    private void initList() {
        this.mApiService = DI.getNeighbourApiService();
        this.mFavNeighbours = mApiService.getFavorites();
        this.mAdapter = new MyNeighbourRecyclerViewAdapter(this.mFavNeighbours, this,2);
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDeleteFavorite(DeleteFavEvent event) {
        mApiService.deleteFavorite(event.neighbour);
        initList();
    }


    @Override
    public void onItemClick(int position) {
        Gson gson = new Gson();
        String json = gson.toJson(mFavNeighbours.get(position));
        Context context = getActivity();
        Intent intent = new Intent(context, NeighbourVueActivity.class);
        intent.putExtra("json", json);
        startActivity(intent);
    }
}
