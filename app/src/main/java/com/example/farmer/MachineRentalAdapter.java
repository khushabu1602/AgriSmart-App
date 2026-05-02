package com.example.farmer;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aniketjain.weatherapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MachineRentalAdapter extends RecyclerView.Adapter<MachineRentalAdapter.ViewHolder> {

    Context context;
    List<MachineRentalModel> list;
    DatabaseReference ref;

    public MachineRentalAdapter(Context context, List<MachineRentalModel> list) {
        this.context = context;
        this.list = list;
        this.ref = FirebaseDatabase.getInstance().getReference("machine_rentals");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPerson, txtMachine, txtPrice, txtContact, txtAddress, txtDate;
        ImageView btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            txtPerson = itemView.findViewById(R.id.txtPerson);
            txtMachine = itemView.findViewById(R.id.txtMachine);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtContact = itemView.findViewById(R.id.txtContact);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtDate = itemView.findViewById(R.id.txtDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_machine_rental, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        MachineRentalModel m = list.get(position);
        h.txtPerson.setText(m.personName);
        h.txtMachine.setText(  m.machine);
        h.txtPrice.setText( "₹"+m.rentPrice +"/month");
        h.txtContact.setText( m.mobile);
        h.txtAddress.setText( m.address);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        h.txtDate.setText(sdf.format(new Date(m.timestamp)));

        h.btnDelete.setOnClickListener(v -> {
            ref.child(String.valueOf(m.timestamp)).removeValue();
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        });

        h.btnEdit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_machine_rental, null);
            builder.setView(dialogView);
            builder.setTitle("Edit Machine Info");

            EditText etPerson = dialogView.findViewById(R.id.etPerson);
            EditText etMobile = dialogView.findViewById(R.id.etMobile);
            EditText etMachine = dialogView.findViewById(R.id.etMachine);
            EditText etPrice = dialogView.findViewById(R.id.etPrice);
            EditText etAddress = dialogView.findViewById(R.id.etAddress);

            etPerson.setText(m.personName);
            etMobile.setText(m.mobile);
            etMachine.setText(m.machine);
            etPrice.setText(m.rentPrice);
            etAddress.setText(m.address);

            builder.setPositiveButton("Update", (dialog, which) -> {
                MachineRentalModel updated = new MachineRentalModel(
                        etPerson.getText().toString(),
                        etMobile.getText().toString(),
                        etMachine.getText().toString(),
                        etPrice.getText().toString(),
                        etAddress.getText().toString(),
                        m.timestamp
                );
                ref.child(String.valueOf(m.timestamp)).setValue(updated);
                Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

