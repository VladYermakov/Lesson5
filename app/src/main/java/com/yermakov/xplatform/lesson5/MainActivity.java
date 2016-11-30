package com.yermakov.xplatform.lesson5;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    ArrayList<String> list = new ArrayList<>();
    ArrayList<Long> ids = new ArrayList<>();
    ListEntityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        reloadList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                startActivity(intent);
            }
        });

        final ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                ListSQLiteOpenHelper.getInstance().delete(
                        ListSQLiteOpenHelper.getInstance().getWritableDatabase(), ids.get(position));

                adapter.remove(position);
                adapter.notifyDataSetChanged();

                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        reloadList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.search_item);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                reloadList();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ListView listView = (ListView) findViewById(R.id.list_view);

                Pair<ArrayList<String>, ArrayList<Long>> pp = ListSQLiteOpenHelper.getInstance()
                        .select(ListSQLiteOpenHelper.getInstance().getWritableDatabase(), query);

                list = pp.first;
                ids = pp.second;

                adapter = new ListEntityAdapter(MainActivity.this, list, ids);

                listView.setAdapter(adapter);

                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                onQueryTextSubmit(s);
                return false;
            }
        });
        return true;
    }

    private void reloadList() {
        ListView listView = (ListView) findViewById(R.id.list_view);

        Pair<ArrayList<String>, ArrayList<Long>> pp = ListSQLiteOpenHelper.getInstance().select_all(
                ListSQLiteOpenHelper.getInstance().getWritableDatabase());

        list = pp.first;
        ids = pp.second;
        adapter = new ListEntityAdapter(this, list, ids);

        listView.setAdapter(adapter);
    }
}
