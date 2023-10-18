package com.example.myapplication.sports.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.sports.model.Team;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
        try {
            holder.setItem(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        public void setItem(Team data) throws IOException {
            tvTitle.setText(data.getName());
            URL url = new URL(data.getLogo());

            // web에서 이미지를 가져와 ImageView에 저장할 Bitmap을 만든다.
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoInput(true); // 서버로부터 응답 수신
            InputStream is;
            try {
                conn.connect(); //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
                is = conn.getInputStream(); //inputStream 값 가져오기
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            imageResource.setImageBitmap(bitmap);
        }
    }
}

