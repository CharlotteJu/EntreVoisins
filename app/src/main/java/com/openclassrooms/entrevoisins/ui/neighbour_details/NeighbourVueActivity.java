package com.openclassrooms.entrevoisins.ui.neighbour_details;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.DummyNeighbourApiService;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NeighbourVueActivity extends AppCompatActivity {

    private Neighbour m_Neighbour;
    private Intent m_Intent;
    private NeighbourApiService m_ApiService= DI.getNeighbourApiService();

    @BindView(R.id.vue_return_btn)
    ImageButton mReturnBtn;

    @BindView(R.id.vue_image_avatar)
    ImageView mImageAvatar;

    @BindView(R.id.vue_name_photo_txt)
    TextView mNamePhoto;

    @BindView(R.id.vue_card1_nom_txt)
    TextView mNameCard1;

    @BindView(R.id.vue_facebook_txt)
    TextView mFacebook;

    @BindView(R.id.vue_add_fav_flbtn)
    FloatingActionButton mFavBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbour_vue);

        m_Intent = getIntent();
        Gson gson = new Gson();
        m_Neighbour = gson.fromJson(m_Intent.getStringExtra("json"), Neighbour.class);

        ButterKnife.bind(this);
        updateText();
        configButton();

        mFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(m_Neighbour.getFavorite() == false)
                {
                    m_ApiService.addFavorite(m_Neighbour);

                }
                else
                {
                    m_ApiService.deleteFavorite(m_Neighbour);
                }
                configButton();
            }
        });

        mReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent (NeighbourVueActivity.this, ListNeighbourActivity.class);
                startActivity(intent);
            }
        });

    }


    /**
     * Update the items with the Neighbour's informations
     */
    private void updateText()
    {
        mNamePhoto.setText(m_Neighbour.getName());
        mNameCard1.setText(m_Neighbour.getName());
        Glide.with(this)
                .load(m_Neighbour.getAvatarUrl())
                .into(mImageAvatar);
        mFacebook.setText("www.facebook.fr/" + m_Neighbour.getName());
    }

    /**
     * Configure the button to know if the neighbour is favorite or not
     */
    private void configButton()
    {
        if(m_Neighbour.getFavorite() == true)
        {
            mFavBtn.setImageResource(R.drawable.ic_star_yellow_24dp);


        }
        else
        {
            mFavBtn.setImageResource(R.drawable.ic_star_border_yellow_24dp);

        }
    }


}
