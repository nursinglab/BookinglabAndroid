package com.nursinglab.booking.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nursinglab.booking.R;
import com.nursinglab.booking.component.ResultComponent;
import com.nursinglab.booking.databinding.ViewRecyclerMyBookingBinding;
import com.nursinglab.booking.helper.ItemClickHelper;

import java.util.ArrayList;
import java.util.List;

public class MyBookingAdapter extends RecyclerView.Adapter<MyBookingAdapter.ViewHolder> implements Filterable {

    private List<ResultComponent> result;
    private List<ResultComponent> resultFull;
    private ItemClickHelper itemClickHelper;

    public MyBookingAdapter(ItemClickHelper itemClickHelper, List<ResultComponent> result) {
        this.itemClickHelper = itemClickHelper;
        this.result = result;
        this.resultFull = result;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView namaLab;
        private TextView namaDosen;
        private TextView waktuMulai;
        private TextView expired;

        ViewHolder(ViewRecyclerMyBookingBinding binding, final ItemClickHelper itemClickHelper) {
            super(binding.getRoot());
            namaLab = binding.namaLab;
            namaDosen = binding.namaDosen;
            waktuMulai = binding.waktuMulai;
            expired = binding.expired;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickHelper != null) {
                        itemClickHelper.onItemClick(getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(itemClickHelper != null) {
                        itemClickHelper.onLongItemClick(getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }

    @NonNull
    @Override
    public MyBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewRecyclerMyBookingBinding binding = ViewRecyclerMyBookingBinding.inflate(LayoutInflater.from(viewGroup.getContext()));
        return new ViewHolder(binding, itemClickHelper);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyBookingAdapter.ViewHolder viewHolder, int i) {
        ResultComponent resultComponent = result.get(i);

        String a = resultComponent.getNama_dosen();
        int b = Integer.parseInt(resultComponent.getAction());

        viewHolder.namaLab.setText(resultComponent.getNama_lab());
        viewHolder.namaDosen.setText(String.format("(%s)", a));
        viewHolder.waktuMulai.setText(resultComponent.getWaktu_mulai());

        if(b == 0) {
            viewHolder.expired.setText("(Dipakai)");
            viewHolder.expired.setTextColor(Color.parseColor("#28a745"));
        } else if(b == 1) {
            viewHolder.expired.setText("(Expired)");
            viewHolder.expired.setTextColor(Color.parseColor("#dc3545"));
        } else {
            viewHolder.expired.setText("(404)");
            viewHolder.expired.setTextColor(Color.parseColor("#ffc107"));
        }
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    @Override
    public Filter getFilter() {
        return pencarian;
    }

    private Filter pencarian = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ResultComponent> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(resultFull);
            }else{
                String filterPatern = constraint.toString().toLowerCase().trim();
                for(ResultComponent resultComponent : resultFull){
                    if(resultComponent.getNama_lab().toLowerCase().contains(filterPatern)){
                        filteredList.add(resultComponent);
                    }
                    if(resultComponent.getNama_dosen().toLowerCase().contains(filterPatern)){
                        filteredList.add(resultComponent);
                    }
                    if(resultComponent.getWaktu_mulai().toLowerCase().contains(filterPatern)){
                        filteredList.add(resultComponent);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            result = (List) results.values;
            notifyDataSetChanged();
        }
    };
}
