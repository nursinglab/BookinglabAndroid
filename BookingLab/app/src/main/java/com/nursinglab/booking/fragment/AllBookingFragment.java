package com.nursinglab.booking.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.nursinglab.booking.R;
import com.nursinglab.booking.adapter.AllBookingAdapter;
import com.nursinglab.booking.api.Booking;
import com.nursinglab.booking.component.RecordsComponent;
import com.nursinglab.booking.component.ResponseComponent;
import com.nursinglab.booking.component.ResultComponent;
import com.nursinglab.booking.component.SharedPreferenceComponent;
import com.nursinglab.booking.databinding.FragmentAllBookingBinding;
import com.nursinglab.booking.helper.ItemClickHelper;
import com.nursinglab.booking.util.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllBookingFragment extends Fragment {

    private MenuItem menuItem;
    private SearchView searchView;
    private List<ResultComponent> result = new ArrayList<>();
    private AllBookingAdapter allBookingAdapter;
    private FragmentAllBookingBinding binding;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAllBookingBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentAllBookingBinding.bind(view);

        //sharedPreference get ID
        String id = new SharedPreferenceComponent(this.getActivity()).getDataId();
        getData(id);
        recyclerView();
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //LoadData
                getData(id);

                //StopAnimate with Delay
                binding.swipeRefresh.setRefreshing(false);
            }
        });
        binding.swipeRefresh.setColorSchemeResources(R.color.colorAccent);
    }

    private void getData(String id) {
        binding.selectData.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = RetrofitUtil.getClient();
        Booking booking = retrofit.create(Booking.class);
        Call<ResponseComponent> call = booking.select_all(id);
        call.enqueue(new Callback<ResponseComponent>() {
            @Override
            public void onResponse(Call<ResponseComponent> call, Response<ResponseComponent> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.selectData.setVisibility(View.VISIBLE);
                Integer error = response.body() != null ? response.body().getError() : null;
                String status = response.body() != null ? response.body().getStatus() : null;
                RecordsComponent records = response.body() != null ? response.body().getRecords() : null;
                List<ResultComponent> list = response.body() != null ? response.body().getResult() : null;
                if(response.isSuccessful()){
                    assert error != null;
                    if(error.equals(1)) {
                        result.clear();
                        if(list != null) {
                            result.addAll(list);
                            allBookingAdapter.notifyDataSetChanged();
                        }
                    }else{
                        String getId = records != null ? records.getId() : "Empty";
                        Toast.makeText(getActivity(), status+" "+getId, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    String errorBody = response.errorBody() != null ? response.errorBody().toString() : null;
                    Toast.makeText(getActivity(), errorBody, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseComponent> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.selectData.setVisibility(View.GONE);
                Snackbar.make(view, "Kesalahan pada jaringan!", Snackbar.LENGTH_LONG)
                        .setAction("Oke", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setDuration(3000)
                        .show();
            }
        });
    }

    private void recyclerView() {
        allBookingAdapter = new AllBookingAdapter(new ItemClickHelper() {
            @Override
            public void onItemClick(int position) {
                ResultComponent resultComponent = result.get(position);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setTitle(resultComponent.getNama_lab());
                alertDialogBuilder
                        .setMessage("Nim : "+resultComponent.getNim_mahasiswa()+"\n"+
                                "Waktu mulai : "+resultComponent.getWaktu_mulai()+"\n"+
                                "Waku selesai : "+resultComponent.getWaktu_selesai()+"\n\n"+
                                "Dosen : "+resultComponent.getNama_dosen()+"\n"+
                                "Praktikum : "+resultComponent.getNama_praktikum()+"\n\n"+
                                "Kelas : "+resultComponent.getKelas()+"\n"+
                                "Tanggal : "+resultComponent.getTanggal())
                        .setCancelable(false)
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

            @Override
            public void onLongItemClick(int position) {
                ResultComponent resultComponent = result.get(position);
                Toast.makeText(getActivity(), resultComponent.getNama_lab(), Toast.LENGTH_SHORT).show();
            }
        }, result);

        binding.selectData.setHasFixedSize(true);
        binding.selectData.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        binding.selectData.setItemAnimator(new DefaultItemAnimator());
        binding.selectData.addItemDecoration(new DividerItemDecoration(this.view.getContext(), LinearLayoutManager.VERTICAL));
        binding.selectData.setAdapter(allBookingAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_all_booking, menu);

        menuItem = menu.findItem(R.id.action_pencarian);
        searchView = (SearchView) menuItem.getActionView();

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_about){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.view.getContext());
            alertDialogBuilder
                    .setTitle("Tentang Aplikasi")
                    .setMessage("BookingLab\n" +
                            "Applications version 1.0")
                    .setCancelable(false)
                    .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }else if(id == R.id.action_pencarian){
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    allBookingAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
