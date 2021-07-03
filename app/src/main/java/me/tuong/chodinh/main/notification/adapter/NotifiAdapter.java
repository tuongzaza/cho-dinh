package me.tuong.chodinh.main.notification.adapter;

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

public class NotifiAdapter extends RecyclerView.Adapter<NotifiAdapter.NotifiViewHolder>{
    private Context context;
    private List<Post> postList;

    public NotifiAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Post> list){
        this.postList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotifiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification,parent,false);
        return new NotifiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifiViewHolder holder, int position) {
        context = holder.itemView.getContext();
        Post post = postList.get(position);
        if (post == null ){
            return;
        }
        Glide.with(context).load(post.getPhoto1()).into(holder.imvPhotoNotifi);
        holder.tvTitleNotifi.setText("Bài đăng bạn yêu thích "+ post.getTitle() + " đã được bán thành công!");
    }


    @Override
    public int getItemCount() {
        if (postList!=null){
            return postList.size();
        }
        return 0;
    }

    public class NotifiViewHolder extends RecyclerView.ViewHolder{
        LinearLayout llNotifi;
        ImageView imvPhotoNotifi;
        TextView tvTitleNotifi;

        public NotifiViewHolder(@NonNull View itemView) {
            super(itemView);
            llNotifi = itemView.findViewById(R.id.llNotifi);
            imvPhotoNotifi = itemView.findViewById(R.id.imvPhotoNotifi);
            tvTitleNotifi = itemView.findViewById(R.id.tvTitleNotifi);
        }
    }
}
