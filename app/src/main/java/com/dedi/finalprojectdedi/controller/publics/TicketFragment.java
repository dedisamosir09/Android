package com.dedi.finalprojectdedi.controller.publics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dedi.finalprojectdedi.R;
import com.dedi.finalprojectdedi.adapter.TicketListAdapter;
import com.dedi.finalprojectdedi.models.bus.Ticket;
import com.dedi.finalprojectdedi.rest.APIClient;
import com.dedi.finalprojectdedi.rest.APIInterface;
import com.dedi.finalprojectdedi.utils.MySession;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketFragment extends Fragment {

    private APIInterface apiInterface;
    private RecyclerView rvListTicket;
    private RecyclerView.Adapter ticketAdapter;
    private RecyclerView.LayoutManager ticketLayoutManager;
    private View fragmentView;
    private MySession mySession;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_ticket, container, false);

        rvListTicket = fragmentView.findViewById(R.id.rvListTicket);
        ticketLayoutManager = new LinearLayoutManager(getActivity());
        rvListTicket.setLayoutManager(ticketLayoutManager);

        getTickets();

        return fragmentView;
    }

    private void getTickets() {
        mySession = new MySession(getActivity());
        if (mySession.isLoggedIn()) {
            HashMap<String, String> sUsernya = mySession.getUserDetails();
            userId = sUsernya.get(MySession.KEY_ID);
        }

        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<List<Ticket>> listCall = apiInterface.getTickets(userId);
        listCall.enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                List<Ticket> ticketList = response.body();
                ticketAdapter = new TicketListAdapter(ticketList, getContext());
                rvListTicket.setAdapter(ticketAdapter);
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
            }
        });

    }
}