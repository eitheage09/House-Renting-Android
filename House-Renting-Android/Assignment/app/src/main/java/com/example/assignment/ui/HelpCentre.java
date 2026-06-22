package com.example.assignment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;
import com.example.assignment.model.DataClass;
import com.example.assignment.util.HelpCentreAdapter;
import com.example.assignment.databinding.FragmentHelpCentreBinding;

import java.util.ArrayList;

public class HelpCentre extends Fragment {
    private FragmentHelpCentreBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<DataClass> dataList;
    private ArrayList<DataClass> searchList;
    private HelpCentreAdapter adapter;
    private NavController nav;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHelpCentreBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);

        int[] imageList = { R.drawable.helpcentre_roomrtl, R.drawable.helpcentre_furn, R.drawable.helpcentre_avdscam,
                R.drawable.helpcentre_roomfil, R.drawable.helpcentre_commit, R.drawable.helpcentre_cleanhome,
                R.drawable.helpcentre_rules, R.drawable.helpcentre_exactrent, R.drawable.helpcentre_policy };
        String[] titleList = { "什么是租房", "房间是否配备家具", "避免诈骗的方法",
                "房间筛选说明", "住户与访客承诺", "谁负责打扫",
                "房屋规则", "确切租金是多少", "COVID-19政策" };
        String[] descList = { getString(R.string.RoomRental), getString(R.string.RoomFurnished), getString(R.string.Scam),
                getString(R.string.RoomFilter), getString(R.string.Commitments), getString(R.string.Cleans),
                getString(R.string.Rules), getString(R.string.ExactRent), getString(R.string.COVID) };

        recyclerView = binding.recyclerView;
        SearchView searchView = binding.search;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setHasFixedSize(true);

        dataList = new ArrayList<>();
        searchList = new ArrayList<>();
        for (int i = 0; i < imageList.length; i++) {
            dataList.add(new DataClass(imageList[i], titleList[i], descList[i]));
        }
        searchList.addAll(dataList);

        adapter = new HelpCentreAdapter(searchList);
        recyclerView.setAdapter(adapter);

        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { searchView.clearFocus(); return true; }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchList.clear();
                String text = newText.toLowerCase();
                if (!text.isEmpty()) {
                    for (DataClass item : dataList) {
                        if (item.getDataTitle().toLowerCase().contains(text)) searchList.add(item);
                    }
                } else {
                    searchList.addAll(dataList);
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        return binding.getRoot();
    }
}
