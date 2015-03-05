package io.prolabs.pro.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import io.prolabs.pro.R;
import io.prolabs.pro.models.github.Language;

public class LanguageListAdapter extends ArrayAdapter<Language> {
    public LanguageListAdapter(Context context, ArrayList<Language> languages) {
        super(context, 0, languages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Language language = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.language_list_item, parent, false);
        }

        TextView name = ButterKnife.findById(convertView, R.id.name);
        TextView bytes = ButterKnife.findById(convertView, R.id.bytes);

        name.setText(language.getName());
        bytes.setText(language.getBytes());

        return convertView;
    }
}