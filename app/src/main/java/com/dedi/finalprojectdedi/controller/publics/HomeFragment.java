package com.dedi.finalprojectdedi.controller.publics;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dedi.finalprojectdedi.R;
import com.dedi.finalprojectdedi.adapter.TripScheduleListAdapter;
import com.dedi.finalprojectdedi.models.bus.Stop;
import com.dedi.finalprojectdedi.models.bus.TripSchedule;
import com.dedi.finalprojectdedi.rest.APIClient;
import com.dedi.finalprojectdedi.rest.APIInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private EditText edCalendar, edStartDate, edEndDate;
    private Button btnCari;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private Spinner spSourceStop,spDestinationStop;
    private APIInterface apiInterface;
    private List<Integer> spinnerListId;
    private List<String> spinnerListName;
    private View fragmentView;
    private RecyclerView rvListTripSchedule;
    private RecyclerView.Adapter tripScheduleAdapter;
    private RecyclerView.LayoutManager tripScheduleLayoutManager;
    private String from, to;
    private Integer destStopId, sourceStopId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        myCalendar = Calendar.getInstance();

        edStartDate = fragmentView.findViewById(R.id.edStartDate);
        edEndDate = fragmentView.findViewById(R.id.edEndDate);
        spSourceStop = fragmentView.findViewById(R.id.spSourceStop);
        spDestinationStop = fragmentView.findViewById(R.id.spDestinationStop);
        btnCari = fragmentView.findViewById(R.id.btnCari);


        date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(edCalendar);
        };

        edStartDate.setOnClickListener(v -> {
            showCalendar(date);
            edCalendar = edStartDate;
        });

        edEndDate.setOnClickListener(v -> {
            showCalendar(date);
            edCalendar = edEndDate;
        });

        getStops();

        btnCari.setOnClickListener(v -> {
            from = edStartDate.getText().toString();
            to = edEndDate.getText().toString();
            sourceStopId = spinnerListId.get(spSourceStop.getSelectedItemPosition());
            destStopId = spinnerListId.get(spDestinationStop.getSelectedItemPosition());

            if (destStopId.equals(null) || destStopId.equals(null) || TextUtils.isEmpty(from) || TextUtils.isEmpty(to) ) {
                Toast.makeText(getActivity(), "Tanggal masih kosong!", Toast.LENGTH_LONG).show();
            } else {
                cariTripSchedule();
            }
        });

        rvListTripSchedule = fragmentView.findViewById(R.id.rvListTripSchedule);
        tripScheduleLayoutManager = new LinearLayoutManager(getActivity());
        rvListTripSchedule.setLayoutManager(tripScheduleLayoutManager);
        getTripSchedule();

        return fragmentView;
    }

    private void showCalendar(DatePickerDialog.OnDateSetListener date) {
        DatePickerDialog dialog = new DatePickerDialog(
                getActivity(),
                date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(new Date().getTime());
        dialog.show();
    }

    private void updateLabel(EditText editText) {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
    }

    private void getStops(){

        spinnerListId = new ArrayList<>();
        spinnerListName = new ArrayList<>();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<Stop>> stopsCall = apiInterface.getStops();

        stopsCall.enqueue(new Callback<List<Stop>>() {
            @Override
            public void onResponse(Call<List<Stop>> call, Response<List<Stop>> response) {
                List<Stop> stops = response.body();
                for (int i = 0; i < stops.size(); i++) {
                    spinnerListId.add(stops.get(i).getId());
                    spinnerListName.add(stops.get(i).getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(fragmentView.getContext(), R.layout.spinner_list_item, spinnerListName);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spSourceStop.setAdapter(adapter);
                spDestinationStop.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Stop>> call, Throwable t) {

            }
        });
    }

    private void getTripSchedule() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<TripSchedule>> listCall = apiInterface.getTripSchedules();
        listCall.enqueue(new Callback<List<TripSchedule>>() {
            @Override
            public void onResponse(Call<List<TripSchedule>> call, Response<List<TripSchedule>> response) {
                List<TripSchedule> tripScheduleList = response.body();
                tripScheduleAdapter = new TripScheduleListAdapter(tripScheduleList, getContext());
                rvListTripSchedule.setAdapter(tripScheduleAdapter);
            }

            @Override
            public void onFailure(Call<List<TripSchedule>> call, Throwable t) {
            }
        });
    }

    private void cariTripSchedule() {

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<TripSchedule>> listCall = apiInterface.getTripSchedulesParam(destStopId, from, sourceStopId, to);
        listCall.enqueue(new Callback<List<TripSchedule>>() {
            @Override
            public void onResponse(Call<List<TripSchedule>> call, Response<List<TripSchedule>> response) {
                List<TripSchedule> tripScheduleList = response.body();
                if(!tripScheduleList.isEmpty()) {
                    tripScheduleAdapter = new TripScheduleListAdapter(tripScheduleList, getContext());
                    rvListTripSchedule.setAdapter(tripScheduleAdapter);
                    rvListTripSchedule.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), "Jadwal perjalanan tidak ditemukan!", Toast.LENGTH_SHORT).show();
                    rvListTripSchedule.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<TripSchedule>> call, Throwable t) {
            }
        });
    }
}
