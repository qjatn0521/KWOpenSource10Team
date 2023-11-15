package com.example.myapplication.todo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
public class SingleAdapter extends BaseAdapter{
    private static final String TAG="SingleAdapter";

    private List<String> items=new ArrayList<>();

    public void addItems(List<String> items){
        this.items=items;
    }

    @Override
    public int getCount(){
        Log.d(TAG, "getCount: ");
        return items.size();
    }

    @Override
    public Object getItem(int position){
        Log.d(TAG, "getItem: ");
        return items.get(position);
    }

    @Override
    public long getItemId(int position){
        Log.d(TAG, "getItemId: ");
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Log.d(TAG, "getView: "+position);
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View itemView=inflater.inflate(R.layout.todo_item,parent, false);
        TextView todo=itemView.findViewById(R.id.todo_single);
        todo.setText(getItem(position).toString());
        return itemView;
    }
}
