package me.tuong.chodinh.main.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.tuong.chodinh.R;
import me.tuong.chodinh.main.home.model.Post;

public class PostTopAdapter extends RecyclerView.Adapter<PostTopAdapter.PostTopViewHolder>{
    private Context context;
    private List<Post> postList;
    private IClickItemListener mIClickItemListener;


    public interface IClickItemListener{
        void onCLickItemUser(Post post);
    }

    public PostTopAdapter( List<Post> postList, IClickItemListener mIClickItemListener) {
        this.postList = postList;
        this.mIClickItemListener = mIClickItemListener;
    }

    @NonNull
    @Override
    public PostTopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_top,parent,false);
        return new PostTopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostTopViewHolder holder, int position) {
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
        holder.llPostTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickItemListener.onCLickItemUser(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (postList!=null){
            return postList.size();
        }
        return 0;
    }

    public class PostTopViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout llPostTop;
        ImageView imvPhoto;
        TextView tvImgNumber;
        TextView tvTitlePost;
        TextView tvPrice;
        TextView tvProvince;

        public PostTopViewHolder(@NonNull View itemView) {
            super(itemView);
            llPostTop = itemView.findViewById(R.id.llPostTop);
            imvPhoto = itemView.findViewById(R.id.imvPhotoTop);
            tvImgNumber = itemView.findViewById(R.id.tvImgNumberTop);
            tvTitlePost = itemView.findViewById(R.id.tvTitlePostTop);
            tvPrice = itemView.findViewById(R.id.tvPriceTop);
            tvProvince = itemView.findViewById(R.id.tvAddressTop);
        }
    }
}
