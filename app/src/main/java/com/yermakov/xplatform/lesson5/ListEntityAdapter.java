package com.yermakov.xplatform.lesson5;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

class ListEntityAdapter extends BaseAdapter {

    private List<String> textList;
    private List<Long> idList;
    private LayoutInflater listInflater;

    Pair<String, Long> remove(int index) {
        return Pair.create(textList.remove(index), idList.remove(index));
    }

    ListEntityAdapter(Context c, List<String> texts, List<Long> ids) {
        textList = texts;
        idList = ids;
        listInflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return textList.size();
    }

    @Override
    public Object getItem(int position) {
        return textList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return idList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout = (LinearLayout) convertView;
        if (layout == null) {
            layout = (LinearLayout) listInflater.inflate(R.layout.list_item_layout, parent, false);
        }

        TextView view = (TextView) layout.findViewById(R.id.listViewItem);
        view.setText(getItem(position).toString());

        return layout;
    }
}
