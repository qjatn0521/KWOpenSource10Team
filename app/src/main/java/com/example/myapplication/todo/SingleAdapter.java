package com.example.myapplication.todo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.sports.adapter.TeamAdapter;

import java.util.ArrayList;
import java.util.List;
public class SingleAdapter extends RecyclerView.Adapter<SingleAdapter.MyViewHolder>{
    private static final String TAG="SingleAdapter";

    private List<TodoInput> items=new ArrayList<>();

    public void addItem(TodoInput todo){
        items.add(todo);
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        Log.d(TAG, "onCreateViewHolder: ");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.todo_item, parent, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position){
        Log.d(TAG, "onBindViewHolder: "+position);
        TodoInput todo=items.get(position);
        holder.setItem(todo);
    }

    public int getItemCount(){
        Log.d(TAG, "getItemCount: ");
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView todo_list;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            todo_list=itemView.findViewById(R.id.todo_single);
        }
        public void setItem(TodoInput todo){
            Log.d(TAG, "MyViewHolder: ");
            todo_list.setText(todo.getTodo_input());
        }
    }
}
