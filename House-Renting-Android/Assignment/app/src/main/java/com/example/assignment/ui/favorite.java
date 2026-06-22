package com.example.assignment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.assignment.R;
import com.example.assignment.controller.AuthViewModel;
import com.example.assignment.controller.RoomViewModel;
import com.example.assignment.databinding.FragmentFavoriteBinding;
import com.example.assignment.model.Favorite;
import com.example.assignment.util.FavoriteAdapter;
import com.example.assignment.util.RoomAdapter;
import com.example.assignment.model.RoomFilterOptions;
import com.example.assignment.repository.RoomRepository;
import com.example.assignment.repository.FavoriteRepository;
import com.example.assignment.controller.FavoriteViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class favorite extends Fragment {
    private FragmentFavoriteBinding binding;
    private RoomViewModel roomVM;
    private NavController nav;

    private FavoriteViewModel favoriteVM;
    private AuthViewModel auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);
        roomVM = new ViewModelProvider(requireActivity()).get(RoomViewModel.class);
        favoriteVM = new ViewModelProvider(requireActivity()).get(FavoriteViewModel.class);
        auth = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        binding.frView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.frView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        final RoomRepository roomRepo = new RoomRepository();
        FavoriteAdapter adapter = new FavoriteAdapter(roomRepo, (holder, fav) -> {
            holder.binding.getRoot().setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putString("roomId", fav.getRoom().getRoomId());
                args.putBoolean("isFavorite", fav.getRoom().isFavorite());
                nav.navigate(R.id.roomDetails4, args);
            });
            auth.getUserLD().observe(getViewLifecycleOwner(), user -> {
                holder.binding.btnFav.setOnClickListener(v -> {
                    fav.getRoom().setFavorite(!fav.getRoom().isFavorite());
                    if (fav.getRoom().isFavorite()) favoriteVM.addToFavorites(user, fav.getRoom());
                    else favoriteVM.removeFromFavorites(user, fav.getRoom());
                });
            });
        });
        binding.frView.setAdapter(adapter);

        auth.getUserLD().observe(getViewLifecycleOwner(), user -> {
            favoriteVM.getFavoritesLD().observe(getViewLifecycleOwner(), favorites -> {
                List<Favorite> filtered = new java.util.ArrayList<>();
                for (Favorite f : favorites) {
                    if (f.getUserId().equals(user.getEmail())) filtered.add(f);
                }
                binding.txtResult.setText(filtered.size() + " 个结果");
                adapter.submitList(filtered);
            });
        });

        binding.btnFilter.setOnClickListener(v -> { });
        return binding.getRoot();
    }

    @Override
    public void onPause() { super.onPause(); favoriteVM.clearSearch(); }
}
