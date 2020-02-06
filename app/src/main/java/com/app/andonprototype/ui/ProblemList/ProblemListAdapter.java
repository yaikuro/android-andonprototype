package com.app.andonprototype.ui.ProblemList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.andonprototype.R;

import java.util.List;

public class ProblemListAdapter extends RecyclerView.Adapter<ProblemListAdapter.ViewHolder> {
    private List<ProblemListItems> values;
    public Context context;
    private OnPressListener onPressListener;
    int[] listviewImage = new int[]
            {
                    R.drawable.color_green,
                    R.drawable.color_red,
                    R.drawable.color_yellow,
                    R.drawable.color_blue
            };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView Line,Station;
        public View layout;
        public ImageView Status;
        OnPressListener onPressListener;
        public ViewHolder(View v,OnPressListener onPressListener){
            super(v);
            layout = v;
            Line = v.findViewById(R.id.Line);
            Station = v.findViewById(R.id.Station);
            Status = v.findViewById(R.id.image);
            this.onPressListener = onPressListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPressListener.onPressClick(getAdapterPosition());
        }
    }
    public ProblemListAdapter(List<ProblemListItems> myDataset, OnPressListener onPressListener, Context context){
        values = myDataset;
        this.context = context;
        this.onPressListener = onPressListener;
    }
    @Override
    public ProblemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.listitem, parent, false);
        ViewHolder vh = new ViewHolder(v, onPressListener);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        final ProblemListItems problemListItems = values.get(position);
        holder.Line.setText(problemListItems.getLine());
        holder.Station.setText(problemListItems.getStation());
        holder.Status.setImageResource(listviewImage[problemListItems.getStatus()]);
    }
    @Override
    public int getItemCount() {
        return values.size();
    }
    public interface OnPressListener{
        void onPressClick(int position);
    }
}
