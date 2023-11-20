package com.example.myapplication.todo;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.sports.database.FixtureDB;
import com.example.myapplication.sports.database.FixtureDBDao;
import com.example.myapplication.sports.database.FixtureDatabase;
import com.example.myapplication.todo.database.TodoDB;
import com.example.myapplication.todo.database.TodoDBDao;
import com.example.myapplication.todo.database.TodoDatabase;
import com.example.myapplication.todo.database.UpdateCheckStatusTask;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private ArrayList<TodoDB> todoList;


    ////할 일 목록 배열로 전달
    public TodoAdapter(ArrayList<TodoDB> todoList) {
        this.todoList = todoList;
    }

    //ViewHolder 생성 및 레이아웃 연결
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new ViewHolder(view);
    }

    //ViewHolder에 데이터 바인딩
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String task = todoList.get(position).todoText;
        holder.taskTextView.setText(task);
        holder.checkBox.setChecked(todoList.get(position).checked);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                long todoIdToUpdate =todoList.get(position).id; // 예를 들어, TodoDB에 id를 얻는 메서드가 있다고 가정
                TodoDBDao todoDao = TodoDatabase.getInstance(holder.itemView.getContext()).todoDao();

                // 체크 박스가 ON일 때
                if (isChecked) {
                    new UpdateCheckStatusTask(todoDao, true).execute(todoIdToUpdate);
                }
                // 체크 박스가 OFF일 때
                else {
                    new UpdateCheckStatusTask(todoDao, false).execute(todoIdToUpdate);
                }
            }
        });
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteTodoTask().execute();
                notifyDataSetChanged();
            }
            class DeleteTodoTask extends AsyncTask<Integer, Void, Void> {
                @Override
                protected Void doInBackground(Integer... teamIds) {
                    TodoDBDao todoDBDao = TodoDatabase.getInstance(holder.itemView.getContext()).todoDao();
                    todoDBDao.deleteFixture(todoList.get(position));
                    todoList.remove(position);
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void addItem(TodoDB data) {todoList.add(data);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTextView;
        public CheckBox checkBox;
        public ImageView removeBtn;


        public ViewHolder(View view) {//textview 초기화
            super(view);
            taskTextView = view.findViewById(R.id.editTextText);
            checkBox = view.findViewById(R.id.checkBox);
            removeBtn = view.findViewById(R.id.removeBtn);
        }
    }


}
