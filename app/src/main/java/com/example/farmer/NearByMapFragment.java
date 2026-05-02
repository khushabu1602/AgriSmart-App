package com.example.farmer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.aniketjain.weatherapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NearByMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private PlacesClient placesClient;
    private RecyclerView recyclerView;
    private ShopAdapter shopAdapter;
    private final List<ShopData> shopList = new ArrayList<>();

    private final String GOOGLE_API_KEY = "AIzaSyAfWwxNGWagAh8MHKezAouLu6n708DUb-Q";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_near_by_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), GOOGLE_API_KEY);
        }
        placesClient = Places.createClient(requireContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        recyclerView = view.findViewById(R.id.recyclerViewShops);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shopAdapter = new ShopAdapter(shopList);
        recyclerView.setAdapter(shopAdapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Handle runtime permission here if needed
            return;
        }

        mMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                LatLng currentLocation = new LatLng(lat, lng);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));
                fetchNearbyPlaces(lat, lng);
            }
        });
    }

    private void fetchNearbyPlaces(double latitude, double longitude) {
        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.RATING,
                Place.Field.TYPES
        );

        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        placesClient.findCurrentPlace(request)
                .addOnSuccessListener((FindCurrentPlaceResponse response) -> {
                    shopList.clear();

                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        Place place = placeLikelihood.getPlace();

                        if (place.getTypes() != null && (
                                place.getTypes().contains(Place.Type.STORE) ||
                                        place.getTypes().contains(Place.Type.HARDWARE_STORE) ||
                                        place.getName().toLowerCase().contains("seed") ||
                                        place.getName().toLowerCase().contains("fertilizer") ||
                                        place.getName().toLowerCase().contains("pesticide")
                        )) {
                            LatLng latLng = place.getLatLng();
                            if (latLng == null) continue;

                            ShopData shop = new ShopData(
                                    place.getName(),
                                    place.getAddress(),
                                    place.getRating() != null ? place.getRating().floatValue() : 0f,
                                    "Not Available",
                                    latLng.latitude,
                                    latLng.longitude
                            );

                            shopList.add(shop);

                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(place.getName())
                                    .snippet(place.getAddress()));
                        }
                    }

                    shopAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(Throwable::printStackTrace);
    }
}
