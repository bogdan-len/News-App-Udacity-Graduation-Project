package com.example.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Article>>, PageSelectorDialog.PageSelectorDialogListener, SearchDialog.SearchDialogListener {

    private static final String LOG_TAG = NewsActivity.class.getName();

    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?&api-key=test";

    private static final int ARTICLE_LOADER_ID = 1;

    private Button btnCurrentPage;
    private ImageButton btnNextPage;
    private ImageButton btnPrevPage;

    private ArticleAdapter mAdapter;

    private TextView mEmptyStateTextView;

    private int pageInt = 1;
    private String topicString = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));


        btnCurrentPage = (Button) findViewById(R.id.btn_current_page);
        btnNextPage = (ImageButton) findViewById(R.id.btn_next_page);
        btnPrevPage = (ImageButton) findViewById(R.id.btn_previuos_page);

        btnPrevPage.setVisibility(View.GONE);

        ListView articleListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        articleListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        articleListView.setAdapter(mAdapter);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Article currentArticle = mAdapter.getItem(position);

                Uri articleUri = Uri.parse(currentArticle.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(getString(R.string.no_internet_connection));
        }


        btnCurrentPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPageSelectorDialog();
            }
        });

        btnCurrentPage.setText(String.valueOf(pageInt));
        btnNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageInt = pageInt + 1;
                restartLoader();
            }
        });

        btnPrevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageInt = pageInt - 1;
                restartLoader();
            }
        });
    }

    public void restartLoader() {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);
        getLoaderManager().restartLoader(ARTICLE_LOADER_ID, null, this);
        btnCurrentPage.setText(String.valueOf(pageInt));
        if (pageInt > 1) {
            btnPrevPage.setVisibility(View.VISIBLE);
        } else {
            btnPrevPage.setVisibility(View.GONE);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {

        String orderBy = "newest";
        String pageString = Integer.toString(pageInt);

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", topicString);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("page", pageString);

        return new ArticlesLoader(this, uriBuilder.toString());


    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(getString(R.string.no_articles));

        mAdapter.clear();

        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_by_topic) {
            openSearchTopicDialog();
            return true;
        }
        if (id == R.id.reset_search) {
            topicString = "all";
            restartLoader();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void applyPageNumber(String pageNumber) {
        pageInt = Integer.parseInt(pageNumber);
        restartLoader();
    }

    private void openPageSelectorDialog() {
        PageSelectorDialog pageSelectorDialog = new PageSelectorDialog();
        pageSelectorDialog.show(getSupportFragmentManager(), "Page select");
    }

    @Override
    public void applySearchInput(String userSearchInput) {
        topicString = userSearchInput;
        restartLoader();
    }

    private void openSearchTopicDialog(){
        SearchDialog searchDialog = new SearchDialog();
        searchDialog.show(getSupportFragmentManager(),"Search by topic");
    }
}
