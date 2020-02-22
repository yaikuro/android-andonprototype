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
    private OnPushListener onPushListener;
    int[] listviewImage = new int[]
            {
                    R.drawable.color_green,
                    R.drawable.color_red,
                    R.drawable.color_yellow,
                    R.drawable.color_blue
            };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Station;
        public View layout;
        public ImageView Status;
        OnPushListener onPushListener;

        public ViewHolder(View v, OnPushListener onPushListener) {
            super(v);
            layout = v;
            Station = v.findViewById(R.id.Station);
            Status = v.findViewById(R.id.image);
            this.onPushListener = onPushListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPushListener.onPushClick(getAdapterPosition());
        }
    }
    public MachineDashboardAdapter(List<MachineListItems> machineListItems, OnPushListener onPushListener, Context context){
        values = machineListItems;
        this.context = context;
        this.onPushListener = onPushListener;
    }
    @Override
    public MachineDashboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.stationlistitem,parent,false);
        ViewHolder vh = new ViewHolder(v,onPushListener);
        return vh;
    }
    @Override
    public void onBindViewHolder (ViewHolder holder,int position) {
         MachineListItems machineListItems = values.get(position);
        holder.Station.setText(machineListItems.getStation());
        holder.Status.setImageResource(listviewImage[machineListItems.getStatus()]);
    }
    @Override
    public int getItemCount(){
        return values.size();
    }
    public interface OnPushListener{
        void onPushClick(int position);
    }
}

