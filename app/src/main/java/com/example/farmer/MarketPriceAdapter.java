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

public class MarketPriceAdapter extends RecyclerView.Adapter<MarketPriceAdapter.MyViewHolder> {
    Context context;
    List<MarketPriceModel> list;
    DatabaseReference databaseRef;

    public MarketPriceAdapter(Context context, List<MarketPriceModel> list) {
        this.context = context;
        this.list = list;
        this.databaseRef = FirebaseDatabase.getInstance().getReference("market_prices");
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cropText, priceText, mandiText, dateText;
        ImageView btnEdit, btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cropText = itemView.findViewById(R.id.txtCrop);
            priceText = itemView.findViewById(R.id.txtPrice);
            mandiText = itemView.findViewById(R.id.txtMandi);
            dateText = itemView.findViewById(R.id.txtDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_market_price, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MarketPriceModel model = list.get(position);

        holder.cropText.setText(model.cropName);
        holder.priceText.setText("₹" + model.minPrice + " - ₹" + model.maxPrice);
        holder.mandiText.setText(model.mandiName);

        // Format timestamp to readable date
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String formattedDate = sdf.format(new Date(model.timestamp));
        holder.dateText.setText(formattedDate);

        // Delete handler
        holder.btnDelete.setOnClickListener(v -> {
            databaseRef.child(String.valueOf(model.timestamp)).removeValue()
                    .addOnSuccessListener(unused ->
                            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show());
        });

        // Edit handler
        holder.btnEdit.setOnClickListener(v -> showEditDialog(model));
    }

    private void showEditDialog(MarketPriceModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Market Price");

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_crop, null);
        EditText etCrop = dialogView.findViewById(R.id.etCrop);
        EditText etMin = dialogView.findViewById(R.id.etMin);
        EditText etMax = dialogView.findViewById(R.id.etMax);
        EditText etMandi = dialogView.findViewById(R.id.etMandi);

        // Set current values
        etCrop.setText(model.cropName);
        etMin.setText(String.valueOf(model.minPrice));
        etMax.setText(String.valueOf(model.maxPrice));
        etMandi.setText(model.mandiName);

        builder.setView(dialogView);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String crop = etCrop.getText().toString().trim();
            String minStr = etMin.getText().toString().trim();
            String maxStr = etMax.getText().toString().trim();
            String mandi = etMandi.getText().toString().trim();

            if (crop.isEmpty() || minStr.isEmpty() || maxStr.isEmpty() || mandi.isEmpty()) {
                Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            int min = Integer.parseInt(minStr);
            int max = Integer.parseInt(maxStr);

            MarketPriceModel updatedModel = new MarketPriceModel(crop, min, max, mandi, model.timestamp);
            databaseRef.child(String.valueOf(model.timestamp)).setValue(updatedModel)
                    .addOnSuccessListener(unused ->
                            Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show());
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
