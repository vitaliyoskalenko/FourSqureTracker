package com.voskalenko.foursquretracker.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.voskalenko.foursquretracker.database.DBManager;
import com.voskalenko.foursquretracker.model.CheckInsHistory;
import com.voskalenko.foursquretracker.views.CheckInsHistoryView;
import com.voskalenko.foursquretracker.views.CheckInsHistoryView_;

import java.util.List;

@EBean
public class HistoryAdapter extends BaseAdapter implements Filterable {

    @Bean
    DBManager dbManager;
    @RootContext
    Context context;

    private List<CheckInsHistory> historyList;
    private final Filter filter = new Filter() {

    @AfterInject
    void init() {
        adapterRefresh();
        historyList = dbManager.getCheckInsHistory(null);
    }


        @Override
        protected FilterResults performFiltering(CharSequence condition) {
            FilterResults results = new FilterResults();
            List<CheckInsHistory> checkInsHistories;
            if (!TextUtils.isEmpty(condition)) {
                checkInsHistories = dbManager.getCheckInsHistory((String) condition);
            } else {
                checkInsHistories = dbManager.getCheckInsHistory(null);
            }

            results.values = checkInsHistories;
            results.count = checkInsHistories.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence condition, FilterResults filterResults) {
            if (filterResults != null) {
                historyList = (List<CheckInsHistory>)filterResults.values;
                notifyDataSetChanged();
            }
        }
    };


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        adapterRefresh();
    }

    private void adapterRefresh() {
        historyList = dbManager.getCheckInsHistory(null);
    }


    @Override
    public int getCount() {
        return historyList == null ? 0 : historyList.size();
    }

    @Override
    public Object getItem(int position) {
        return historyList == null ? null : historyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = CheckInsHistoryView_.build(context);
        }

        ((CheckInsHistoryView)convertView).setData(historyList.get(position));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
}

