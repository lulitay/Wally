package com.example.pam_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pam_app.R;
import com.example.pam_app.model.Income;

import java.util.ArrayList;
import java.util.List;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {

    private final List<Income> incomeList;

    public IncomeAdapter() {
        this.incomeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bucket_entry_home_item, parent, false);
        return new IncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        holder.bind(incomeList.get(position));
    }

    @Override
    public int getItemCount() {
        return incomeList.size();
    }

    public void showNewIncome(final Income income) {
        incomeList.add(income);
        notifyItemInserted(incomeList.indexOf(income));
    }

    public void update(final List<Income> newIncomeList) {
        if (newIncomeList != null) {
            incomeList.addAll(newIncomeList);
            notifyDataSetChanged();
        }
    }

    public static class IncomeViewHolder extends RecyclerView.ViewHolder {

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(final Income entry) {
            final TextView comment = itemView.findViewById(R.id.comment);
            final TextView amount = itemView.findViewById(R.id.amount);
            final TextView date = itemView.findViewById(R.id.date);

            comment.setText(entry.getTitle());
            amount.setText(entry.getAmountString());
            date.setText(entry.getDateString());
        }
    }
}
