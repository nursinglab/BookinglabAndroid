package com.nursinglab.booking.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.nursinglab.booking.R;
import com.nursinglab.booking.api.Booking;
import com.nursinglab.booking.component.BookingIdComponent;
import com.nursinglab.booking.component.ResponseComponent;
import com.nursinglab.booking.component.SharedPreferenceComponent;
import com.nursinglab.booking.databinding.ActivityCreateBookingBinding;
import com.nursinglab.booking.util.RetrofitUtil;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateBookingActivity extends AppCompatActivity {

    private ActivityCreateBookingBinding binding;
    private View view;
    private static String dosen_id, lab_id, praktikum_id;
    public static final String EXTRA_DOSEN = "extra_dosen";
    public static final String EXTRA_LAB = "extra_lab";
    public static final String EXTRA_PRAKTIKUM = "extra_praktikum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateBookingBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Create BookingLab");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtClick();
    }

    private void txtClick() {
        binding.txtWaktuMulai.setFocusable(false);
        binding.txtWaktuSelesai.setFocusable(false);
        binding.txtTanggal.setFocusable(false);
        binding.txtDosen.setFocusable(false);
        binding.txtLab.setFocusable(false);
        binding.txtPraktikum.setFocusable(false);

        binding.txtWaktuMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateBookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        binding.txtWaktuMulai.setText(i + ":" + i1);
                    }
                }, hour, minute, android.text.format.DateFormat.is24HourFormat(CreateBookingActivity.this));
                timePickerDialog.show();
            }
        });
        binding.txtWaktuSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateBookingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        binding.txtWaktuSelesai.setText(i + ":" + i1);
                    }
                }, hour, minute, android.text.format.DateFormat.is24HourFormat(CreateBookingActivity.this));
                timePickerDialog.show();
            }
        });
        binding.txtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateBookingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        binding.txtTanggal.setText(i + "/" + (i1+1) + "/" + i2);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        binding.txtDosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateBookingActivity.this, DosenActivity.class);
                startActivityForResult(i, 1);
            }
        });
        binding.txtLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateBookingActivity.this, LabActivity.class);
                startActivityForResult(i, 2);
            }
        });
        binding.txtPraktikum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateBookingActivity.this, PraktikumActivity.class);
                startActivityForResult(i, 3);
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = binding.txtWaktuMulai.getText().toString().trim();
                String b = binding.txtWaktuSelesai.getText().toString().trim();
                String c = binding.txtTanggal.getText().toString().trim();
                String[] data = new String[]{a, b, c, dosen_id, lab_id, praktikum_id};
                saveData(data);
            }
        });
    }

    private void saveData(String[] data) {
        ProgressDialog progressDialog = new ProgressDialog(CreateBookingActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Tunggu...");
        progressDialog.show();
        String id = new SharedPreferenceComponent(CreateBookingActivity.this).getDataId();

        Retrofit retrofit = RetrofitUtil.getClient();
        Booking bkng = retrofit.create(Booking.class);
        Call<ResponseComponent> call = bkng.insert(id, data[0], data[1], data[2], data[3], data[4], data[5]);
        call.enqueue(new Callback<ResponseComponent>() {
            @Override
            public void onResponse(Call<ResponseComponent> call, Response<ResponseComponent> response) {
                progressDialog.dismiss();
                Integer error = response.body() != null ? response.body().getError() : null;
                String status = response.body() != null ? response.body().getStatus() : null;
                if(response.isSuccessful()){
                    assert error != null;
                    if(error.equals(1)) {
                        new Intent(CreateBookingActivity.this, MainActivity.class);
                        setResult(RESULT_OK);
                        finish();
                    }else{
                        Toast.makeText(CreateBookingActivity.this, status, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    String errorBody = response.errorBody() != null ? response.errorBody().toString() : null;
                    Toast.makeText(CreateBookingActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseComponent> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(R.id._created_booking), "Kesalahan pada jaringan!", Snackbar.LENGTH_LONG)
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            BookingIdComponent dosen = data != null ? data.getParcelableExtra(EXTRA_DOSEN) : null;
            dosen_id = dosen != null ? dosen.getId() : null;
            String b = dosen != null ? dosen.getNama_dosen() : null;
            binding.txtDosen.setText(b);
        }
        if(requestCode == 2 && resultCode == RESULT_OK) {
            BookingIdComponent lab = data != null ? data.getParcelableExtra(EXTRA_LAB) : null;
            lab_id = lab != null ? lab.getId() : null;
            String b = lab != null ? lab.getNama() : null;
            binding.txtLab.setText(b);
        }
        if(requestCode == 3 && resultCode == RESULT_OK) {
            BookingIdComponent praktikum = data != null ? data.getParcelableExtra(EXTRA_PRAKTIKUM) : null;
            praktikum_id = praktikum != null ? praktikum.getId() : null;
            String b = praktikum != null ? praktikum.getNama() : null;
            binding.txtPraktikum.setText(b);
        }
    }
}
