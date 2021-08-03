package com.dedi.finalprojectdedi.controller.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dedi.finalprojectdedi.R;
import com.dedi.finalprojectdedi.controller.publics.MainActivity;
import com.dedi.finalprojectdedi.models.user.AuthLogin;
import com.dedi.finalprojectdedi.models.user.User;
import com.dedi.finalprojectdedi.models.user.UserLogin;
import com.dedi.finalprojectdedi.rest.APIClient;
import com.dedi.finalprojectdedi.rest.APIInterface;
import com.dedi.finalprojectdedi.utils.MySession;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView btnSignup;
    private Intent intent;
    private EditText edEmail, edPassword;
    private MySession mySession;
    private APIInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        edEmail = findViewById(R.id.edUser);
        edPassword = findViewById(R.id.edPassword);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mySession = new MySession(this);

        if (mySession.isLoggedIn()){
            intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin = findViewById(R.id.bLogin);
        btnLogin.setOnClickListener(v -> {
            cekLogin();
        });

        btnSignup = findViewById(R.id.textViewRegister);
        btnSignup.setOnClickListener(v -> {
            intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void cekLogin() {
        UserLogin userLogin = new UserLogin(edEmail.getText().toString(),edPassword.getText().toString());
        Call<AuthLogin> authLoginCall = apiInterface.login(userLogin);
        authLoginCall.enqueue(new Callback<AuthLogin>() {
            @Override
            public void onResponse(Call<AuthLogin> call, Response<AuthLogin> response) {
                AuthLogin userData = response.body();
                if (userData != null){
                    User user = userData.getUser();
                    mySession.createLoginSession(user.getId(),user.getEmail(),user.getFirstName(),user.getLastName(), user.getMobileNumber(), userData.accessToken);
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(LoginActivity.this, "Selamat Datang", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Email atau Password salah", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthLogin> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "No internet Access", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }
}