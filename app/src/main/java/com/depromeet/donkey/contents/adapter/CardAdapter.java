package com.depromeet.donkey.contents.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.depromeet.donkey.R;
import com.depromeet.donkey.contents.data.CardInfo;
import com.depromeet.donkey.contents.view.ContentsListActivity;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.RecyclerViewHolders> {

    private Context context;
    private ArrayList<CardInfo> cardList;

    public CardAdapter(Context context, ArrayList<CardInfo> cardList) {
        this.context = context;
        this.cardList = cardList;
    }

    @Override
   public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_item, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {

        final int STRLENGTH = 20;

        holder.tvDday.setText("D - " + cardList.get(position).getDday());

        String strTitle = cardList.get(position).getTitle();

        if (strTitle.length() > STRLENGTH) {
            strTitle = strTitle.substring(0, STRLENGTH);
            strTitle = strTitle.substring(0, strTitle.lastIndexOf(' ')) +"...";
        }

        holder.tvTitle.setText(strTitle);
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    //뷰홀더 클래스 정의 - 별도 파일로 하거나 어답터 안에서 정의해 줄 수 있다.
    //여기에서 반복적으로 사용되는 각종 뷰들을 정의해 준다.
    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle;
        private TextView tvDday;
        private TextView tvMore;
        private ImageView imgMore;

        public RecyclerViewHolders(View itemView) {
            super(itemView);

            tvDday = (TextView) itemView.findViewById(R.id.tv_dday);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_preview);
            tvMore = (TextView) itemView.findViewById(R.id.tv_more);
            imgMore = (ImageView) itemView.findViewById(R.id.more_icon);

            tvMore.setOnClickListener(activityTrans);
            imgMore.setOnClickListener(activityTrans);

        }

        View.OnClickListener activityTrans = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ContentsListActivity) context).ActivityTrans(getLayoutPosition());
            }
        };

        //클릭 이벤트 정의
        @Override
        public void onClick(View view) {

        }

    }

    public void setListItems(ArrayList<CardInfo> cardList) {
        this.cardList = cardList;
        notifyDataSetChanged();
    }
}
