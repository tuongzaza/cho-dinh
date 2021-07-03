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
import me.tuong.chodinh.main.home.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private Context context;
    private List<Category> categoryList;
    private IClickItemListener mIClickItemListener;

    public interface IClickItemListener{
        void onCLickItemUser(Category category);
    }

    public CategoryAdapter(List<Category> categoryList, IClickItemListener mIClickItemListener) {
        this.categoryList = categoryList;
        this.mIClickItemListener = mIClickItemListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        context = holder.itemView.getContext();
        Category category = categoryList.get(position);
        if (category == null ){
            return;
        }
        Glide.with(context).load(category.getCategoryThumb()).into(holder.imgCategory);
        holder.tvTitleCategory.setText(category.getCategoryName());
        holder.llCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickItemListener.onCLickItemUser(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (categoryList!=null){
            return categoryList.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        LinearLayout llCategory;
        ImageView imgCategory;
        TextView tvTitleCategory;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            llCategory = itemView.findViewById(R.id.llCate);
            imgCategory = itemView.findViewById(R.id.imgThumb);
            tvTitleCategory = itemView.findViewById(R.id.tvTitleCategory);
        }
    }
}
