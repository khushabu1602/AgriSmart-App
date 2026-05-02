package com.example.farmer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aniketjain.weatherapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RentalMachineFragment extends Fragment {


    RecyclerView recyclerRental;
    FloatingActionButton fabAdd;
    List<MachineRentalModel> rentalList;
    MachineRentalAdapter adapter;
    DatabaseReference ref;


    public RentalMachineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rental_machine, container, false);

        recyclerRental = v.findViewById(R.id.recyclerRental);
        fabAdd = v.findViewById(R.id.fabAddRental);
        rentalList = new ArrayList<>();
        adapter = new MachineRentalAdapter(getContext(), rentalList);
        recyclerRental.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerRental.setAdapter(adapter);

        ref = FirebaseDatabase.getInstance().getReference("machine_rentals");

        loadData();
        return v;

    }

    private void loadData() {
        ref.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rentalList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    MachineRentalModel model = snap.getValue(MachineRentalModel.class);
                    rentalList.add(model);
                }
                adapter.notifyDataSetChanged();
            }
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }


}