package com.example.assignment.util;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;
import com.example.assignment.model.Favorite;
import com.example.assignment.model.Room;
import com.example.assignment.repository.RoomRepository;
import com.example.assignment.databinding.ItemRoomdetailsBinding;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends ListAdapter<Favorite, FavoriteAdapter.ViewHolder> {

    private final RoomRepository roomRepository;
    private final OnItemClickListener listener;

    public FavoriteAdapter(RoomRepository roomRepository, OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.roomRepository = roomRepository;
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Favorite> DIFF_CALLBACK = new DiffUtil.ItemCallback<Favorite>() {
        @Override
        public boolean areItemsTheSame(@NonNull Favorite a, @NonNull Favorite b) {
            return a.getRoomId() != null && a.getRoomId().equals(b.getRoomId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Favorite a, @NonNull Favorite b) {
            return a.equals(b);
        }
    };

    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, Favorite favorite);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ItemRoomdetailsBinding binding;
        public ViewHolder(ItemRoomdetailsBinding binding) { super(binding.getRoot()); this.binding = binding; }

        public void updateFavoriteIcon(boolean isFavorite) {
            int drawableRes = isFavorite ? R.drawable.ic_is_favorite : R.drawable.ic_favorite2;
            android.graphics.drawable.Drawable drawable =
                    androidx.core.content.ContextCompat.getDrawable(binding.getRoot().getContext(), drawableRes);
            binding.btnFav.setImageDrawable(drawable);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRoomdetailsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Favorite favorite = getItem(position);
        Room room = roomRepository.get(favorite.getRoomId());
        if (room != null) {
            favorite.setRoom(room);
        }

        List<Integer> images = new ArrayList<>();

        images.add(R.drawable.house1);
        images.add(R.drawable.house2);
        images.add(R.drawable.house3);

        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(images);

        holder.binding.imageSlider.setAdapter(imageSliderAdapter);

        favorite.getRoom().setFavorite(true);
        holder.binding.txtPrice.setText(String.format("RM %.2f", favorite.getRoom().getRoomPrice()));
        holder.binding.txtAddress.setText(favorite.getRoom().getRoomPlace());
        holder.binding.txtRoomName.setText(favorite.getRoom().getRoomName());

        if (holder.binding.facView.getLayoutManager() == null) {
            holder.binding.facView.setLayoutManager(
                    new androidx.recyclerview.widget.LinearLayoutManager(
                            holder.itemView.getContext(),
                            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false));
        }

        RecyclerView.Adapter currentAdapter = holder.binding.facView.getAdapter();
        if (!(currentAdapter instanceof RoomAdapter.FacilityAdapter)
                || ((RoomAdapter.FacilityAdapter) currentAdapter).needsUpdate(
                favorite.getRoom().getHomeAmenities(), favorite.getRoom().getRoomSize(),
                favorite.getRoom().getNumOfBath(), favorite.getRoom().getNumOfBed(),
                favorite.getRoom().getRoomType())) {
            RoomAdapter.FacilityAdapter facilityAdapter = new RoomAdapter.FacilityAdapter(
                    favorite.getRoom().getHomeAmenities(), favorite.getRoom().getRoomSize(),
                    favorite.getRoom().getNumOfBath(), favorite.getRoom().getNumOfBed(),
                    favorite.getRoom().getRoomType());
            holder.binding.facView.setAdapter(facilityAdapter);
        }

        holder.updateFavoriteIcon(favorite.getRoom().isFavorite());
        listener.onItemClick(holder, favorite);
    }
}
