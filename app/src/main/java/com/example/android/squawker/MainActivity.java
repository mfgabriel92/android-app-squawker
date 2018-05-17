package com.example.android.squawker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.squawker.following.FollowingPreferenceActivity;
import com.example.android.squawker.provider.SquawkerContract;
import com.example.android.squawker.provider.SquawkerProvider;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID_MESSAGES = 0;
    private RecyclerView mRvMainActivity;
    private LinearLayoutManager mLayoutManager;
    private SquawkerAdapter mAdapter;

    static final String[] MESSAGES_PROJECTION = {
        SquawkerContract.COLUMN_AUTHOR,
        SquawkerContract.COLUMN_MESSAGE,
        SquawkerContract.COLUMN_DATE,
        SquawkerContract.COLUMN_AUTHOR_KEY,
    };
    static final int COL_NUM_AUTHOR = 0;
    static final int COL_NUM_MESSAGE = 1;
    static final int COL_NUM_DATE = 2;
    static final int COL_NUM_AUTHOR_KEY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRvMainActivity = findViewById(R.id.rvMainActivity);
        mAdapter = new SquawkerAdapter();
        mLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration divider = new DividerItemDecoration(mRvMainActivity.getContext(), mLayoutManager.getOrientation());

        mRvMainActivity.setLayoutManager(mLayoutManager);
        mRvMainActivity.addItemDecoration(divider);
        mRvMainActivity.setHasFixedSize(true);
        mRvMainActivity.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(LOADER_ID_MESSAGES, null, this);

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("D/MainActivity", getString(R.string.message_token_format, token));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionFollowingPreferences:
                Intent intent = new Intent(this, FollowingPreferenceActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        SharedPreferences following = PreferenceManager.getDefaultSharedPreferences(this);

        return new CursorLoader(
            this,
            SquawkerProvider.Squawker.CONTENT_URI,
            MESSAGES_PROJECTION,
            SquawkerContract.createSelectionForCurrentFollowing(following),
            null,
            SquawkerContract.COLUMN_DATE + " DESC"
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
