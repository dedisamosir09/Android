package com.dedi.finalprojectdedi.controller.publics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dedi.finalprojectdedi.R;
import com.dedi.finalprojectdedi.utils.MySession;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private MySession mySession;
    private String firstName, lastName, email, mobileNumber;
    private TextView txtName, txtEmail, txtFirstName, txtLastName, txtEmail2, txtMobileNumber;
    private Button btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);

        mySession = new MySession(getActivity());

        if (mySession.isLoggedIn()) {
            //Get Session
            HashMap<String, String> userSession = mySession.getUserDetails();
            firstName = userSession.get(MySession.KEY_FIRST_NAME);
            lastName = userSession.get(MySession.KEY_LAST_NAME);
            mobileNumber = userSession.get(MySession.KEY_MOBILE_NUMBER);
            email = userSession.get(MySession.KEY_EMAIL);
        }

        btnLogout = fragmentView.findViewById(R.id.btnLogout);
        txtName = fragmentView.findViewById(R.id.txtName);
        txtEmail = fragmentView.findViewById(R.id.txtEmail);
        txtEmail2 = fragmentView.findViewById(R.id.txtEmail2);
        txtMobileNumber = fragmentView.findViewById(R.id.txtMobileNumber);


        txtName.setText(firstName + " " + lastName);
        txtEmail.setText(email);
        txtEmail2.setText(email);
        txtMobileNumber.setText(mobileNumber);

        btnLogout.setOnClickListener(v -> {
            ((MainActivity) getActivity()).logout();
        });

        return fragmentView;
    }
}