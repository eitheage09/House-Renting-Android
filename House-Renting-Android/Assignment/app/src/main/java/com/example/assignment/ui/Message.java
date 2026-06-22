package com.example.assignment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.assignment.R;
import com.example.assignment.controller.AuthViewModel;
import com.example.assignment.controller.ChatRoomViewModel;
import com.example.assignment.controller.UserViewModel;
import com.example.assignment.databinding.FragmentMessageBinding;
import com.example.assignment.model.ChatRoom;
import com.example.assignment.util.ChatRoomAdapter;
import com.example.assignment.util.OnlineUserAdapter;

public class Message extends Fragment {
    private FragmentMessageBinding binding;
    private ChatRoomViewModel chatRoomVM;
    private AuthViewModel auth;
    private NavController nav;

    private UserViewModel userVM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);
        chatRoomVM = new ViewModelProvider(requireActivity()).get(ChatRoomViewModel.class);
        auth = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        userVM = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        String email = auth.getUser().getEmail();

        ChatRoomAdapter roomAdapter = new ChatRoomAdapter((holder, chatRoom) -> {
            holder.binding.getRoot().setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putString("chatRoomId", chatRoom.getChatRoomId());
                args.putString("token", chatRoom.getUser().getFcmToken());
                args.putString("userMail", chatRoom.getUser().getEmail());
                nav.navigate(R.id.messageChatFragment, args);
            });
        });

        OnlineUserAdapter onlineAdapter = new OnlineUserAdapter((holder, chatRoom) -> {
            holder.binding.getRoot().setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putString("chatRoomId", chatRoom.getChatRoomId());
                args.putString("token", chatRoom.getUser().getFcmToken());
                args.putString("userMail", chatRoom.getUser().getEmail());
                nav.navigate(R.id.messageChatFragment, args);
            });
        });

        binding.msgRV.setAdapter(roomAdapter);
        chatRoomVM.getAllByEmail(email).observe(getViewLifecycleOwner(), chatRooms -> {
            for (ChatRoom cr : chatRooms) {
                for (String uid : cr.getUserId()) {
                    if (!uid.equals(email)) {
                        cr.setUser(userVM.get(uid));
                        break;
                    }
                }
            }
            binding.txtNoChat.setVisibility(chatRooms.isEmpty() ? View.VISIBLE : View.GONE);
            roomAdapter.submitList(chatRooms);
        });

        binding.msgOnlineUserRV.setAdapter(onlineAdapter);
        chatRoomVM.getAllByEmail(email).observe(getViewLifecycleOwner(), chatRooms -> {
            java.util.List<ChatRoom> onlineChat = new java.util.ArrayList<>();
            for (ChatRoom cr : chatRooms) {
                for (String uid : cr.getUserId()) {
                    if (!uid.equals(email)) { cr.setUser(userVM.get(uid)); break; }
                }
                if (cr.getUser() != null && "online".equals(cr.getUser().getStatus())) {
                    onlineChat.add(cr);
                }
            }
            binding.txtNoOnline.setVisibility(onlineChat.isEmpty() ? View.VISIBLE : View.GONE);
            onlineAdapter.submitList(onlineChat);
        });

        binding.searchMsg.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return true; }
            @Override
            public boolean onQueryTextChange(String s) { chatRoomVM.search(s); return true; }
        });

        return binding.getRoot();
    }
}
