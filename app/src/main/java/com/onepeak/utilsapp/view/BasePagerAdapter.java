package com.onepeak.utilsapp.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by OnePeak on 2018/7/12.
 * 优化View复用
 */

public abstract class BasePagerAdapter<T> extends PagerAdapter {

    private List<T> data;
    private LinkedList<View> mViews = null;

    private Context mContext;
    private LayoutInflater mLayoutInflater = null;
    private int layoutId = 0;

    public BasePagerAdapter(Context context, List<T> data, int layoutId){
        super();
        this.data = data;
        this.mContext = context;
        this.layoutId = layoutId;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mViews = new LinkedList<>();
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

//        ViewHolder viewHolder = null;
        View convertView = null;
        if (mViews.size() == 0) {
            convertView = this.mLayoutInflater.inflate(layoutId,null, false);
//            viewHolder = new ViewHolder();
//            viewHolder.view = convertView;
//            convertView.setTag(viewHolder);
        } else {
            convertView = mViews.removeFirst();
//            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (null != convertView) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onItemClickListener){
                        onItemClickListener.OnItemClick(v,position);
                    }
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if(null != onItemLongClickListener){
                        onItemLongClickListener.OnItemLongClick(v,position);
                    }

                    return false;
                }
            });

            bindView(convertView,position,getItem(position));
        }

//        container.addView(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        container.addView(convertView);

        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View contentView = (View) object;
        container.removeView(contentView);
        this.mViews.add(contentView);
    }

    public T getItem(int position) {
        return data.get(position);
    }

    public abstract void bindView(View view,int position, T item);

    private  OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void OnItemClick(View v, int position);
    }
    public void setItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemLongClickListener onItemLongClickListener;
    public interface OnItemLongClickListener{
        void OnItemLongClick(View v, int position);
    }
    public void setItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public LinkedList<View> getViewList(){
        return mViews;
    }
    public View getItemView(int position){
        return mViews.get(position);
    }

    public final class ViewHolder {
        public View view;
    }
}
