package me.tuong.chodinh.main.more.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dd.processbutton.iml.ActionProcessButton;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import me.tuong.chodinh.R;
import me.tuong.chodinh.databinding.FragmentEditUserBinding;
import me.tuong.chodinh.main.NavHideShow;
import me.tuong.chodinh.main.Ultil.RealPathUtil;
import me.tuong.chodinh.main.home.adapter.PostAdapter;
import me.tuong.chodinh.main.home.model.Post;
import me.tuong.chodinh.main.home.model.UserInfo;
import me.tuong.chodinh.main.more.minterface.EditUserInterface;
import me.tuong.chodinh.main.more.presenter.EditUserPresenter;

import static android.app.Activity.RESULT_OK;


public class EditUserFragment extends Fragment implements EditUserInterface {
    public static final String TAG = EditUserFragment.class.getName();
    FragmentEditUserBinding binding;
    private EditUserPresenter editUserPresenter;
    public static final int MY_REQUEST_CODE = 100;
    private Uri uri;
    private String password, passwordold;
    private UserInfo userInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_user, container, false);
        editUserPresenter = new EditUserPresenter(this);
        //ẩn thanh nav
        ((NavHideShow) getActivity()).setNavHideShow(false, "Chỉnh sửa trang cá nhân", getResources().getDrawable(R.drawable.ic_baseline_keyboard_backspace_24));
        setHasOptionsMenu(true);

        binding.btnUpdateProgress.setMode(ActionProcessButton.Mode.ENDLESS);

        //lấy dữ liệu
        editUserPresenter.getUserInfo(getActivity());


        userInfo = new UserInfo(null,null,null,null,0,null,null,"",null, null);

        binding.btnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        binding.btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btnUpdateProgress.setVisibility(View.VISIBLE);
                binding.btnUpdateProgress.setProgress(1);
                userInfo.setName(binding.etName.getText().toString().trim());
                userInfo.setAddress(binding.etAddress.getText().toString().trim());
                userInfo.setEmailreal(binding.etEmail.getText().toString().trim());
                String pw = binding.etPass.getText().toString().trim();
                if (pw!=""){
                    userInfo.setPassword(pw);
                }
                userInfo.setEmail(binding.etSdt.getText().toString().trim());
                if (uri !=null){
                    userInfo.setAvatar(RealPathUtil.getRealPath(getContext(),uri));
                }
                getDialog(userInfo);
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

    private void getDialog(UserInfo userInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận mật khẩu");

        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog_password, (ViewGroup) getView(),false);
        final EditText input = view.findViewById(R.id.tvPassDialog);

        builder.setView(view);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                password = input.getText().toString().trim();
                editUserPresenter.checkPass(password,passwordold, userInfo);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                binding.btnUpdateProgress.setVisibility(View.GONE);
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }

    private void onClickRequestPermission() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            openGallery();
            return;
        }

        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery();
        }else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),MY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == MY_REQUEST_CODE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                uri = selectedImageUri;
                if (null != selectedImageUri) {
                    try{
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                        binding.imgAvt.setImageBitmap(bitmap);

                    }catch (IOException e){

                    }
                }
            }
        }
    }

    @Override
    public void setUserInfo(UserInfo user) {
        passwordold = user.getPassword();
        binding.etName.setText(user.getName());
        binding.etAddress.setText(user.getAddress());
        binding.etSdt.setText(user.getEmail());
        binding.etEmail.setText(user.getEmailreal());
        if (user.getAvatar().equals("")==false){
            Glide.with(getContext()).load(user.getAvatar()).into(binding.imgAvtOld);
        }
    }

    @Override
    public void wrongPassword() {

    }

    @Override
    public void messenger(String mess) {
        Toast.makeText(getContext(),mess, Toast.LENGTH_LONG).show();
        binding.btnUpdateProgress.setProgress(100);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.btnUpdateProgress.setVisibility(View.GONE);
            }
        },2000);
    }
}