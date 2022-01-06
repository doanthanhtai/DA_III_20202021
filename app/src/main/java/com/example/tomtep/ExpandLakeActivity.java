package com.example.tomtep;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tomtep.adapter.ExpandLakeViewPagerAdapter;
import com.example.tomtep.model.Lake;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ExpandLakeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Lake lake;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_lake);
        lake = (Lake) getIntent().getSerializableExtra("lake_from_lakefragment");
        initView();
        setEvent();
        settupToolbar();
        setTitleForTabLayout();
    }

    private void setEvent() {

    }

    private void settupToolbar() {
        setSupportActionBar(toolbar);
        setTitle(lake.getKey() + " - " + lake.getName());
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initView() {
        toolbar = findViewById(R.id.expandlake_toolbar);
        viewPager2 = findViewById(R.id.expandlake_viewpager2);
        tabLayout = findViewById(R.id.expandlake_tablayout);
        ExpandLakeViewPagerAdapter expandLakeViewPagerAdapter = new ExpandLakeViewPagerAdapter(this, lake);
        viewPager2.setAdapter(expandLakeViewPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (lake.isCondition()){
            getMenuInflater().inflate(R.menu.expandlake_toolbar_menu_two, menu);
            return super.onCreateOptionsMenu(menu);
        }
        getMenuInflater().inflate(R.menu.expandlake_toolbar_menu_one, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.lake_harvest) {
            String strMassage = "Hãy chắc rằng bạn muốn Thu hoạch ao: " + lake.getKey() + "-" + lake.getName() + "\nSau khi hoàn tất thu hoạch,ao sẽ chuyển sang tab \"Đã thu hoạch\" và bạn chỉ có thể xem lại dữ liệu của ao mà không thao tác ghi dữ liệu mới với ao được nũa!";
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.expandlake_title_confirmharvest)
                    .setMessage(strMassage)
                    .setNegativeButton(R.string.all_button_agree_text, (dialog, which) -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("condition", true);
                        map.put("harvestTime", DateFormat.getInstance().format(Calendar.getInstance().getTime()));
                        FirebaseDatabase.getInstance().getReference("Lake").child(lake.getId()).updateChildren(map).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, R.string.expandlake_toast_harvestsuccess, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                onBackPressed();
                            }
                        });

                    })
                    .setPositiveButton(R.string.all_button_cancel_text, (dialog, which) -> dialog.dismiss());
            builder.create().show();

            Toast.makeText(this, "Chưa configure chức năng này", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.lake_delete) {
            String strMessage = "Hãy chắc rằng bạn muốn xóa ao " + lake.getKey() + "-" + lake.getName() + "\nMọi thông tin liên quan của ao sẽ bị xóa khỏi giao diện nhìn thấy của bạn.\nVà sẽ không thể khôi phục lại được.";
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.expandlake_title_confirmdetelake)
                    .setMessage(strMessage)
                    .setNegativeButton(R.string.all_button_agree_text, (dialog, which) -> FirebaseDatabase.getInstance().getReference("Lake").child(lake.getId()).child("deleted").setValue(true).addOnCompleteListener(task -> {
                        Toast.makeText(this, R.string.expandlake_toast_deletelakesuccess, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finishAffinity();
                    }))
                    .setPositiveButton(R.string.all_button_cancel_text, (dialog, which) -> dialog.dismiss());
            builder.create().show();
        }
        return true;
    }

    private void setTitleForTabLayout() {
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(getString(R.string.expandlake_tab_producthistory));
                    break;
                case 1:
                    tab.setText(getString(R.string.expandlake_tab_otherusehistory));
                    break;
                default:
                    tab.setText(getString(R.string.expandlake_tab_environmenthistory));
                    break;
            }
        }).attach();
    }
}


//    private void createPdfReportForLake() {
//        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//            requestPermissions(permission, REQUEST_PERMISSION_CODE);
//        } else {
//            writePdf(lake);
//        }
//    }

//    private void writePdf(Lake lake) {
//        String fileName = lake.getKey() + ".pdf";
//        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
//        File file = new File(filePath, fileName);
//        try {
//            PdfWriter pdfWriter = new PdfWriter(file);
//            Log.e("TOMTEP", filePath);
//            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
//            Document document = new Document(pdfDocument);
//            pdfDocument.setDefaultPageSize(PageSize.A4);
//            document.setMargins(1f, 1f, 1f, 1f);
//
//
//
//            Drawable drawable = AppCompatResources.getDrawable(this, R.drawable.logo_24px);
//            if (drawable != null) {
//                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] bitmapData = stream.toByteArray();
//
//                ImageData imageData = ImageDataFactory.create(bitmapData);
//                Image image = new Image(imageData).setHorizontalAlignment(HorizontalAlignment.CENTER);
//                document.add(image);
//            }
//
//            Paragraph tomtep = new Paragraph(new Text(getString(R.string.app_name))).setFontSize(12).setTextAlignment(TextAlignment.CENTER);
//            Paragraph lakeInfo = new Paragraph(new Text(lake.getKey() + "-" + lake.getName())).setBold().setFontSize(28).setTextAlignment(TextAlignment.CENTER);
//            Paragraph lakeStatus = new Paragraph(new Text("Tạo lúc: " + lake.getCreationTime() + "\nThu hoạch lúc: " + lake.getHarvestTime())).setFontSize(12).setTextAlignment(TextAlignment.CENTER);
//
//            Paragraph productHistoryTitle = new Paragraph(new Text("Lịch sử sử dụng sản phẩm:")).setItalic().setFontSize(16).setTextAlignment(TextAlignment.LEFT);
//            float[] withTableProductHistory = {50f, 100f, 200f, 150f, 150f, 100f};
//            Table tableProductHistory = new Table(withTableProductHistory).setHorizontalAlignment(HorizontalAlignment.CENTER);
//            tableProductHistory.addCell(new Cell().add(new Paragraph(new Text("STT")).setBold().setTextAlignment(TextAlignment.CENTER)));
//            tableProductHistory.addCell(new Cell().add(new Paragraph(new Text("Mã Sản phẩm")).setBold().setTextAlignment(TextAlignment.CENTER)));
//            tableProductHistory.addCell(new Cell().add(new Paragraph(new Text("Tên Sản phẩm")).setBold().setTextAlignment(TextAlignment.CENTER)));
//            tableProductHistory.addCell(new Cell().add(new Paragraph(new Text("Ngày dùng")).setBold().setTextAlignment(TextAlignment.CENTER)));
//            tableProductHistory.addCell(new Cell().add(new Paragraph(new Text("Cập nhật")).setBold().setTextAlignment(TextAlignment.CENTER)));
//            tableProductHistory.addCell(new Cell().add(new Paragraph(new Text("Số lượng")).setBold().setTextAlignment(TextAlignment.CENTER)));
//
//            Paragraph otherHistoryTitle = new Paragraph("Lịch sử chi dùng khác:").setItalic().setFontSize(16).setTextAlignment(TextAlignment.LEFT);
//            float[] withTableOtherHistory = {50f, 100f, 200f, 150f, 150f, 100f};
//            Table tableOtherHistory = new Table(withTableOtherHistory).setHorizontalAlignment(HorizontalAlignment.CENTER);
//            tableOtherHistory.addCell(new Cell().add(new Paragraph(new Text("STT")).setBold().setTextAlignment(TextAlignment.CENTER)));
//            tableOtherHistory.addCell(new Cell().add(new Paragraph(new Text("Tên sử dụng")).setBold().setTextAlignment(TextAlignment.CENTER)));
//            tableOtherHistory.addCell(new Cell().add(new Paragraph(new Text("Mô tả")).setBold().setTextAlignment(TextAlignment.CENTER)));
//            tableOtherHistory.addCell(new Cell().add(new Paragraph(new Text("Ngày dùng")).setBold().setTextAlignment(TextAlignment.CENTER)));
//            tableOtherHistory.addCell(new Cell().add(new Paragraph(new Text("Cập nhật")).setBold().setTextAlignment(TextAlignment.CENTER)));
//            tableOtherHistory.addCell(new Cell().add(new Paragraph(new Text("Số tiền")).setBold().setTextAlignment(TextAlignment.CENTER)));
//
//            Paragraph envHistoryTitle = new Paragraph("Lịch sử môi trường:").setItalic().setFontSize(16).setTextAlignment(TextAlignment.LEFT);
//            float[] withEnvHistory = {200f, 100f, 100f, 100f};
//            Table tableEnvHistory = new Table(withEnvHistory).setHorizontalAlignment(HorizontalAlignment.CENTER);
//            tableEnvHistory.addCell(new Cell().add(new Paragraph(new Text("Ngày cập nhật")).setBold().setTextAlignment(TextAlignment.CENTER)));
//            tableEnvHistory.addCell(new Cell().add(new Paragraph(new Text("pH")).setBold().setTextAlignment(TextAlignment.CENTER)));
//            tableEnvHistory.addCell(new Cell().add(new Paragraph(new Text("Oxy (mg/l)")).setBold().setTextAlignment(TextAlignment.CENTER)));
//            tableEnvHistory.addCell(new Cell().add(new Paragraph(new Text("Độ mặn (‰)")).setBold().setTextAlignment(TextAlignment.CENTER)));
//
//            Paragraph total = new Paragraph(new Text("Tổng chi phí đầu tư: ")).setItalic().setFontSize(16).setTextAlignment(TextAlignment.RIGHT);
//
//
//            document.add(tomtep);
//            document.add(lakeInfo);
//            document.add(lakeStatus);
//            document.add(productHistoryTitle);
//            document.add(tableProductHistory);
//            document.add(otherHistoryTitle);
//            document.add(tableOtherHistory);
//            document.add(envHistoryTitle);
//            document.add(tableEnvHistory);
//            document.add(total);
//
//            document.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults.length > 0 && requestCode == REQUEST_PERMISSION_CODE && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            writePdf(lake);
//        }
//    }