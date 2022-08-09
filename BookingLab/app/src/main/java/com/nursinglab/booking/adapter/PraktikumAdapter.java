package com.nursinglab.booking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nursinglab.booking.component.BookingIdComponent;
import com.nursinglab.booking.databinding.ViewRecyclerBookingBinding;

import java.util.ArrayList;
import java.util.List;

public class PraktikumAdapter extends RecyclerView.Adapter<PraktikumAdapter.ViewHolder> implements Filterable {

    private List<BookingIdComponent> booking;
    private List<BookingIdComponent> bookingFull;
    private Context context;

    public PraktikumAdapter(Context context, List<BookingIdComponent> booking) {
        this.context = context;
        this.booking = booking;
        this.bookingFull = booking;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nama;

        ViewHolder(ViewRecyclerBookingBinding binding) {
            super(binding.getRoot());
            nama = binding.nama;
        }
    }

    @Override
    public Filter getFilter() {
        return pencarian;
    }

    private Filter pencarian = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<BookingIdComponent> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(bookingFull);
            }else{
                String filterPatern = constraint.toString().toLowerCase().trim();
                for(BookingIdComponent bookingIdComponent : bookingFull){
                    if(bookingIdComponent.getNama().toLowerCase().contains(filterPatern)){
                        filteredList.add(bookingIdComponent);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            booking = (List) results.values;
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public PraktikumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewRecyclerBookingBinding binding = ViewRecyclerBookingBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        BookingIdComponent bookingIdComponent = booking.get(i);
        viewHolder.nama.setText(bookingIdComponent.getNama());
    }

    @Override
    public int getItemCount() {
        return booking.size();
    }
}
