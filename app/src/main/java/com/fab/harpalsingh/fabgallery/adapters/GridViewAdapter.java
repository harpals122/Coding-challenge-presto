package com.fab.harpalsingh.fabgallery.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.fab.harpalsingh.fabgallery.APILayer.RetrofitServices;
import com.fab.harpalsingh.fabgallery.R;
import com.fab.harpalsingh.fabgallery.models.ImageSizeData;
import com.fab.harpalsingh.fabgallery.models.Photo;
import com.fab.harpalsingh.fabgallery.models.Photos;
import com.fab.harpalsingh.fabgallery.utilities.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.GridViewHolder> {

    private final Photos data;
    private final RequestManager glide;
    private final Context context;
    private RelativeLayout mainContentLayout;


    public GridViewAdapter(Context context, Photos photoData, RelativeLayout mainContentLayout) {
        this.data = photoData;
        glide = Glide.with(context);
        this.context = context;
        this.mainContentLayout = mainContentLayout;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_layout, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GridViewHolder holder, final int position) {
        holder.bindImageData(position);
        holder.bindImageSizeData(position);
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
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.size)
        TextView size;
        @BindView(R.id.image_dimensions)
        TextView image_dimensions;

        GridViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindImageData(final int position) {
            Photo photo = data.getPhoto().get(position);

            if (photo.getTitle().equals(""))
                title.setText(R.string.noTitle);
            else {
                String titleString = "Title: " + photo.getTitle();
                title.setText(titleString);
            }

            glide.load("http://farm2.staticflickr.com/" + photo.getServer() + "/" +
                    photo.getId() + "_" + photo.getSecret() + ".jpg").
                    thumbnail(Glide.with(context).load(R.drawable.image_placeholder)).into(image);
        }

        void bindImageSizeData(int position) {
            String photoId = data.getPhoto().get(position).getId();
            Call<ImageSizeData> callPlaces = RetrofitServices.getNYServiceInstance().getImageSizeData(photoId);
            callPlaces.enqueue(new Callback<ImageSizeData>() {
                @Override
                public void onResponse(@NonNull Call<ImageSizeData> call, @NonNull Response<ImageSizeData> response) {
                    ImageSizeData imageSizeData = response.body();

                    if (imageSizeData != null && imageSizeData.getStat().equals("ok") && imageSizeData.getSizes().getSize().size() > 0) {
                        StringBuilder sizeString = new StringBuilder();
                        StringBuilder dimensionString = new StringBuilder();

                        for (int i = 0; i <= imageSizeData.getSizes().getSize().size() - 1; i++) {
                            sizeString.append("Size :").append(imageSizeData.getSizes().getSize().get(i).getLabel()).append("\n");
                            dimensionString.append("Dimensions :").append(imageSizeData.getSizes().getSize().get(i).getHeight()).append(" * ").
                                    append(imageSizeData.getSizes().getSize().get(i).getWidth()).append(" px\n");
                        }

                        size.setText(sizeString.toString());
                        image_dimensions.setText(dimensionString.toString());

                    } else {
                        size.setText(R.string.errorSizeInformation);
                        image_dimensions.setText(R.string.errorSizeDimension);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ImageSizeData> call, @NonNull Throwable t) {
                    Utilities.showSnackBar("Failed to load image size and dimensions. Please try again", mainContentLayout);
                }
            });
        }
    }
}