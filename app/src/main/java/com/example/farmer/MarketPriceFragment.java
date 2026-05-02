package com.example.farmer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aniketjain.weatherapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MarketPriceFragment extends Fragment {

    private RecyclerView priceRecycler;

    private List<MarketPriceModel> priceList = new ArrayList<>();
    private MarketPriceAdapter adapter;
    private DatabaseReference databaseRef;


    public MarketPriceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_market_price, container, false);

        priceRecycler = view.findViewById(R.id.priceRecycler);


        databaseRef = FirebaseDatabase.getInstance().getReference("market_prices");

        adapter = new MarketPriceAdapter(getContext(), priceList);
        priceRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        priceRecycler.setAdapter(adapter);



        fetchDataFromFirebase();

        return view;
    }

    private void fetchDataFromFirebase() {
        databaseRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                priceList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    MarketPriceModel model = snap.getValue(MarketPriceModel.class);
                    priceList.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}