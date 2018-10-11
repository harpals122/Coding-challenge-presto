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
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.harpalsingh.fabgallery.R;
import com.example.harpalsingh.fabgallery.adapters.GridViewAdapter;
import com.example.harpalsingh.fabgallery.genericEventBus.GenericEventBus;
import com.example.harpalsingh.fabgallery.models.Photos;
import com.example.harpalsingh.fabgallery.utilities.NetworkStateChangeReceiver;
import com.example.harpalsingh.fabgallery.utilities.Utilities;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity   {

    @BindView(R.id.app_drawer_layout)
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
    @BindView(R.id.retry_parent_view)
    LinearLayout errorLayout;
    @BindView(R.id.retry)
    Button retry;
    private BroadcastReceiver networkBroadcast;
    private GridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        networkBroadcast = new NetworkStateChangeReceiver(parentView);

        Utilities.setupToolbarAndNavigationBar(this, toolbar, navigationView, drawerLayout);

        Utilities.doNetworkRequest(MainActivity.this, parentView);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        final LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext());
      
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setClipToPadding(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Utilities.registerNetworkStateChangerReciever(this, networkBroadcast);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkBroadcast);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GenericEventBus event) {
        Photos photos = event.getPhotoData().getPhotos();
        gridAdapter = new GridViewAdapter(this,photos);
        recyclerView.setAdapter(gridAdapter);
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

    @OnClick(R.id.retry)
    public void retry(View view) {
        Utilities.doNetworkRequest(MainActivity.this, parentView);
    }
}