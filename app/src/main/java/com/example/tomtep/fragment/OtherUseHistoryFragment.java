package com.example.tomtep.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.Interface.IClickItemOtherUseHistoryListener;
import com.example.tomtep.R;
import com.example.tomtep.adapter.OtherUseHistoryAdapter;
import com.example.tomtep.dialog.NewOtherUseHistoryDailog;
import com.example.tomtep.dialog.UpdateOtherUseHistoryDailog;
import com.example.tomtep.model.Lake;
import com.example.tomtep.model.OtherUseHistory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OtherUseHistoryFragment extends Fragment implements IClickItemOtherUseHistoryListener {
    private final Lake lake;
    private List<OtherUseHistory> otherUseHistories;
    private Context context;
    private FloatingActionButton floatingOtherUseHistory;
    private OtherUseHistoryAdapter otherUseHistoryAdapter;
    private RecyclerView rcvOtherUseHistory;

    public OtherUseHistoryFragment(Lake lake) {
        this.lake = lake;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otherusehistory, container, false);
        otherUseHistories = new ArrayList<>();
        addChildEventListener();
        initView(view);
        setEvent();
        setSwipeDeleteProductHistory();
        return view;
    }

    private void setSwipeDeleteProductHistory() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                OtherUseHistory otherUseHistory = otherUseHistories.get(position);
                String strMessage = otherUseHistory.getName() + "\nPhí chi: " + otherUseHistory.getCost() + "\nChi vào: " + otherUseHistory.getUseTime() + getText(R.string.otherusehistoryfragment_message_confirmactiondelete) + lake.getName();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.all_title_dialogconfirmdelete)
                        .setMessage(strMessage)
                        .setNegativeButton(R.string.all_button_agree_text, (dialog, which) -> {
                            deleteOtherUseHistory(otherUseHistory);
                            Toast.makeText(getContext(), R.string.producthistoryfragmemt_toast_deletesuccess, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        })
                        .setPositiveButton(R.string.all_button_cancel_text, (dialogInterface, i) -> {
                            otherUseHistoryAdapter.notifyItemChanged(position);
                            dialogInterface.dismiss();
                        });
                builder.create().show();
            }

            private void deleteOtherUseHistory(OtherUseHistory otherUseHistory) {
                FirebaseDatabase.getInstance().getReference("OtherUseHistory").child(otherUseHistory.getId()).child("deleted").setValue(true).addOnCompleteListener(task -> Toast.makeText(context, R.string.otherusehistoryfragment_toast_success, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    View foreGroundView = ((OtherUseHistoryAdapter.OtherUseHistoryViewHolder) viewHolder).itemOtherUseHistoryForeGround;
                    getDefaultUIUtil().onSelected(foreGroundView);
                }
            }

            @Override
            public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View foreGroundView = ((OtherUseHistoryAdapter.OtherUseHistoryViewHolder) viewHolder).itemOtherUseHistoryForeGround;
                getDefaultUIUtil().onDrawOver(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View foreGroundView = ((OtherUseHistoryAdapter.OtherUseHistoryViewHolder) viewHolder).itemOtherUseHistoryForeGround;
                getDefaultUIUtil().onDraw(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                View foreGroundView = ((OtherUseHistoryAdapter.OtherUseHistoryViewHolder) viewHolder).itemOtherUseHistoryForeGround;
                getDefaultUIUtil().clearView(foreGroundView);
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvOtherUseHistory);
    }

    private void addChildEventListener() {
        FirebaseDatabase.getInstance().getReference("OtherUseHistory").orderByChild("lakeId").equalTo(lake.getId())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        OtherUseHistory otherUseHistory = snapshot.getValue(OtherUseHistory.class);
                        if (otherUseHistory == null || otherUseHistory.isDeleted()) return;
                        otherUseHistories.add(0, otherUseHistory);
                        otherUseHistoryAdapter.notifyItemInserted(0);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        OtherUseHistory otherUseHistory = snapshot.getValue(OtherUseHistory.class);
                        if (otherUseHistory == null) return;
                        for (int i = otherUseHistories.size() - 1; i >= 0; i--) {
                            if (otherUseHistories.get(i).getId().equals(otherUseHistory.getId())) {
                                if (otherUseHistory.isDeleted()) {
                                    otherUseHistories.remove(i);
                                    otherUseHistoryAdapter.notifyItemRemoved(i);
                                    return;
                                }
                                otherUseHistories.set(i, otherUseHistory);
                                otherUseHistoryAdapter.notifyItemChanged(i);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        OtherUseHistory otherUseHistory = snapshot.getValue(OtherUseHistory.class);
                        if (otherUseHistory == null) return;
                        for (int i = otherUseHistories.size() - 1; i >= 0; i--) {
                            if (otherUseHistories.get(i).getId().equals(otherUseHistory.getId())) {
                                otherUseHistories.remove(i);
                                otherUseHistoryAdapter.notifyItemRemoved(i);
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

    private void setEvent() {
        floatingOtherUseHistory.setOnClickListener(v -> insertOtherUseHistory());
    }

    private void insertOtherUseHistory() {
        new NewOtherUseHistoryDailog(context, lake).show();
    }

    private void initView(View view) {
        otherUseHistoryAdapter = new OtherUseHistoryAdapter(otherUseHistories, this);
        rcvOtherUseHistory = view.findViewById(R.id.otherusehistory_rcv);
        floatingOtherUseHistory = view.findViewById(R.id.otherusehistory_floating);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcvOtherUseHistory.setLayoutManager(linearLayoutManager);
        rcvOtherUseHistory.setAdapter(otherUseHistoryAdapter);
    }

    @Override
    public boolean onLongClick(OtherUseHistory otherUseHistory) {
        new UpdateOtherUseHistoryDailog(context, otherUseHistory).show();
        return true;
    }
}
