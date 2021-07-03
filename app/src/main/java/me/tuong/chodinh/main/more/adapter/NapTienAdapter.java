package me.tuong.chodinh.main.more.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import me.tuong.chodinh.R;

public class NapTienAdapter extends RecyclerView.Adapter<NapTienAdapter.NapTienViewHolder>{
    private List<String> napList;
    private IClickItemListener mIClickItemListener;

    public interface IClickItemListener{
        void onCLickItemUser(String tien);
    }

    public NapTienAdapter(List<String> napList, IClickItemListener mIClickItemListener) {
        this.napList = napList;
        this.mIClickItemListener = mIClickItemListener;
    }

    @NonNull
    @Override
    public NapTienViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nap_tien,parent,false);
        return new NapTienViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NapTienViewHolder holder, int position) {
        String tien = napList.get(position);
        if (tien == null ){
            return;
        }
        holder.tvDongTot.setText(tien);
        holder.tvTien.setText(tien);
        holder.cvNapTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickItemListener.onCLickItemUser(tien);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (napList!=null){
            return napList.size();
        }
        return 0;
    }

    public class NapTienViewHolder extends RecyclerView.ViewHolder{
        CardView cvNapTien;
        TextView tvDongTot;
        TextView tvTien;
        public NapTienViewHolder(@NonNull View itemView) {
            super(itemView);
            cvNapTien = itemView.findViewById(R.id.cvNapTien);
            tvDongTot = itemView.findViewById(R.id.tvNapdong);
            tvTien = itemView.findViewById(R.id.tvGiaMoney);
        }
    }
}
