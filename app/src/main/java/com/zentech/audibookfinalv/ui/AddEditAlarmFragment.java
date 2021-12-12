package com.zentech.audibookfinalv.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.zentech.audibookfinalv.R;
import com.zentech.audibookfinalv.data.DatabaseHelper;
import com.zentech.audibookfinalv.model.Alarm;
import com.zentech.audibookfinalv.service.AlarmReceiver;
import com.zentech.audibookfinalv.service.LoadAlarmsService;
import com.zentech.audibookfinalv.util.ViewUtils;

import java.util.Calendar;

public final class AddEditAlarmFragment extends Fragment implements IOnBackPressed {

    private TimePicker mTimePicker;
    private ImageButton action_delete, action_back, action_save;
    private EditText mLabel;
    private CheckBox mMon, mTues, mWed, mThurs, mFri, mSat, mSun;

    public static AddEditAlarmFragment newInstance(Alarm alarm) {

        Bundle args = new Bundle();
        args.putParcelable(AddEditAlarmActivity.ALARM_EXTRA, alarm);

        AddEditAlarmFragment fragment = new AddEditAlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_add_edit_alarm, container, false);
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            getContext().setTheme(R.style.AppTheme);
        }else {
            getContext().setTheme(R.style.Theme_Light);
        }
        setHasOptionsMenu(true);

        final Alarm alarm = getAlarm();

        action_delete = (ImageButton) v.findViewById(R.id.action_delete);
        action_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        action_back = (ImageButton) v.findViewById(R.id.backbttn);
        action_save = (ImageButton) v.findViewById(R.id.savebttn);


        action_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        action_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mMon.isChecked() && !mTues.isChecked() && !mWed.isChecked() && !mThurs.isChecked() && !mFri.isChecked()
                        && !mSat.isChecked() && !mSun.isChecked()){
                    alarmChecker();
                }else {
                    save();
                }
            }
        });

        final long minutes =1;
        mTimePicker = (TimePicker) v.findViewById(R.id.edit_alarm_time_picker);
        ViewUtils.setTimePickerTime(mTimePicker, alarm.getTime()+minutes*60000);

        mLabel = (EditText) v.findViewById(R.id.edit_alarm_label);
        mLabel.setText(alarm.getLabel());

        mMon = (CheckBox) v.findViewById(R.id.edit_alarm_mon);
        mTues = (CheckBox) v.findViewById(R.id.edit_alarm_tues);
        mWed = (CheckBox) v.findViewById(R.id.edit_alarm_wed);
        mThurs = (CheckBox) v.findViewById(R.id.edit_alarm_thurs);
        mFri = (CheckBox) v.findViewById(R.id.edit_alarm_fri);
        mSat = (CheckBox) v.findViewById(R.id.edit_alarm_sat);
        mSun = (CheckBox) v.findViewById(R.id.edit_alarm_sun);

        setDayCheckboxes(alarm);

        return v;
    }

    private Alarm getAlarm() {
        return getArguments().getParcelable(AddEditAlarmActivity.ALARM_EXTRA);
    }

    private void setDayCheckboxes(Alarm alarm) {
        mMon.setChecked(alarm.getDay(Alarm.MON));
        mTues.setChecked(alarm.getDay(Alarm.TUES));
        mWed.setChecked(alarm.getDay(Alarm.WED));
        mThurs.setChecked(alarm.getDay(Alarm.THURS));
        mFri.setChecked(alarm.getDay(Alarm.FRI));
        mSat.setChecked(alarm.getDay(Alarm.SAT));
        mSun.setChecked(alarm.getDay(Alarm.SUN));
    }

    private void save() {

        final Alarm alarm = getAlarm();

        final Calendar time = Calendar.getInstance();
        time.set(Calendar.MINUTE, ViewUtils.getTimePickerMinute(mTimePicker));
        time.set(Calendar.HOUR_OF_DAY, ViewUtils.getTimePickerHour(mTimePicker));
        alarm.setTime(time.getTimeInMillis());

        alarm.setLabel(mLabel.getText().toString());

        alarm.setDay(Alarm.MON, mMon.isChecked());
        alarm.setDay(Alarm.TUES, mTues.isChecked());
        alarm.setDay(Alarm.WED, mWed.isChecked());
        alarm.setDay(Alarm.THURS, mThurs.isChecked());
        alarm.setDay(Alarm.FRI, mFri.isChecked());
        alarm.setDay(Alarm.SAT, mSat.isChecked());
        alarm.setDay(Alarm.SUN, mSun.isChecked());


        final int rowsUpdated = DatabaseHelper.getInstance(getContext()).updateAlarm(alarm);
        final int messageId = (rowsUpdated == 1) ? R.string.update_complete : R.string.update_failed;

        Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();

        AlarmReceiver.setReminderAlarm(getContext(), alarm);

        getActivity().finish();

    }

    private void delete() {

        final Alarm alarm = getAlarm();

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(getContext(), R.style.DeleteAlarmDialogTheme);
        builder.setTitle(R.string.delete_dialog_title);
        builder.setMessage(R.string.delete_dialog_content);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Cancel any pending notifications for this alarm
                AlarmReceiver.cancelReminderAlarm(getContext(), alarm);

                final int rowsDeleted = DatabaseHelper.getInstance(getContext()).deleteAlarm(alarm);
                int messageId;
                if(rowsDeleted == 1) {
                    messageId = R.string.delete_complete;
                    Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                    LoadAlarmsService.launchLoadAlarmsService(getContext());
                    getActivity().finish();
                } else {
                    messageId = R.string.delete_failed;
                    Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.no, null);
        builder.show();

    }


    protected void exitByBackKey() {
        if(!mMon.isChecked() && !mTues.isChecked() && !mWed.isChecked() && !mThurs.isChecked() && !mFri.isChecked()
                && !mSat.isChecked() && !mSun.isChecked()){
            AlertDialog alertbox = new AlertDialog.Builder(getContext())
                    .setMessage("Are you sure you want to go back? Changes wont be saved")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                            final Alarm alarm = getAlarm();
                            AlarmReceiver.cancelReminderAlarm(getContext(), alarm);

                            final int rowsDeleted = DatabaseHelper.getInstance(getContext()).deleteAlarm(alarm);
                            int messageId;
                            if(rowsDeleted == 1) {
                                messageId = R.string.delete_complete;
                                //Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                                LoadAlarmsService.launchLoadAlarmsService(getContext());
                                getActivity().finish();
                            } else {
                                messageId = R.string.delete_failed;
                                //Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    })
                    .show();
        }else {
            save();
        }

    }

    protected void alarmChecker() {

        AlertDialog alertbox = new AlertDialog.Builder(getContext())
                .setMessage("Please select day for alarm")
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }
    @Override
    public boolean onBackPressed() {
            exitByBackKey();
            return true;
    }
}