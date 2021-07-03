package me.tuong.chodinh.main.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.tuong.chodinh.R;
import me.tuong.chodinh.main.home.model.Post;

public class Post1Adapter extends RecyclerView.Adapter<Post1Adapter.PostViewHolder>{
    private Context context;
    private List<Post> postList;
    private IClickItemListener mIClickItemListener;


    public interface IClickItemListener{
        void onCLickItemUser(Post post);
    }

    public Post1Adapter( List<Post> postList, IClickItemListener mIClickItemListener) {
        this.postList = postList;
        this.mIClickItemListener = mIClickItemListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_1,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        context = holder.itemView.getContext();
        Post post = postList.get(position);
        if (post == null ){
            return;
        }
        Glide.with(context).load(post.getPhoto1()).into(holder.imvPhoto);
        holder.tvImgNumber.setText(post.getCountimg());
        holder.tvTitlePost.setText(post.getTitle());
        holder.tvPrice.setText(post.getPrice());
        holder.tvProvince.setText(post.getProvince());
        holder.tvTimes.setText(getTime(post));
        holder.llPostHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickItemListener.onCLickItemUser(post);
            }
        });
    }

    private String getTime(Post post) {
        String times = null;
        try {
            long currentTime = System.currentTimeMillis();
            long posttime = Long.parseLong(post.getTime());
            long time = (currentTime-posttime)/1000;
            if (time >= 31104000){
                times = (time/31104000)+" năm trước";
            }else {
                if (time >= 2592000){
                    times = (time/2592000)+" tháng trước";
                }else {
                    if (time >= 86400){
                        times = (time/86400)+" ngày trước";
                    } else {
                        if (time >= 3600){
                            times = (time/3600)+" giờ trước";
                        }else {
                            if (time >= 60){
                                times = (time/60)+ " phút trước";
                            }else {
                                times = "Vừa mới đăng";
                            }
                        }
                    }
                }
            }
        }catch (Exception e){

        }

        return times;
    }

    @Override
    public int getItemCount() {
        if (postList!=null){
            return postList.size();
        }
        return 0;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{
        LinearLayout llPostHome;
        ImageView imvPhoto;
        TextView tvImgNumber;
        TextView tvTitlePost;
        TextView tvPrice;
        TextView tvProvince;
        TextView tvTimes;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            llPostHome = itemView.findViewById(R.id.llPost1);
            imvPhoto = itemView.findViewById(R.id.imvPhoto1);
            tvImgNumber = itemView.findViewById(R.id.tvImgNumber1);
            tvTitlePost = itemView.findViewById(R.id.tvTitlePost1);
            tvPrice = itemView.findViewById(R.id.tvPrice1);
            tvProvince = itemView.findViewById(R.id.tvAddress1);
            tvTimes = itemView.findViewById(R.id.tvTime1);
        }
    }
}
