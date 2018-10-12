package com.example.harpalsingh.fabgallery.activities;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.harpalsingh.fabgallery.R;
import com.example.harpalsingh.fabgallery.adapters.GridViewAdapter;
import com.example.harpalsingh.fabgallery.models.AllData;
import com.example.harpalsingh.fabgallery.utilities.NetworkStateChangeReceiver;
import com.example.harpalsingh.fabgallery.utilities.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity   {
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.grid_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.loading_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.main_layout)
    View parentView;
    @BindView(R.id.main_content_container)
    RelativeLayout mainContentLayout;
    private BroadcastReceiver networkBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        networkBroadcast = new NetworkStateChangeReceiver(parentView);

        Utilities.setupToolbarAndNavigationBar(this, toolbar, navigationView, drawerLayout);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        final LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setClipToPadding(false);
        GridViewAdapter gridAdapter = new GridViewAdapter(this, AllData.getInstance().getPhotoData());
        recyclerView.setAdapter(gridAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Utilities.registerNetworkStateChangerReciever(this, networkBroadcast);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkBroadcast);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}