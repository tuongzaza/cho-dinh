package me.tuong.chodinh.main.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.tuong.chodinh.R;
import me.tuong.chodinh.main.home.model.Post;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<Post> postList;
    private IClickItemListener mIClickItemListener;
    private static final int TYPE_ITEM =1;
    private static final int TYPE_LOADING =2;
    private boolean isLoadingAdd;

    @Override
    public int getItemViewType(int position) {
        if (postList!=null && position == postList.size()-1 && isLoadingAdd){
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    public interface IClickItemListener{
        void onCLickItemUser(Post post);
    }

    public PostAdapter( List<Post> postList, IClickItemListener mIClickItemListener) {
        this.postList = postList;
        this.mIClickItemListener = mIClickItemListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_ITEM == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_home,parent,false);
            return new PostViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM){
            context = holder.itemView.getContext();
            Post post = postList.get(position);
            PostViewHolder postViewHolder = (PostViewHolder) holder;
            if (post == null ){
                return;
            }
            Glide.with(context).load(post.getPhoto1()).into(postViewHolder.imvPhoto);
            postViewHolder.tvImgNumber.setText(post.getCountimg());
            postViewHolder.tvTitlePost.setText(post.getTitle());
            postViewHolder.tvPrice.setText(post.getPrice());
            postViewHolder.tvProvince.setText(post.getProvince());
            postViewHolder.tvTimes.setText(getTime(post));
            postViewHolder.llPostHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIClickItemListener.onCLickItemUser(post);
                }
            });
        }
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
            llPostHome = itemView.findViewById(R.id.llPostHome);
            imvPhoto = itemView.findViewById(R.id.imvPhoto);
            tvImgNumber = itemView.findViewById(R.id.tvImgNumber);
            tvTitlePost = itemView.findViewById(R.id.tvTitlePost);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvProvince = itemView.findViewById(R.id.tvAddress);
            tvTimes = itemView.findViewById(R.id.tvTime);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{

        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public void addFooterLoading(){
        isLoadingAdd = true;
        postList.add(new Post("","","","","","","","","","","","","","","","","",""));
    }

    public void removeFooterLoading(){
        isLoadingAdd = false;

        int position = postList.size() -1;
        Post post = postList.get(position);
        if (post!=null){
            postList.remove(position);
            notifyItemRemoved(position);
        }
    }
}
