package com.example.appsoxo;

import android.graphics.*;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import org.json.*;

import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout container;
    TextView txtDate;
    EditText edtSo;
    String soDo = "";

    ProgressBar loading;

    Handler handler = new Handler(Looper.getMainLooper());
    int retryCount = 0;
    final int MAX_RETRY = 3;



    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.container);
        txtDate = findViewById(R.id.txtDate);
        edtSo = findViewById(R.id.edtSo);
        loading = findViewById(R.id.loading);

        findViewById(R.id.btnDo).setOnClickListener(v -> {
            soDo = edtSo.getText().toString().trim();
            container.removeAllViews();
            loadData();
        });

        loadData();
    }

    void loadData() {

        loading.setVisibility(View.VISIBLE);
        txtDate.setText("Đang tải dữ liệu...");

        String url = "https://lotteryapi-qo0e.onrender.com/api/xoso/mien-nam";
        RequestQueue q = Volley.newRequestQueue(this);

        q.add(new JsonObjectRequest(Request.Method.GET, url, null,
                res -> {
                    loading.setVisibility(View.GONE);
                    retryCount = 0; // reset khi thành công

                    try {
                        txtDate.setText("Ngày " + res.getString("date"));
                        JSONArray arr = res.getJSONArray("provinces");

                        container.removeAllViews();

                        for (int i = 0; i < arr.length(); i++) {
                            drawProvince(arr.getJSONObject(i));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                err -> {
                    loading.setVisibility(View.GONE);

                    if (retryCount < MAX_RETRY) {
                        retryCount++;

                        txtDate.setText(
                                "Hệ thống đang khởi động dữ liệu...\n" +
                                        "Vui lòng chờ trong giây lát (" + retryCount + "/3)"
                        );

                        handler.postDelayed(this::loadData, 5000); // 🔥 retry sau 5s
                    } else {
                        txtDate.setText("Không thể tải dữ liệu.\nVui lòng thử lại sau.");
                        Toast.makeText(
                                this,
                                "Máy chủ đang bận. Vui lòng thử lại sau.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ));
    }


    // ====================== VẼ 1 TỈNH ======================
    void drawProvince(JSONObject o) throws Exception {

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(12,12,12,12);
        box.setBackgroundResource(R.drawable.bg_box);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,0,0,18);
        box.setLayoutParams(lp);

        // TÊN TỈNH
        TextView title = new TextView(this);
        title.setText(o.getString("province"));
        title.setTextSize(34);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(Color.BLUE);
        title.setGravity(Gravity.CENTER);
        box.addView(title);

        row(box,"ĐẶC BIỆT", List.of(o.getString("dacBiet")), true, true);
        row(box,"GIẢI 1", List.of(o.getString("giaiNhat")), false, false);
        row(box,"GIẢI 2", jsonToList(o.getJSONArray("giaiNhi")), false, false);
        row(box,"GIẢI 3", jsonToList(o.getJSONArray("giaiBa")), false, false);
        row(box,"GIẢI 4", jsonToList(o.getJSONArray("giaiTu")), false, false);
        row(box,"GIẢI 5", List.of(o.getString("giaiNam")), false, false);
        row(box,"GIẢI 6", jsonToList(o.getJSONArray("giaiSau")), false, false);
        row(box,"GIẢI 7", List.of(o.getString("giaiBay")), false, false);
        row(box,"GIẢI 8", List.of(o.getString("giaiTam")), false, true);

        container.addView(box);
    }

    // ====================== 1 GIẢI – 1 HÀNG CHUẨN ======================
    void row(LinearLayout box, String label, List<String> numbers, boolean dacBiet, boolean giai8) {

        List<List<String>> lines = chunk(numbers, 3);

        for (int i = 0; i < lines.size(); i++) {
            LinearLayout r = new LinearLayout(this);
            r.setOrientation(LinearLayout.HORIZONTAL);
            r.setPadding(8,10,8,10);


            TextView tvLabel = text(i == 0 ? label : "", 26, true, Color.DKGRAY);
            tvLabel.setGravity(Gravity.CENTER_VERTICAL);

            TextView tvValue = text(joinDash(lines.get(i)), 32, true, Color.BLACK);
            tvValue.setGravity(Gravity.CENTER);               // CĂNG GIỮA NGANG
            tvValue.setLineSpacing(6, 1.2f);                  // GIÃN DÒNG
            tvValue.setPadding(8, 6, 8, 6);                    // THOÁNG

            if (dacBiet) tvValue.setTextColor(Color.RED);
            if (dacBiet) {
                tvValue.setTextSize(36);
                tvValue.setTextColor(Color.RED);
                tvValue.setTypeface(null, Typeface.BOLD);
            }

            if (giai8) tvValue.setTextColor(Color.parseColor("#F57C00"));
            if (giai8) {
                tvValue.setTextSize(34);
                tvValue.setTextColor(Color.parseColor("#F57C00"));
            }


            if (!soDo.isEmpty() && tvValue.getText().toString().endsWith(soDo)) {
                tvValue.setBackgroundColor(Color.YELLOW);
                tvValue.setTextColor(Color.RED);
            }

            r.addView(tvLabel, new LinearLayout.LayoutParams(0,-2,1));
            r.addView(tvValue, new LinearLayout.LayoutParams(0,-2,2));
            box.addView(r);
        }

        divider(box);
    }

    // ====================== TIỆN ÍCH ======================
    TextView text(String t, int size, boolean bold, int color) {
        TextView v = new TextView(this);
        v.setText(t);
        v.setTextSize(size);
        v.setTextColor(color);
        if (bold) v.setTypeface(null, Typeface.BOLD);
        return v;
    }

    void divider(LinearLayout box) {
        View d = new View(this);
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 2);
        lp.setMargins(0,8,0,8);
        d.setLayoutParams(lp);
        d.setBackgroundColor(Color.LTGRAY);
        box.addView(d);
    }


    List<String> jsonToList(JSONArray a) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < a.length(); i++) list.add(a.optString(i));
        return list;
    }

    List<List<String>> chunk(List<String> list, int size) {
        List<List<String>> out = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            out.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return out;
    }

    String joinDash(List<String> list) {
        return String.join(" - ", list);
    }
}