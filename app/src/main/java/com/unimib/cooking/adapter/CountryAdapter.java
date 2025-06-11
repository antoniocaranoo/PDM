package com.unimib.cooking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unimib.cooking.R;
import com.unimib.cooking.model.Country;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private Context context;
    private List<Country> countries;
    private OnItemClickListener listener;

    public CountryAdapter(Context context, List<Country> countries, OnItemClickListener listener) {
        this.context = context;
        this.countries = countries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountryViewHolder holder, int position) {
        Country country = countries.get(position);
        holder.countryName.setText(country.getName());
        holder.flagImage.setImageResource(country.getFlagResourceId());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(country));
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public static class CountryViewHolder extends RecyclerView.ViewHolder {
        TextView countryName;
        ImageView flagImage;

        public CountryViewHolder(View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.item_text);
            flagImage = itemView.findViewById(R.id.item_icon);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Country country);
    }
}
