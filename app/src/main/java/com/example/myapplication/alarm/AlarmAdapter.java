package com.example.myapplication.alarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.List;

public class AlarmAdapter extends BaseAdapter {

    private List<String> alarms;
    private Context context;

    public AlarmAdapter(Context context, List<String> alarms) {
        this.context = context;
        this.alarms = alarms;
    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public Object getItem(int position) {
        return alarms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.alarm_item, parent, false);
            holder = new ViewHolder();
            holder.ampmTextView = convertView.findViewById(R.id.ampm);
            holder.timeTextView = convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 해당 위치의 알람을 가져와서 표시
        String alarm[] = alarms.get(position).split(" ");

        // ampm과 time을 가져와서 TextView에 설정
        holder.ampmTextView.setText(alarm[0]);
        holder.timeTextView.setText(alarm[1]);

        // 추가적인 레이아웃 설정이 필요하다면 여기에 추가

        return convertView;
    }

    // ViewHolder 패턴 사용
    private static class ViewHolder {
        TextView ampmTextView;
        TextView timeTextView;
    }
}
