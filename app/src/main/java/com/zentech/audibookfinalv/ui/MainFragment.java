package com.zentech.audibookfinalv.ui;

import static com.zentech.audibookfinalv.ui.AddEditAlarmActivity.ADD_ALARM;
import static com.zentech.audibookfinalv.ui.AddEditAlarmActivity.buildAddEditAlarmActivityIntent;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zentech.audibookfinalv.R;
import com.zentech.audibookfinalv.adapter.AlarmsAdapter;
import com.zentech.audibookfinalv.model.Alarm;
import com.zentech.audibookfinalv.service.LoadAlarmsReceiver;
import com.zentech.audibookfinalv.service.LoadAlarmsService;
import com.zentech.audibookfinalv.util.AlarmUtils;
import com.zentech.audibookfinalv.view.DividerItemDecoration;
import com.zentech.audibookfinalv.view.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class MainFragment extends Fragment
        implements LoadAlarmsReceiver.OnAlarmsLoadedListener, AdapterView.OnItemSelectedListener {

    private List<Alarm> mAlarms;
    private LoadAlarmsReceiver mReceiver;
    private AlarmsAdapter mAdapter;
    Spinner spinner;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReceiver = new LoadAlarmsReceiver(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_main, container, false);

        spinner = v.findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Sort, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        final EmptyRecyclerView rv = v.findViewById(R.id.recycler);
        mAdapter = new AlarmsAdapter();
        rv.setEmptyView(v.findViewById(R.id.empty_view));

        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new DividerItemDecoration(getContext()));
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());

        final FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> {
            AlarmUtils.checkAlarmPermissions(getActivity());
            final Intent i = buildAddEditAlarmActivityIntent(getContext(), ADD_ALARM);
            startActivity(i);
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter(LoadAlarmsService.ACTION_COMPLETE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, filter);
        LoadAlarmsService.launchLoadAlarmsService(getContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    @Override
    public void onAlarmsLoaded(ArrayList<Alarm> alarms) {
        mAlarms = alarms;
        int value2 = 0;
        final SharedPreferences sharedPreferencesSpinner = getContext().getSharedPreferences("defaultSpinner", 0);
        value2 = sharedPreferencesSpinner.getInt("defaultSpinner", value2);
        spinner.setSelection(value2);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Object item = parentView.getItemAtPosition(position);
                if(position==0){
                    sharedPreferencesSpinner.edit().putInt("defaultSpinner",0).apply();
                    sortOldest();
                    mAdapter.setAlarms(alarms);
                }else{
                    sharedPreferencesSpinner.edit().putInt("defaultSpinner",1).apply();
                    sortNewest();
                    mAdapter.setAlarms(alarms);
                }
                System.out.println(item.toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sortOldest();
        mAdapter.setAlarms(alarms);
    }

    private void sortNewest(){
        Collections.sort(mAlarms, new Comparator<Alarm>() {
            @Override
            public int compare(Alarm o1, Alarm o2) {
                if(o1.notificationId() > o2.notificationId()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }
    private void sortOldest(){
        Collections.sort(mAlarms, new Comparator<Alarm>() {
            @Override
            public int compare(Alarm o1, Alarm o2) {
                if(o1.notificationId() > o2.notificationId()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            spinner.getBackground().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parentView) {
        // Your code here
    }

}

