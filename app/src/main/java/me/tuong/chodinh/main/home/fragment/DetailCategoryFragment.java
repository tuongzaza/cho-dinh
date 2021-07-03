package me.tuong.chodinh.main.home.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.List;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.R;
import me.tuong.chodinh.databinding.FragmentDetailCategoryBinding;
import me.tuong.chodinh.main.NavHideShow;
import me.tuong.chodinh.main.home.adapter.Post1Adapter;
import me.tuong.chodinh.main.home.adapter.PostTopAdapter;
import me.tuong.chodinh.main.home.minterface.DetailCategoryInterface;
import me.tuong.chodinh.main.home.model.Post;
import me.tuong.chodinh.main.home.presenter.DetailCategoryPresenter;

import static androidx.recyclerview.widget.LinearLayoutManager.VERTICAL;

public class DetailCategoryFragment extends Fragment implements DetailCategoryInterface {
    public static final String TAG = DetailCategoryFragment.class.getName();
    FragmentDetailCategoryBinding binding;
    DetailCategoryPresenter detailCategoryPresenter;
    private MainActivity activity;
    private String provinceName, categoryName, key;
    private Post1Adapter post1Adapter;
    private PostTopAdapter postTopAdapter;
    private String cateDefault = "Tất cả danh mục";

    public DetailCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail_category, container, false);
        detailCategoryPresenter = new DetailCategoryPresenter(this);
        activity = (MainActivity) getActivity();

        //setHasOptionsMenu(true);

        //hiện thanh nav
        ((NavHideShow) getActivity()).setNavHideShow(true, "DANH MỤC", getResources().getDrawable(R.drawable.ic_baseline_keyboard_backspace_24));

        //lấy category, key mặc định
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null){
            key = (String) bundleReceive.get("key");
            categoryName = (String) bundleReceive.get("category_name");
        }

        //lấy danh sách thư mục
        detailCategoryPresenter.getCategory();

        //lấy địa chỉ từ định vị
        detailCategoryPresenter.getAddressLocal(activity);
        //lấy địa chỉ từ json
        detailCategoryPresenter.getDataAddress();

        return binding.getRoot();
    }

    @Override
    public void setAddress(String province) {
        provinceName = province;
    }

    @Override
    public void getDataAddress(List<String> list) {
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, list);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spProvince.setAdapter(stringArrayAdapter);

        if (provinceName != null) {
            int spinnerPosition = stringArrayAdapter.getPosition(provinceName);
            binding.spProvince.setSelectedIndex(spinnerPosition);
        }else {
            binding.spProvince.setSelectedIndex(0);
            provinceName = "Hà Nội";
        }

        //lấy danh sách tin
        detailCategoryPresenter.getPost(provinceName,categoryName, key);

        binding.spProvince.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                provinceName = list.get(position);

                //lấy danh sách tin
                detailCategoryPresenter.getPost(provinceName,categoryName, key);
            }
        });
    }

    @Override
    public void setCate(List<String> listCate) {
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, listCate);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spCate.setAdapter(stringArrayAdapter);

        if (categoryName != null) {
            int spinnerPosition = stringArrayAdapter.getPosition(categoryName);
            binding.spCate.setSelectedIndex(spinnerPosition);
        }

        if (categoryName == null){
            int spinnerPosition = stringArrayAdapter.getPosition(cateDefault);
            binding.spCate.setSelectedIndex(spinnerPosition);
        }

        binding.spCate.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                categoryName = listCate.get(position);
                //lấy danh sách tin
                detailCategoryPresenter.getPost(provinceName,categoryName,key);
            }
        });
    }

    @Override
    public void setPost(List<Post> listNew, List<Post> listTopNew) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        binding.rcvPostCate.setLayoutManager(linearLayoutManager);
        post1Adapter = new Post1Adapter(listNew, new Post1Adapter.IClickItemListener(){
            @Override
            public void onCLickItemUser(Post post) {
                activity.goToDetailPost(post);
            }
        });
        postTopAdapter = new PostTopAdapter(listTopNew, new PostTopAdapter.IClickItemListener(){
            @Override
            public void onCLickItemUser(Post post) {
                activity.goToDetailPost(post);
            }
        });

        ConcatAdapter concatAdapter = new ConcatAdapter(postTopAdapter, post1Adapter);
        binding.rcvPostCate.setAdapter(concatAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        binding.rcvPostCate.addItemDecoration(itemDecoration);

    }
}