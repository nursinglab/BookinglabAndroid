package com.nursinglab.booking.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.nursinglab.booking.R;
import com.nursinglab.booking.adapter.DosenAdapter;
import com.nursinglab.booking.api.Booking;
import com.nursinglab.booking.component.BookingIdComponent;
import com.nursinglab.booking.component.ResponseComponent;
import com.nursinglab.booking.databinding.ActivityDosenBinding;
import com.nursinglab.booking.listener.RecyclerOnItemListener;
import com.nursinglab.booking.util.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.nursinglab.booking.activity.CreateBookingActivity.EXTRA_DOSEN;

public class DosenActivity extends AppCompatActivity {

    private SearchView searchView;
    private List<BookingIdComponent> booking = new ArrayList<>();
    private DosenAdapter dosenAdapter;
    private ActivityDosenBinding binding;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDosenBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Pilih Dosen");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getData();
        recyclerView();

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //LoadData
                getData();

                //StopAnimate with Delay
                binding.swipeRefresh.setRefreshing(false);
            }
        });
        binding.swipeRefresh.setColorSchemeResources(R.color.colorAccent);
    }

    private void recyclerView() {
        dosenAdapter = new DosenAdapter(DosenActivity.this, booking);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(DosenActivity.this));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(DosenActivity.this, LinearLayoutManager.VERTICAL));
        binding.recyclerView.setAdapter(dosenAdapter);
        binding.recyclerView.addOnItemTouchListener(new RecyclerOnItemListener(DosenActivity.this, binding.recyclerView, new RecyclerOnItemListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                BookingIdComponent bookingIdComponent = booking.get(position);
                Intent i = new Intent();
                i.putExtra(EXTRA_DOSEN, bookingIdComponent);
                setResult(RESULT_OK, i);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void getData() {
        binding.recyclerView.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = RetrofitUtil.getClient();
        Booking bkng = retrofit.create(Booking.class);
        Call<ResponseComponent> call = bkng.booking("dosen");
        call.enqueue(new Callback<ResponseComponent>() {
            @Override
            public void onResponse(Call<ResponseComponent> call, Response<ResponseComponent> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                Integer error = response.body() != null ? response.body().getError() : null;
                String status = response.body() != null ? response.body().getStatus() : null;
                booking = response.body() != null ? response.body().getBooking() : null;
                if(response.isSuccessful()){
                    assert error != null;
                    if(error.equals(1)) {
                        dosenAdapter = new DosenAdapter(DosenActivity.this, booking);
                        binding.recyclerView.setAdapter(dosenAdapter);
                    }else{
                        Toast.makeText(DosenActivity.this, status, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    String errorBody = response.errorBody() != null ? response.errorBody().toString() : null;
                    Toast.makeText(DosenActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseComponent> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.GONE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fragment_all_booking, menu);

        MenuItem menuItem = menu.findItem(R.id.action_pencarian);
        searchView = (SearchView) menuItem.getActionView();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_about){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DosenActivity.this);
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
                    dosenAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
