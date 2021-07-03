package me.tuong.chodinh.main.notification.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.List;

import me.tuong.chodinh.R;
import me.tuong.chodinh.room.News;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{
    private List<News> newsList;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private IClickItemUser iClickItemUser;

    public interface IClickItemUser{
        void deleteNews(News news);
    }

    public void setData(List<News> list){
        this.newsList = list;
        notifyDataSetChanged();
    }

    public NewsAdapter(IClickItemUser iClickItemUser) {
        this.iClickItemUser = iClickItemUser;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        if (news == null ){
            return;
        }
        viewBinderHelper.bind(holder.swipeNews, String.valueOf(news.getId()));
        holder.tvTitleNews.setText(news.getMessenger());
        holder.imgDeleteNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemUser.deleteNews(news);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (newsList!=null){
            return newsList.size();
        }
        return 0;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{
        SwipeRevealLayout swipeNews;
        TextView tvTitleNews;
        LinearLayout imgDeleteNews;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            swipeNews = itemView.findViewById(R.id.swipeNews);
            tvTitleNews = itemView.findViewById(R.id.tvTitleNews);
            imgDeleteNews = itemView.findViewById(R.id.imgDeleteNews);
        }
    }
}
