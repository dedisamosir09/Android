package com.dedi.finalprojectdedi.controller.publics;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dedi.finalprojectdedi.R;
import com.dedi.finalprojectdedi.models.bus.Ticket;
import com.dedi.finalprojectdedi.models.bus.TicketReservation;
import com.dedi.finalprojectdedi.models.bus.TripSchedule;
import com.dedi.finalprojectdedi.rest.APIClient;
import com.dedi.finalprojectdedi.rest.APIInterface;
import com.dedi.finalprojectdedi.utils.MySession;


import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends AppCompatActivity {

    private MySession mySession;
    private APIInterface apiInterface;
    private TripSchedule tripSchedule;
    private TextView txtJourneyDate, txtSourceStop, txtDestinationStop, txtAgency;
    private Button btnPesanTiket;
    private Integer passengerId;
    private TicketReservation ticketReservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mySession = new MySession(BookingActivity.this);
        HashMap<String, String> userSession = mySession.getUserDetails();
        passengerId = Integer.valueOf(userSession.get(MySession.KEY_ID));

        txtJourneyDate = findViewById(R.id.txtJourneyDate);
        txtSourceStop = findViewById(R.id.txtSourceStop);
        txtDestinationStop = findViewById(R.id.txtDestinationStop);
        txtAgency = findViewById(R.id.txtAgency);
        btnPesanTiket = findViewById(R.id.btnPesanTiket);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        getTripSchedule(id);

        btnPesanTiket.setOnClickListener(v -> {
            pesanTiket();
        });
    }

    private void getTripSchedule(String id) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<TripSchedule> tripScheduleCall = apiInterface.getTripSchedule(id);

        tripScheduleCall.enqueue(new Callback<TripSchedule>() {
            @Override
            public void onResponse(Call<TripSchedule> call, Response<TripSchedule> response) {
                tripSchedule = response.body();
                txtJourneyDate.setText(tripSchedule.getTripDate());
                txtSourceStop.setText(tripSchedule.getTripDetail().getSourceStop().getName());
                txtDestinationStop.setText(tripSchedule.getTripDetail().getDestStop().getName());
                txtAgency.setText(tripSchedule.getTripDetail().getAgency().getName());
            }

            @Override
            public void onFailure(Call<TripSchedule> call, Throwable t) {

            }
        });
    }

    private void pesanTiket() {
        ticketReservation = new TicketReservation(1, false, tripSchedule.getTripDate(), tripSchedule.getId(), passengerId);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Ticket> ticketCall = apiInterface.createTicket(ticketReservation);
        ticketCall.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                Toast.makeText(BookingActivity.this, "Tiket berhasil dipesan", Toast.LENGTH_LONG).show();
                Intent i = new Intent(BookingActivity.this, MainActivity.class);
                i.putExtra("ticketFragment",1);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {

            }
        });
    }
}