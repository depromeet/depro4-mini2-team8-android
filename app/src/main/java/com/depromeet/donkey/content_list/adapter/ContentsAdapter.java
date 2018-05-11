package com.depromeet.donkey.content_list.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.depromeet.donkey.R;
import com.depromeet.donkey.content_list.callback.AdapterCallback;
import com.depromeet.donkey.main.data.Marker;

import java.util.ArrayList;

import butterknife.BindView;

public class ContentsAdapter extends RecyclerView.Adapter<ContentsAdapter.ContentsViewHolder>{
    private static final String TAG = ContentsAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<Marker> items;
    private AdapterCallback callback;

    public ContentsAdapter(Context context, ArrayList<Marker> items, AdapterCallback callback) {
        this.context = context;
        this.items = items;
        this.callback = callback;
    }

    public class ContentsViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvDday;
        private TextView tvMore;

        public ContentsViewHolder(View itemView) {
            super(itemView);
            tvDday = (TextView) itemView.findViewById(R.id.tv_dday);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_preview);
            tvMore = (TextView) itemView.findViewById(R.id.tv_more);
        }
    }

    @NonNull
    @Override
    public ContentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, null);
        return new ContentsViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentsViewHolder holder, final int position) {
        final int STRLENGTH = 20;

        holder.tvDday.setText("D - " + items.get(position).getCreateAt());

        String strTitle = items.get(position).getTitle();

        if (strTitle.length() > STRLENGTH) {
            strTitle = strTitle.substring(0, STRLENGTH);
            strTitle = strTitle.substring(0, strTitle.lastIndexOf(' ')) +"...";
        }

        holder.tvTitle.setText(strTitle);

        holder.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.startContentActivity(items.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
