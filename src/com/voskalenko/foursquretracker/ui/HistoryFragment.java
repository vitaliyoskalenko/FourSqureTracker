/*
 * @(#)HistoryFragment.java  1.0 2013/09/21
 *
 * Copyright (C) 2013 Vitaly Oskalenko, oskalenkoVit@ukr.net
 * All rights for the program belong to the postindustria company
 * and are its intellectual property
 */

package com.voskalenko.foursquretracker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import com.googlecode.androidannotations.annotations.*;
import com.voskalenko.foursquretracker.R;
import com.voskalenko.foursquretracker.adapter.HistoryAdapter;

@EFragment(R.layout.fragment_history)
@OptionsMenu(R.menu.fragment_history_menu)
public class HistoryFragment extends BaseFragment {

    @ViewById(R.id.list_history)
    ListView listHistory;
    @Bean
    HistoryAdapter adapter;

    @OptionsItem(R.id.action_clean)
    void cleanMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.clean_checkins_history);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                getDbManager().cleanCheckInsHistory();
                adapter.notifyDataSetChanged();
            }
        }
        );

        builder.show();
    }

    @AfterViews
    void init() {
        listHistory.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                adapter.getFilter().filter(null);
                return false;
            }

        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() > 2)
                    adapter.getFilter().filter(query);
                return false;
            }
        });
    }
}
