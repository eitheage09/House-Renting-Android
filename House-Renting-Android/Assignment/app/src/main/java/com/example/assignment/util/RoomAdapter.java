package com.example.assignment.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;
import com.example.assignment.model.Room;
import com.example.assignment.databinding.ItemRoomdetailsBinding;
import com.example.assignment.databinding.ItemFacilityBinding;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends ListAdapter<Room, RoomAdapter.ViewHolder> {

    private final OnItemClickListener listener;

    public RoomAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ItemRoomdetailsBinding binding;

        public ViewHolder(ItemRoomdetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void updateFavoriteIcon(boolean isFavorite) {
            int drawableRes = isFavorite ? R.drawable.ic_is_favorite : R.drawable.ic_favorite2;
            android.graphics.drawable.Drawable drawable = ContextCompat.getDrawable(
                    binding.getRoot().getContext(), drawableRes);
            binding.btnFav.setImageDrawable(drawable);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRoomdetailsBinding binding = ItemRoomdetailsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
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
                            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                            false));
        }

        RecyclerView.Adapter currentAdapter = holder.binding.facView.getAdapter();
        if (!(currentAdapter instanceof FacilityAdapter)
                || ((FacilityAdapter) currentAdapter).needsUpdate(
                room.getHomeAmenities(), room.getRoomSize(),
                room.getNumOfBath(), room.getNumOfBed(), room.getRoomType())) {
            FacilityAdapter facilityAdapter = new FacilityAdapter(
                    room.getHomeAmenities(), room.getRoomSize(),
                    room.getNumOfBath(), room.getNumOfBed(), room.getRoomType());
            holder.binding.facView.setAdapter(facilityAdapter);
        }

        holder.updateFavoriteIcon(room.isFavorite());
        listener.onItemClick(holder, room);
    }

    public static class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.FacilityViewHolder> {

        private static final int VIEW_TYPE_ROOM_SIZE = 0;
        private static final int VIEW_TYPE_BATHROOM = 1;
        private static final int VIEW_TYPE_BEDROOM = 2;
        private static final int VIEW_TYPE_ROOM_TYPE = 3;
        private static final int VIEW_TYPE_AMENITY = 4;

        private final List<String> amenities;
        private final double roomSize;
        private final int numBath;
        private final int numBed;
        private final String roomType;

        public FacilityAdapter(List<String> amenities, double roomSize,
                               int numBath, int numBed, String roomType) {
            this.amenities = amenities;
            this.roomSize = roomSize;
            this.numBath = numBath;
            this.numBed = numBed;
            this.roomType = roomType;
        }

        public boolean needsUpdate(List<String> amenities, double roomSize,
                                   int numBath, int numBed, String roomType) {
            return !this.amenities.equals(amenities) || this.roomSize != roomSize
                    || this.numBath != numBath || this.numBed != numBed
                    || !this.roomType.equals(roomType);
        }

        public static class FacilityViewHolder extends RecyclerView.ViewHolder {
            public final ItemFacilityBinding binding;
            public FacilityViewHolder(ItemFacilityBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }

        @NonNull
        @Override
        public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemFacilityBinding binding = ItemFacilityBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new FacilityViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
            int viewType = getItemViewType(position);
            switch (viewType) {
                case VIEW_TYPE_ROOM_SIZE:
                    holder.binding.imgIcon.setImageResource(R.drawable.ic_room_size);
                    holder.binding.txtFacility.setText((int) roomSize + " 平方英尺");
                    break;
                case VIEW_TYPE_BATHROOM:
                    holder.binding.imgIcon.setImageResource(R.drawable.bathtub);
                    holder.binding.txtFacility.setText(numBath + " 浴室" + (numBath > 1 ? "s" : ""));
                    break;
                case VIEW_TYPE_BEDROOM:
                    holder.binding.imgIcon.setImageResource(R.drawable.icon_bedroom);
                    holder.binding.txtFacility.setText(numBed + " 卧室" + (numBed > 1 ? "s" : ""));
                    break;
                case VIEW_TYPE_ROOM_TYPE:
                    holder.binding.imgIcon.setImageResource(R.drawable.ic_room_type);
                    holder.binding.txtFacility.setText(roomType);
                    break;
                case VIEW_TYPE_AMENITY:
                    int amenityIdx = position - getExtraItemCount();
                    String amenity = amenities.get(amenityIdx);
                    int iconRes;
                    switch (amenity) {
                        case "Wi-Fi": iconRes = R.drawable.ic_wifi; break;
                        case "TV": iconRes = R.drawable.ic_tv; break;
                        case "Air-Conditioner": iconRes = R.drawable.ic_air_conditioner; break;
                        case "Washing Machine": iconRes = R.drawable.washing_machine; break;
                        default: iconRes = R.drawable.ic_unknown;
                    }
                    holder.binding.imgIcon.setImageResource(iconRes);
                    holder.binding.txtFacility.setText(amenity);
                    break;
            }
            holder.binding.imgIcon.setVisibility(View.VISIBLE);
            holder.binding.txtFacility.setVisibility(View.VISIBLE);
        }

        @Override
        public int getItemViewType(int position) {
            int adjustedPos = 0;
            if (roomSize > 0) { if (position == adjustedPos) return VIEW_TYPE_ROOM_SIZE; adjustedPos++; }
            if (numBath > 0) { if (position == adjustedPos) return VIEW_TYPE_BATHROOM; adjustedPos++; }
            if (numBed > 0) { if (position == adjustedPos) return VIEW_TYPE_BEDROOM; adjustedPos++; }
            if (roomType != null && !roomType.isEmpty()) { if (position == adjustedPos) return VIEW_TYPE_ROOM_TYPE; adjustedPos++; }
            return VIEW_TYPE_AMENITY;
        }

        private int getExtraItemCount() {
            int count = 0;
            if (roomSize > 0) count++;
            if (numBath > 0) count++;
            if (numBed > 0) count++;
            if (roomType != null && !roomType.isEmpty()) count++;
            return count;
        }

        @Override
        public int getItemCount() {
            return getExtraItemCount() + amenities.size();
        }
    }
}
