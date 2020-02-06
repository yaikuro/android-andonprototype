package com.app.andonprototype.ui.Report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.andonprototype.R;

import java.util.List;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ViewHolder>{
    private List<ReportListItems> values;
    public Context context;
    private OnNoteListener onNoteListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView MachineID,Line,Station,Repair_Start,Repair_Finish,Duration,PIC;
        public View layout;
        OnNoteListener onNoteListener;

        public ViewHolder(View v,OnNoteListener onNoteListener){
            super(v);
            layout = v;
            MachineID = v.findViewById(R.id.MachineID);
            Line = v.findViewById(R.id.Line);
            Station = v.findViewById(R.id.Station);
            Repair_Start = v.findViewById(R.id.RepairTimeStart);
            Repair_Finish = v.findViewById(R.id.RepairTimeFinish);
            Duration = v.findViewById(R.id.Duration);
            PIC = v.findViewById(R.id.PIC);
            this.onNoteListener = onNoteListener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }
    public ReportListAdapter(List<ReportListItems> myDataset, OnNoteListener onNoteListener, Context context){
        values = myDataset;
        this.context = context;
        this.onNoteListener = onNoteListener;
    }
    @Override
    public ReportListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.report_activity_listitem, parent, false);
        ViewHolder vh = new ViewHolder(v, onNoteListener);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        final ReportListItems reportListItems = values.get(position);
        holder.MachineID.setText(reportListItems.getMachineID());
        holder.Line.setText(reportListItems.getLine());
        holder.Station.setText(reportListItems.getStation());
        holder.Repair_Start.setText(reportListItems.getRepair_Start());
        holder.Repair_Finish.setText(reportListItems.getRepair_Finish());
        holder.Duration.setText(reportListItems.getRepair_Duration());
        holder.PIC.setText(reportListItems.getPIC());
    }
    @Override
    public int getItemCount() {
        return values.size();
    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
