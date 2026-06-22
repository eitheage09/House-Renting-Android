package com.example.assignment.util;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.model.ChatRoom;
import com.example.assignment.databinding.ItemOnlineuserBinding;

public class OnlineUserAdapter extends ListAdapter<ChatRoom, OnlineUserAdapter.ViewHolder> {

    private final OnItemClickListener listener;

    public OnlineUserAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<ChatRoom> DIFF_CALLBACK = new DiffUtil.ItemCallback<ChatRoom>() {
        @Override
        public boolean areItemsTheSame(@NonNull ChatRoom a, @NonNull ChatRoom b) {
            return a.getChatRoomId() != null && a.getChatRoomId().equals(b.getChatRoomId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ChatRoom a, @NonNull ChatRoom b) {
            return a.equals(b);
        }
    };

    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, ChatRoom chatRoom);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ItemOnlineuserBinding binding;
        public ViewHolder(ItemOnlineuserBinding binding) { super(binding.getRoot()); this.binding = binding; }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemOnlineuserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatRoom chatRoom = getItem(position);
        if (chatRoom.getUser() != null) {
            ImageUtils.setImageBytes(holder.binding.imgMsgUser, chatRoom.getUser().getPhoto());
            holder.binding.txtName.setText(chatRoom.getUser().getName());
        }
        listener.onItemClick(holder, chatRoom);
    }
}
