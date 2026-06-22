 package com.example.assignment.ui;
 
 import android.os.Bundle;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 
 import androidx.fragment.app.Fragment;
 import androidx.navigation.NavController;
 import androidx.navigation.fragment.NavHostFragment;
 
 import com.example.assignment.MainActivity;
 import com.example.assignment.R;
 import com.example.assignment.databinding.FragmentAddmapBinding;
 import com.example.assignment.util.FragmentUtils;
 import com.baidu.mapapi.map.BaiduMap;
 import com.baidu.mapapi.map.MapView;
 import com.baidu.mapapi.map.MapStatusUpdateFactory;
 import com.baidu.mapapi.model.LatLng;
 import com.baidu.mapapi.map.MarkerOptions;
 import com.baidu.mapapi.search.geocode.GeoCoder;
 import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
 import com.baidu.mapapi.search.geocode.GeoCodeResult;
 import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
 import com.baidu.mapapi.search.geocode.GeoCodeOption;
 
 public class AddLocation extends Fragment {
     private BaiduMap mBaiduMap;
     private MapView mMapView;
     private FragmentAddmapBinding binding;
     private NavController nav;
 
     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         binding = FragmentAddmapBinding.inflate(inflater, container, false);
         nav = NavHostFragment.findNavController(this);
         String title = getArguments() != null ? getArguments().getString("title", "") : "";
 
         final double[] latitude = {0.0};
         final double[] longitude = {0.0};
         final String[] locationName = {""};
 
         // Get MapView and BaiduMap instance
         mMapView = (MapView) binding.getRoot().findViewById(R.id.mapView);
         mBaiduMap = mMapView.getMap();
 
         binding.mapsearch.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 GeoCoder geoCoder = GeoCoder.newInstance();
                 OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
                     @Override
                     public void onGetGeoCodeResult(GeoCodeResult result) {
                         if (result == null || result.error != com.baidu.mapapi.search.core.SearchResult.ERRORNO.NO_ERROR) {
                             FragmentUtils.toast(AddLocation.this, "未找到地址");
                             return;
                         }
                         LatLng latLng = result.getLocation();
                         mBaiduMap.clear();
                         mBaiduMap.addOverlay(new MarkerOptions().position(latLng).title(query));
                         mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(latLng, 12f));
                         binding.tvAddressLine.setText(result.getAddress());
                         locationName[0] = result.getAddress();
                         latitude[0] = latLng.latitude;
                         longitude[0] = latLng.longitude;
                     }
 
                     @Override
                     public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                     }
                 };
                 geoCoder.setOnGetGeoCodeResultListener(listener);
                 geoCoder.geocode(new GeoCodeOption().city("").address(query));
                 return false;
             }
 
             @Override
             public boolean onQueryTextChange(String s) {
                 return false;
             }
         });
 
         binding.btnAddAddress.setOnClickListener(v -> {
             if (latitude[0] != 0.0 && longitude[0] != 0.0 && !locationName[0].isEmpty()) {
                 Bundle args = new Bundle();
                 args.putString("latitude", String.valueOf(latitude[0]));
                 args.putString("longitude", String.valueOf(longitude[0]));
                 args.putString("location", locationName[0]);
                 args.putString("title", title);
                 nav.navigate(R.id.activityAddPostFragment, args);
             } else {
                 FragmentUtils.toast(this, "请先搜索位置。");
             }
         });
 
         return binding.getRoot();
     }
 
     @Override
     public void onResume() {
         super.onResume();
         if (mMapView != null) mMapView.onResume();
         ((MainActivity) requireActivity()).hideBV();
     }
 
     @Override
     public void onPause() {
         super.onPause();
         if (mMapView != null) mMapView.onPause();
     }
 
     @Override
     public void onDestroy() {
         super.onDestroy();
         if (mMapView != null) mMapView.onDestroy();
     }
 
     @Override
     public void onSaveInstanceState(Bundle outState) {
         super.onSaveInstanceState(outState);
         if (mMapView != null) mMapView.onSaveInstanceState(outState);
     }
 
     @Override
     public void onStop() {
         super.onStop();
         ((MainActivity) requireActivity()).showBV();
     }
 }
