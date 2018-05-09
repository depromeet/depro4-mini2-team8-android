package com.depromeet.donkey.contents.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import com.depromeet.donkey.R;
import com.depromeet.donkey.contents.adapter.CardAdapter;
import com.depromeet.donkey.contents.data.CardInfo;

public class ContentsListActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayList<CardInfo> listItem;
    private CardAdapter cardAdapter;
    private GridLayoutManager gridManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list_activity);

        listItem = getCardList();
        gridManager = new GridLayoutManager(ContentsListActivity.this, 2);
        recyclerView = (RecyclerView) findViewById(R.id.preview_recycler);
        recyclerView.setLayoutManager(gridManager);
        cardAdapter = new CardAdapter(this, listItem);
        recyclerView.setAdapter(cardAdapter);

    }

    private ArrayList<CardInfo> getCardList(){
        // 나중에 여기서 카드 리스트 가져오는 api를 호출하면 되겠지 ??
        ArrayList<CardInfo> items = new ArrayList<CardInfo>();
        items.add(new CardInfo(3, "Don't mess it up, talking that shit. Only gonna push me away, that's it. When you say you love me. That make ma crazy. Here we go again."));
        items.add(new CardInfo(0, "설레어 너와 나의 랑데뷰"));
        items.add(new CardInfo(1,"내맘을 들었다놨다해"));
        items.add(new CardInfo(4,"맘대루"));
        items.add(new CardInfo(8,"지금내눈엔눈엔"));
        items.add(new CardInfo(16,"네어깨무릎발"));
        items.add(new CardInfo(30,"OH"));
        items.add(new CardInfo(7,"숨이탁막힐것같아"));
        items.add(new CardInfo(5,"정신을또놔"));
        items.add(new CardInfo(22,"네매력에"));
        items.add(new CardInfo(10,"난 놀라게 돼 또"));
        items.add(new CardInfo(5,"히릿히릿호"));
        items.add(new CardInfo(27,"우우우 무슨말이필요해"));
        items.add(new CardInfo(1,"숨이콱 막힐것같아"));
        items.add(new CardInfo(9,"자꾸만 봐 자꾸와"));
        items.add(new CardInfo(31,"이제 나만 보게 될거야"));
        items.add(new CardInfo(4,"너를 들었다놨다"));
        items.add(new CardInfo(4,"들었다놨다"));
        items.add(new CardInfo(8,"들었다놨다해"));
        items.add(new CardInfo(23,"니맘을"));
        items.add(new CardInfo(29,"들었다놨다"));
        items.add(new CardInfo(19,"들었다놨다"));
        items.add(new CardInfo(2,"들었다놨다해"));
        items.add(new CardInfo(7,"쏟아지는 마 터터터터터터터치"));
        items.add(new CardInfo(11,"내머리부터 뿜뿜"));
        items.add(new CardInfo(14,"내 발끝까지 뿜뿜 뿜뿜"));

        return items;
    }

    public void ActivityTrans(int pos) {
        // Intent for selected card
        Intent intent = new Intent(ContentsListActivity.this, ContentActivity.class);
        intent.putExtra("title", listItem.get(pos).getTitle());
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            // Intent for edit card
            case R.id.floating_add :
                Intent intent = new Intent(ContentsListActivity.this, ContentEditActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
        }
    }
}