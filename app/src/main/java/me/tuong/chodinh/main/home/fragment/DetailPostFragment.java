package me.tuong.chodinh.main.home.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.R;
import me.tuong.chodinh.databinding.FragmentDetailPostBinding;
import me.tuong.chodinh.main.NavHideShow;
import me.tuong.chodinh.main.home.adapter.PhotoPostAdapter;
import me.tuong.chodinh.main.home.minterface.DetailPostInterface;
import me.tuong.chodinh.main.home.model.PhotoSlide;
import me.tuong.chodinh.main.home.model.Post;
import me.tuong.chodinh.main.home.model.UserInfo;
import me.tuong.chodinh.main.home.presenter.DetailPostPresenter;

public class DetailPostFragment extends Fragment implements DetailPostInterface {
    public static final String TAG = DetailPostFragment.class.getName();

    FragmentDetailPostBinding binding;
    private DetailPostPresenter detailPostPresenter;
    private List<PhotoSlide> photoSlides;
    private PhotoPostAdapter photoPostAdapter;
    private MainActivity mainActivity;
    private Post post;
    private UserInfo user;
    private String user_id_post, user_id, post_id, mua, timeexp, getAddress;
    private String image1="";
    private String image2="";
    private String image3="";
    private String image4="";
//    private ProgressDialog progressDialog;

    public DetailPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail_post, container, false);
        detailPostPresenter = new DetailPostPresenter(this);
        mainActivity = (MainActivity) getActivity();

        //ẩn thanh nav
        ((NavHideShow) getActivity()).setNavHideShow(false, "Chi tiết tin đăng", getResources().getDrawable(R.drawable.ic_baseline_keyboard_backspace_24));

        //hiển thị progressDialog
//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setCancelable(false);
//        progressDialog.show();

        //lấy dữ liệu
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null){
            post = (Post) bundleReceive.get("object_post");
            if (post != null){
                binding.tvShowAddress.setText(post.getAddress());
                binding.tvTitlePostDetail.setText(post.getTitle());
                binding.tvPriceDetail.setText(post.getPrice());
                binding.tvUserNamePost.setText(post.getUser_name());
                binding.tvShowDescription.setText(post.getDescription());
                binding.tvShowPhone.setText(post.getPhonenumber());
                image1 = post.getPhoto1();
                image2 = post.getPhoto2();
                image3 = post.getPhoto3();
                image4 = post.getPhoto4();
                String time = post.getTime();
                user_id_post = post.getUser_id();
                post_id = post.getId();
                detailPostPresenter.getTime(time);
                timeexp = post.getTop();
                getAddress = post.getAddress();
            }
        }

        //lấy thông tin người đăng
        detailPostPresenter.getUserInfo(user_id_post);

        //lấy thông tin user, check đăng nhập
        detailPostPresenter.getEdit(mainActivity);

        //kiểm tra xem post đã được lưu chưa
        detailPostPresenter.getCheckFavorite(user_id, post_id);

        //slide hình ảnh
        photoSlides = getListPhoto();
        photoPostAdapter = new PhotoPostAdapter(getContext(),photoSlides);

        binding.vpPostSlide.setAdapter(photoPostAdapter);
        binding.circleIndiPost.setViewPager(binding.vpPostSlide);
        photoPostAdapter.registerDataSetObserver(binding.circleIndiPost.getDataSetObserver());

        //gọi điện
        String callnumber = binding.tvShowPhone.getText().toString().trim();
        String titleSMS = binding.tvTitlePostDetail.getText().toString().trim();
        binding.llCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(callnumber)) {
                    String dial = "tel:" + callnumber;
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                }
            }
        });

        binding.llSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:" + callnumber);

                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
                // Add the message at the sms_body extra field
                smsIntent.putExtra("sms_body", titleSMS);
                try {
                    startActivity(smsIntent);
                } catch (Exception ex) {

                }
            }
        });

        //chuyển sang trang user

        binding.tvBtnViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.goToDetailProfile(user_id_post);
            }
        });

        //chuyển sang trang sửa tin
        binding.btnEditPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.goToEditPost(post);
            }
        });

        //chuyển tin sang trạng thái đã bán
        binding.btnSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post_id=post.getId();
                detailPostPresenter.setStatus(post_id,mainActivity);
                mainActivity.goToSellFragment();
            }
        });

        //lưu tin
        binding.tvFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post_id=post.getId();
                if (user_id.equals("")){
                    Toast.makeText(getActivity(), "Bạn phải đăng nhập để thực hiện chức năng này!", Toast.LENGTH_SHORT).show();
                }else {
                    if (user_id.equals(user_id_post)){
                        Toast.makeText(getActivity(), "Bạn không được lưu tin mình đăng!", Toast.LENGTH_SHORT).show();
                    }else {
                        detailPostPresenter.savePostFavorite(post_id,user_id,mainActivity);
                    }
                }
            }
        });

        //đẩy tin lên top
        binding.btnTopPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog();
            }
        });

        //chuyển sang trang map

        binding.btnChiDuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.goToMap(getAddress);
            }
        });

        return binding.getRoot();
    }

    private void getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Đẩy tin của bạn lên Top");
        String[] types = {"3 ngày (30.000đ)", "7 ngày (70.000đ)", "1 tháng (300.000đ)"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(),R.layout.support_simple_spinner_dropdown_item,types);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog_spinner, (ViewGroup) getView(),false);
        Spinner spinner = view.findViewById(R.id.spnGiaPost);
        TextView textView = view.findViewById(R.id.tvGiaPost);
        textView.setText("Bạn đang có "+user.getDong()+" đồng trong tài khoản!");
        spinner.setAdapter(arrayAdapter);
        //lấy giá trị
        mua = "3 ngày (30.000đ)";
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mua = types[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        builder.setView(view);
//        builder.setMessage("Bạn đang có "+user.getDong()+" đồng trong tài khoản!");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                detailPostPresenter.dayTin(mua,user,post_id);
//                String totaldong = binding.tvMoney.getText().toString().trim();
//                moneyPresenter.napTien(tien,totaldong,userInfo);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private List<PhotoSlide> getListPhoto() {
        List<PhotoSlide> list = new ArrayList<>();
        list.add(new PhotoSlide(image1));
        if (image2.isEmpty()==false){
            list.add(new PhotoSlide(image2));
        }
        if (image3.isEmpty()==false){
            list.add(new PhotoSlide(image3));
        }
        if (image4.isEmpty()==false){
            list.add(new PhotoSlide(image4));
        }
        return list;
    }

    @Override
    public void setTime(String times) {
        binding.tvTimeDetail.setText(times);
    }

    @Override
    public void setEditPost(String userId, UserInfo userInfo) {
        user = userInfo;
        user_id = userId;
        if (user_id_post.equals(userId)){
            binding.llEditPost.setVisibility(View.VISIBLE);
            long currentTime = System.currentTimeMillis();
            Long longtimeexp = Long.valueOf(timeexp);
            if (longtimeexp>currentTime){
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(longtimeexp);

                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH)+1;
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                binding.tvTimeExp.setText(mDay+"/"+mMonth+"/"+mYear);
                binding.llTimeExp.setVisibility(View.VISIBLE);
                binding.btnTopPostR.setVisibility(View.VISIBLE);
                binding.btnTopPost.setVisibility(View.GONE);
            }
        }
        //tắt progressDialog
//        progressDialog.dismiss();
    }

    @Override
    public void getSuccess(String mess) {
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
        mainActivity.goToSellFragment();
    }

    @Override
    public void getError(String mess) {
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getToast(String mess) {
        binding.tvFavorite.setVisibility(View.GONE);
        binding.tvFavoriteOK.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setFavoriteOK() {
        binding.tvFavorite.setVisibility(View.GONE);
        binding.tvFavoriteOK.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTimeExp(String mess, long timeExp) {
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeExp);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH)+1;
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        binding.tvTimeExp.setText(mDay+"/"+mMonth+"/"+mYear);
        binding.llTimeExp.setVisibility(View.VISIBLE);
        binding.btnTopPostR.setVisibility(View.VISIBLE);
        binding.btnTopPost.setVisibility(View.GONE);
    }

    @Override
    public void setUserPostInfo(UserInfo userInfo) {
        if (userInfo.getAvatar().equals("")==false){
            Glide.with(getContext()).load(userInfo.getAvatar()).into(binding.imgAvatarrr);
        }
    }
}