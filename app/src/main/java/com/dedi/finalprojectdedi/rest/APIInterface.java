package com.dedi.finalprojectdedi.rest;

import com.dedi.finalprojectdedi.models.bus.Stop;
import com.dedi.finalprojectdedi.models.bus.Ticket;
import com.dedi.finalprojectdedi.models.bus.TicketReservation;
import com.dedi.finalprojectdedi.models.bus.TripSchedule;
import com.dedi.finalprojectdedi.models.user.AuthLogin;
import com.dedi.finalprojectdedi.models.user.User;
import com.dedi.finalprojectdedi.models.user.UserLogin;
import com.dedi.finalprojectdedi.models.user.UserRegister;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {
    @POST("auth/register")
    Call<User> createUser(@Body UserRegister register);

    @POST("auth/login")
    Call<AuthLogin> login(@Body UserLogin userLogin);

    @GET("stop")
    Call<List<Stop>> getStops();

    @GET("ticket")
    Call<List<Ticket>> getTickets(@Query("passengerId") String id);

    @POST("ticket")
    Call<Ticket> createTicket(@Body TicketReservation ticketReservation);

    @GET("tripSchedule")
    Call<List<TripSchedule>> getTripSchedules();

    @GET("tripSchedule")
    Call<List<TripSchedule>> getTripSchedulesParam(@Query("destStopId") Integer destStopId, @Query("from") String from, @Query("sourceStopId") Integer sourceStopId, @Query("to") String to);

    @GET("tripSchedule/{id}")
    Call<TripSchedule> getTripSchedule(@Path("id") String id);
}
