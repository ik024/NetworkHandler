package com.ik.networkhandler.controller;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ik.networkhandler.R;
import com.ik.networkhandler.common.ImageDownloader;
import com.ik.networkhandler.model.pojo.Post;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class DataAdapter extends Adapter<DataAdapter.Holder> {

    private List<Post> mItems;
    DataAdapter(List<Post> items){
        mItems = items;
    }

    void updateList(List<Post> updatedItems){
        mItems = updatedItems;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_data, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        Post post = mItems.get(position);

        holder.setImagePost(post.getUrls().getThumb());
        holder.setUserImage(post.getUser().getProfileImage().getSmall());
        holder.setUserName(post.getUser().getName());
        holder.setLikeImage(post.getLikedByUser());
        holder.setLikes(post.getLikes());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        private ImageView ivImagePost, ivLike;
        private CircleImageView ivUserImage;
        private TextView tvUserName, tvLikes;


        Holder(View itemView) {
            super(itemView);
            ivImagePost = (ImageView) itemView.findViewById(R.id.image_view);
            ivUserImage = (CircleImageView) itemView.findViewById(R.id.iv_user_image);
            ivLike = (ImageView) itemView.findViewById(R.id.iv_like);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            tvLikes = (TextView) itemView.findViewById(R.id.tv_likes);
        }

        void setImagePost(String url){
            ImageDownloader.loadBitmap(url, ivImagePost);
        }
        void setUserImage(String url){
            ImageDownloader.loadBitmap(url, ivUserImage);
        }

        void setUserName(String name){
            tvUserName.setText(name);
        }

        void setLikeImage(boolean likedByUser){
            int resId = likedByUser ? R.drawable.test : R.drawable.unlike;
            ivLike.setImageResource(resId);
        }

        void setLikes(int likes){
            tvLikes.setText(String.format("%d likes", likes));
        }
    }
}
