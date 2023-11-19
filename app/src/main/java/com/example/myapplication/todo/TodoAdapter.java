package com.example.myapplication.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private ArrayList<String> todoList;


    ////할 일 목록 배열로 전달
    public TodoAdapter(ArrayList<String> todoList) {
        this.todoList = todoList;
    }

    //ViewHolder 생성 및 레이아웃 연결
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    //ViewHolder에 데이터 바인딩
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String task = todoList.get(position);
        holder.taskTextView.setText(task);
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTextView;

        public ViewHolder(View view) {//textview 초기화
            super(view);
            taskTextView = view.findViewById(android.R.id.text1);
        }
    }
}
