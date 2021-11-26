package com.example.tomtep.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomtep.ExpandProductActivity;
import com.example.tomtep.Interface.IClickItemProductListener;
import com.example.tomtep.R;
import com.example.tomtep.adapter.ProductAdapter;
import com.example.tomtep.dialog.EnterQuantityProductDialog;
import com.example.tomtep.dialog.UpdateProductDailog;
import com.example.tomtep.model.SanPham;
import com.example.tomtep.model.TaiKhoan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment implements IClickItemProductListener {
    private ProductAdapter productAdapter;
    public static List<SanPham> sanPhams;
    public static List<String> donVis;
    private RecyclerView rcvProduct;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TaiKhoan/" + TaiKhoan.getInstance().getId() + "/sanPhams");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        sanPhams = new ArrayList<>();
        donVis = new ArrayList<>();
        getDanhSachSanPham();
        getDonVis();
        initView(view);
        setSwipeDeleteProduct();
        return view;
    }

    private void setSwipeDeleteProduct() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                SanPham sanPham = sanPhams.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.all_title_dialogconfirmdelete)
                        .setMessage(sanPham.getMaSP() + "-" + sanPham.getTenSP() + getText(R.string.all_message_confirmactiondeleteproduct))
                        .setNegativeButton(R.string.all_button_agree_text, (dialogInterface, i) -> databaseReference.child(sanPham.getId()).child("daXoa").setValue(true).addOnCompleteListener(task -> {
                            Toast.makeText(getContext(), R.string.productfragmemt_toast_deletesuccess, Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }))
                        .setPositiveButton(R.string.all_button_cancel_text, (dialogInterface, i) -> {
                            productAdapter.notifyItemChanged(position);
                            dialogInterface.dismiss();
                        });
                builder.create().show();
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    View foreGroundView = ((ProductAdapter.ProductViewHolder) viewHolder).itemProdcutForeGround;
                    getDefaultUIUtil().onSelected(foreGroundView);
                }
            }

            @Override
            public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View foreGroundView = ((ProductAdapter.ProductViewHolder) viewHolder).itemProdcutForeGround;
                getDefaultUIUtil().onDrawOver(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View foreGroundView = ((ProductAdapter.ProductViewHolder) viewHolder).itemProdcutForeGround;
                getDefaultUIUtil().onDraw(c, recyclerView, foreGroundView, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                View foreGroundView = ((ProductAdapter.ProductViewHolder) viewHolder).itemProdcutForeGround;
                getDefaultUIUtil().clearView(foreGroundView);
            }
        });
        itemTouchHelper.attachToRecyclerView(rcvProduct);
    }

    private void getDonVis() {
        FirebaseDatabase.getInstance().getReference("DonViDung").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String newDonVi = snapshot.getValue(String.class);
                if (newDonVi == null) {
                    return;
                }
                donVis.add(newDonVi);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String donVi = snapshot.getValue(String.class);
                assert donVi != null;
                int changeIndex = Integer.parseInt(donVi);
                donVis.set(changeIndex, donVi);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String donVi = snapshot.getValue(String.class);
                assert donVi != null;
                int changeIndex = Integer.parseInt(donVi);
                donVis.remove(changeIndex);
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

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                SanPham sanPham = snapshot.getValue(SanPham.class);
                if (sanPham == null) {
                    return;
                }
                if (!sanPham.isDaXoa()) {
                    sanPhams.add(sanPham);
                    productAdapter.notifyItemChanged(sanPhams.size() - 1);
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
                        sanPhams.set(i, sanPham);
                        if (sanPham.isDaXoa()) {
                            sanPhams.remove(i);
                            productAdapter.notifyItemRemoved(i);
                            return;
                        }
                        productAdapter.notifyItemChanged(i);
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
                        productAdapter.notifyItemChanged(i);
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
        rcvProduct = view.findViewById(R.id.product_rcv);
        productAdapter = new ProductAdapter(sanPhams, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvProduct.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        rcvProduct.addItemDecoration(decoration);
        rcvProduct.setAdapter(productAdapter);

    }

    @Override
    public void onClickEnterQuatity(SanPham sanPham) {
        new EnterQuantityProductDialog(requireContext(), sanPham, sanPhams).show();
    }

    @Override
    public void onClickItemProduct(SanPham sanPham) {
        Intent intent = new Intent(getActivity(), ExpandProductActivity.class).putExtra("sanpham_from_productfragment",sanPham);
        startActivity(intent);
    }

    @Override
    public boolean onLongClickItemProduct(SanPham sanPham) {
        new UpdateProductDailog(requireContext(), sanPham, donVis).show();
        return false;
    }
}