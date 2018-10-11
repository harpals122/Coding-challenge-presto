package com.example.harpalsingh.fabgallery.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.harpalsingh.fabgallery.R;
import com.example.harpalsingh.fabgallery.models.Photos;

import java.util.Locale;


public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.GridViewHolder> {

    private final ConstraintSet constraintSet = new ConstraintSet();
    private final Photos data;
    private final int visibleThreshold = 6;
    private final RequestManager glide;
    Context context;

    public GridViewAdapter(Context context, Photos photoData) {
        this.data = photoData;
        glide = Glide.with(context);
        this.context = context;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_layout, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GridViewHolder holder, final int position) {
        holder.bindData(position);


    }

    @Override
    public int getItemCount() {
        return data.getPhoto().size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class GridViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final ConstraintLayout constraintLayout;


        GridViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            constraintLayout = itemView.findViewById(R.id.parent_constraint);
        }

        private void bindData(final int position) {

            glide.load("http://farm2.staticflickr.com/" + data.getPhoto().get(position).getServer() + "/" +
                    data.getPhoto().get(position).getId() + "_" + data.getPhoto().get(position).getSecret() + ".jpg").
                    thumbnail(Glide.with(context).load(R.drawable.image_placeholder)).
                    listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(image);
        }
    }
}