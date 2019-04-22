package com.order66.team66.spacetraderapp.views;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.order66.team66.spacetraderapp.R;
import com.order66.team66.spacetraderapp.models.*;
import com.order66.team66.spacetraderapp.viewmodels.MarketViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents Travel Screen
 */
public class TravelActivity extends AppCompatActivity {

    private Spinner planetSpinner;
    private MarketViewModel viewModel;
    private SolarSystem solarSystem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        viewModel = new MarketViewModel();
        Planet planet = viewModel.getPlanet();
        solarSystem = viewModel.getSolarSystem();
        Player player = viewModel.getPlayer();
        CargoHold cargo = viewModel.getCargoHold();

        TextView currentPlanet = findViewById(R.id.current_planet_text);
        TextView shipType = findViewById(R.id.ship_type_text);
        TextView fuelRemaining = findViewById(R.id.fuel_remaining_amount_text);
        TextView cargoRemaining = findViewById(R.id.available_cargo_amount_text);
        planetSpinner = findViewById(R.id.planet_spinner);
        Button travelButton = findViewById(R.id.travel_button);

        currentPlanet.setText(viewModel.getPlanetName());
        Spaceship spaceship = viewModel.getSpaceship();
        shipType.setText(viewModel.getSpaceshipName());
        fuelRemaining.setText(String.format("%s", viewModel.getCurrentFuel()));
        cargoRemaining.setText(String.format("%s", (viewModel.getMaxCapacity() -
                viewModel.getCurrentCapacity())));


        List<String> planetNames = new ArrayList<>();
        for (Planet p : solarSystem.getPlanets()) {
            planetNames.add(p.getName());
        }

        ArrayAdapter<String> planetAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, planetNames);
        planetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        planetSpinner.setAdapter(planetAdapter);

        planetSpinner.setSelection(planetNames.indexOf(viewModel.getPlanetName()));

        Button buttonPlayVideo2 = (Button)findViewById(R.id.button1);

        getWindow().setFormat(PixelFormat.UNKNOWN);

//displays a video file
        VideoView mVideoView2 = (VideoView)findViewById(R.id.videoView1);

        String uriPath2 = "android.resource://"+getPackageName()+"/"+R.raw.spcshp;
        Uri uri2 = Uri.parse(uriPath2);
        mVideoView2.setVideoURI(uri2);
        mVideoView2.requestFocus();
        mVideoView2.start();

        buttonPlayVideo2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoView mVideoView2 = (VideoView) findViewById(R.id.videoView1);
                // VideoView mVideoView = new VideoView(this);
                String uriPath = "android.resource://com.example.toyo.playvideo/" + R.raw.spcshp;
                Uri uri2 = Uri.parse(uriPath);
                mVideoView2.setVideoURI(uri2);
                mVideoView2.requestFocus();
                mVideoView2.start();
            }
        });
    }

    /**
     * Travel on confirmation
     *
     * @param view travel button
     */
    public void onClick(View view) {
        try {
            viewModel.travel(solarSystem.getPlanet(planetSpinner.getSelectedItemPosition()), solarSystem);
            Intent intent = new Intent(TravelActivity.this, HomeActivity.class);
            startActivity(intent);
        } catch (RuntimeException e) {
            Toast toast = Toast.makeText(this,"You don't have enough fuel for that!", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
