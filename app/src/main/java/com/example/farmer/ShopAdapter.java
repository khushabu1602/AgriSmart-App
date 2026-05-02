package com.example.farmer;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aniketjain.weatherapp.R;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    private final List<ShopData> shopList;

    public ShopAdapter(List<ShopData> shopList) {
        this.shopList = shopList;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        ShopData shopData = shopList.get(position);
        holder.name.setText(shopData.getName());
        holder.address.setText(shopData.getAddress());
        holder.rating.setText("Rating: " + shopData.getRating());
        holder.phone.setText("Phone: " + shopData.getPhone());

        holder.itemView.setOnClickListener(v -> {
            String uri = String.format("geo:%f,%f?q=%s",
                    shopData.getLatitude(),
                    shopData.getLongitude(),
                    Uri.encode(shopData.getName()));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            v.getContext().startActivity(intent);
        });

        // Optional: Click-to-call if phone number is available
        holder.phone.setOnClickListener(v -> {
            if (!shopData.getPhone().equals("Not Available")) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + shopData.getPhone()));
                v.getContext().startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    static class ShopViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, rating, phone;

        public ShopViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.shopName);
            address = itemView.findViewById(R.id.shopAddress);
            rating = itemView.findViewById(R.id.shopRating);
            phone = itemView.findViewById(R.id.shopPhone);
        }
    }
}
