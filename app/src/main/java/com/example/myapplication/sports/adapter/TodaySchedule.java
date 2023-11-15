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
import com.example.myapplication.sports.database.FixtureDB;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TodaySchedule  extends RecyclerView.Adapter<TodaySchedule.MyViewHolder> {

    //리스트는 무조건 데이터를 필요로함
    private List<FixtureDB> items=new ArrayList<>();
    public void addItem(FixtureDB fixture){
        items.add(fixture);
    }

    // Constructor to accept the ViewModel
    public TodaySchedule() {

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.sports_schedule,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FixtureDB data=items.get(position);
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
        private TextView tvTitle1;
        private ImageView imageResource1;
        private TextView tvTitle2;
        private ImageView imageResource2;
        private Bitmap bitmap1;
        private Bitmap bitmap2;
        private TextView tvTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //규칙2
            tvTitle1=itemView.findViewById(R.id.tv_title1);
            imageResource1=itemView.findViewById(R.id.iv_img_resource1);
            tvTitle2=itemView.findViewById(R.id.tv_title2);
            imageResource2=itemView.findViewById(R.id.iv_img_resource2);
            tvTime = itemView.findViewById(R.id.time_tv);
        }
        public void setItem(FixtureDB data) throws IOException {
            tvTitle1.setText(data.homeTeamName);
            tvTitle2.setText(data.awayTeamName);
            tvTime.setText(data.timeString);
            URL url1 = new URL(data.homeTeamlogo);
            URL url2 = new URL(data.awayTeamlogo);
            Thread uThread = new Thread() {
                @Override
                public void run() {
                    try{
                        // web에서 이미지를 가져와 ImageView에 저장할 Bitmap을 만든다.
                        HttpURLConnection conn = (HttpURLConnection)url1.openConnection();

                        conn.setDoInput(true); // 서버로부터 응답 수신
                        InputStream is= conn.getInputStream();;
                        conn.connect();
                        bitmap1 = BitmapFactory.decodeStream(is);

                        conn = (HttpURLConnection)url2.openConnection();

                        conn.setDoInput(true); // 서버로부터 응답 수신
                        is= conn.getInputStream();;
                        conn.connect();
                        bitmap2 = BitmapFactory.decodeStream(is);

                    }catch (MalformedURLException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            };
            uThread.start(); // 작업 Thread 실행

            try{
                //메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야 한다.
                //join() 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리도록 한다.
                //join() 메서드는 InterruptedException을 발생시킨다.
                uThread.join();

                //작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
                //UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지 지정
                imageResource1.setImageBitmap(bitmap1);
                imageResource2.setImageBitmap(bitmap2);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}


