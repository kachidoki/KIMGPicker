package com.kachidoki.ma.kimgpicker.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kachidoki.ma.kimgpicker.Bean.ImgFolder;
import com.kachidoki.ma.kimgpicker.KImgPicker;
import com.kachidoki.ma.kimgpicker.R;
import com.kachidoki.ma.kimgpicker.Utils.Utils;
import com.kachidoki.ma.kimgpicker.Widget.PopFolderWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kachidoki on 2017/6/12.
 */

public class PopFolderAdapter extends RecyclerView.Adapter<PopFolderAdapter.PopViewHolder>{

    private KImgPicker picker;
    private Context context;
    private int imageSize;
    private List<ImgFolder> folderList;
    private int lastSelsct = 0;
    private PopFolderWindow.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(PopFolderWindow.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public PopFolderAdapter(Context context){
        this.context = context;
        folderList = new ArrayList<>();
        picker = KImgPicker.getInstance();
        imageSize = Utils.getImageItemWidth(context);
    }

    public void setFolders(List<ImgFolder> list){
        folderList = list;
        notifyDataSetChanged();
    }

    public int getLastSelsct() {
        return lastSelsct;
    }

    public void setLastSelsct(int lastSelsct) {
        if (this.lastSelsct!=lastSelsct){
            this.lastSelsct = lastSelsct;
            notifyDataSetChanged();
        }
    }

    @Override
    public PopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PopViewHolder(LayoutInflater.from(context).inflate(R.layout.pop_folder,parent));
    }

    @Override
    public void onBindViewHolder(PopViewHolder holder, final int position) {
        holder.setData(folderList.get(position),position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null) onItemClickListener.onItemClick(folderList.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    class PopViewHolder extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView folderName;
        TextView imageCount;
        ImageView folderCheck;

        public PopViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.iv_cover);
            folderName = (TextView) itemView.findViewById(R.id.tv_folder_name);
            imageCount = (TextView) itemView.findViewById(R.id.tv_image_count);
            folderCheck = (ImageView) itemView.findViewById(R.id.iv_folder_check);
        }

        public void setData(ImgFolder folder,int position){
            folderName.setText(folder.getName());
            imageCount.setText("共"+folder.getImgs().size()+"张照片");
            picker.getImageLoader().displayImage(context, folder.getCover().getPath(), cover, imageSize, imageSize);

            if (lastSelsct == position) {
                folderCheck.setVisibility(View.VISIBLE);
            } else {
                folderCheck.setVisibility(View.INVISIBLE);
            }
        }
    }
}
