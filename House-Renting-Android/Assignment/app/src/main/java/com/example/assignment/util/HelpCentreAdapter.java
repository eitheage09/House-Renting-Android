package com.example.assignment.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;
import com.example.assignment.model.DataClass;

import java.util.ArrayList;

public class HelpCentreAdapter extends RecyclerView.Adapter<HelpCentreAdapter.ViewHolderClass> {

    private final ArrayList<DataClass> dataList;

    public HelpCentreAdapter(ArrayList<DataClass> dataList) {
        this.dataList = dataList;
    }

    public static class ViewHolderClass extends RecyclerView.ViewHolder {
        public ImageView rvImage;
        public TextView rvTitle;
        public TextView rvMessage;

        public ViewHolderClass(View itemView) {
            super(itemView);
            rvImage = itemView.findViewById(R.id.image);
            rvTitle = itemView.findViewById(R.id.title);
            rvMessage = itemView.findViewById(R.id.message);
        }
    }

    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.help_layout, parent, false);
        return new ViewHolderClass(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolderClass holder, int position) {
        DataClass currentItem = dataList.get(position);
        holder.rvImage.setImageResource(currentItem.getDataImage());
        holder.rvTitle.setText(currentItem.getDataTitle());
        holder.rvMessage.setText(currentItem.getDataDesc());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
