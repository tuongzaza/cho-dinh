package me.tuong.chodinh.main.post.presenter;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.api.ApiService;
import me.tuong.chodinh.main.home.model.Category;
import me.tuong.chodinh.main.home.model.Province;
import me.tuong.chodinh.main.post.minterface.PostInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static me.tuong.chodinh.login.LoginActivity.SHARED_PREF_NAME;

public class PostPresenter {
    private PostInterface postInterface;
    private String province, district;
    FusedLocationProviderClient fusedLocationProviderClient;

    public PostPresenter(PostInterface postInterface) {
        this.postInterface = postInterface;
    }

    public void getAddressLocal(FragmentActivity activity) {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocations(activity);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void getLocations(FragmentActivity activity) {
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
                        district = parts[(parts.length-3)];

                        String[] part = addresss.split(", "+district);
                        String address = part[0];

                        postInterface.setAddress(address,province,district);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void requestPermission(FragmentActivity activity) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                selectImageFromGallery(activity);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                String mess = "Permission Denied\n" + deniedPermissions.toString();
                postInterface.getError(mess);
            }
        };
        TedPermission.with(activity)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    private void selectImageFromGallery(FragmentActivity activity) {
        TedBottomPicker.with(activity)
                .setPeekHeight(1600)
                .showTitle(false)
                .setCompleteButtonText("Done")
                .setEmptySelectionText("No Select")
                .setSelectMaxCount(4)
                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(List<Uri> uriList) {
                        if (uriList!=null && !uriList.isEmpty()){
                            postInterface.getPhoto(uriList);
                            //getImagetoString(uriList, activity);
                            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(),uriList.get(0));
                        }
                    }
                });
    }

    private String getImagetoString(Bitmap bitmap) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }

    public void getCategory() {
        ApiService.apiService.getCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> list = new ArrayList<>();
                list = response.body();

                List<String> listCate = new ArrayList<>();
                for (int i=0; i<list.size();i++){
                    listCate.add(list.get(i).getCategoryName().toString().trim());
                }

                postInterface.getCate(listCate);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    public void setData(String url, String title, String category_id, String description, String price, String address, String districts, String provinces, String phonenumber, String userid, String imageData1, String imageData2, String imageData3, String imageData4, String timeString, FragmentActivity activity) {
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Data Submit Successfully")){
                    String mess = "Đăng tin thành công!";
                    postInterface.getSuccess(mess);
                }else {
                    String mess = "Lỗi!";
                    postInterface.getError(mess);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mess = "Xảy ra lỗi!";
                postInterface.getError(mess);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("description", description);
                params.put("category_id", category_id);
                params.put("price", price);
                params.put("address", address);
                params.put("district", districts);
                params.put("province", provinces);
                params.put("phonenumber", phonenumber);
                params.put("user_id", userid);
                params.put("photo1", imageData1);
                params.put("photo2", imageData2);
                params.put("photo3", imageData3);
                params.put("photo4", imageData4);
                params.put("time", timeString);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void uploadPhoto(List<Uri> uriList, FragmentActivity activity) {
        try {
            List<String> imageData = new ArrayList<>();
            for (int i=0;i<uriList.size();i++){
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(),uriList.get(i));
                imageData.add(getImagetoString(bitmap));
            }
            postInterface.getUploadPhoto(imageData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getDataAddress() {
        ApiService.apiService.getAddress().enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(Call<List<Province>> call, Response<List<Province>> response) {
                List<Province> list = new ArrayList<>();
                list = response.body();

                postInterface.getDataAddress(list);
            }

            @Override
            public void onFailure(Call<List<Province>> call, Throwable t) {

            }
        });
    }

    public void setDataSave(String url, String post_id, String title, String category_id, String description, String price, String address, String districts, String provinces, String phonenumber, String imageData1, String imageData2, String imageData3, String imageData4, FragmentActivity activity) {

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.trim().equals("Data Submit Successfully")){
                    String mess = "Cập nhật tin thành công!";
                    postInterface.getSuccess(mess);
                }else {
                    String mess = "Lỗi!";
                    postInterface.getError(mess);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mess = "Xảy ra lỗi!";
                postInterface.getError(mess);
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("post_id", post_id);
                params.put("title", title);
                params.put("description", description);
                params.put("category_id", category_id);
                params.put("price", price);
                params.put("address", address);
                params.put("district", districts);
                params.put("province", provinces);
                params.put("phonenumber", phonenumber);
                params.put("photo1", imageData1);
                params.put("photo2", imageData2);
                params.put("photo3", imageData3);
                params.put("photo4", imageData4);

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void getInfo(FragmentActivity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("loggedIn",false);
        if (isLoggedIn){
            int user_id = preferences.getInt("userid",0);
            postInterface.setUser(user_id);
        } else {
            postInterface.requestLogin();
        }
    }
}
