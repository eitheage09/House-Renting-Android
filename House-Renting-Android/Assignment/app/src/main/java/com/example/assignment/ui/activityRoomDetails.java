package com.example.assignment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.assignment.R;
import com.example.assignment.controller.AuthViewModel;
import com.example.assignment.controller.RoomViewModel;
import com.example.assignment.model.Room;
import com.example.assignment.databinding.FragmentActivityRoomDetailsBinding;
import com.example.assignment.util.ImageSliderAdapter;
import com.example.assignment.util.ImageUtils;
import com.example.assignment.util.RoomAdapter;

import java.util.ArrayList;
import java.util.List;

public class activityRoomDetails extends Fragment {

    private FragmentActivityRoomDetailsBinding binding;
    private RoomViewModel roomVM;
    private NavController nav;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentActivityRoomDetailsBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);

        String roomId = getArguments() != null ? getArguments().getString("roomId", "") : "";
        roomVM = new ViewModelProvider(requireActivity()).get(RoomViewModel.class);

        Room room = roomVM.get(roomId);
        if (room == null) { nav.navigateUp(); return null; }

        List<Integer> images = new ArrayList<>();

        images.add(R.drawable.house1);
        images.add(R.drawable.house2);
        images.add(R.drawable.house3);

        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(images);
        binding.imageSliderDetail.setAdapter(imageSliderAdapter);

        binding.txtRoomDName.setText(room.getRoomName());
        binding.txtRoomDPlace.setText(room.getRoomPlace());
        binding.txtRoomDPrice.setText(String.format("RM %.2f", room.getRoomPrice()));
        binding.txtRoomD.setText(room.getRoomDetail());

        RoomAdapter.FacilityAdapter adapter = new RoomAdapter.FacilityAdapter(
                room.getHomeAmenities(), room.getRoomSize(),
                room.getNumOfBath(), room.getNumOfBed(), room.getRoomType());
        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }
}
