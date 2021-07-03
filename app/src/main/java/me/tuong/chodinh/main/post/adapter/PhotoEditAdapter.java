package me.tuong.chodinh.main.post.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.tuong.chodinh.R;

public class PhotoEditAdapter extends RecyclerView.Adapter<PhotoEditAdapter.PhotoViewHolder>{

    private Context context;
    private List<String> stringList;

    public PhotoEditAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> list){
        this.stringList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_post,parent,false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {

        context = holder.itemView.getContext();
        String image = stringList.get(position);
        if (image == null ){
            return;
        }
        Glide.with(context).load(image).into(holder.imgPhoto);

    }

    @Override
    public int getItemCount() {
        if (stringList!=null){
            return stringList.size();
        }
        return 0;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgPhoto;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPhoto = itemView.findViewById(R.id.img_photo_post);
        }
    }

}
