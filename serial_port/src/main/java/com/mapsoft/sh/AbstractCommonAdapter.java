package com.mapsoft.sh;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * author: annocx
 * ___           ___            ___              _____         _____          _ _
 * /  /\         /__/\          /__/\            /  /::\       /  /::|        /__/\
 * /  /::\        \  \:\         \  \:\         /  /:/\:\     /  /:/          |: :|
 * /  /:/\:\        \  \:\         \  \:\      /  /:/  \:\   /  /:/           |: :|
 * /  /:/~/::\    ____\__\:\     ___ \__\:\   /__/:/    \_\  |__/:/        ___|: :|___
 * /__/:/ /:/\:\ /__/::::::::\  /__/::::::::\ \  \:\    /:/  |\:\     __  /__/::::::::\
 * \  \:\/:/__\/ \  \:\~~\~~\/  \  \:\~~\~~\/  \  \::  /:/   \  \::  /:/  \~~\:\~~\~~\/
 * \  \::/        \  \:\  ~~~    \  \:\  ~~~    \  \:\/:/     \  \:\/:/       |: :|
 * \  \:\          \  \:\         \  \:\         \  \/;/       \  \/;/        |: :|
 * \__\:\           \__\/          \__\/          \__\/         \ : /         \_:_\
 * <p>
 * created on: 2019/2/22 002215:01
 * packagename: com.mapsoft.lcd.util.weight
 * projectname: LCDApplication
 * description:
 * @author Administrator
 */
public abstract class AbstractCommonAdapter<A> extends RecyclerView.Adapter<BaseVH> {
    private List<A> mDatas;//要显示的数据
    private OnItemClickListener mOnItemClickListener;//列表点击监听器
    private Context mContext;
    private int mLayoutId;

    public AbstractCommonAdapter(Context context, int layoutId, List<A> datas) {
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
    }

    public void removeItem(int i) {//非必要，只是为了调用删除函数方便
        if (i > 0) {
            mDatas.remove(i);
            notifyItemRemoved(i);
        }
    }

    //开始三个过程的实现
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @NonNull
    @Override
    public BaseVH onCreateViewHolder(final ViewGroup parent, int viewType) {
        BaseVH holder = BaseVH.get(mContext, parent, mLayoutId);
        return holder;
    }

    @Override
    public void onBindViewHolder(final BaseVH holder, int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return true;
                }
            });
        }
        convert(holder, mDatas.get(position),position);
    }

    public void convert(BaseVH holder, A a,int postion) {//根据自己意愿绑定数据
        
    }

    public interface OnItemClickListener {
        void onClick(int position);

        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}

