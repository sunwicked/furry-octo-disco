package app.com.application.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import app.com.application.AppConstants;
import app.com.application.OnItemClickListener;
import app.com.application.R;
import app.com.application.display.DisplayActivity;
import app.com.application.network.FetchDataAPi;
import app.com.application.network.NetworkCallback;
import app.com.application.storage.Storage;
import app.com.application.utils.NetworkUtils;
import butterknife.Bind;
import butterknife.ButterKnife;

import static app.com.application.AppConstants.CATEGORY;
import static app.com.application.AppConstants.EXTRA_DATA_MODEL;
import static app.com.application.AppConstants.NETWORK_FETCH_ALL;
import static app.com.application.AppConstants.PREF_DATA_LIST;
import static app.com.application.AppConstants.PUBLISHER;

public class MainActivity extends AppCompatActivity implements NetworkCallback, OnItemClickListener<DataModel>, FilterFragment.FragmentClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rv_data)
    RecyclerView rvData;


    DataAdapter dataAdapter;
    List<DataModel> dataModels = new ArrayList<>();
    @Bind(R.id.btn_show_all)
    Button btnShowAll;
    private ArrayList<String> categoryItems = new ArrayList<>();
    private ArrayList<String> publisherItems = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private Storage storage;

    boolean showFilter = false;
    private FilterFragment bottomSheetDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(AppConstants.APP_PREFERENCE, Context.MODE_PRIVATE);
        gson = new Gson();
        storage = new Storage(sharedPreferences, gson);

        btnShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataAdapter.update(dataModels);
                btnShowAll.setVisibility(View.GONE);
            }
        });
        setUpAdapter();

        if (NetworkUtils.isNetworkAvailable(this)) {
            FetchDataAPi.getList(this, NETWORK_FETCH_ALL);
        } else {
            Snackbar.make(rvData, R.string.info_no_internet, Snackbar.LENGTH_LONG).show();
        }

    }

    private void setUpAdapter() {
        final List<DataModel> dataFromCache = storage.getDataFromLocal(AppConstants.PREF_DATA_LIST);
        if (null != dataFromCache) {
            dataModels = dataFromCache;
            prepareBottomView();
        }
        dataAdapter = new DataAdapter(this, dataModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvData.setLayoutManager(mLayoutManager);
        rvData.setItemAnimator(new DefaultItemAnimator());
        rvData.setAdapter(dataAdapter);

    }

    private void prepareBottomView() {
        prepFilters();
        bottomSheetDialogFragment = FilterFragment.newInstance(categoryItems, publisherItems);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    private void prepFilters() {
        for (DataModel dataModel : dataModels) {
            final String category = dataModel.getCategory();
            if (!categoryItems.contains(category)) {
                categoryItems.add(category);
            }

            final String publisher = dataModel.getPublisher();
            if (!publisherItems.contains(publisher)) {
                publisherItems.add(publisher);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onResponse(Object o, int requestType) {
        switch (requestType) {
            case NETWORK_FETCH_ALL:
                final List<DataModel> dataModels = (List<DataModel>) o;
                dataAdapter.update(dataModels);
//save locally
                if (bottomSheetDialogFragment == null) {
                    bottomSheetDialogFragment = FilterFragment.newInstance(categoryItems, publisherItems);
                    prepareBottomView();
                } else {
                    bottomSheetDialogFragment.updateData(categoryItems, publisherItems);
                }
                storage.saveDataToLocal(PREF_DATA_LIST, dataModels);

                break;


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            // action with ID action_filter was selected
            case R.id.action_filter:
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                break;
            // action with ID action_sort was selected
            case R.id.action_sort:
                dataAdapter.sortClicked();
                break;
        }
        return true;
    }

    @Override
    public void onFailure(Throwable t) {

        Snackbar.make(rvData, R.string.info_feed_failed, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(@NonNull DataModel item) {
        Intent nextScreenIntent = new Intent(this, DisplayActivity.class);
        nextScreenIntent.putExtra(EXTRA_DATA_MODEL, item);
        startActivity(nextScreenIntent);


    }

    @Override
    public void onItemClick(@NonNull DataModel item, int type) {

    }


    @Override
    public void onFilterItemClicked(int position, int type) {
        btnShowAll.setVisibility(View.VISIBLE);
        switch (type) {
            case PUBLISHER:
                dataAdapter.filterData(type, publisherItems.get(position));
                break;
            case CATEGORY:
                dataAdapter.filterData(type, categoryItems.get(position));
                break;
        }
    }
}
