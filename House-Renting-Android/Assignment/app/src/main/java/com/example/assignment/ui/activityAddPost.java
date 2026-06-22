package com.example.assignment.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
import com.example.assignment.databinding.FragmentActivityAddPostBinding;
import com.example.assignment.util.FragmentUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class activityAddPost extends Fragment {

    private FragmentActivityAddPostBinding binding;
    private RoomViewModel vm;
    private AuthViewModel auth;
    private NavController nav;

    private static final int REQUEST_EXTERNAL_STORAGE = 100;
    private static final int MAX_IMAGE_COUNT = 9;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentActivityAddPostBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);
        vm = new ViewModelProvider(requireActivity()).get(RoomViewModel.class);
        auth = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        String latitude = getArguments() != null ? getArguments().getString("latitude", "") : "";
        String longitude = getArguments() != null ? getArguments().getString("longitude", "") : "";
        String location = getArguments() != null ? getArguments().getString("location", "") : "";

        reset();
        binding.btnLocation.setOnClickListener(v -> {
            String title = binding.ettTitle.getText().toString().trim();
            Bundle args = new Bundle();
            args.putString("title", title);
            nav.navigate(R.id.addLocationFragment, args);
        });
        binding.btnConfirm.setOnClickListener(v -> create(latitude, longitude));
        binding.btnReset.setOnClickListener(v -> reset());
        binding.btnInPhoto.setOnClickListener(v -> launchGalleryIntent());

        if (!latitude.isEmpty() && !longitude.isEmpty()) {
            binding.btnLocation.setText(location);
        }

        setupCheckboxListeners();
        return binding.getRoot();
    }

    private void setupCheckboxListeners() {
        // Room type - only one
        binding.chbSingleRoom.setOnCheckedChangeListener((b, checked) -> {
            if (checked) { binding.chbMediumRoom.setChecked(false); binding.chbMasterRoom.setChecked(false); }
        });
        binding.chbMediumRoom.setOnCheckedChangeListener((b, checked) -> {
            if (checked) { binding.chbSingleRoom.setChecked(false); binding.chbMasterRoom.setChecked(false); }
        });
        binding.chbMasterRoom.setOnCheckedChangeListener((b, checked) -> {
            if (checked) { binding.chbSingleRoom.setChecked(false); binding.chbMediumRoom.setChecked(false); }
        });
        // Bedroom options
        binding.chbBedOne.setOnCheckedChangeListener((b, checked) -> {
            if (checked) { binding.chbBedTwo.setChecked(false); binding.chbBedThree.setChecked(false); binding.chbBedFour.setChecked(false); }
        });
        binding.chbBedTwo.setOnCheckedChangeListener((b, checked) -> {
            if (checked) { binding.chbBedOne.setChecked(false); binding.chbBedThree.setChecked(false); binding.chbBedFour.setChecked(false); }
        });
        binding.chbBedThree.setOnCheckedChangeListener((b, checked) -> {
            if (checked) { binding.chbBedOne.setChecked(false); binding.chbBedTwo.setChecked(false); binding.chbBedFour.setChecked(false); }
        });
        binding.chbBedFour.setOnCheckedChangeListener((b, checked) -> {
            if (checked) { binding.chbBedOne.setChecked(false); binding.chbBedTwo.setChecked(false); binding.chbBedThree.setChecked(false); }
        });
        // Bathroom options
        binding.chbBathOne.setOnCheckedChangeListener((b, checked) -> {
            if (checked) { binding.chbBathTwo.setChecked(false); binding.chbBathThree.setChecked(false); }
        });
        binding.chbBathTwo.setOnCheckedChangeListener((b, checked) -> {
            if (checked) { binding.chbBathOne.setChecked(false); binding.chbBathThree.setChecked(false); }
        });
        binding.chbBathThree.setOnCheckedChangeListener((b, checked) -> {
            if (checked) { binding.chbBathOne.setChecked(false); binding.chbBathTwo.setChecked(false); }
        });
    }

    private void reset() {
        binding.ettTitle.getText().clear();
        binding.dttDetails.getText().clear();
        binding.imageContainer.removeAllViews();
        binding.ettRoomSize.getText().clear();
        binding.ettPrice.getText().clear();
        clearCheckboxes();
        binding.ettTitle.requestFocus();
    }

    private void clearCheckboxes() {
        binding.chbSingleRoom.setChecked(false); binding.chbMediumRoom.setChecked(false); binding.chbMasterRoom.setChecked(false);
        binding.chbBedOne.setChecked(false); binding.chbBedTwo.setChecked(false); binding.chbBedThree.setChecked(false); binding.chbBedFour.setChecked(false);
        binding.chbBathOne.setChecked(false); binding.chbBathTwo.setChecked(false); binding.chbBathThree.setChecked(false);
        binding.chbWifi.setChecked(false); binding.chbTv.setChecked(false); binding.chbAir.setChecked(false); binding.chbWashM.setChecked(false);
    }

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
            LinearLayout imageContainer = binding.imageContainer;
            imageContainer.removeAllViews();
            List<Uri> uris = new ArrayList<>();

            if (data.getClipData() != null) {
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    uris.add(data.getClipData().getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                uris.add(data.getData());
            }

            if (uris.size() > MAX_IMAGE_COUNT) {
                FragmentUtils.errorDialog(this, "最多只能选择 " + MAX_IMAGE_COUNT + " 张图片。");
                return;
            }

            for (Uri uri : uris) {
                try {
                    InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if (bitmap != null) addImageToContainer(bitmap, imageContainer);
                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    private void addImageToContainer(Bitmap bitmap, LinearLayout container) {
        ImageView imageView = new ImageView(requireContext());
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, dpToPx(100), dpToPx(100), true);
        imageView.setImageBitmap(resized);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 0, 8, 0);
        imageView.setLayoutParams(params);
        container.addView(imageView);
    }

    private int dpToPx(int dp) { return (int) (dp * getResources().getDisplayMetrics().density); }

    private void create(String latitude, String longitude) {
        String roomType = binding.chbSingleRoom.isChecked() ? "Single" :
                          binding.chbMediumRoom.isChecked() ? "Medium" :
                          binding.chbMasterRoom.isChecked() ? "Master" : "";
        int numBed = binding.chbBedOne.isChecked() ? 1 : binding.chbBedTwo.isChecked() ? 2 :
                     binding.chbBedThree.isChecked() ? 3 : binding.chbBedFour.isChecked() ? 4 : 0;
        int numBath = binding.chbBathOne.isChecked() ? 1 : binding.chbBathTwo.isChecked() ? 2 :
                      binding.chbBathThree.isChecked() ? 3 : 0;

        List<String> amenities = new ArrayList<>();
        if (binding.chbWifi.isChecked()) amenities.add("Wi-Fi");
        if (binding.chbTv.isChecked()) amenities.add("TV");
        if (binding.chbAir.isChecked()) amenities.add("Air-Conditioner");
        if (binding.chbWashM.isChecked()) amenities.add("Washing Machine");

        LinearLayout imageContainer = binding.imageContainer;
        List<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < imageContainer.getChildCount(); i++) {
            ImageView iv = (ImageView) imageContainer.getChildAt(i);
            bitmaps.add(((BitmapDrawable) iv.getDrawable()).getBitmap());
        }

        Room room = new Room();
        room.setRoomName(binding.ettTitle.getText().toString().trim());
        room.setRoomPlace(binding.btnLocation.getText().toString().trim());
        room.setRoomDetail(binding.dttDetails.getText().toString().trim());
        room.setPhotoJson("[]");
        room.setRoomSize(binding.ettRoomSize.getText().toString().isEmpty() ? 0.0 :
                Double.parseDouble(binding.ettRoomSize.getText().toString()));
        room.setRoomType(roomType);
        room.setNumOfBed(numBed);
        room.setNumOfBath(numBath);
        room.setHomeAmenities(amenities);
        room.setRoomPrice(binding.ettPrice.getText().toString().isEmpty() ? 0.0 :
                Double.parseDouble(binding.ettPrice.getText().toString()));
        room.setUserId(auth.getUser().getEmail());
        room.setLatitude(latitude.isEmpty() ? 0.0 : Double.parseDouble(latitude));
        room.setLongitude(longitude.isEmpty() ? 0.0 : Double.parseDouble(longitude));

        String e = vm.validate(room, true);
        if (!e.isEmpty()) { FragmentUtils.errorDialog(this, e); return; }

        vm.add(room);
        nav.navigate(R.id.activityFragment);
    }
}
