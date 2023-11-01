package com.example.myapplication.sports.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.sports.TeamViewModel;
import com.example.myapplication.sports.database.FixtureDB;
import com.example.myapplication.sports.database.FixtureDBDao;
import com.example.myapplication.sports.database.FixtureDatabase;
import com.example.myapplication.sports.model.Fixture;
import com.example.myapplication.sports.model.Team;
import com.example.myapplication.sports.noti.AlarmRecevier;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.TimeZone;

public class TeamAdapter  extends RecyclerView.Adapter<TeamAdapter.MyViewHolder> {

    //리스트는 무조건 데이터를 필요로함
    private List<Team> items=new ArrayList<>();
    public void addItem(Team team){
        items.add(team);
    }
    //껍데기만 만듬. 1번 실행
    public TeamViewModel viewModel; // Replace ViewModelType with the actual type of your ViewModel

    // Constructor to accept the ViewModel
    public TeamAdapter(TeamViewModel viewModel) {
        this.viewModel = viewModel;
    }
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
            holder.setItem(data,viewModel);
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
        private Bitmap bitmap;
        private Switch switchButton;
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat originFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX",new Locale("ko", "KR"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //규칙2
            tvTitle=itemView.findViewById(R.id.tv_title);
            imageResource=itemView.findViewById(R.id.iv_img_resource);
            switchButton = itemView.findViewById(R.id.switch1);
        }
        public void setItem(Team data,TeamViewModel viewModel) throws IOException {
            tvTitle.setText(data.getName());
            if(data.getSub()) switchButton.setChecked(true);
            //Log.d("sub!",data.getSub()+"");

            switchButton.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        viewModel.getAllFixtureOfTeam(data.getTeamId());
                        viewModel.getFixtureList().observe((LifecycleOwner) itemView.getContext(), new Observer<List<Fixture>>() {
                            @Override
                            public void onChanged(List<Fixture> fixtures) {
                                if(fixtures!=null) {

                                    TimeZone seoulTimeZone = TimeZone.getTimeZone("Asia/Seoul");
                                    ArrayList<FixtureDB> tmp = new ArrayList<>();
                                    for(Fixture f : fixtures) {
                                        try {
                                            // 일자 문자열을 파싱
                                            Date date = originFormat.parse(f.getEventDate());
                                            date.setTime(date.getTime() + seoulTimeZone.getRawOffset() + seoulTimeZone.getDSTSavings());
                                            // 현재 날짜와 비교
                                            if (date.after(currentDate)) {
                                                String fixtureInfo = f.getFixtureInfoAsString();
                                                //Log.d("fix",data.getTeamId()+"");
                                                FixtureDatabase database = FixtureDatabase.getInstance(itemView.getContext());
                                                FixtureDBDao fixtureDao = database.fixtureDao();

                                                String datePart = dateFormat.format(date); // "2023-10-24"
                                                String timePart = timeFormat.format(date); // "04:00"

                                                FixtureDB fixture = new FixtureDB();
                                                fixture.date = originFormat.format(date);
                                                fixture.dateString = datePart;
                                                fixture.timeString = timePart;
                                                fixture.teamId = data.getTeamId();
                                                fixture.awayTeamName = f.getAwayTeam().getTeamName();
                                                fixture.homeTeamName = f.getHomeTeam().getTeamName();
                                                tmp.add(fixture);
                                                //fixtureDao.insertFixture(fixture);

                                            }
                                        } catch (ParseException e) {
                                            // 날짜 파싱 오류 처리
                                            e.printStackTrace();
                                        }
                                    }
                                    new InsertFixtureTask().execute(tmp);
                                }

                            }

                        });
                    } else {
                        new DeleteFixtureTask().execute(data.getTeamId());
                    }
                }
                class InsertFixtureTask extends AsyncTask<List<FixtureDB>, Void, Void> {
                    @Override
                    protected Void doInBackground(List<FixtureDB>... fixtureList) {
                        if (fixtureList != null) {
                            List<FixtureDB> fixtures = fixtureList[0];
                            FixtureDBDao fixtureDao = FixtureDatabase.getInstance(itemView.getContext()).fixtureDao();
                            // 데이터베이스에 대량 삽입
                            fixtureDao.insertFixtures(fixtures);
                        }
                        FixtureDB earliestFixture = getFixtureFromDatabase(itemView.getContext(), originFormat.format(currentDate));
                        // 다음 알람 시간을 계산
                        long nextAlarmTimeMillis = calculateNextAlarmTime(earliestFixture);

                        //Toast.makeText(itemView.getContext(), "데이터가 추가되었습니다.", Toast.LENGTH_SHORT).show();
                        setNextAlarm(itemView.getContext(), nextAlarmTimeMillis);
                        return null;
                    }
                }
                class DeleteFixtureTask extends AsyncTask<Integer, Void, Void> {
                    @Override
                    protected Void doInBackground(Integer... teamIds) {
                        FixtureDBDao fixtureDao = FixtureDatabase.getInstance(itemView.getContext()).fixtureDao();
                        fixtureDao.deleteFixturesByTeamId(teamIds[0]);
                        //Toast.makeText(itemView.getContext(), "데이터가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                }
            });
            URL url = new URL(data.getLogo());

            Thread uThread = new Thread() {
                @Override
                public void run() {
                    try{
                        // web에서 이미지를 가져와 ImageView에 저장할 Bitmap을 만든다.
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setDoInput(true); // 서버로부터 응답 수신
                        InputStream is= conn.getInputStream();;
                        conn.connect();
                        bitmap = BitmapFactory.decodeStream(is);

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
                imageResource.setImageBitmap(bitmap);
            }catch (InterruptedException e){
                e.printStackTrace();
            }



        }
        private long calculateNextAlarmTime(FixtureDB fixture) {
            // FixtureDB의 date를 사용하여 다음 알람 시간을 계산
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            Date date = null;

            try {
                date = dateFormat.parse(fixture.date);
                Log.d("DateParsing", "Parsed date: " + date.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                //Log.d("succe!","suc");
                return calendar.getTimeInMillis(); // 다음 알람 시간을 밀리초로 반환
            } else {
                // 파싱에 실패하면 기본값을 반환 또는 오류 처리
                //Log.d("errorCal","errr");
                return 0;
            }
        }
        private void setNextAlarm(Context context, long nextAlarmTimeMillis) {
            AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmRecevier.class);

            // PendingIntent를 생성하여 알람 리시버를 호출
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // 다음 알람을 설정 (다음 알람 시간은 nextAlarmTimeMillis에 의해 결정됨)
            am.set(AlarmManager.RTC_WAKEUP, nextAlarmTimeMillis, pendingIntent);
        }
        private FixtureDB getFixtureFromDatabase(Context context, String currentDate) {
            FixtureDBDao yourDatabase = FixtureDatabase.getInstance(context).fixtureDao();
            return yourDatabase.getEarliestFixture(currentDate);
        }
    }
}


