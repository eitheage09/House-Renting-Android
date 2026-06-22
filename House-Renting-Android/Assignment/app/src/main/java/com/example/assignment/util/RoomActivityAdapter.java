package com.example.assignment.util;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;
import com.example.assignment.model.Room;
import com.example.assignment.repository.FavoriteRepository;
import com.example.assignment.repository.RoomRepository;
import com.example.assignment.databinding.ItemActivityRoomdetailsBinding;

import java.util.ArrayList;
import java.util.List;

public class RoomActivityAdapter extends ListAdapter<Room, RoomActivityAdapter.ViewHolder> {

    private final OnItemClickListener listener;
    private final PostCountListener postCountListener;
    private Room currentRoom;

    public RoomActivityAdapter(OnItemClickListener listener, PostCountListener postCountListener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
        this.postCountListener = postCountListener;
    }

    private static final DiffUtil.ItemCallback<Room> DIFF_CALLBACK = new DiffUtil.ItemCallback<Room>() {
        @Override
        public boolean areItemsTheSame(@NonNull Room a, @NonNull Room b) {
            return a.getRoomId() != null && a.getRoomId().equals(b.getRoomId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Room a, @NonNull Room b) {
            return a.equals(b);
        }
    };

    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, Room room);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public final ItemActivityRoomdetailsBinding binding;

        public ViewHolder(ItemActivityRoomdetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuInflater inflater = new MenuInflater(v.getContext());
            inflater.inflate(R.menu.post_setting, menu);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemActivityRoomdetailsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = getItem(position);

        List<Integer> images = new ArrayList<>();

        images.add(R.drawable.house1);
        images.add(R.drawable.house2);
        images.add(R.drawable.house3);

        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(images);

        holder.binding.imageSlider.setAdapter(imageSliderAdapter);

        holder.binding.txtPrice.setText(String.format("RM %.2f", room.getRoomPrice()));
        holder.binding.txtAddress.setText(room.getRoomPlace());
        holder.binding.txtRoomName.setText(room.getRoomName());

        if (holder.binding.facView.getLayoutManager() == null) {
            holder.binding.facView.setLayoutManager(
                    new androidx.recyclerview.widget.LinearLayoutManager(
                            holder.itemView.getContext(),
                            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false));
        }

        RecyclerView.Adapter currentAdapter = holder.binding.facView.getAdapter();
        if (!(currentAdapter instanceof RoomAdapter.FacilityAdapter)
                || ((RoomAdapter.FacilityAdapter) currentAdapter).needsUpdate(
                room.getHomeAmenities(), room.getRoomSize(),
                room.getNumOfBath(), room.getNumOfBed(), room.getRoomType())) {
            RoomAdapter.FacilityAdapter facilityAdapter = new RoomAdapter.FacilityAdapter(
                    room.getHomeAmenities(), room.getRoomSize(),
                    room.getNumOfBath(), room.getNumOfBed(), room.getRoomType());
            holder.binding.facView.setAdapter(facilityAdapter);
        }

        holder.binding.getRoot().setOnLongClickListener(v -> {
            currentRoom = room;
            v.showContextMenu();
            return true;
        });

        listener.onItemClick(holder, room);
    }

    public boolean onContextItemSelected(MenuItem item, NavController nav,
                                          RoomRepository viewModel, Context context,
                                          FavoriteRepository favoriteVM) {
        int _id = item.getItemId();
        if (_id == R.id.opt1) {
                if (currentRoom != null) {
                    nav.navigate(R.id.activityUpdatePost,
                            new android.os.Bundle());
                }
                return true;
        } else if (_id == R.id.opt2) {
                if (currentRoom != null) {
                    showDeleteConfirmationDialog(currentRoom, viewModel, context, favoriteVM);
                }
                return true;
        } else {
                return false;
        }
    }

    private void showDeleteConfirmationDialog(Room room, RoomRepository viewModel,
                                                Context context, FavoriteRepository favoriteVM) {
        new AlertDialog.Builder(context)
                .setTitle("删除房间")
                .setMessage("您确定要删除此房间吗？")
                .setPositiveButton("确认", (dialog, which) -> {
                    viewModel.delete(room.getRoomId());
                    favoriteVM.deleteByRoomId(room.getRoomId());
                    Toast.makeText(context, "房间已删除", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    Toast.makeText(context, "已取消删除", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .create().show();
    }
}
