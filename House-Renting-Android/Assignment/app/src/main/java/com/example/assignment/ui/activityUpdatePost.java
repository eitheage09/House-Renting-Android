package com.example.assignment.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.assignment.R;
import com.example.assignment.controller.AuthViewModel;
import com.example.assignment.controller.RoomViewModel;
import com.example.assignment.model.Room;
import com.example.assignment.databinding.FragmentActivityUpdatePostBinding;
import com.example.assignment.util.FragmentUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class activityUpdatePost extends Fragment {
    private FragmentActivityUpdatePostBinding binding;
    private RoomViewModel vm;
    private AuthViewModel auth;
    private NavController nav;

    private static final int REQUEST_EXTERNAL_STORAGE = 100;
    private static final int MAX_IMAGE_COUNT = 9;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentActivityUpdatePostBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);
        vm = new ViewModelProvider(requireActivity()).get(RoomViewModel.class);
        auth = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        String roomId = getArguments() != null ? getArguments().getString("roomId", "") : "";
        String latitude = getArguments() != null ? getArguments().getString("latitude", "") : "";
        String longitude = getArguments() != null ? getArguments().getString("longitude", "") : "";
        String location = getArguments() != null ? getArguments().getString("location", "") : "";

        binding.btnLocation.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("roomId", roomId);
            nav.navigate(R.id.editLocationFragment, args);
        });

        binding.btnUpdate.setOnClickListener(v -> update(roomId, latitude, longitude));
        binding.btnReset.setOnClickListener(v -> reset());
        binding.btnInPhoto.setOnClickListener(v -> launchGalleryIntent());

        if (!latitude.isEmpty() && !longitude.isEmpty()) {
            binding.btnLocation.setText(location);
        }

        Room room = vm.get(roomId);
        if (room != null) {
            binding.ettTitle.setText(room.getRoomName());
            binding.btnLocation.setText(room.getRoomPlace());
            binding.dttDetails.setText(room.getRoomDetail());
            binding.ettRoomSize.setText(String.valueOf(room.getRoomSize()));
            binding.ettPrice.setText(String.valueOf(room.getRoomPrice()));

            if ("Single".equals(room.getRoomType())) binding.chbSingleRoom.setChecked(true);
            else if ("Medium".equals(room.getRoomType())) binding.chbMediumRoom.setChecked(true);
            else if ("Master".equals(room.getRoomType())) binding.chbMasterRoom.setChecked(true);

            switch (room.getNumOfBed()) {
                case 1: binding.chbBedOne.setChecked(true); break;
                case 2: binding.chbBedTwo.setChecked(true); break;
                case 3: binding.chbBedThree.setChecked(true); break;
                case 4: binding.chbBedFour.setChecked(true); break;
            }
            switch (room.getNumOfBath()) {
                case 1: binding.chbBathOne.setChecked(true); break;
                case 2: binding.chbBathTwo.setChecked(true); break;
                case 3: binding.chbBathThree.setChecked(true); break;
            }
            for (String amenity : room.getHomeAmenities()) {
                if ("Wi-Fi".equals(amenity)) binding.chbWifi.setChecked(true);
                else if ("TV".equals(amenity)) binding.chbTv.setChecked(true);
                else if ("Air-Conditioner".equals(amenity)) binding.chbAir.setChecked(true);
                else if ("Washing Machine".equals(amenity)) binding.chbWashM.setChecked(true);
            }
        }

        setupCheckboxListeners();
        return binding.getRoot();
    }

    private void setupCheckboxListeners() {
        binding.chbSingleRoom.setOnCheckedChangeListener((b, c) -> { if (c) { binding.chbMediumRoom.setChecked(false); binding.chbMasterRoom.setChecked(false); }});
        binding.chbMediumRoom.setOnCheckedChangeListener((b, c) -> { if (c) { binding.chbSingleRoom.setChecked(false); binding.chbMasterRoom.setChecked(false); }});
        binding.chbMasterRoom.setOnCheckedChangeListener((b, c) -> { if (c) { binding.chbSingleRoom.setChecked(false); binding.chbMediumRoom.setChecked(false); }});
    }

    private void reset() { binding.ettTitle.getText().clear(); binding.dttDetails.getText().clear(); binding.imageContainer.removeAllViews(); binding.ettRoomSize.getText().clear(); binding.ettPrice.getText().clear(); }

    private void launchGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_EXTERNAL_STORAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXTERNAL_STORAGE && resultCode == Activity.RESULT_OK) {
            LinearLayout container = binding.imageContainer;
            container.removeAllViews();
            List<Uri> uris = new ArrayList<>();

            if (data.getClipData() != null) {
                for (int i = 0; i < data.getClipData().getItemCount(); i++) uris.add(data.getClipData().getItemAt(i).getUri());
            } else if (data.getData() != null) { uris.add(data.getData()); }

            if (uris.size() > MAX_IMAGE_COUNT) { FragmentUtils.errorDialog(this, "最多 " + MAX_IMAGE_COUNT + " 张图片。"); return; }

            for (Uri uri : uris) {
                try {
                    InputStream is = requireContext().getContentResolver().openInputStream(uri);
                    Bitmap bm = BitmapFactory.decodeStream(is);
                    if (bm != null) {
                        ImageView iv = new ImageView(requireContext());
                        iv.setImageBitmap(Bitmap.createScaledBitmap(bm, dpToPx(100), dpToPx(100), true));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(8, 0, 8, 0);
                        iv.setLayoutParams(params);
                        container.addView(iv);
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    private int dpToPx(int dp) { return (int)(dp * getResources().getDisplayMetrics().density); }

    private void update(String roomId, String lat, String lng) {
        Room existing = vm.get(roomId);
        String roomType = binding.chbSingleRoom.isChecked() ? "Single" : binding.chbMediumRoom.isChecked() ? "Medium" : binding.chbMasterRoom.isChecked() ? "Master" : "";
        int numBed = binding.chbBedOne.isChecked() ? 1 : binding.chbBedTwo.isChecked() ? 2 : binding.chbBedThree.isChecked() ? 3 : binding.chbBedFour.isChecked() ? 4 : 0;
        int numBath = binding.chbBathOne.isChecked() ? 1 : binding.chbBathTwo.isChecked() ? 2 : binding.chbBathThree.isChecked() ? 3 : 0;

        List<String> amenities = new ArrayList<>();
        if (binding.chbWifi.isChecked()) amenities.add("Wi-Fi");
        if (binding.chbTv.isChecked()) amenities.add("TV");
        if (binding.chbAir.isChecked()) amenities.add("Air-Conditioner");
        if (binding.chbWashM.isChecked()) amenities.add("Washing Machine");

        Room room = new Room();
        room.setRoomId(existing != null ? existing.getRoomId() : "");
        room.setRoomName(binding.ettTitle.getText().toString().trim());
        room.setRoomPlace(binding.btnLocation.getText().toString().trim());
        room.setRoomDetail(binding.dttDetails.getText().toString().trim());
        room.setPhotoJson(existing != null ? existing.getPhotoJson() : "[]");
        room.setRoomSize(binding.ettRoomSize.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(binding.ettRoomSize.getText().toString()));
        room.setRoomType(roomType);
        room.setNumOfBed(numBed);
        room.setNumOfBath(numBath);
        room.setHomeAmenities(amenities);
        room.setRoomPrice(binding.ettPrice.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(binding.ettPrice.getText().toString()));
        room.setUserId(auth.getUser() != null ? auth.getUser().getEmail() : "");
        room.setLatitude(lat.isEmpty() ? 0.0 : Double.parseDouble(lat));
        room.setLongitude(lng.isEmpty() ? 0.0 : Double.parseDouble(lng));

        vm.set(room);
        nav.navigate(R.id.activityFragment);
    }
}
