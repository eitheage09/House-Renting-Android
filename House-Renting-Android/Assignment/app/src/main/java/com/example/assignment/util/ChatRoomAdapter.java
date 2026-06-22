package com.example.assignment.util;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.model.ChatRoom;
import com.example.assignment.databinding.ItemUsermessageBinding;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

public class ChatRoomAdapter extends ListAdapter<ChatRoom, ChatRoomAdapter.ViewHolder> {

    private final OnItemClickListener listener;

    public ChatRoomAdapter(OnItemClickListener listener) {
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
        public final ItemUsermessageBinding binding;
        public ViewHolder(ItemUsermessageBinding binding) { super(binding.getRoot()); this.binding = binding; }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemUsermessageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatRoom chatRoom = getItem(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        if (dateFormat.format(new Date(chatRoom.getLastMessageTime()))
                .equals(dateFormat.format(new Date(System.currentTimeMillis())))) {
            holder.binding.msgTime.setText(timeFormat.format(new Date(chatRoom.getLastMessageTime())));
        } else {
            holder.binding.msgTime.setText(dateFormat.format(new Date(chatRoom.getLastMessageTime())));
        }

        if (chatRoom.isRead()) {
            holder.binding.imgChatRead.setSelected(true);
        } else {
            holder.binding.txtNewMsg.setTypeface(holder.binding.txtNewMsg.getTypeface(), Typeface.BOLD);
            holder.binding.imgChatRead.setSelected(false);
        }

        if (chatRoom.getUser() != null) {
            holder.binding.txtMsgUserName.setText(chatRoom.getUser().getName());
            ImageUtils.setImageBytes(holder.binding.imgMsgUser, chatRoom.getUser().getPhoto());
        }
        holder.binding.txtNewMsg.setText(chatRoom.getLastMessage());

        listener.onItemClick(holder, chatRoom);
    }
}
