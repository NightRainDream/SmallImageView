package com.night.smallimageview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.night.image.SmallImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private LayoutInflater mLayout;
    private List<Integer> mImageRes = new ArrayList<>();

    public ListAdapter() {
        mImageRes.add(R.drawable.test_square);
        mImageRes.add(R.drawable.test_height);
        mImageRes.add(R.drawable.test_width);
        mImageRes.add(R.drawable.text_list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mLayout.inflate(R.layout.item_test, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iv_image.setImageResource(getImageRes());
    }

    @Override
    public int getItemCount() {
        return 1000;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private SmallImageView iv_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_image);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.mLayout = LayoutInflater.from(recyclerView.getContext());
    }

    private final Random mRandom = new Random();

    private int getImageRes() {

        return mImageRes.get(mRandom.nextInt(4));
    }
}
