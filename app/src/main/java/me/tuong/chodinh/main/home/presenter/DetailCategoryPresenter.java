package me.tuong.chodinh.main.home.presenter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.api.ApiService;
import me.tuong.chodinh.main.home.minterface.DetailCategoryInterface;
import me.tuong.chodinh.main.home.model.Category;
import me.tuong.chodinh.main.home.model.Post;
import me.tuong.chodinh.main.home.model.Province;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailCategoryPresenter {
    private DetailCategoryInterface detailCategoryInterface;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String province;

    public DetailCategoryPresenter(DetailCategoryInterface detailCategoryInterface) {
        this.detailCategoryInterface = detailCategoryInterface;
    }

    public void getAddressLocal(MainActivity activity) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocations(activity);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void getLocations(MainActivity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {

                    try {
                        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        String addresss = addresses.get(0).getAddressLine(0);
                        String[] parts = addresss.split(", ");
                        province = parts[(parts.length-2)];

                        detailCategoryInterface.setAddress(province);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getDataAddress() {
        ApiService.apiService.getAddress().enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(Call<List<Province>> call, Response<List<Province>> response) {
                List<Province> list = new ArrayList<>();
                list = response.body();

                List<String> listPro = new ArrayList<>();
                for (int i=0; i<list.size();i++){
                    listPro.add(list.get(i).getName().toString().trim());
                }

                detailCategoryInterface.getDataAddress(listPro);
            }

            @Override
            public void onFailure(Call<List<Province>> call, Throwable t) {

            }
        });
    }

    public void getCategory() {
        ApiService.apiService.getCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> list = new ArrayList<>();
                list = response.body();

                List<String> listCate = new ArrayList<>();
                listCate.add("Tất cả danh mục");
                for (int i=0; i<list.size();i++){
                    listCate.add(list.get(i).getCategoryName().toString().trim());
                }

                detailCategoryInterface.setCate(listCate);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    public void getPost(String provinceName, String categoryName, String key) {
        long currentTime = System.currentTimeMillis();
        ApiService.apiService.getPost().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                List<Post> listall = new ArrayList<>();
                List<Post> list = new ArrayList<>();
                List<Post> listTop = new ArrayList<>();
                List<Post> listTopNew = new ArrayList<>();
                List<Post> listNew = new ArrayList<>();
                listall = response.body();

                for (int $j = 0; $j < listall.size(); $j++){
                    if (Long.parseLong(listall.get($j).getTop())<currentTime){
                        list.add(listall.get($j));
                    }else {
                        listTop.add(listall.get($j));
                    }
                }


                if (key!=null){
                    if (categoryName == "Tất cả danh mục" || categoryName == null){
                        for (int $i = 0; $i < list.size(); $i++){
                            if (list.get($i).getProvince().equals(provinceName)&&list.get($i).getTitle().toLowerCase().contains(key.toLowerCase())){
                                listNew.add(list.get($i));
                            }
                        }
                        for (int $k = 0; $k < listTop.size(); $k++){
                            if (listTop.get($k).getProvince().equals(provinceName)&&listTop.get($k).getTitle().toLowerCase().contains(key.toLowerCase())){
                                listTopNew.add(listTop.get($k));
                            }
                        }
                    }else {
                        for (int $i = 0; $i < list.size(); $i++){
                            if (list.get($i).getProvince().equals(provinceName)&&list.get($i).getCategory_name().equals(categoryName)&&list.get($i).getTitle().toLowerCase().contains(key.toLowerCase())){
                                listNew.add(list.get($i));
                            }
                        }
                        for (int $k = 0; $k < listTop.size(); $k++){
                            if (listTop.get($k).getProvince().equals(provinceName)&&listTop.get($k).getCategory_name().equals(categoryName)&&listTop.get($k).getTitle().toLowerCase().contains(key.toLowerCase())){
                                listTopNew.add(listTop.get($k));
                            }
                        }
                    }
                }else {
                    if (categoryName == "Tất cả danh mục"){
                        for (int $i = 0; $i < list.size(); $i++){
                            if (list.get($i).getProvince().equals(provinceName)){
                                listNew.add(list.get($i));
                            }
                        }
                        for (int $k = 0; $k < listTop.size(); $k++){
                            if (listTop.get($k).getProvince().equals(provinceName)){
                                listTopNew.add(listTop.get($k));
                            }
                        }
                    }else {
                        for (int $i = 0; $i < list.size(); $i++){
                            if (list.get($i).getProvince().equals(provinceName)&&list.get($i).getCategory_name().equals(categoryName)){
                                listNew.add(list.get($i));
                            }
                        }
                        for (int $k = 0; $k < listTop.size(); $k++){
                            if (listTop.get($k).getProvince().equals(provinceName)&&listTop.get($k).getCategory_name().equals(categoryName)){
                                listTopNew.add(listTop.get($k));
                            }
                        }
                    }
                }

                detailCategoryInterface.setPost(listNew, listTopNew);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }
}
