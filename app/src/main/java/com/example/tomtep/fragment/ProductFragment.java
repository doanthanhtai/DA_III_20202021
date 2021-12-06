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
import com.example.tomtep.MainActivity;
import com.example.tomtep.R;
import com.example.tomtep.adapter.ProductAdapter;
import com.example.tomtep.dialog.EnterQuantityProductDialog;
import com.example.tomtep.dialog.UpdateProductDailog;
import com.example.tomtep.model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment implements IClickItemProductListener {
    private ProductAdapter productAdapter;
    private RecyclerView rcvProduct;
    private List<Product> products;
    private List<String> units;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        products = new ArrayList<>();
        units = new ArrayList<>();
        addChildEventListener();
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
                Product product = products.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.all_title_dialogconfirmdelete)
                        .setMessage(product.getKey() + "-" + product.getName() + getText(R.string.all_message_confirmactiondeleteproduct))
                        .setNegativeButton(R.string.all_button_agree_text, (dialogInterface, i) -> FirebaseDatabase.getInstance().getReference("Product").child(product.getId()).child("deleted").setValue(true).addOnCompleteListener(task -> {
                            Toast.makeText(getContext(), R.string.productfragmemt_toast_deletesuccess, Toast.LENGTH_SHORT).show();
                            productAdapter.notifyItemRemoved(position);
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

    private void initView(View view) {
        rcvProduct = view.findViewById(R.id.product_rcv);
        productAdapter = new ProductAdapter(products, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvProduct.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        rcvProduct.addItemDecoration(decoration);
        rcvProduct.setAdapter(productAdapter);

    }

    @Override
    public void onClickEnterQuatity(Product product) {
        new EnterQuantityProductDialog(requireContext(), product, products).show();
    }

    @Override
    public void onClickItemProduct(Product product) {
        Intent intent = new Intent(getActivity(), ExpandProductActivity.class).putExtra("product_from_productfragment", product);
        startActivity(intent);
    }

    @Override
    public boolean onLongClickItemProduct(Product product) {
        new UpdateProductDailog(requireContext(), product, units).show();
        return false;
    }

    private void addChildEventListener() {
        FirebaseDatabase.getInstance().getReference("Unit").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String unit = snapshot.getValue(String.class);
                if (unit == null) return;
                units.add(unit);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getKey() == null) return;
                int index = Integer.parseInt(snapshot.getKey());
                String unit = snapshot.getValue(String.class);
                units.set(index, unit);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.getKey() == null) return;
                int index = Integer.parseInt(snapshot.getKey());
                units.remove(index);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference("Product").orderByChild("accountId").equalTo(MainActivity.account.getId())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @com.google.firebase.database.annotations.Nullable String previousChildName) {
                        Product product = snapshot.getValue(Product.class);
                        if (product == null || product.isDeleted()) return;
                        products.add(product);
                        productAdapter.notifyItemChanged(products.size());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @com.google.firebase.database.annotations.Nullable String previousChildName) {
                        Product product = snapshot.getValue(Product.class);
                        if (product == null) return;
                        for (int i = products.size() - 1; i >= 0; i--) {
                            if (products.get(i).getId().equals(product.getId())) {
                                if (product.isDeleted()) {
                                    products.remove(i);
                                    return;
                                }
                                products.set(i, product);
                                productAdapter.notifyItemChanged(i);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Product product = snapshot.getValue(Product.class);
                        if (product == null) return;
                        for (int i = products.size() - 1; i >= 0; i--) {
                            if (products.get(i).getId().equals(product.getId())) {
                                products.remove(i);
                                productAdapter.notifyItemChanged(i);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @com.google.firebase.database.annotations.Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}