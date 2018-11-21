package com.kmutt.sit.apps.fit.core.main.dailydata;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kmutt.sit.apps.fit.FITApplication;
import com.kmutt.sit.apps.fit.R;
import com.kmutt.sit.apps.fit.core.main.dailydata.calories.realm.CaloriesEntry;
import com.kmutt.sit.apps.fit.utils.InstantDialog;

import java.util.List;

/**
 * Created by Freshy on 7/20/2017 AD.
 */

public class CaloriesEntryAdapter extends RecyclerView.Adapter<CaloriesEntryAdapter.ViewHolder> {

    Activity mContext;
    LayoutInflater mInflater;

    /**
     *   Deleting
     */
    public void showDeleteDialog(Activity context, final ViewHolder viewHolder, final DeleteDialogCallback callback) {
        String itemName = viewHolder.foodData.getTitle();
        String message = String.format(context.getString(R.string.text_prompt_remove_formattable), itemName);

        AlertDialog dialog = InstantDialog.makeAlertDialog(context, "", message);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.action_confirm_remove), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Perform item remove
                CaloriesEntry foodData = viewHolder.foodData;

                FITApplication.getUserDataRealm().beginTransaction();
                foodData.deleteFromRealm();
                FITApplication.getUserDataRealm().commitTransaction();

                if (callback != null)
                    callback.onItemDeleted();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.action_cancel_remove), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing..
            }
        });
        dialog.show();
    }
    public interface DeleteDialogCallback {
        void onItemDeleted();
    }

    /**
     *  ItemClickListener
     */
    OnItemClickListener mItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }
    OnItemLongPressedListener mItemLongPressedListener;
    public void setOnItemLongPressedListener(OnItemLongPressedListener listener) {
        mItemLongPressedListener = listener;
    }

    /**
     *  Data
     */
    List<CaloriesEntry> mCaloriesEntryList;
    public void setCaloriesEntryList(List<CaloriesEntry> foodList) {
        mCaloriesEntryList = foodList;
        notifyDataSetChanged();
    }

    public CaloriesEntryAdapter(Activity context, List<CaloriesEntry> foodList) {
        mContext = context;
        mCaloriesEntryList = foodList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mInflater == null)
            mInflater = LayoutInflater.from(mContext);

        View itemView = mInflater.inflate(R.layout.listrow_item_food, null, false);
        final ViewHolder viewHolder = new ViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(viewHolder);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mItemLongPressedListener != null) {
                    mItemLongPressedListener.onItemLongPressed(viewHolder);
                    return true;
                }
                return false;
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.tvTitle.setText("CaloriesEntry #" + (position + 1));
        holder.currentPosition = position;
        holder.bindData(mCaloriesEntryList.get(position));
    }

    @Override
    public int getItemCount() {
        return mCaloriesEntryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvSubtitle;
        int currentPosition;
        CaloriesEntry foodData;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvSubtitle = (TextView) itemView.findViewById(R.id.tvSubtitle);
        }

        public void bindData(CaloriesEntry food) {
            tvTitle.setText(food.getTitle());

            tvSubtitle.setText(String.format("%.0f", food.getKcals()));

            this.foodData = food;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ViewHolder viewHolder);
    }

    public interface OnItemLongPressedListener {
        void onItemLongPressed(ViewHolder viewHolder);
    }
}
