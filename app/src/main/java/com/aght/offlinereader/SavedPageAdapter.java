package com.aght.offlinereader;

import android.content.res.Resources;
import android.service.restrictions.RestrictionsReceiver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SavedPageAdapter extends RecyclerView.Adapter<SavedPageViewHolder> {

    private List<SavedPage> savedPages;

    public SavedPageAdapter(List<SavedPage> savedPages) {
        this.savedPages = savedPages;
    }

    @Override
    public SavedPageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new SavedPageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SavedPageViewHolder holder, int position) {
        SavedPage savedPage = savedPages.get(position);

        // Issue with getting image for some reason
//        holder.getPagePreview().setImageDrawable(Resources.getSystem().getDrawable(R.drawable.placeholder));
        holder.getPageTitle().setText(savedPage.getPageTitle());
        holder.getPageUrl().setText(savedPage.getPageUrl());
    }

    @Override
    public int getItemCount() {
        return savedPages.size();
    }
}
