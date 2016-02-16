package com.example.barsik.gootax.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.barsik.gootax.ListItem;
import com.example.barsik.gootax.R;

import java.util.ArrayList;


public class ToTabAdapter extends ArrayAdapter<ListItem>
{

    public ToTabAdapter(Context context, int resource, ArrayList<ListItem> items)
    {
        super(context, resource, items);
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.to_adapter_layout, parent, false);
            TextView toTvOut = (TextView) convertView.findViewById(R.id.toTvOut);
            toTvOut.setText(getItem(position).getAddress());
        }
        return convertView;
    }
}
