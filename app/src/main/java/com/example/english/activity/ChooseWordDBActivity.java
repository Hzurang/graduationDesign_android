package com.example.english.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.english.R;
import com.example.english.adapter.WordBookAdapter;
import com.example.english.config.ConstantData;
import com.example.english.dto.ItemWordBook;
import com.example.english.util.ActivityCollector;

import java.util.ArrayList;
import java.util.List;

public class ChooseWordDBActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ImageView imgRecover, imBack;

    // 书单数据
    private List<ItemWordBook> itemWordBookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_word_db);

        init();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        initData();
        WordBookAdapter wordBookAdapter = new WordBookAdapter(itemWordBookList, this);
        recyclerView.setAdapter(wordBookAdapter);

        imgRecover.setVisibility(View.GONE);
//        imgRecover.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseWordDBActivity.this);
//                builder.setTitle("提示")
//                        .setMessage("如果您之前有备份数据，可以点此进行还原数据")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent(ChooseWordDBActivity.this, SynchronyActivity.class);
//                                intent.putExtra(SynchronyActivity.TYPE_NAME, false);
//                                startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("取消", null)
//                        .show();
//            }
//        });
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    // 初始化控件
    private void init() {
        recyclerView = findViewById(R.id.recycler_word_book_list);
        imgRecover = findViewById(R.id.img_wb_recover);
        imBack = findViewById(R.id.im_back1);
    }

    // 初始化数据
    private void initData() {
        itemWordBookList.add(new ItemWordBook(ConstantData.CET4, ConstantData.bookNameById(ConstantData.CET4), ConstantData.wordTotalNumberById(ConstantData.CET4), "来源：趣词词典", ConstantData.bookPicById(ConstantData.CET4)));
        itemWordBookList.add(new ItemWordBook(ConstantData.CET6, ConstantData.bookNameById(ConstantData.CET6), ConstantData.wordTotalNumberById(ConstantData.CET6), "来源：趣词词典", ConstantData.bookPicById(ConstantData.CET6)));
        itemWordBookList.add(new ItemWordBook(ConstantData.TEM4, ConstantData.bookNameById(ConstantData.TEM4), ConstantData.wordTotalNumberById(ConstantData.TEM4), "来源：趣词词典", ConstantData.bookPicById(ConstantData.TEM4)));
        itemWordBookList.add(new ItemWordBook(ConstantData.TEM8, ConstantData.bookNameById(ConstantData.TEM8), ConstantData.wordTotalNumberById(ConstantData.TEM8), "来源：趣词词典", ConstantData.bookPicById(ConstantData.TEM8)));
        itemWordBookList.add(new ItemWordBook(ConstantData.KAOYAN, ConstantData.bookNameById(ConstantData.KAOYAN), ConstantData.wordTotalNumberById(ConstantData.KAOYAN), "来源：趣词词典", ConstantData.bookPicById(ConstantData.KAOYAN)));
        itemWordBookList.add(new ItemWordBook(ConstantData.GRE, ConstantData.bookNameById(ConstantData.GRE), ConstantData.wordTotalNumberById(ConstantData.GRE), "来源：趣词词典", ConstantData.bookPicById(ConstantData.GRE)));
        itemWordBookList.add(new ItemWordBook(ConstantData.TOEFL, ConstantData.bookNameById(ConstantData.TOEFL), ConstantData.wordTotalNumberById(ConstantData.TOEFL), "来源：趣词词典", ConstantData.bookPicById(ConstantData.TOEFL)));
        itemWordBookList.add(new ItemWordBook(ConstantData.IELTS, ConstantData.bookNameById(ConstantData.IELTS), ConstantData.wordTotalNumberById(ConstantData.IELTS), "来源：趣词词典", ConstantData.bookPicById(ConstantData.IELTS)));
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChooseWordDBActivity.this);
        builder.setTitle("提示")
                .setMessage("确定要退出吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCollector.finishAll();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
