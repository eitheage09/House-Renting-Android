package com.example.assignment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.assignment.MainActivity;
import com.example.assignment.R;
import com.example.assignment.controller.AuthViewModel;
import com.example.assignment.controller.FavoriteViewModel;
import com.example.assignment.controller.RoomViewModel;
import com.example.assignment.controller.UserViewModel;
import com.example.assignment.model.Room;
import com.example.assignment.databinding.FragmentRoomDetailsBinding;
import com.example.assignment.util.ImageSliderAdapter;
import com.example.assignment.util.FragmentUtils;
import com.example.assignment.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class roomDetails extends Fragment {

    private FragmentRoomDetailsBinding binding;
    private RoomViewModel roomVM;
    private FavoriteViewModel favoriteVM;
    private AuthViewModel auth;
    private NavController nav;

    private UserViewModel users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRoomDetailsBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);

        String roomId = getArguments() != null ? getArguments().getString("roomId", "") : "";
        boolean isFavorite = getArguments() != null && getArguments().getBoolean("isFavorite", false);

        roomVM = new ViewModelProvider(requireActivity()).get(RoomViewModel.class);
        favoriteVM = new ViewModelProvider(requireActivity()).get(FavoriteViewModel.class);
        auth = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        users = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        Room room = roomVM.get(roomId);
        if (room == null) { nav.navigateUp(); return null; }

        if (auth.getUser() != null && auth.getUser().getEmail().equals(room.getUserId())) {
            binding.btnFavDetail.setVisibility(View.GONE);
            binding.btnMessage.setVisibility(View.GONE);
        } else {
            binding.btnFavDetail.setVisibility(View.VISIBLE);
            binding.btnMessage.setVisibility(View.VISIBLE);
        }

        int drawableRes = isFavorite ? R.drawable.ic_is_favorite : R.drawable.ic_favorite2;
        binding.btnFavDetail.setImageDrawable(ContextCompat.getDrawable(requireContext(), drawableRes));

        auth.getUserLD().observe(getViewLifecycleOwner(), user -> {
            binding.btnFavDetail.setOnClickListener(v -> {
                room.setFavorite(!room.isFavorite());
                if (room.isFavorite()) {
                    if (user != null) favoriteVM.addToFavorites(user, room);
                } else {
                    if (user != null) favoriteVM.removeFromFavorites(user, room);
                }
                int newRes = room.isFavorite() ? R.drawable.ic_is_favorite : R.drawable.ic_favorite2;
                binding.btnFavDetail.setImageDrawable(ContextCompat.getDrawable(requireContext(), newRes));
            });

            binding.btnMessage.setOnClickListener(v -> {
                com.example.assignment.model.User otherUser = users.get(room.getUserId());
                Bundle args = new Bundle();
                args.putString("userMail", room.getUserId());
                nav.navigate(R.id.messageChatFragment, args);
            });
        });

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

        setupFacilitiesRecyclerView(room);

        return binding.getRoot();
    }

    private void setupFacilitiesRecyclerView(Room room) {
        com.example.assignment.util.RoomAdapter.FacilityAdapter adapter =
                new com.example.assignment.util.RoomAdapter.FacilityAdapter(
                        room.getHomeAmenities(), room.getRoomSize(),
                        room.getNumOfBath(), room.getNumOfBed(), room.getRoomType());
        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.hideBV();
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.showBV();
    }
}
