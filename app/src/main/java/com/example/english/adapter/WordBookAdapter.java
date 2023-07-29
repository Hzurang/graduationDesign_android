package com.example.english.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.english.MainActivity;
import com.example.english.R;
import com.example.english.activity.ChangePlanActivity;
import com.example.english.config.ConfigData;
import com.example.english.dto.ItemWordBook;
import com.example.english.service.UserService;
import com.example.english.util.GsonUtil;
import com.example.english.util.MyApplication;
import com.example.english.util.ResponseUtil;
import com.example.english.util.RetrofitRequest;
import com.example.english.util.ToastUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WordBookAdapter extends RecyclerView.Adapter<WordBookAdapter.ViewHolder> {

    private List<ItemWordBook> mItemWordBookList;

    private Thread thread;

    private Context context;
    private Retrofit retrofit;
    private UserService userService;

    static class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView imgBook;
        TextView textBookName, textBookSource, textBookWordNum;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imgBook = itemView.findViewById(R.id.item_img_book);
            textBookName = itemView.findViewById(R.id.item_text_book_name);
            textBookSource = itemView.findViewById(R.id.item_text_book_source);
            textBookWordNum = itemView.findViewById(R.id.item_text_book_word_num);
        }

    }

    public WordBookAdapter(List<ItemWordBook> mItemWordBookList, Context context) {
        this.mItemWordBookList = mItemWordBookList;
        this.context = context;
        retrofit = RetrofitRequest.getInstance(context);
        userService = retrofit.create(UserService.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_list, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final ItemWordBook itemWordBook = mItemWordBookList.get(position);

                Call<ResponseUtil> call = userService.setVocabularyBook(itemWordBook.getBookId());
                int num = itemWordBook.getBookId();
                call.enqueue(new Callback<ResponseUtil>() {
                    @Override
                    public void onResponse(Call<ResponseUtil> call, Response<ResponseUtil> response) {
                        ResponseUtil res = response.body();
                        if (res.getCode() == 200) {
                            // 修改
                            Intent intent = new Intent(MyApplication.getContext(), MainActivity.class);
//                            Intent intent = new Intent(MyApplication.getContext(), ChangePlanActivity.class);
                            intent.putExtra(ConfigData.UPDATE_NAME, ConfigData.notUpdate);
                            intent.putExtra("bookId", num);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            MyApplication.getContext().startActivity(intent);
                        } else {
                            ToastUtil.display(MyApplication.getContext(), res.getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseUtil> call, Throwable t) {
                        Logger.e("setVocabularyBook", "onFailure: 网络连接错误");
                        t.printStackTrace();
                    }
                });
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemWordBook itemWordBook = mItemWordBookList.get(position);
        Glide.with(MyApplication.getContext()).load(itemWordBook.getBookImg()).into(holder.imgBook);
        holder.textBookName.setText(itemWordBook.getBookName());
        holder.textBookSource.setText(itemWordBook.getBookSource());
        holder.textBookWordNum.setText(itemWordBook.getBookWordNum() + "");
    }

    @Override
    public int getItemCount() {
        return mItemWordBookList.size();
    }

}
