package com.kmutt.sit.apps.fit.core.food;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;
import com.kmutt.sit.apps.fit.R;
import com.kmutt.sit.apps.fit.core.food.realm.Food;

import java.util.List;

/**
 * Created by Freshy on 7/20/2017 AD.
 */

public class FoodChooserAdapter extends RecyclerView.Adapter<FoodChooserAdapter.ViewHolder> implements SectionTitleProvider {

    Activity mContext;
    LayoutInflater mInflater;

    /**
     *  ItemClickListener
     */
    OnItemClickListener mItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     *  Data
     */
    protected List<Food> mFoodList;
    public void setFoodList(List<Food> foodList) {
        mFoodList = foodList;
        notifyDataSetChanged();
    }

    public FoodChooserAdapter(Activity context, List<Food> foodList) {
        mContext = context;
        mFoodList = foodList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null)
            mInflater = LayoutInflater.from(mContext);

        View itemView = mInflater.inflate(R.layout.layout_food_chooser_item, null, false);
        final ViewHolder viewHolder = new ViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(viewHolder);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.tvTitle.setText("Food #" + (position + 1));
        holder.currentPosition = position;
        holder.bindData(mFoodList.get(position));
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvSubtitle;
        int currentPosition;
        Food foodData;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvSubtitle = (TextView) itemView.findViewById(R.id.tvSubtitle);
        }

        public void bindData(Food food) {
            tvTitle.setText(food.getFoodName());

            String formatableKcal = mContext.getString(R.string.text_kcal_formattable);
            tvSubtitle.setText(String.format(formatableKcal, food.getKcal(), food.getUnit()));

            this.foodData = food;
        }
    }

    /**
     *  SectionTitleProvider
     */
    String thVowels = "เ แ ไ ใ โ";
    @Override
    public String getSectionTitle(int position) {
        String foodName = mFoodList.get(position).getFoodName();
        String firstChr = foodName.substring(0, 1);
        if (thVowels.indexOf(firstChr) == -1)
            return firstChr;
        else
            return foodName.substring(1, 2);
    }

    public interface OnItemClickListener {
        void onItemClick(ViewHolder viewHolder);
    }
}
