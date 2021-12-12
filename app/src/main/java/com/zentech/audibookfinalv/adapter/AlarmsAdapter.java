package com.zentech.audibookfinalv.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.zentech.audibookfinalv.R;
import com.zentech.audibookfinalv.data.DatabaseHelper;
import com.zentech.audibookfinalv.model.Alarm;
import com.zentech.audibookfinalv.service.AlarmReceiver;
import com.zentech.audibookfinalv.service.LoadAlarmsService;
import com.zentech.audibookfinalv.ui.AddEditAlarmActivity;
import com.zentech.audibookfinalv.util.AlarmUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import cn.pedant.SweetAlert.SweetAlertDialog;

public final class AlarmsAdapter extends RecyclerView.Adapter<AlarmsAdapter.ViewHolder> {

    private List<Alarm> mAlarms;

    private String[] mDays;
    private int mAccentColor = -1;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context c = parent.getContext();
        final View v = LayoutInflater.from(c).inflate(R.layout.alarm_row, parent, false);

/*            Collections.sort(mAlarms, new Comparator<Alarm>() {
                @Override
                public int compare(Alarm o1, Alarm o2) {
                    if(o1.notificationId() > o2.notificationId()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });*/

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Context c = holder.itemView.getContext();

        if(mAccentColor == -1) {
            mAccentColor = ContextCompat.getColor(c, R.color.NoteColor2);
        }

        if(mDays == null){
            mDays = c.getResources().getStringArray(R.array.days_abbreviated);
        }

        final Alarm alarm = mAlarms.get(position);

        holder.time.setText(AlarmUtils.getReadableTime(alarm.getTime()));
        holder.amPm.setText(AlarmUtils.getAmPm(alarm.getTime()));
        holder.label.setText(alarm.getLabel());
        holder.days.setText(buildSelectedDays(alarm));

        if(mAlarms.isEmpty()){
            
        }

        int[] androidColors = c.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[position % androidColors.length];
///////////////////////////////////PARA RANDOM/////////////////////////////////////////////////
        //int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];

        holder.alarmView.setBackgroundTintList(ColorStateList.valueOf(randomAndroidColor));

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                new SweetAlertDialog(view.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Delete alarm?")
                        .setContentText("")
                        .setConfirmText("Delete")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog
                                        .setTitleText("Successful")
                                        .setContentText("Alarm deleted")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                AlarmReceiver.cancelReminderAlarm(view.getContext(), alarm);

                                final int rowsDeleted = DatabaseHelper.getInstance(view.getContext()).deleteAlarm(alarm);
                                int messageId;
                                if(rowsDeleted == 1) {
                                    messageId = R.string.delete_complete;
                                    Toast.makeText(view.getContext(), messageId, Toast.LENGTH_SHORT).show();
                                    LoadAlarmsService.launchLoadAlarmsService(view.getContext());

                                } else {
                                    messageId = R.string.delete_failed;
                                    Toast.makeText(view.getContext(), messageId, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .show();

                int p=holder.getLayoutPosition();
                System.out.println("LongClick: "+p);
                return true;// returning true instead of false, works for me
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context c = view.getContext();
                final Intent launchEditAlarmIntent =
                        AddEditAlarmActivity.buildAddEditAlarmActivityIntent(
                                c, AddEditAlarmActivity.EDIT_ALARM
                        );
                launchEditAlarmIntent.putExtra(AddEditAlarmActivity.ALARM_EXTRA, alarm);
                c.startActivity(launchEditAlarmIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (mAlarms == null) ? 0 : mAlarms.size();
    }

    private Spannable buildSelectedDays(Alarm alarm) {

        final int numDays = 7;
        final SparseBooleanArray days = alarm.getDays();

        final SpannableStringBuilder builder = new SpannableStringBuilder();
        ForegroundColorSpan span;

        int startIndex, endIndex;
        for (int i = 0; i < numDays; i++) {

            startIndex = builder.length();

            final String dayText = mDays[i];
            builder.append(dayText);
            builder.append(" ");

            endIndex = startIndex + dayText.length();

            final boolean isSelected = days.valueAt(i);
            if(isSelected) {
                span = new ForegroundColorSpan(mAccentColor);
                builder.setSpan(span, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return builder;
    }

    public void setAlarms(List<Alarm> alarms) {
        mAlarms = alarms;

        notifyDataSetChanged();
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout alarmView;
        final TextView time, amPm, label, days;

        ViewHolder(View itemView) {
            super(itemView);
            alarmView = itemView.findViewById(R.id.alarm_row);
            time = itemView.findViewById(R.id.ar_time);
            amPm = itemView.findViewById(R.id.ar_am_pm);
            label = itemView.findViewById(R.id.ar_label);
            days = itemView.findViewById(R.id.ar_days);

        }
    }

}