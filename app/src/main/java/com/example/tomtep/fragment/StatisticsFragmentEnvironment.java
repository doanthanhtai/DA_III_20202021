package com.example.tomtep.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tomtep.MainActivity;
import com.example.tomtep.R;
import com.example.tomtep.model.EnvironmentHistory;
import com.example.tomtep.model.Lake;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragmentEnvironment extends Fragment {

    private Lake lake;
    private List<Lake> lakes;
    private List<EnvironmentHistory> environmentHistories;
    private ChildEventListener childEventListenerEnvironment;
    private Spinner sprLake;
    private LineChart lcPH, lcOxy, lcDoMan;

    LineDataSet lineDataSetPH, lineDataSetOxy, lineDataSetDoMan;
    LineData lineDataPH, lineDataOxy, lineDataDoMan;
    ArrayList<Entry> lineEntriesPH, lineEntriesOxy, lineEntriesDoMan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics_environment, container, false);
        environmentHistories = new ArrayList<>();
        lakes = new ArrayList<>();
        lake = new Lake();
        intitChildEventListener();
        initView(view);
        addChildEventListener();
        pinData();
        return view;
    }

    private void intitChildEventListener() {
        childEventListenerEnvironment = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                EnvironmentHistory environmentHistory = snapshot.getValue(EnvironmentHistory.class);
                if (environmentHistory == null) return;
                environmentHistories.add(environmentHistory);
                lcPH.notifyDataSetChanged();
                lcDoMan.notifyDataSetChanged();
                lcOxy.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                EnvironmentHistory environmentHistory = snapshot.getValue(EnvironmentHistory.class);
                if (environmentHistory == null) return;
                for (int i = environmentHistories.size() - 1; i >= 0; i--) {
                    if (environmentHistories.get(i).getLakeId().equals(environmentHistory.getLakeId())) {
                        environmentHistories.set(i, environmentHistory);
                        lcPH.notifyDataSetChanged();
                        lcDoMan.notifyDataSetChanged();
                        lcOxy.notifyDataSetChanged();
                        return;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                EnvironmentHistory environmentHistory = snapshot.getValue(EnvironmentHistory.class);
                if (environmentHistory == null) return;
                for (int i = environmentHistories.size() - 1; i >= 0; i--) {
                    if (environmentHistories.get(i).getLakeId().equals(environmentHistory.getLakeId())) {
                        environmentHistories.remove(i);
                        lcPH.notifyDataSetChanged();
                        lcDoMan.notifyDataSetChanged();
                        lcOxy.notifyDataSetChanged();
                        return;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        setDataForSpiner();
    }

    private void pinData() {
        lcPH.clear();
        lcOxy.clear();
        lcDoMan.clear();
        getEntries();
        //env
        lineDataSetPH = new LineDataSet(lineEntriesPH, getString(R.string.statistics_tab_environment_label_ph));
        lineDataSetPH.setColors(ColorTemplate.rgb("#00CCFF"));
        lineDataSetPH.setValueTextColor(Color.BLACK);
        lineDataSetPH.setValueTextSize(8f);
        lineDataPH = new LineData(lineDataSetPH);
        lcPH.setData(lineDataPH);
        lcPH.getDescription().setEnabled(false);
        //oxy
        lineDataSetOxy = new LineDataSet(lineEntriesOxy, getString(R.string.statistics_tab_environment_label_oxy));
        lineDataSetOxy.setColors(ColorTemplate.rgb("#FF3366"));
        lineDataSetOxy.setValueTextColor(Color.BLACK);
        lineDataSetOxy.setValueTextSize(8f);
        lineDataOxy = new LineData(lineDataSetOxy);
        lcOxy.setData(lineDataOxy);
        lcOxy.getDescription().setEnabled(false);
        //doman
        lineDataSetDoMan = new LineDataSet(lineEntriesDoMan, getString(R.string.statistics_tab_environment_label_doman));
        lineDataSetDoMan.setColors(ColorTemplate.rgb("#0000FF"));
        lineDataSetDoMan.setValueTextColor(Color.BLACK);
        lineDataSetDoMan.setValueTextSize(8f);
        lineDataDoMan = new LineData(lineDataSetDoMan);
        lcDoMan.setData(lineDataDoMan);
        lcDoMan.getDescription().setEnabled(false);
        //notify
        lcPH.notifyDataSetChanged();
        lcOxy.notifyDataSetChanged();
        lcDoMan.notifyDataSetChanged();
    }

    private void getEntries() {
        lineEntriesPH = new ArrayList<>();
        lineEntriesDoMan = new ArrayList<>();
        lineEntriesOxy = new ArrayList<>();
        int i = 0;
        for (EnvironmentHistory env : environmentHistories) {
            if (lake.getId().equals(env.getLakeId())) {
                lineEntriesPH.add(new Entry(i, env.getpH()));
                lineEntriesOxy.add(new Entry(i, env.getoXy()));
                lineEntriesDoMan.add(new Entry(i, env.getSalinity()));
                i++;
            }
        }
    }

    private void setDataForSpiner() {
        if (lakes == null) return;
        List<String> lakeNames = new ArrayList<>();
        for (Lake lake : lakes) {
            lakeNames.add(lake.getKey() + " - " + lake.getName());
        }

        ArrayAdapter<String> spinerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lakeNames);
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sprLake.setAdapter(spinerAdapter);
        sprLake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lake = lakes.get(i);
                pinData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void addChildEventListener() {
        FirebaseDatabase.getInstance().getReference("Lake").orderByChild("accountId").equalTo(MainActivity.MY_ACCOUNT.getId())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Lake lake = snapshot.getValue(Lake.class);
                        if (lake == null || lake.isDeleted()) return;
                        lakes.add(lake);
                        addChildEventListenerForLake(lake);
                        lcPH.notifyDataSetChanged();
                        lcDoMan.notifyDataSetChanged();
                        lcOxy.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Lake lake = snapshot.getValue(Lake.class);
                        if (lake == null) return;
                        for (int i = lakes.size() - 1; i >= 0; i--) {
                            if (lakes.get(i).getId().equals(lake.getId())) {
                                lakes.set(i, lake);
                                if (lake.isDeleted()) {
                                    lakes.remove(i);
                                    lcPH.notifyDataSetChanged();
                                    lcDoMan.notifyDataSetChanged();
                                    lcOxy.notifyDataSetChanged();
                                    return;
                                }
                                lcPH.notifyDataSetChanged();
                                lcDoMan.notifyDataSetChanged();
                                lcOxy.notifyDataSetChanged();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Lake lake = snapshot.getValue(Lake.class);
                        if (lake == null) return;
                        for (int i = lakes.size() - 1; i >= 0; i--) {
                            if (lakes.get(i).getId().equals(lake.getId())) {
                                lakes.remove(i);
                                removeChildEventListenerOfLake(lake);
                                lcPH.notifyDataSetChanged();
                                lcDoMan.notifyDataSetChanged();
                                lcOxy.notifyDataSetChanged();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void removeChildEventListenerOfLake(Lake lake) {
        FirebaseDatabase.getInstance().getReference("EnvironmentHistory").orderByChild("lakeId").equalTo(lake.getId())
                .removeEventListener(childEventListenerEnvironment);
    }

    private void addChildEventListenerForLake(Lake lake) {
        FirebaseDatabase.getInstance().getReference("EnvironmentHistory").orderByChild("lakeId").equalTo(lake.getId())
                .addChildEventListener(childEventListenerEnvironment);
    }

    private void initView(View view) {
        sprLake = view.findViewById(R.id.statisticsenvironment_spr_chonao);
        lcPH = view.findViewById(R.id.statisticsenvironment_lc_ph);
        lcOxy = view.findViewById(R.id.statisticsenvironment_lc_oxy);
        lcDoMan = view.findViewById(R.id.statisticsenvironment_lc_doman);
    }
}