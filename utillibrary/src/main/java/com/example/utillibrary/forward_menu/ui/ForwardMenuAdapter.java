package com.example.utillibrary.forward_menu.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utillibrary.R;
import com.example.utillibrary.forward_menu.logic.ForwardMenuLogic;

import java.util.List;

public class ForwardMenuAdapter extends RecyclerView.Adapter<ForwardMenuAdapter.MenuHolder> {
    Context ctx;
    List<ForwardMenuLogic.ForwardMenuItem> listItems;
    AdapterView.OnItemClickListener itemClickListener;

    public ForwardMenuAdapter(Context context, List<ForwardMenuLogic.ForwardMenuItem> items,
                              AdapterView.OnItemClickListener listener) {
        this.ctx = context;
        this.listItems = items;
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(ctx).inflate(R.layout.forward_menu_item, parent, false);
        return new MenuHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
        holder.onBind(position, listItems.get(position));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class MenuHolder extends RecyclerView.ViewHolder {
        private final ImageView itemImg;
        private final TextView itemName;

        public MenuHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.menu_item_image);
            itemName = itemView.findViewById(R.id.menu_item_text);
        }

        public void onBind(int pos, ForwardMenuLogic.ForwardMenuItem item) {
            // 图标
            if (item.iconId > 0) {
                itemImg.setImageResource(item.iconId);
                itemImg.setVisibility(View.VISIBLE);
            } else {
                itemImg.setVisibility(View.GONE);
            }

            // 名称
            itemName.setText(item.itemName);

            itemView.setOnClickListener(v -> {
                itemClickListener.onItemClick(null, itemView, pos, 0);
            });
        }
    }
}
