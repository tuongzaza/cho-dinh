package me.tuong.chodinh.main.post.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.R;
import me.tuong.chodinh.databinding.FragmentPostBinding;
import me.tuong.chodinh.login.LoginActivity;
import me.tuong.chodinh.main.NavHideShow;
import me.tuong.chodinh.main.home.model.Districts;
import me.tuong.chodinh.main.home.model.Post;
import me.tuong.chodinh.main.home.model.Province;
import me.tuong.chodinh.main.post.adapter.PhotoAdapter;
import me.tuong.chodinh.main.post.adapter.PhotoEditAdapter;
import me.tuong.chodinh.main.post.minterface.PostInterface;
import me.tuong.chodinh.main.post.presenter.PostPresenter;

public class PostFragment extends Fragment implements PostInterface {
    public static final String TAG = PostFragment.class.getName();
    FragmentPostBinding binding;
    private Context context;
    private MainActivity activity;
    private PostPresenter postPresenter;
    private PhotoAdapter photoAdapter;
    private PhotoEditAdapter photoEditAdapter;
    private Post post;
    private String provinces, districts, categoryName, post_id;
    private List<String> stringList;
    private String category_id="1";
    private String userid;
    private String imageData1="";
    private String imageData2="";
    private String imageData3="";
    private String imageData4="";
    private String urlInsert = "http://domain.com/insetpost.php";
    private String urlUpdate = "http://domain.com/updatepost.php";



    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_post, container, false);
        activity = (MainActivity) getActivity();
        postPresenter =new PostPresenter(this);
        setHasOptionsMenu(true);
        //ẩn thanh nav
        ((NavHideShow) getActivity()).setNavHideShow(false, "Đăng tin mới", getResources().getDrawable(R.drawable.ic_baseline_keyboard_backspace_24));

        //lấy thông tin người đăng
        postPresenter.getInfo(getActivity());

        photoAdapter = new PhotoAdapter(context);
        photoEditAdapter = new PhotoEditAdapter(context);
        stringList = new ArrayList<>();

        //set layout recycleview
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        binding.rcvPhotoPost.setLayoutManager(gridLayoutManager);

        //lấy dữ liệu nếu là chỉnh sửa
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null){
            post = (Post) bundleReceive.get("object_post");
            if (post != null){
                binding.etName.setText(post.getTitle());
                binding.etPrice.setText(post.getPrice());
                binding.etDesc.setText(post.getDescription());
                binding.etPNumber.setText(post.getPhonenumber());
                provinces = post.getProvince();
                districts = post.getDistrict();
                categoryName = post.getCategory_name();
                post_id = post.getId();
                binding.etAddress.setText(post.getAddress());

                try {
                    stringList.add(post.getPhoto1());
                    stringList.add(post.getPhoto2());
                    stringList.add(post.getPhoto3());
                    stringList.add(post.getPhoto4());
                }catch (Exception e){

                }

                binding.rcvPhotoPost.setAdapter(photoEditAdapter);
                photoEditAdapter.setData(stringList);

                binding.btnPostTin.setVisibility(View.GONE);
                binding.btnSavePost.setVisibility(View.VISIBLE);
            }
        }


        //lấy địa chỉ từ định vị
        postPresenter.getAddressLocal(getActivity());
        //lấy địa chỉ từ json
        postPresenter.getDataAddress();
        //lấy danh sách thư mục
        postPresenter.getCategory();

        binding.btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postPresenter.requestPermission(getActivity());
            }
        });

        binding.btnPostTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPost(urlInsert);
                binding.btnPostTin.setVisibility(View.GONE);
                binding.btnPostTinLoad.setVisibility(View.VISIBLE);
            }
        });

        binding.btnPostTinLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Không nhấn liên tục nhiều lần!!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnSavePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSavePost(urlUpdate);
            }
        });

        //chuyển sang trang đăng nhập
        binding.tvLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull @NotNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem itemSearch = menu.findItem(R.id.menu_action_search);
        itemSearch.setVisible(false);
    }

    //lấy hình ảnh từ thư viện
    @Override
    public void getPhoto(List<Uri> uriList) {
        binding.rcvPhotoPost.setAdapter(photoAdapter);
        photoAdapter.setData(uriList);

        postPresenter.uploadPhoto(uriList,getActivity());
    }

    //hiển thị và lấy cate
    @Override
    public void getCate(List<String> list) {
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, list);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.snCate.setAdapter(stringArrayAdapter);

        //nếu là chỉnh sửa thì select mặc định là category lấy từ post
        if (categoryName != null) {
            int spinnerPosition = stringArrayAdapter.getPosition(categoryName);
            binding.snCate.setSelectedIndex(spinnerPosition);
        }

        binding.snCate.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                category_id = String.valueOf(position);
            }
        });
    }

    @Override
    public void setUser(int info) {
        userid = String.valueOf(info);
    }

    @Override
    public void getUploadPhoto(List<String> imageData) {
        try {
            imageData1 = imageData.get(0)!=null?imageData.get(0):"";
            imageData2 = imageData.get(1)!=null?imageData.get(1):"";
            imageData3 = imageData.get(2)!=null?imageData.get(2):"";
            imageData4 = imageData.get(3) !=null?imageData.get(3):"";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //lấy địa chỉ, province, district từ định vị
    @Override
    public void setAddress(String address, String province, String district) {
        //kiểm tra xem có phải là đang edit hay không
        if (province!=null || district!=null){
            binding.etAddress.setText(address);
            provinces = province;
            districts = district;
        }
    }

    @Override
    public void getDataAddress(List<Province> list) {
        List<String> listPro = new ArrayList<>();
        for (int i=0; i<list.size();i++){
            listPro.add(list.get(i).getName().toString().trim());
        }

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, listPro);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.snProvince.setAdapter(stringArrayAdapter);

        if (provinces != null) {
            int spinnerPosition = stringArrayAdapter.getPosition(provinces);
            binding.snProvince.setSelectedIndex(spinnerPosition);
            List<Districts> districtsList = list.get(spinnerPosition).getDistricts();
            getDistrict(districtsList);
        }else {
            int spinnerPosition = stringArrayAdapter.getPosition("Hà Nội");
            binding.snProvince.setSelectedIndex(spinnerPosition);
            List<Districts> districtsList = list.get(spinnerPosition).getDistricts();
            getDistrict(districtsList);
        }

        binding.snProvince.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                provinces = list.get(position).getName();
                //provinces = null;
                List<Districts> districtsList = list.get(position).getDistricts();
                getDistrict(districtsList);
            }
        });
    }

    @Override
    public void getSuccess(String mess) {
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
        activity.goToSellFragment();
    }

    @Override
    public void requestLogin() {
        binding.svPost.setVisibility(View.GONE);
        binding.llLogin.setVisibility(View.VISIBLE);
    }


    @Override
    public void getError(String mess) {
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }

    private void getDistrict(List<Districts> listDT) {
        List<String> listDist = new ArrayList<>();
        for (int i=0; i<listDT.size();i++){
            listDist.add(listDT.get(i).getName().toString().trim());
        }
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, listDist);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.snDistrict.setAdapter(stringArrayAdapter);

        if (districts != null && provinces != null) {
            try {
                int spinnerPosition = stringArrayAdapter.getPosition(districts);
                binding.snDistrict.setSelectedIndex(spinnerPosition);
            }catch (Exception e){

            }
        }

        if (districts == null && provinces == null) {
            try {
                int spinnerPosition = stringArrayAdapter.getPosition("Hoàng Mai");
                binding.snDistrict.setSelectedIndex(spinnerPosition);
            }catch (Exception e){

            }
        }

        binding.snDistrict.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                districts = listDT.get(position).getName();
                //districts = null;
            }
        });
    }


    private void getPost(String url) {
        long currentTime = System.currentTimeMillis();
        String timeString = String.valueOf(currentTime);
        String title = binding.etName.getText().toString().trim();
        String description = binding.etDesc.getText().toString().trim();
        String price = binding.etPrice.getText().toString().trim();
        String phonenumber = binding.etPNumber.getText().toString().trim();
        String address = binding.etAddress.getText().toString().trim();

        if (title.equals("")){
            binding.etName.setError("Vui lòng nhập tiêu đề tin!");
            binding.etName.requestFocus();
            return;
        }
        if (description.equals("")){
            binding.etDesc.setError("Vui lòng nhập mô tả!");
            binding.etDesc.requestFocus();
            return;
        }
        if (price.equals("")){
            binding.etPrice.setError("Vui lòng nhập giá tiền!");
            binding.etPrice.requestFocus();
            return;
        }
        if (phonenumber.equals("")){
            binding.etPNumber.setError("Vui lòng nhập số điện thoại!");
            binding.etPNumber.requestFocus();
            return;
        }
        if (address.equals("")){
            binding.etAddress.setError("Vui lòng nhập địa chỉ!");
            binding.etAddress.requestFocus();
            return;
        }

        postPresenter.setData(url,title,category_id,description,price,address,districts,provinces,phonenumber,userid,imageData1,imageData2,imageData3,imageData4,timeString,getActivity());
    }

    private void getSavePost(String url) {
        String title = binding.etName.getText().toString().trim();
        String description = binding.etDesc.getText().toString().trim();
        String price = binding.etPrice.getText().toString().trim();
        String phonenumber = binding.etPNumber.getText().toString().trim();
        String address = binding.etAddress.getText().toString().trim();

        if (title.equals("")){
            binding.etName.setError("Vui lòng nhập tiêu đề tin!");
            binding.etName.requestFocus();
            return;
        }
        if (description.equals("")){
            binding.etDesc.setError("Vui lòng nhập mô tả!");
            binding.etDesc.requestFocus();
            return;
        }
        if (price.equals("")){
            binding.etPrice.setError("Vui lòng nhập giá tiền!");
            binding.etPrice.requestFocus();
            return;
        }
        if (phonenumber.equals("")){
            binding.etPNumber.setError("Vui lòng nhập số điện thoại!");
            binding.etPNumber.requestFocus();
            return;
        }
        if (address.equals("")){
            binding.etAddress.setError("Vui lòng nhập địa chỉ!");
            binding.etAddress.requestFocus();
            return;
        }

        postPresenter.setDataSave(url,post_id,title,category_id,description,price,address,districts,provinces,phonenumber,imageData1,imageData2,imageData3,imageData4,getActivity());
    }
}