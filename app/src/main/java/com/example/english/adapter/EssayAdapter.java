package com.example.english.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.english.R;
import com.example.english.model.Essay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;


public class EssayAdapter extends RecyclerView.Adapter<EssayAdapter.EssayViewHolder> {

    private OnItemClickListener listener; // 声明点击事件监听器

    // 定义接口用于回调选中的 item
    public interface OnItemClickListener {
        void onItemClick(Essay essay);
    }

    // 设置点击事件监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    Context context;

    ArrayList<Essay> essayArrayList;

    public EssayAdapter(Context context, ArrayList<Essay> essayArrayList) {
        this.context = context;
        this.essayArrayList = essayArrayList;
    }

    public class EssayViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView publish;
        TextView author;
        TextView collect_num;
        TextView essay_id;
        public EssayViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_title);
            publish = itemView.findViewById(R.id.text_publish_time);
            author = itemView.findViewById(R.id.text_author);
            collect_num = itemView.findViewById(R.id.text_favorite_count);
            essay_id = itemView.findViewById(R.id.text_essay_id);

            // 设置点击事件监听器
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    System.out.println(position);
                    Essay essay = essayArrayList.get(position);
                    System.out.println(essay.getEssay_id());
                    if (position != RecyclerView.NO_POSITION && listener != null) {
//                        Essay essay = essayArrayList.get(position);
//                        System.out.println(essay.getEssay_id());
                        listener.onItemClick(essay); // 回调选中的 item
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public EssayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.essay_item, parent, false);
        return new EssayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EssayViewHolder holder, int position) {
        Essay essay = essayArrayList.get(position);
        holder.title.setText("标题:"+essay.getTitle());
        holder.publish.setText("发布时间:"+essay.getPublishAt());
        holder.author.setText("作者:"+essay.getAuthor());
        holder.collect_num.setText("收藏数:"+essay.getCollect_num());
        holder.essay_id.setText(essay.getEssay_id());
    }

    @Override
    public int getItemCount() {
        return essayArrayList.size();
    }
}
