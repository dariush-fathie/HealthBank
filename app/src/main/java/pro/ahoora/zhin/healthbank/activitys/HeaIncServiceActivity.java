package pro.ahoora.zhin.healthbank.activitys;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import pro.ahoora.zhin.healthbank.R;
import pro.ahoora.zhin.healthbank.adapters.HeaIncAdapter;

public class HeaIncServiceActivity extends AppCompatActivity {

    HeaIncAdapter adapter;
    LinearLayoutManager layoutManger;
    RecyclerView rv_hea_inc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hea);

        ABC();
        init();
    }

    private void init() {
        adapter=new HeaIncAdapter(HeaIncServiceActivity.this);
        layoutManger = new LinearLayoutManager(getApplicationContext());
        rv_hea_inc.setLayoutManager(layoutManger);
        rv_hea_inc.setAdapter(adapter);

    }

    private void ABC() {
        rv_hea_inc=findViewById(R.id.rv_hea_inc);

    }
}
