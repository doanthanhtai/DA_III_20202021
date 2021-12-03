package com.example.tomtep.fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.ExpandDietActivity;
import com.example.tomtep.Interface.IClickItemDietListener;
import com.example.tomtep.R;
import com.example.tomtep.adapter.DietAdapter;
import com.example.tomtep.dialog.UpdateDietDialog;
import com.example.tomtep.model.Ao;
import com.example.tomtep.model.LichSuChoAn;
import com.example.tomtep.model.LichSuSuDungSanPham;
import com.example.tomtep.model.SanPham;
import com.example.tomtep.model.TaiKhoan;
import com.example.tomtep.service.DietReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DietFragment extends Fragment implements IClickItemDietListener {

    private List<Ao> aos;
    private List<SanPham> sanPhams;
    private DietAdapter dietAdapter;
    private Context context;
    private int position;
    private DatabaseReference databaseReference;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("TaiKhoan").child(TaiKhoan.getInstance().getId());
        aos = new ArrayList<>();
        sanPhams = new ArrayList<>();
        getDanhSachSanPham();
        getDanhSachAo();
        initView(view);

        return view;
    }

    private void getDanhSachAo() {
        String idCurrentUser = FirebaseAuth.getInstance().getUid();
        assert idCurrentUser != null;
        FirebaseDatabase.getInstance().getReference("TaiKhoan").child(idCurrentUser).child("aos").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Ao ao = snapshot.getValue(Ao.class);
                assert ao != null;
                if (!ao.isDaXoa()) {
                    aos.add(snapshot.getValue(Ao.class));
                    dietAdapter.notifyItemChanged(aos.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Ao ao = snapshot.getValue(Ao.class);
                if (ao == null) {
                    return;
                }
                for (int i = aos.size() - 1; i >= 0; i--) {
                    if (aos.get(i).getId().equals(ao.getId())) {
                        aos.set(i, ao);
                        if (ao.isDaXoa()) {
                            aos.remove(i);
                            dietAdapter.notifyItemRemoved(i);
                            return;
                        }
                        dietAdapter.notifyItemChanged(i);
                        return;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Ao ao = snapshot.getValue(Ao.class);
                if (ao == null) {
                    return;
                }
                for (int i = aos.size() - 1; i >= 0; i--) {
                    if (aos.get(i).getId().equals(ao.getId())) {
                        aos.remove(i);
                        dietAdapter.notifyItemRemoved(i);
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

    private void getDanhSachSanPham() {
        String idCurrentUser = FirebaseAuth.getInstance().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
        assert idCurrentUser != null;
        databaseReference.child(idCurrentUser).child("sanPhams").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                assert sanPham != null;
                if (!sanPham.isDaXoa()) {
                    sanPhams.add(sanPham);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                if (sanPham == null) {
                    return;
                }
                for (int i = sanPhams.size() - 1; i >= 0; i--) {
                    if (sanPhams.get(i).getId().equals(sanPham.getId())) {
                        if (sanPham.isDaXoa()) {
                            sanPhams.remove(i);
                            return;
                        }
                        sanPhams.set(i, sanPham);
                        return;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                if (sanPham == null) {
                    return;
                }
                for (int i = sanPhams.size() - 1; i >= 0; i--) {
                    if (sanPhams.get(i).getId().equals(sanPham.getId())) {
                        sanPhams.remove(i);
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

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.diet_rcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        dietAdapter = new DietAdapter(aos, this);
        recyclerView.setAdapter(dietAdapter);
    }

    @Override
    public void onClick(Ao ao) {
        Intent intent = new Intent(context, ExpandDietActivity.class);
        intent.setAction(ao.getId());
        startActivity(intent);
    }

    private void notifyItemDiet() {
        dietAdapter.notifyItemChanged(position);
    }

    @Override
    public void onChoAn(Ao ao, boolean isChecked) {

        position = aos.size() - 1;
        for (; position >= 0; position--) {
            if (aos.get(position).getId().equals(ao.getId())) {
                break;
            }
        }

        SanPham sanPham = ao.getCheDoAn().getSanPhamChoAn();
        for (int i = sanPhams.size() - 1;i >= 0;i--){
            if (sanPham.getId().equals(sanPhams.get(i).getId())){
                if (sanPhams.get(i).getSoLuong() < ao.getCheDoAn().getLuongChoAn()){
                    Toast.makeText(context,"Sản phẩm hiện tại không còn đủ cho một lần ăn.",Toast.LENGTH_SHORT).show();
                    notifyItemDiet();
                    return;
                }
                sanPham.setSoLuong(sanPhams.get(i).getSoLuong() - ao.getCheDoAn().getLuongChoAn());
            }
        }

        if (ao.getCheDoAn().getSanPhamChoAn().getId().equals("default_product")) {
            Toast.makeText(context, getText(R.string.dietfragment_toast_dietinvalid), Toast.LENGTH_SHORT).show();
            dietAdapter.notifyItemChanged(position);
            return;
        }

        AlertDialog.Builder builder;
        if (isChecked) {
            builder = new AlertDialog.Builder(context)
                    .setTitle("Xác nhận bật cho ăn")
                    .setMessage(ao.getMaAo() + ao.getTenAo())
                    .setPositiveButton(R.string.all_button_agree_text, (dialogInterface, i) -> {
                        FirebaseDatabase.getInstance().getReference("TaiKhoan")
                                .child(TaiKhoan.getInstance().getId())
                                .child("aos")
                                .child(ao.getId())
                                .child("cheDoAn")
                                .child("trangThai")
                                .setValue(true);
                        createLichSuSuDungSanPham(ao);
                        updateQuantityProduct(sanPham);
                        createAlarm(ao, true);
                    })
                    .setNegativeButton(R.string.all_button_cancel_text, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        notifyItemDiet();
                    });
        } else {
            builder = new AlertDialog.Builder(context)
                    .setTitle("Xác nhận kết thúc cho ăn")
                    .setMessage(ao.getMaAo() + ao.getTenAo())
                    .setPositiveButton(R.string.all_button_agree_text, (dialogInterface, i) -> {
                        FirebaseDatabase.getInstance().getReference("TaiKhoan")
                                .child(TaiKhoan.getInstance().getId())
                                .child("aos")
                                .child(ao.getId())
                                .child("cheDoAn")
                                .child("trangThai")
                                .setValue(false);
                        createAlarm(ao, false);
                    })
                    .setNegativeButton(R.string.all_button_cancel_text, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        notifyItemDiet();
                    });
        }
        builder.create().show();
    }

    private void updateQuantityProduct(SanPham sanPham) {
        databaseReference.child("sanPhams")
                .child(sanPham.getId())
                .child("soLuong")
                .setValue(sanPham.getSoLuong());
    }

    private void createLichSuSuDungSanPham(Ao ao) {
        LichSuChoAn lichSuChoAn = new LichSuChoAn();
        LichSuSuDungSanPham lichSuSuDungSanPham = new LichSuSuDungSanPham();
        if (ao.getLichSuSuDungSanPhams() == null) {
            ao.setLichSuSuDungSanPhams(new ArrayList<>());
        }

        lichSuChoAn.setTonTai(true);
        lichSuChoAn.setKetQua((String) getResources().getText(R.string.dietfragment_kqchoan_default));

        String thoiGian = DateFormat.getInstance().format(Calendar.getInstance().getTime());

        lichSuSuDungSanPham.setId(String.valueOf(ao.getLichSuSuDungSanPhams().size()));
        lichSuSuDungSanPham.setSanPham(ao.getCheDoAn().getSanPhamChoAn());
        lichSuSuDungSanPham.setSoLuong(ao.getCheDoAn().getLuongChoAn());
        lichSuSuDungSanPham.setThoiGianDung(thoiGian);
        lichSuSuDungSanPham.setThoiGianCapNhat(thoiGian);
        lichSuSuDungSanPham.setLichSuChoAns(lichSuChoAn);
        lichSuSuDungSanPham.setDaXoa(false);
        databaseReference.child("aos")
                .child(ao.getId())
                .child("lichSuSuDungSanPhams")
                .child(lichSuSuDungSanPham.getId())
                .setValue(lichSuSuDungSanPham);
    }

    @Override
    public void onLongClick(Ao ao) {
        new UpdateDietDialog(requireContext(), ao, sanPhams).show();
    }

    private void createAlarm(Ao ao, boolean trangThai) {
        Intent intent = new Intent(context, DietReceiver.class);
        String strTitle = (String) getText(R.string.dietfagment_notification_titile);
        String strContent = ao.getMaAo() + "-" + ao.getTenAo() + "\n" + ao.getCheDoAn().getSanPhamChoAn().getTenSP() + ": " + ao.getCheDoAn().getLuongChoAn() + ao.getCheDoAn().getSanPhamChoAn().getDonViDung();
        intent.putExtra("ao_id", ao.getId());
        intent.putExtra("title", strTitle);
        intent.putExtra("content", strContent);
        intent.setAction("EATED");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, aos.indexOf(ao), intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (trangThai) {
            Toast.makeText(context, getText(R.string.dietfragment_toast_turnonsuccess), Toast.LENGTH_SHORT).show();
            long thoiGianAn = System.currentTimeMillis() + Integer.parseInt(ao.getCheDoAn().getThoiGianChoAn()) * 60 * 1000L;
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, thoiGianAn, pendingIntent);
        } else {
            Toast.makeText(context, getText(R.string.dietfragment_toast_turnoffsuccess), Toast.LENGTH_SHORT).show();
            alarmManager.cancel(pendingIntent);
            Intent i = new Intent(context, ExpandDietActivity.class);
            i.setAction(ao.getId());
            startActivity(i);
        }
    }
}