package com.app.andonprototype.ui.MachineDashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.andonprototype.R;

import java.util.LinkedList;

//

public class LineListAdapter extends RecyclerView.Adapter<LineListAdapter.LineViewHolder> {

    private final LinkedList<String> mLineList;
    private final LayoutInflater mInflater;

    public LineListAdapter(Context context, LinkedList<String> lineList) {
        mInflater = LayoutInflater.from(context);
        this.mLineList = lineList;
    }

    @Override
    public LineListAdapter.LineViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // Inflate an item view.
        View mItemView = mInflater.inflate(
                R.layout.linelist_item, parent, false);
        return new LineViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(LineListAdapter.LineViewHolder holder,
                                 int position) {
        // Retrieve the data for that position.
        String mCurrent = mLineList.get(position);
        // Add the data to the view holder.
        holder.lineItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mLineList.size();
    }

    class LineViewHolder extends RecyclerView.ViewHolder {
        public final TextView lineItemView;
        final LineListAdapter mAdapter;

        public LineViewHolder(View itemView, LineListAdapter adapter) {
            super(itemView);
            lineItemView = itemView.findViewById(R.id.lineTitle);
            this.mAdapter = adapter;
        }

    }
}
