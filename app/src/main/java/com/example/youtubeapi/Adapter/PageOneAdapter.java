package com.example.youtubeapi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.youtubeapi.MainActivity2;
import com.example.youtubeapi.Model.PageOneModel;
import com.example.youtubeapi.R;

import java.util.ArrayList;

public class PageOneAdapter extends RecyclerView.Adapter<PageOneAdapter.PageOneViewHolder> {
    ArrayList<PageOneModel> list;
    Context context;


    public PageOneAdapter(ArrayList<PageOneModel> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public PageOneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.signle_item,parent,false);
        return new PageOneViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PageOneViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.des.setText(list.get(position).getDes());
        holder.views.setText(list.get(position).getViews());
        holder.likes.setText(list.get(position).getLike());
        Glide.with(context).load(list.get(position).getImgUrl()).into(holder.img);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent(list.get(position).getVideoId(),list.get(position).getLike(),list.get(position).getViews()
                ,list.get(position).getTitle(),list.get(position).getDes());
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendIntent(list.get(position).getVideoId(),list.get(position).getLike(),list.get(position).getViews()
                        ,list.get(position).getTitle(),list.get(position).getDes());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void sendIntent(String vdId,String likes,String views,String title,String des){
        Intent intent=new Intent(context, MainActivity2.class);
        intent.putExtra("videoId",vdId);
        intent.putExtra("views",views);
        intent.putExtra("likes",likes);
        intent.putExtra("title",title);
        intent.putExtra("des",des);
        context.startActivity(intent);
    }

    public class PageOneViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView title,des,views,likes;
        public PageOneViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img);
            title=itemView.findViewById(R.id.title);
            des=itemView.findViewById(R.id.description);
            views=itemView.findViewById(R.id.views);
            likes=itemView.findViewById(R.id.likes);

        }
    }
}
