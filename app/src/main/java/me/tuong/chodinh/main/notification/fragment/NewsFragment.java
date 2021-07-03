package me.tuong.chodinh.main.notification.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.tuong.chodinh.MainActivity;
import me.tuong.chodinh.R;
import me.tuong.chodinh.databinding.FragmentNewsBinding;
import me.tuong.chodinh.main.notification.adapter.NewsAdapter;
import me.tuong.chodinh.main.notification.minterface.NewsInterface;
import me.tuong.chodinh.main.notification.presenter.NewsPresenter;
import me.tuong.chodinh.room.News;
import me.tuong.chodinh.room.NewsDatabase;


public class NewsFragment extends Fragment implements NewsInterface {
    FragmentNewsBinding binding;
    private NewsPresenter newsPresenter;
    private MainActivity activity;
    private NewsAdapter newsAdapter;
    private List<News> newsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_news, container, false);
        newsPresenter = new NewsPresenter(this);
        activity = (MainActivity) getActivity();

        newsList = new ArrayList<>();

        newsPresenter.getInfo(activity);

        return binding.getRoot();
    }

    @Override
    public void loadList() {
        newsList = NewsDatabase.getInstance(activity).newsDAO().getListNews();
        newsAdapter = new NewsAdapter(new NewsAdapter.IClickItemUser() {
            @Override
            public void deleteNews(News news) {
                clickDeleteNews(news);
            }
        });
        newsAdapter.setData(newsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        binding.rcvNews.setLayoutManager(linearLayoutManager);
        binding.rcvNews.setAdapter(newsAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        binding.rcvNews.addItemDecoration(itemDecoration);
    }

    private void clickDeleteNews(News news) {
        new AlertDialog.Builder(activity).setTitle("Confirm delete News").setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Delete
                NewsDatabase.getInstance(activity).newsDAO().deleteNews(news);
                Toast.makeText(activity,"Delete news successfully", Toast.LENGTH_LONG).show();

                //load data
                newsList = NewsDatabase.getInstance(activity).newsDAO().getListNews();
                newsAdapter.setData(newsList);
            }
        }).setNegativeButton("No", null).show();
    }

    @Override
    public void requestLogin() {
        binding.rcvNews.setVisibility(View.GONE);
        binding.llLogin.setVisibility(View.VISIBLE);
    }
}