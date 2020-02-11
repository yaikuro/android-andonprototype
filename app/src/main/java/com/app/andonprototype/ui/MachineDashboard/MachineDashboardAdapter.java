package com.app.andonprototype.ui.MachineDashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.andonprototype.R;

import java.util.List;

public class MachineDashboardAdapter extends RecyclerView.Adapter<MachineDashboardAdapter.ViewHolder>{
    private List<MachineListItems> values;
    public Context context;
    private OnPressListener onPressListener;
    int[] listviewImage = new int[]
            {
                    R.drawable.color_green,
                    R.drawable.color_red,
                    R.drawable.color_yellow,
                    R.drawable.color_blue
            };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Line, Station;
        public View layout;
        public ImageView Status;
        OnPressListener onPressListener;

        public ViewHolder(View v, OnPressListener onPressListener) {
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
    public MachineDashboardAdapter(List<MachineListItems> machineListItems, OnPressListener onPressListener, Context context){
        values = machineListItems;
        this.context = context;
        this.onPressListener = onPressListener;
    }
    @Override
    public MachineDashboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.stationlistitem,parent,false);
        ViewHolder vh = new ViewHolder(v,onPressListener);
        return vh;
    }
    @Override
    public void onBindViewHolder (ViewHolder holder,final int position) {
        final MachineListItems machineListItems = values.get(position);
        holder.Line.setText(machineListItems.getLine());
        holder.Station.setText(machineListItems.getStation());
        holder.Status.setImageResource(listviewImage[machineListItems.Status]);
    }
    @Override
    public int getItemCount(){
        return values.size();
    }
    public interface OnPressListener{
        void onPressClick(int position);
    }
}

