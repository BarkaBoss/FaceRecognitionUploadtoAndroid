package xyz.nokturnal.barka.privateeye;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ViewHolder> {
    private List<ReportsJO> reports;
    private Context context;
    public ReportListAdapter(List<ReportsJO> reports, Context context) {
        this.reports = reports;
        this.context = context;
    }

    @NonNull
    @Override
    public ReportListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reports_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportListAdapter.ViewHolder holder, int position) {
        final ReportsJO reportsJO = reports.get(position);

        holder.tvName.setText(reportsJO.getName());
        holder.tvTime.setText(reportsJO.getTime());
        holder.tvDate.setText(reportsJO.getDate());

        holder.ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewPageIntent = new Intent(context, ViewPage.class);
                viewPageIntent.putExtra("username", reportsJO.getName());
                viewPageIntent.putExtra("date", reportsJO.getDate());
                viewPageIntent.putExtra("time", reportsJO.getTime());
                viewPageIntent.putExtra("link", reportsJO.getURL());
                context.startActivity(viewPageIntent);
            }
        });
    }

    public void filter(ArrayList<ReportsJO> listItem)
    {
        reports = new ArrayList<>();
        reports.addAll(listItem);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvTime;
        public TextView tvDate;
        public LinearLayout ln;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDate = itemView.findViewById(R.id.tvDate);

            ln = itemView.findViewById(R.id.lnLayout);
        }
    }
}
