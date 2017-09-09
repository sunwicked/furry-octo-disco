package app.com.application.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.com.application.OnItemClickListener;
import app.com.application.R;

/**
 * Created by admin on 10/09/17.
 */

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final List<String> items;
    private final int type;
    private OnItemClickListener<Integer> listener;

    public FilterAdapter(List<String> items, int type, OnItemClickListener<Integer> listener) {
        this.items = items;
        this.type = type;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_filter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        FilterViewHolder vh = new FilterViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final String text = items.get(position);
        FilterViewHolder filterViewHolder = (FilterViewHolder) holder;
        filterViewHolder.tvFilter.setText(text);
        filterViewHolder.tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position,type);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class FilterViewHolder extends RecyclerView.ViewHolder {
        TextView tvFilter;

        public FilterViewHolder(View itemView) {
            super(itemView);
            tvFilter = (TextView) itemView.findViewById(R.id.tv_filter);
        }
    }


}
