package com.mapsoft.sh;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

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
 * created on: 2019/2/22 002215:02
 * packagename: com.mapsoft.lcd.util.weight
 * projectname: LCDApplication
 * description:
 * @author Administrator
 */
public class BaseVH extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;//存储list_Item的View
    private View mConvertView;//list_Item
    public BaseVH(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        mConvertView=itemView;
        mViews=new SparseArray<View>();
    }
    //获取实例
    public static BaseVH get(Context context, ViewGroup parent, int layoutId) {
        View itemView= LayoutInflater.from(context).inflate(layoutId,parent,false);
        BaseVH holder=new BaseVH(context,itemView,parent);
        return holder;
    }
    public <T extends View> T getView(int viewId) {
        View view=mViews.get(viewId);
        if(view==null) {
            view=mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T)view;
    }
}
