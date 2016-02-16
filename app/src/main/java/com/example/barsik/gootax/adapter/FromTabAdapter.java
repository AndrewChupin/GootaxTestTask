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


public class FromTabAdapter extends ArrayAdapter<ListItem>
{

    public FromTabAdapter(Context context, int resource, ArrayList<ListItem> items)
    {
        super(context, resource, items);
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.from_adapter_layout, parent, false);
            TextView fromTvOut = (TextView) convertView.findViewById(R.id.fromTvOut);
            fromTvOut.setText(getItem(position).getAddress());
        }
        return convertView;
    }
}
