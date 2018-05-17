package com.example.android.squawker;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.squawker.provider.SquawkerContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SquawkerAdapter extends RecyclerView.Adapter<SquawkerAdapter.SquawkerViewHolder> {

    private Cursor mData;
    private static SimpleDateFormat mDateFormat = new SimpleDateFormat("dd MM", Locale.getDefault());
    private static final long MINUTE_MILLIS = 1000 * 60;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;

    @NonNull
    @Override
    public SquawkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_squawk_list, parent, false);
        return new SquawkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SquawkerViewHolder holder, int position) {
        mData.moveToPosition(position);

        String author = mData.getString(MainActivity.COL_NUM_AUTHOR);
        String message = mData.getString(MainActivity.COL_NUM_MESSAGE);
        String authorKey = mData.getString(MainActivity.COL_NUM_AUTHOR_KEY);
        long dateMillis = mData.getLong(MainActivity.COL_NUM_DATE);

        String date;
        long now = System.currentTimeMillis();

        if (now - dateMillis < DAY_MILLIS) {
            if (now  - dateMillis < HOUR_MILLIS) {
                long minutes = Math.round((now - dateMillis) / MINUTE_MILLIS);
                date = String.valueOf(minutes) + "m";
            } else {
                long minutes = Math.round((now - dateMillis) / HOUR_MILLIS);
                date = String.valueOf(minutes) + "h";
            }
        } else {
            Date dateDate = new Date(dateMillis);
            date = mDateFormat.format(dateDate);
        }

        int authorImage;

        holder.mImAuthorImage.setImageResource(pickAuthorImage(authorKey));
        holder.mTvAuthor.setText(author);
        holder.mTvMessage.setText(message);
        holder.mTvDate.setText("\u2022" + date);
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }

        return mData.getCount();
    }

    public void swapCursor(Cursor cursor) {
        mData = cursor;
        notifyDataSetChanged();
    }

    private int pickAuthorImage(String authorKey) {
        int authorImage;

        switch (authorKey) {
            case SquawkerContract.ASSER_KEY:
                authorImage = R.drawable.asser;
                break;
            case SquawkerContract.CEZANNE_KEY:
                authorImage = R.drawable.cezanne;
                break;
            case SquawkerContract.JLIN_KEY:
                authorImage = R.drawable.jlin;
                break;
            case SquawkerContract.LYLA_KEY:
                authorImage = R.drawable.lyla;
                break;
            case SquawkerContract.NIKITA_KEY:
                authorImage = R.drawable.nikita;
                break;
            default:
                authorImage = R.drawable.ic_no_image;
                break;
        }

        return authorImage;
    }

    class SquawkerViewHolder extends RecyclerView.ViewHolder {
        ImageView mImAuthorImage;
        TextView mTvAuthor;
        TextView mTvMessage;
        TextView mTvDate;

        SquawkerViewHolder(View itemView) {
            super(itemView);
            mImAuthorImage = itemView.findViewById(R.id.imAuthorImage);
            mTvAuthor = itemView.findViewById(R.id.tvAuthor);
            mTvMessage = itemView.findViewById(R.id.tvMessage);
            mTvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
