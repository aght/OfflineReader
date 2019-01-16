package com.aght.offlinereader;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SavedPageViewHolder extends RecyclerView.ViewHolder {
    private ImageView pagePreview;
    private TextView pageTitle;
    private TextView pageUrl;

    public SavedPageViewHolder(View view) {
        super(view);

        pagePreview = (ImageView) view.findViewById(R.id.page_preview);
        pageTitle = (TextView) view.findViewById(R.id.page_title);
        pageUrl = (TextView) view.findViewById(R.id.page_url);
    }

    public ImageView getPagePreview() {
        return pagePreview;
    }

    public TextView getPageTitle() {
        return pageTitle;
    }

    public TextView getPageUrl() {
        return pageUrl;
    }
}