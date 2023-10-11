package com.example.myapplication.sports.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.sports.model.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamAdapter  extends RecyclerView.Adapter<TeamAdapter.MyViewHolder> {

    //리스트는 무조건 데이터를 필요로함
    private List<Team> items=new ArrayList<>();
    public void addItem(Team team){
        items.add(team);
    }
    //껍데기만 만듬. 1번 실행
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.sports_team_item,parent,false);
        return new MyViewHolder(view);
    }

    //껍데기에 데이터 바인딩. 2번 실행
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Team data=items.get(position);
        holder.setItem(data);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //ViewHolder : 뷰들의 책꽂이
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView imageResource;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //규칙2
            tvTitle=itemView.findViewById(R.id.tv_title);
            imageResource=itemView.findViewById(R.id.iv_img_resource);
        }
        public void setItem(Team data){
            tvTitle.setText(data.getName());
            //imageResource.setImageResource(data.getLogo());
        }
    }
}

