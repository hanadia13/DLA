package com.astra.polytechnic.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astra.polytechnic.api.ApiUtils;
import com.astra.polytechnic.dao.UserDao;
import com.astra.polytechnic.model.LoginModel;
import com.astra.polytechnic.model.msuser;
import com.astra.polytechnic.model.response.AddResponse;
import com.astra.polytechnic.model.response.LoginResponse;
import com.astra.polytechnic.service.msuserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class msuserRepo {
    private static final String TAG = "UserRepository";

    private static msuserRepo INSTANCE;

    private msuserService mmsuserService;
    private UserDao mDetailViewModelMsuser;
    private msuserRepo(Context context) {
        mmsuserService = ApiUtils.getMemberService();
    }

    public static void initialize(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new msuserRepo(context);
        }
    }

    public static msuserRepo get() {
        return INSTANCE;
    }

    public MutableLiveData<List<msuser>> getUsers() {
        MutableLiveData<List<msuser>> users = new MutableLiveData<>();

        Call<List<msuser>> call = mmsuserService.getUsers();
        call.enqueue(new Callback<List<msuser>>() {
            @Override
            public void onResponse(Call<List<msuser>> call, Response<List<msuser>> response) {
                if (response.isSuccessful()) {
                    users.setValue(response.body());
                    Log.d(TAG, "getmembers.onResponse() called");
                }
            }

            @Override
            public void onFailure(Call<List<msuser>> call, Throwable t) {
                Log.e("Error API cal : ", t.getMessage());

            }
        });

        return users;
    }
    public MutableLiveData<msuser> getUser(String userId) {
        MutableLiveData<msuser> user = new MutableLiveData<>();

        Call<msuser> call = mmsuserService.getUserById(userId);
        call.enqueue(new Callback<msuser>() {
            @Override
            public void onResponse(Call<msuser> call, Response<msuser> response) {
                if (response.isSuccessful()) {
                    user.setValue(response.body());
                    Log.d(TAG, "getUserById.onResponse() called");
                }
            }

            @Override
            public void onFailure(Call<msuser> call, Throwable t) {
                Log.e("Error API call : ", t.getMessage());
            }
        });

        return user;
    }

    public LiveData<LoginModel> getUserLogin(){
        return mDetailViewModelMsuser.getUserLogin();
    }

    public LiveData<AddResponse> addUser(msuser user) {
        MutableLiveData<AddResponse> loginResponseMutableLiveData = new MutableLiveData<>();

        Log.d(TAG, "addUser: Called");
        Call<AddResponse> call = mmsuserService.addUser(user);
        call.enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                Log.d(TAG, "onResponse: " + response.body());
                loginResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AddResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

        return loginResponseMutableLiveData;
    }

    public LiveData<LoginResponse> Login(String nim, String password){
        MutableLiveData <LoginResponse> mUserLogin = new MutableLiveData<>();

        Log.i(TAG, "Login() called");
        Call <LoginResponse> call =mmsuserService.Login(nim,password);
        call.enqueue(new Callback <LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                String perusahaan = user.getUsr_perusahaan();
                LoginResponse loginResponse= response.body();
                Log.d(TAG, "onResponse: " + response.body());
                mUserLogin.setValue(loginResponse);
                //mDetailViewModelMsuser.setUserLogin(loginResponse.getUser());
            }

            @Override
            public void onFailure(Call<LoginResponse>call, Throwable t) {
                Log.e("Error API call : ", t.getMessage());
            }
        });
            return mUserLogin;
    }
}
