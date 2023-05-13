package com.example.projecta;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.projecta.adapter.RegionAdapter;
import com.example.projecta.model.District;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AddressActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvNT, tvKL, tvHK;
    RecyclerView rvNT, rvKL, rvHK;

    String userId;

    boolean[] booleans = {false, false, false};
    ArrayList<RecyclerView> recyclerViews = new ArrayList<>();

    HashMap<String, District> mapNT, mapKL, mapHK;
    ArrayList<String> arrayNT, arrayKL, arrayHK;
    ArrayList<String> disCnW, disWC, disE, disS;
    ArrayList<String> disYTM, disSSP, disKLC, disWTS, disKT;
    ArrayList<String> disKwT, disTW, disTM, disYL, disN, disTP, disST, disSK, disIS;

    public void getUser() {
        userId = getIntent().getStringExtra("UUID");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        getUser();
        setupNTDis();
        setupKLDis();
        setupHKDis();
        setupMap();

        initView();
        setSupportActionBar(toolbar);

    }

    private void setupRecyclerView() {
        rvNT.setNestedScrollingEnabled(false);
        rvNT.setLayoutManager(new LinearLayoutManager(this));
        rvNT.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvNT.setAdapter(new RegionAdapter(arrayNT, mapNT, this, userId));
        rvKL.setNestedScrollingEnabled(false);
        rvKL.setLayoutManager(new LinearLayoutManager(this));
        rvKL.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvKL.setAdapter(new RegionAdapter(arrayKL, mapKL, this, userId));
        rvHK.setNestedScrollingEnabled(false);
        rvHK.setLayoutManager(new LinearLayoutManager(this));
        rvHK.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvHK.setAdapter(new RegionAdapter(arrayHK, mapHK, this, userId));
    }

    private void initView() {
        tvNT = findViewById(R.id.tv_nt);
        tvKL = findViewById(R.id.tv_kl);
        tvHK = findViewById(R.id.tv_hk);
        rvNT = findViewById(R.id.rv_nt);
        rvKL = findViewById(R.id.rv_kl);
        rvHK = findViewById(R.id.rv_hk);
        recyclerViews.add(rvNT);
        recyclerViews.add(rvKL);
        recyclerViews.add(rvHK);

        setupRecyclerView();

        tvNT.setOnClickListener(view -> setVisibility(0));
        tvKL.setOnClickListener(view -> setVisibility(1));
        tvHK.setOnClickListener(view -> setVisibility(2));

        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("選擇地區");
    }

    private void setVisibility(int index) {
        setBoolean(index);
        for (int i = 0; i < booleans.length; i++) {

            recyclerViews.get(i).setVisibility(booleans[i] ? View.VISIBLE : View.GONE);
            Log.d(TAG, "setVisibility: SETTING " + i + " VIS: " + booleans[i]);
        }
    }

    private void setBoolean(int index) {
        for (int i = 0; i < booleans.length; i++) {
            if (i != index)
                booleans[i] = false;
            else
                booleans[index] = !booleans[index];
        }
    }

    public void setupMap() {
        arrayNT = new ArrayList<>(addressSplit("葵青區、荃灣區、屯門區、元朗區、北區、大埔區、沙田區、西貢區、離島區"));
        arrayKL = new ArrayList<>(addressSplit("油尖旺區、深水埗區、九龍城區、黃大仙區、觀塘區"));
        arrayHK = new ArrayList<>(addressSplit("中西區、灣仔區、東區、南區"));
        mapNT = new HashMap<>();
        mapKL = new HashMap<>();
        mapHK = new HashMap<>();
        int i = 0;
        mapNT.put(arrayNT.get(i), new District(arrayNT.get(i++), disKwT, R.drawable.kwai_tsing_district_logo));
        mapNT.put(arrayNT.get(i), new District(arrayNT.get(i++), disTW, R.drawable.tsuen_wan_district_logo));
        mapNT.put(arrayNT.get(i), new District(arrayNT.get(i++), disTM, R.drawable.tuen_mun_district_logo));
        mapNT.put(arrayNT.get(i), new District(arrayNT.get(i++), disYL, R.drawable.yuen_long_district_logo));
        mapNT.put(arrayNT.get(i), new District(arrayNT.get(i++), disN, R.drawable.north_district_logo));
        mapNT.put(arrayNT.get(i), new District(arrayNT.get(i++), disTP, R.drawable.tai_po_district_logo));
        mapNT.put(arrayNT.get(i), new District(arrayNT.get(i++), disST, R.drawable.shatin_district_logo));
        mapNT.put(arrayNT.get(i), new District(arrayNT.get(i++), disSK, R.drawable.sai_kung_district_logo));
        mapNT.put(arrayNT.get(i), new District(arrayNT.get(i), disIS, R.drawable.islands_district_logo));

        i = 0;
        mapKL.put(arrayKL.get(i), new District(arrayKL.get(i++), disYTM, R.drawable.yau_tsim_mong_district_logo));
        mapKL.put(arrayKL.get(i), new District(arrayKL.get(i++), disSSP, R.drawable.sham_shui_po_district_logo));
        mapKL.put(arrayKL.get(i), new District(arrayKL.get(i++), disKLC, R.drawable.kowloon_city_district_logo));
        mapKL.put(arrayKL.get(i), new District(arrayKL.get(i++), disWTS, R.drawable.wong_tai_sin_district_logo));
        mapKL.put(arrayKL.get(i), new District(arrayKL.get(i), disKT, R.drawable.kwun_tong_district_logo));

        i = 0;
        mapHK.put(arrayHK.get(i), new District(arrayHK.get(i++), disCnW, R.drawable.central_and_western_district_logo));
        mapHK.put(arrayHK.get(i), new District(arrayHK.get(i++), disWC, R.drawable.wan_chai_district_logo));
        mapHK.put(arrayHK.get(i), new District(arrayHK.get(i++), disE, R.drawable.eastern_district_logo));
        mapHK.put(arrayHK.get(i), new District(arrayHK.get(i), disS, R.drawable.southern_district_logo));
    }

    public void setupNTDis() {
        disKwT = new ArrayList<>(addressSplit("葵涌、青衣"));
        disTW = new ArrayList<>(addressSplit("荃灣、梨木樹、汀九、深井、青龍頭、馬灣、欣澳"));
        disTM = new ArrayList<>(addressSplit("大欖涌、掃管笏、屯門、藍地"));
        disYL = new ArrayList<>(addressSplit("洪水橋、廈村、流浮山、天水圍、元朗、新田、落馬洲、錦田、石崗、八鄉"));
        disN = new ArrayList<>(addressSplit("粉嶺、聯和墟、上水、石湖墟、沙頭角、鹿頸、烏蛟騰"));
        disTP = new ArrayList<>(addressSplit("大埔墟、大埔、大埔滘、大尾篤、船灣、樟木頭、企嶺下"));
        disST = new ArrayList<>(addressSplit("大圍、沙田、火炭、馬料水、烏溪沙、馬鞍山"));
        disSK = new ArrayList<>(addressSplit("清水灣、西貢、大網仔、將軍澳、坑口、調景嶺、馬游塘"));
        disIS = new ArrayList<>(addressSplit("長洲、坪洲、大嶼山、東涌、南丫島"));
    }

    public void setupKLDis() {
        disYTM = new ArrayList<>(addressSplit("尖沙咀、油麻地、西九龍填海區、京士柏、旺角、大角咀"));
        disSSP = new ArrayList<>(addressSplit("美孚、荔枝角、長沙灣、深水埗、石硤尾、又一村、大窩坪、昂船洲"));
        disKLC = new ArrayList<>(addressSplit("紅磡、土瓜灣、馬頭角、馬頭圍、啟德、九龍城、何文田、九龍塘、筆架山"));
        disWTS = new ArrayList<>(addressSplit("新蒲崗、黃大仙、東頭、橫頭磡、樂富、鑽石山、慈雲山、牛池灣"));
        disKT = new ArrayList<>(addressSplit("坪石、九龍灣、牛頭角、佐敦谷、觀塘、秀茂坪、藍田、油塘、鯉魚門"));
    }

    public void setupHKDis() {
        disCnW = new ArrayList<>(addressSplit("堅尼地城、石塘咀、西營盤、上環、中環、金鐘、半山區、山頂"));
        disWC = new ArrayList<>(addressSplit("灣仔、銅鑼灣、跑馬地、大坑、掃桿埔、渣甸山"));
        disE = new ArrayList<>(addressSplit("天后、寶馬山、北角、鰂魚涌、西灣河、筲箕灣、柴灣、小西灣"));
        disS = new ArrayList<>(addressSplit("薄扶林、香港仔、鴨脷洲、黃竹坑、壽臣山、淺水灣、舂磡角、赤柱、大潭、石澳"));
    }

    public List<String> addressSplit(String address) {
        return Arrays.asList(address.split("、"));
    }

}