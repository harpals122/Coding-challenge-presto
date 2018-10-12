package com.example.harpalsingh.fabgallery.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.harpalsingh.fabgallery.APILayer.RetrofitServices;
import com.example.harpalsingh.fabgallery.R;
import com.example.harpalsingh.fabgallery.models.ImageSizeData;
import com.example.harpalsingh.fabgallery.models.Photos;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.GridViewHolder> {

    private final Photos data;
    private final RequestManager glide;
    private final Context context;

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

        String photoId = data.getPhoto().get(position).getId();
        Call<ImageSizeData> callPlaces = RetrofitServices.getNYServiceInstance().getImageSizeData(photoId);
        callPlaces.enqueue(new Callback<ImageSizeData>() {
            @Override
            public void onResponse(@NonNull Call<ImageSizeData> call, @NonNull Response<ImageSizeData> response) {

                if (response.body() != null && Objects.requireNonNull(response.body()).getStat().equals("ok") && Objects.requireNonNull(response.body()).getSizes().getSize().size() > 0) {

                    StringBuilder size = new StringBuilder();
                    StringBuilder dimensions = new StringBuilder();
                    for (int i = 0; i <= Objects.requireNonNull(response.body()).getSizes().getSize().size() - 1; i++) {
                        size.append("Size :").append(Objects.requireNonNull(response.body()).getSizes().getSize().get(i).getLabel()).append("\n");
                        dimensions.append("Dimensions :").append(Objects.requireNonNull(response.body()).getSizes().getSize().get(i).getHeight()).append(" * ").
                                append(Objects.requireNonNull(response.body()).getSizes().getSize().get(i).getWidth()).append(" px\n");
                    }
                    holder.size.setText(size.toString());
                    holder.dimensions.setText(dimensions.toString());
                } else {
                    holder.size.setText(R.string.noSizeInformation);
                    holder.dimensions.setText(R.string.noSizeDimension);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ImageSizeData> call, @NonNull Throwable t) {

            }
        });

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
        final TextView title;
        final TextView size;
        final TextView dimensions;

        GridViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            size = itemView.findViewById(R.id.size);
            dimensions = itemView.findViewById(R.id.image_dimensions);
        }

        private void bindData(final int position) {
            if (data.getPhoto().get(position).getTitle().equals(""))
                title.setText(R.string.noTitle);
            else {
                String titleString = R.string.titleLable + data.getPhoto().get(position).getTitle();
                title.setText(titleString);
            }

            glide.load("http://farm2.staticflickr.com/" + data.getPhoto().get(position).getServer() + "/" +
                    data.getPhoto().get(position).getId() + "_" + data.getPhoto().get(position).getSecret() + ".jpg").
                    thumbnail(Glide.with(context).load(R.drawable.image_placeholder)).into(image);
        }
    }
}