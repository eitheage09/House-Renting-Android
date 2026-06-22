package com.example.assignment.ui;

import android.os.Bundle;
import android.view.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import com.example.assignment.R;
import com.example.assignment.controller.AuthViewModel;
import com.example.assignment.controller.RoomViewModel;
import com.example.assignment.controller.FavoriteViewModel;
import com.example.assignment.databinding.FragmentActivityBinding;
import com.example.assignment.util.RoomActivityAdapter;
import com.example.assignment.util.PostCountListener;

public class activity extends Fragment implements PostCountListener {

    private FragmentActivityBinding binding;
    private RoomViewModel vm;
    private RoomActivityAdapter adapter;
    private AuthViewModel auth;
    private NavController nav;

    private FavoriteViewModel favoriteVM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentActivityBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);

        vm = new ViewModelProvider(requireActivity()).get(RoomViewModel.class);
        favoriteVM = new ViewModelProvider(requireActivity()).get(FavoriteViewModel.class);
        auth = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        adapter = new RoomActivityAdapter(
                (holder, room) -> holder.binding.getRoot().setOnClickListener(v -> {
                    Bundle args = new Bundle();
                    args.putString("roomId", room.getRoomId());
                    nav.navigate(R.id.activityRoomDetails, args);
                }),
                this);

        binding.rdView.setAdapter(adapter);
        binding.rdView.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        vm.getAllRoomByUser(auth.getUser().getEmail()).observe(getViewLifecycleOwner(), adapter::submitList);

        binding.btnCreate.setOnClickListener(v -> nav.navigate(R.id.activityAddPostFragment));
        binding.btnFilter.setOnClickListener(v -> { });

        return binding.getRoot();
    }

    @Override
    public void onPostCountUpdated(int count) {
        binding.txtPostNum.setText(count + " 个帖子" );
    }

    @Override
    public void onPause() {
        super.onPause();
        vm.clearSearch();
    }
}
