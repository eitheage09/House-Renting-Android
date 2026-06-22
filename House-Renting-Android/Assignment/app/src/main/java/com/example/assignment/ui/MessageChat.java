package com.example.assignment.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.assignment.R;
import com.example.assignment.controller.AuthViewModel;
import com.example.assignment.controller.ChatRoomViewModel;
import com.example.assignment.controller.ChatViewModel;
import com.example.assignment.controller.UserViewModel;
import com.example.assignment.databinding.FragmentMessageChatBinding;
import com.example.assignment.model.Chat;
import com.example.assignment.model.ChatRoom;
import com.example.assignment.model.User;
import com.example.assignment.util.ChatAdapter;
import com.example.assignment.util.ImageUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MessageChat extends Fragment {
    private FragmentMessageChatBinding binding;
    private ChatViewModel chatVM;
    private ChatRoomViewModel chatRoomVM;
    private AuthViewModel auth;
    private NavController nav;

    private UserViewModel userVM;
    private ChatAdapter adapter = new ChatAdapter();
    private String chatRoomId = "", userMail = "", email = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMessageChatBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);
        chatVM = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
        chatRoomVM = new ViewModelProvider(requireActivity()).get(ChatRoomViewModel.class);
        auth = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        userVM = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        if (getArguments() != null) {
            chatRoomId = getArguments().getString("chatRoomId", "");
            userMail = getArguments().getString("userMail", "");
        }
        email = auth.getUser().getEmail();

        binding.chatMsgRV.setAdapter(adapter);

        binding.btnChatSend.setOnClickListener(v -> sendTextMessage());
        binding.btnChatBack.setOnClickListener(v -> nav.navigateUp());

        if (!chatRoomId.isEmpty()) {
            ChatRoom room = chatRoomVM.get(chatRoomId);
            if (room != null) {
                userVM.getUserUpdate(room.getUser().getEmail()).observe(getViewLifecycleOwner(), users -> {
                    if (users != null && !users.isEmpty()) setUserInfo(users.get(0));
                });
                chatVM.setRead(chatRoomId, email);
                if (!email.equals(room.getLastSender())) {
                    room.setRead(true);
                    chatRoomVM.set(room);
                }
            }
        } else {
            userVM.getUserUpdate(userMail).observe(getViewLifecycleOwner(), users -> {
                if (users != null && !users.isEmpty()) setUserInfo(users.get(0));
            });
        }

        loadChats();
        chatRoomVM.getChatRoomLD().observe(getViewLifecycleOwner(), v -> loadChats());

        return binding.getRoot();
    }

    private void loadChats() {
        String roomId = chatRoomId;
        if (roomId.isEmpty()) {
            ChatRoom cr = chatRoomVM.getRoomByUser(userMail, email);
            if (cr != null) roomId = cr.getChatRoomId();
        }
        final String finalRoomId = roomId;
        if (!finalRoomId.isEmpty()) {
            chatVM.getAllByRoomId(finalRoomId).observe(getViewLifecycleOwner(), chatList -> {
                java.util.List<Chat> displayList = new java.util.ArrayList<>();
                Date lastDate = null;
                for (Chat c : chatList) {
                    c.setUser(userVM.get(c.getSenderId()));
                    Date currentDate = new Date(c.getMessageTime());
                    if (lastDate == null || !isSameDay(currentDate, lastDate)) {
                        Chat dateChat = new Chat();
                        dateChat.setMessageTime(c.getMessageTime());
                        displayList.add(dateChat);
                        lastDate = currentDate;
                    }
                    c.setChatType(determineType(c));
                    displayList.add(c);
                }
                adapter.submitList(displayList);
            });
        }
    }

    private String determineType(Chat chat) {
        if (chat.getSenderId().equals(email) && chat.getMessage().isEmpty()) return "rightimage";
        if (!chat.getSenderId().equals(email) && chat.getMessage().isEmpty()) return "leftimage";
        if (chat.getSenderId().equals(email)) return "right";
        return "left";
    }

    private void sendTextMessage() {
        String text = binding.txtSend.getText().toString().trim();
        if (text.isEmpty()) return;

        String roomId = chatRoomId;
        if (roomId.isEmpty()) {
            if (chatRoomVM.getRoomByUser(userMail, email) == null) {
                ChatRoom newRoom = new ChatRoom();
                newRoom.setUserId(java.util.Arrays.asList(userMail, email));
                chatRoomVM.add(newRoom);
            }
            chatRoomVM.getChatRoomLD().observe(getViewLifecycleOwner(), rooms -> {
                ChatRoom room = chatRoomVM.getRoomByUser(userMail, email);
                if (room != null) {
                    doSendMessage(text, room.getChatRoomId());
                    chatRoomVM.getChatRoomLD().removeObservers(getViewLifecycleOwner());
                }
            });
        } else {
            doSendMessage(text, roomId);
        }
    }

    private void doSendMessage(String messageText, String roomId) {
        Chat chat = new Chat();
        chat.setChatRoomId(roomId);
        chat.setSenderId(email);
        chat.setMessage(messageText);
        chatVM.add(chat);

        ChatRoom room = chatRoomVM.get(roomId);
        if (room != null) {
            room.setLastSender(email);
            room.setLastMessage(messageText);
            room.setLastMessageTime(chat.getMessageTime());
            room.setRead(false);
            chatRoomVM.set(room);
        }
        binding.txtSend.getText().clear();
    }

    private void setUserInfo(User user) {
        ImageUtils.setImageBytes(binding.imgChatUser, user.getPhoto());
        binding.txtSendName.setText(user.getName());
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        SimpleDateFormat tf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        if ("online".equals(user.getStatus())) binding.txtStatus.setText("Online");
        else if (df.format(new Date(user.getLastLoginTime())).equals(df.format(new Date(System.currentTimeMillis()))))
            binding.txtStatus.setText("最后在线时间：今天 " + tf.format(new Date(user.getLastLoginTime())));
        else
            binding.txtStatus.setText("最后在线时间：" + df.format(new Date(user.getLastLoginTime())));
    }

    private boolean isSameDay(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance(); c1.setTime(d1);
        Calendar c2 = Calendar.getInstance(); c2.setTime(d2);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }
}
