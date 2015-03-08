package io.prolabs.pro.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import io.prolabs.pro.R;

public class LanguageAdapter extends BaseAdapter {
    private List<Map.Entry<String, Long>> languages;
    private Context thisContext;

    public LanguageAdapter(Context thisContext, Map<String, Long> languages) {
        List<Map.Entry<String, Long>> languageList = new ArrayList<>(languages.entrySet());
        Collections.sort(languageList, (lhs, rhs) -> (int)(rhs.getValue() - lhs.getValue()));
        this.languages = languageList;
        this.thisContext = thisContext;
    }

    @Override
    public int getCount() {
        return languages.size();
    }

    @Override
    public Object getItem(int position) {
        return languages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Map.Entry<String, Long> languageAndBytes = (Map.Entry<String, Long>) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(thisContext).inflate(R.layout.language_list_item, parent, false);
        }

        TextView name = ButterKnife.findById(convertView, R.id.name);
        TextView bytes = ButterKnife.findById(convertView, R.id.bytes);

        name.setText(languageAndBytes.getKey());
        bytes.setText(String.valueOf(languageAndBytes.getValue()));

        return convertView;
    }
}