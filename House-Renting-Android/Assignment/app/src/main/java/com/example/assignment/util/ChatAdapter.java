package com.example.assignment.util;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.model.Chat;
import com.example.assignment.databinding.ItemMessagedateBinding;
import com.example.assignment.databinding.ItemReceiverimageBinding;
import com.example.assignment.databinding.ItemReceivermessageBinding;
import com.example.assignment.databinding.ItemSenderimageBinding;
import com.example.assignment.databinding.ItemSendermessageBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatAdapter extends ListAdapter<Chat, RecyclerView.ViewHolder> {

    public ChatAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Chat> DIFF_CALLBACK = new DiffUtil.ItemCallback<Chat>() {
        @Override
        public boolean areItemsTheSame(@NonNull Chat a, @NonNull Chat b) {
            return a.getChatId() != null && a.getChatId().equals(b.getChatId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Chat a, @NonNull Chat b) {
            return a.equals(b);
        }
    };

    public static class LeftViewHolder extends RecyclerView.ViewHolder {
        public final ItemReceivermessageBinding binding;
        public LeftViewHolder(ItemReceivermessageBinding binding) { super(binding.getRoot()); this.binding = binding; }
    }

    public static class RightViewHolder extends RecyclerView.ViewHolder {
        public final ItemSendermessageBinding binding;
        public RightViewHolder(ItemSendermessageBinding binding) { super(binding.getRoot()); this.binding = binding; }
    }

    public static class LeftImageViewHolder extends RecyclerView.ViewHolder {
        public final ItemReceiverimageBinding binding;
        public LeftImageViewHolder(ItemReceiverimageBinding binding) { super(binding.getRoot()); this.binding = binding; }
    }

    public static class RightImageViewHolder extends RecyclerView.ViewHolder {
        public final ItemSenderimageBinding binding;
        public RightImageViewHolder(ItemSenderimageBinding binding) { super(binding.getRoot()); this.binding = binding; }
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        public final ItemMessagedateBinding binding;
        public DateViewHolder(ItemMessagedateBinding binding) { super(binding.getRoot()); this.binding = binding; }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 1: return new LeftViewHolder(ItemReceivermessageBinding.inflate(inflater, parent, false));
            case 2: return new RightViewHolder(ItemSendermessageBinding.inflate(inflater, parent, false));
            case 3: return new LeftImageViewHolder(ItemReceiverimageBinding.inflate(inflater, parent, false));
            case 4: return new RightImageViewHolder(ItemSenderimageBinding.inflate(inflater, parent, false));
            default: return new DateViewHolder(ItemMessagedateBinding.inflate(inflater, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat chat = getItem(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy - MM - dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        if (holder instanceof RightViewHolder) {
            RightViewHolder h = (RightViewHolder) holder;
            h.binding.txtSendMsg.setText(chat.getMessage());
            h.binding.txtMsgTime.setText(timeFormat.format(new Date(chat.getMessageTime())));
            h.binding.imgReadStatus.setSelected(chat.isRead());
        } else if (holder instanceof RightImageViewHolder) {
            RightImageViewHolder h = (RightImageViewHolder) holder;
            ImageUtils.setImageBytes(h.binding.imgSend, chat.getImage());
            h.binding.txtMsgTime.setText(timeFormat.format(new Date(chat.getMessageTime())));
            h.binding.imgReadStatus.setSelected(chat.isRead());
        } else if (holder instanceof LeftImageViewHolder) {
            LeftImageViewHolder h = (LeftImageViewHolder) holder;
            ImageUtils.setImageBytes(h.binding.imgMsgUser, chat.getUser().getPhoto());
            ImageUtils.setImageBytes(h.binding.imgSend, chat.getImage());
            h.binding.txtMsgTime.setText(timeFormat.format(new Date(chat.getMessageTime())));
        } else if (holder instanceof LeftViewHolder) {
            LeftViewHolder h = (LeftViewHolder) holder;
            ImageUtils.setImageBytes(h.binding.imgMsgUser, chat.getUser().getPhoto());
            h.binding.txtReceiveMsg.setText(chat.getMessage());
            h.binding.txtMsgTime.setText(timeFormat.format(new Date(chat.getMessageTime())));
        } else if (holder instanceof DateViewHolder) {
            DateViewHolder h = (DateViewHolder) holder;
            h.binding.txtDate.setText(dateFormat.format(new Date(chat.getMessageTime())));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = getItem(position);
        String type = chat.getChatType();
        if ("left".equals(type)) return 1;
        if ("right".equals(type)) return 2;
        if ("leftimage".equals(type)) return 3;
        if ("rightimage".equals(type)) return 4;
        return 0;
    }
}
