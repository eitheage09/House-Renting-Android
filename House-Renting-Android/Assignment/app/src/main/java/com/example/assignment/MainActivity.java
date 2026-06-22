package com.example.assignment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.assignment.controller.*;
import com.example.assignment.model.User;
import com.example.assignment.databinding.ActivityMainBinding;
import com.example.assignment.databinding.HeaderBinding;
import com.example.assignment.repository.*;
import com.example.assignment.util.ImageUtils;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppBarConfiguration abc;
    private NavController nav;

    private AuthViewModel auth;

    private RoomRepository roomRepository;
    private UserRepository userRepository;
    private ChatRepository chatRepository;
    private ChatRoomRepository chatRoomRepository;
    private FavoriteRepository favoriteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // =========================
        // 1️⃣ Repository 初始化（轻量 OK）
        // =========================
        roomRepository = new RoomRepository();
        userRepository = new UserRepository();
        chatRepository = new ChatRepository();
        chatRoomRepository = new ChatRoomRepository();
        favoriteRepository = new FavoriteRepository();

        // =========================
        // 2️⃣ ViewModel 初始化（轻量 OK）
        // =========================
        auth = new ViewModelProvider(this).get(AuthViewModel.class);
        RoomViewModel roomVM = new ViewModelProvider(this).get(RoomViewModel.class);
        UserViewModel userVM = new ViewModelProvider(this).get(UserViewModel.class);
        ChatViewModel chatVM = new ViewModelProvider(this).get(ChatViewModel.class);
        ChatRoomViewModel chatRoomVM = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        FavoriteViewModel favoriteVM = new ViewModelProvider(this).get(FavoriteViewModel.class);

        auth.setUserRepository(userRepository);
        roomVM.setRoomRepository(roomRepository);
        userVM.setUserRepository(userRepository);
        chatVM.setChatRepository(chatRepository);
        chatRoomVM.setChatRoomRepository(chatRoomRepository);
        favoriteVM.setFavoriteRepository(favoriteRepository);

        // ❌ 关键修改：init() 不在主线程立即执行
        // 👉 避免 ANR
        // =========================
        // 延迟初始化数据
        // =========================
        getWindow().getDecorView().post(() -> {
            userVM.init();
            roomVM.init();
            chatVM.init();
            auth.init();
        });

        // =========================
        // 3️⃣ UI 初始化（必须最后）
        // =========================
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        nav = ((NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.host))
                .getNavController();

        abc = new AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.messageFragment,
                R.id.activityFragment,
                R.id.favoriteFragment,
                R.id.accountFragment
        )
                .setDrawerLayout(binding.getRoot())
                .build();

        NavigationUI.setupActionBarWithNavController(this, nav, abc);
        NavigationUI.setupWithNavController(binding.bv, nav);
        NavigationUI.setupWithNavController(binding.nv, nav);

        // =========================
        // 4️⃣ 登录恢复（轻量化）
        // =========================
        auth.getUserLD().observe(this, user -> {
            binding.nv.getMenu().clear();

            View h = binding.nv.getHeaderView(0);
            binding.nv.removeHeaderView(h);

            binding.nv.inflateMenu(R.menu.drawer);
            binding.nv.inflateHeaderView(R.layout.header);

            if (user != null) {
                setHeader(user);
            }
        });

        binding.getRoot().post(() -> auth.loginFromPreferences());

        // =========================
        // 5️⃣ Logout
        // =========================
        binding.nv.getMenu()
                .findItem(R.id.logout)
                .setOnMenuItemClickListener(item -> {
                    auth.logout();
                    nav.navigate(R.id.firstFragment);
                    return true;
                });

        // =========================
        // 6️⃣ 夜间模式
        // =========================
        SharedPreferences sp = getSharedPreferences("Mode", MODE_PRIVATE);
        boolean nightMode = sp.getBoolean("night", false);

        AppCompatDelegate.setDefaultNightMode(
                nightMode
                        ? AppCompatDelegate.MODE_NIGHT_YES
                        : AppCompatDelegate.MODE_NIGHT_NO
        );

        // =========================
        // 7️⃣ 权限（轻量）
        // =========================
        boolean isFirst = sp.getBoolean("is_first_time", true);
        if (isFirst) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        300
                );
            }
            sp.edit().putBoolean("is_first_time", false).apply();
        }

        // =========================
        // 8️⃣ 网络检测
        // =========================
        if (!checkNetwork()) {
            Toast.makeText(this, "无网络连接", Toast.LENGTH_LONG).show();
        }
    }

    // =========================
    // Header UI
    // =========================
    private void setHeader(User user) {
        View h = binding.nv.getHeaderView(0);
        HeaderBinding b = HeaderBinding.bind(h);

        ImageUtils.setImageBytes(b.imgPhoto, user.getPhoto());
        b.txtName.setText(user.getName());
        b.txtEmail.setText(user.getEmail());
    }

    // =========================
    // 网络检测
    // =========================
    private boolean checkNetwork() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.net.Network network = cm.getActiveNetwork();
            if (network == null) return false;

            NetworkCapabilities cap = cm.getNetworkCapabilities(network);

            return cap != null && (
                    cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            );
        }
        return false;
    }

    public void hideBV() {
        binding.bv.setVisibility(View.GONE);
    }

    public void showBV() {
        binding.bv.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // ❌ 去掉 sleep（避免 UI 卡顿）
        binding.getRoot().post(() -> {
            if (auth.getUser() != null) {
                auth.updateUserStatus("online");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (auth.getUser() != null) {
            auth.updateUserStatus("offline");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(nav, abc);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        roomRepository.cleanup();
        userRepository.cleanup();
        chatRepository.cleanup();
        chatRoomRepository.cleanup();
        favoriteRepository.cleanup();
    }
}