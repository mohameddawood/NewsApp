package com.dawood.newsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import com.dawood.newsapp.network.ConnectivityReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    @BindView(R.id.netLine) LinearLayout netLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container);
        ButterKnife.bind(this);
        NavController navController = Navigation.findNavController(this, R.id.myNavHostFramgent);
        NavigationUI.setupActionBarWithNavController(this, navController);
        getSupportActionBar().setTitle("News App");
    }

    @Override public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.myNavHostFramgent);
        return navController.navigateUp();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


    @Override protected void onResume() {
        super.onResume();
        NewApp.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            netLine.setVisibility(View.GONE);
            recreate();
        } else {
            netLine.setVisibility(View.VISIBLE);
        }
    }
}
