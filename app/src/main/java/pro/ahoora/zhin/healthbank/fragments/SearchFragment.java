package pro.ahoora.zhin.healthbank.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import pro.ahoora.zhin.healthbank.R;
import pro.ahoora.zhin.healthbank.activitys.OfficeActivity;
import pro.ahoora.zhin.healthbank.adapters.SearchAdapter;
import pro.ahoora.zhin.healthbank.models.RealmItemModel;
import pro.ahoora.zhin.healthbank.utils.SharedPer;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rvSearch;
    private AppCompatTextView tvItemNum;
    private AppCompatEditText etSearch;
    private AppCompatImageView ivSearch;
    private SearchAdapter adapter;
    private CircularProgressView progressView;
    private int groupId = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initViews(view);
        try {
            groupId = ((OfficeActivity) getActivity()).getGroupId();
            groupId++;
        } catch (Exception e) {
            Log.e("ERR_GID", e.getMessage() + "");
        }
        etSearch.requestFocus();
        return view;
    }

    private void initViews(View view) {
        progressView = view.findViewById(R.id.cpv_progress);
        rvSearch = view.findViewById(R.id.rv_search);
        tvItemNum = view.findViewById(R.id.tv_itemNums);
        etSearch = view.findViewById(R.id.et_search);
        ivSearch = view.findViewById(R.id.iv_search);
        ivSearch.setOnClickListener(this);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                dataBaseSearch(editable.toString());
            }
        });

    }

    private void search() {
        if (!etSearch.getText().toString().equals("")) {
            dataBaseSearch(etSearch.getText().toString().trim());
        } else {
            Toast.makeText(getActivity(), "نام دکتر نباید خالی باشد", Toast.LENGTH_SHORT).show();
        }
    }

    private void dataBaseSearch(String searchedText) {
        progressView.setVisibility(View.VISIBLE);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        RealmResults<RealmItemModel> result = realm.where(RealmItemModel.class).equalTo("groupId", groupId).findAll();
        if (result.size() > 0) {
            ArrayList<RealmItemModel> list1 = new ArrayList<>();
            for (RealmItemModel item : result) {
                if (item.getFirstName().contains(searchedText) || item.getLastName().contains(searchedText)) {
                    list1.add(item);
                }
            }
            if (list1.size() != 0) {
                initList(list1);
            } else {
                tvItemNum.setText("موردی یافت نشد .");
                rvSearch.setAdapter(null);
            }
            realm.commitTransaction();
        } else {
            tvItemNum.setText("موردی یافت نشد .");
            rvSearch.setAdapter(null);
        }
        progressView.setVisibility(View.GONE);

    }

    private void closeKeyBoard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.e("ERR_KEYBOARD", e.getMessage() + " ");
        }
    }

    private void initList(List<RealmItemModel> dataSet) {
        adapter = new SearchAdapter(getActivity(), dataSet);
        rvSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSearch.setAdapter(adapter);
        tvItemNum.setText(dataSet.size() + " مورد پیدا شد .");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().post("loaded");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                search();
                closeKeyBoard();
        }
    }
}
