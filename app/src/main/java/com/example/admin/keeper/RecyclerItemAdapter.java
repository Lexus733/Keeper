package com.example.admin.keeper;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecyclerItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_ITEM_TEXT = 0;
    public static final int TYPE_ITEM_IMAGE = 1;
    private List<ListItems> items;
    private LayoutInflater inflater;
    private RecyclerView.ViewHolder tempViewHolder;
    private List<RecyclerView.ViewHolder> viewHolderList;
    private int tempViewHolderPosition;


    public RecyclerItemAdapter(List<ListItems> items, Context context) {
        this.items = items;
        viewHolderList = new ArrayList<RecyclerView.ViewHolder>();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        switch (viewType) {

            case ListItems.TYPE_ITEM_TEXT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_task_list_item,
                        parent, false);
                tempViewHolder = new TextItemHolder(v);
                viewHolderList.add(tempViewHolder);
                v.setOnLongClickListener(new LongElementClickListener(tempViewHolder));
                v.setOnClickListener(new ElementClickListener(tempViewHolder));
                return tempViewHolder;
            case ListItems.TYPE_ITEM_IMAGE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_task_list_item,
                        parent, false);
                tempViewHolder = new ImgItemHolder(v);
                viewHolderList.add(tempViewHolder);
                v.setOnLongClickListener(new LongElementClickListener(tempViewHolder));
                v.setOnClickListener(new ElementClickListener(tempViewHolder));
                return tempViewHolder;
            default:
                return null;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ListItems taskListItem = items.get(position);
        if(taskListItem.isSelected())
            holder.itemView.setBackgroundResource(R.color.black);
        else
            holder.itemView.setBackgroundResource(R.color.white);
        int type = taskListItem.getType();
        switch (type) {
            case ListItems.TYPE_ITEM_TEXT:
                Text textTaskListItem = (Text) taskListItem;
                TextItemHolder textItemHolder = (TextItemHolder) holder;
                textItemHolder.mName.setText(textTaskListItem.getName());
                textItemHolder.mText.setText(textTaskListItem.getText());
                break;

            case ListItems.TYPE_ITEM_IMAGE:
                Img imgTaskListItem = (Img) taskListItem;
                ImgItemHolder imgItemHolder = (ImgItemHolder) holder;
                imgItemHolder.mImgName.setText(imgTaskListItem.getName());
                imgItemHolder.mImage.setImageBitmap(imgTaskListItem.getImage());
                break;
        }
    }


    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (items != null) {
            ListItems taskListItem = items.get(position);
            if (taskListItem != null) {
                return taskListItem.getType();
            }
        }
        return 0;
    }

    void removeItem(int position) {
        GeneralActivity activity = (GeneralActivity) inflater.getContext();
        activity.delete(position);
    }


    public static class TextItemHolder extends RecyclerView.ViewHolder {

        TextView mName;
        TextView mText;

        public TextItemHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mText = itemView.findViewById(R.id.text);
        }
    }

    public static class ImgItemHolder extends RecyclerView.ViewHolder {

        TextView mImgName;
        ImageView mImage;

        public ImgItemHolder(View itemView) {
            super(itemView);

            mImgName = itemView.findViewById(R.id.imgName);
            mImage = itemView.findViewById(R.id.img);
        }
    }

    public class LongElementClickListener implements View.OnLongClickListener {

        RecyclerView.ViewHolder mViewHolder;

        public LongElementClickListener(RecyclerView.ViewHolder viewHolder) {
            mViewHolder = viewHolder;
        }

        @Override
        public boolean onLongClick(View v) {
            GeneralActivity activity = (GeneralActivity) inflater.getContext();
            if (activity.mActionMode == null) {
                activity.mActionMode = activity.startSupportActionMode(activity.mActionModeCallback);
                activity.mActionMode.setTitle("Action Mode");
                tempViewHolderPosition =  mViewHolder.getAdapterPosition();
                addSelection(mViewHolder);
            } else {

                activity.mActionMode.finish();

                removeSelection();
            }
            return true;
        }
    }

    public class ElementClickListener implements View.OnClickListener{

        RecyclerView.ViewHolder mViewHolder;

        public ElementClickListener(RecyclerView.ViewHolder viewHolder) {
            mViewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {
        GeneralActivity activity = (GeneralActivity) inflater.getContext();
        tempViewHolderPosition = tempViewHolder.getAdapterPosition();
        switch (tempViewHolder.getItemViewType()){
            case TYPE_ITEM_TEXT:{
                Intent textIntent = new Intent(activity, TextActivity.class);
                textIntent.putExtra("data", activity.getById(items.get(tempViewHolderPosition).getId()));
                textIntent.putExtra("id",items.get(tempViewHolderPosition).getId());
                activity.startActivityForResult(textIntent,GeneralActivity.TEXT_RESULT);
                break;
            }
        }

        }
    }

    private void addSelection(RecyclerView.ViewHolder viewHolder) {

        viewHolder.itemView.setBackgroundResource(R.color.gray);

        items.get(tempViewHolderPosition).setSelected(true);
    }

    private void removeSelection() {

        for (ListItems item: items) {
            item.setSelected(false);
        }

        for (RecyclerView.ViewHolder viewHolder: viewHolderList) {
            viewHolder.itemView.setBackgroundResource(R.color.gray);
        }
    }


    public void initActionMode() {
        final GeneralActivity activity = (GeneralActivity) inflater.getContext();
        activity.mActionModeCallback = new android.support.v7.view.ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_context_task, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(android.support.v7.view.ActionMode mode, MenuItem item) {
                removeItem(items.get(tempViewHolderPosition).getId());
                activity.mActionMode.finish();
                return false;
            }

            @Override
            public void onDestroyActionMode(android.support.v7.view.ActionMode mode) {
                removeSelection();
                activity.mActionMode = null;
            }

        };
    }
}