package me.tuong.chodinh.main.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

import me.tuong.chodinh.R;
import me.tuong.chodinh.main.home.model.PhotoSlide;

public class PhotoSlideAdapter extends PagerAdapter {

    private Context context;
    private List<PhotoSlide> photoSlides;

    public PhotoSlideAdapter(Context context, List<PhotoSlide> photoSlides) {
        this.context = context;
        this.photoSlides = photoSlides;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_slide_home,container,false);
        ImageView imgPhoto = view.findViewById(R.id.img_slide);

        PhotoSlide photoSlide = photoSlides.get(position);
        if (photoSlide !=null){
            Glide.with(context).load(photoSlide.getUrlPhoto()).into(imgPhoto);
        }

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if (photoSlides!=null){
            return photoSlides.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
