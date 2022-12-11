package com.example.aip.register;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.aip.R;
import com.example.aip.register.RecentNames;

import java.util.List;

public class RecentAdapter extends ArrayAdapter<RecentNames> {

    Context recentContext;
    int resource;
    List<RecentNames> recentNames;

    public RecentAdapter(Context recentContext, int resource, List<RecentNames> recentNames) {
        super(recentContext, resource, recentNames);
        this.recentContext = recentContext;
        this.resource = resource;
        this.recentNames = recentNames;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(recentContext);

        View view = inflater.inflate(R.layout.recent_adds, null);

        TextView nameView = view.findViewById(R.id.textView7);

        RecentNames name = recentNames.get(position);
        nameView.setText(name.getName());

        return view;
    }
}
