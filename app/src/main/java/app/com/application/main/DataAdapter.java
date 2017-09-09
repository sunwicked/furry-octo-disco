package app.com.application.main;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import app.com.application.OnItemClickListener;
import app.com.application.R;

import static app.com.application.AppConstants.CATEGORY;
import static app.com.application.AppConstants.MMM_DD;
import static app.com.application.AppConstants.PUBLISHER;

/**
 * Created by admin on 09/09/17.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {


    private final Context context;
    private List<DataModel> dataModels, filteredDataModels;
    private OnItemClickListener<DataModel> onItemClickListener;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MMM_DD, Locale.getDefault());
    boolean sortNewToOld = false;

    public DataAdapter(Context context, List<DataModel> dataModels, OnItemClickListener<DataModel> onItemClickListener) {

        this.context = context;
        this.dataModels = dataModels;
        this.filteredDataModels = dataModels;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_data, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        final DataModel dataModel = filteredDataModels.get(position);
        holder.tvTitle.setText(dataModel.getTitle());
        holder.tvPublisher.setText(dataModel.getPublisher());
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(dataModel.getTimestamp()));
        holder.tvTime.setText(simpleDateFormat.format(cal.getTime()));
        holder.cvParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(dataModel);
            }
        });

    }


    @Override
    public int getItemCount() {
        return filteredDataModels.size();
    }


    public void update(List<DataModel> modelList) {
        filteredDataModels = modelList;
        notifyDataSetChanged();
    }

    /**
     * Method for sorting data
     */
    public void sortClicked() {

        if (sortNewToOld) {
            Collections.sort(filteredDataModels);

        } else {
            Collections.reverse(filteredDataModels);
        }
        sortNewToOld = !sortNewToOld;
        notifyDataSetChanged();

    }

    /**
     * Filtering the data based on the filter type and value
     * @param type
     * @param filter
     */
    public void filterData(int type, String filter) {
        List<DataModel> temp = new ArrayList<>();
        filteredDataModels = dataModels;
        for (DataModel dataModel : filteredDataModels) {
            if (type == PUBLISHER) {
                if (dataModel.getPublisher().equals(filter)) {
                    temp.add(dataModel);
                }
            } else if (type == CATEGORY) {
                if (dataModel.getCategory().equals(filter)) {
                    temp.add(dataModel);
                }
            }

        }
        filteredDataModels = temp;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvPublisher;
        TextView tvTime;
        CardView cvParent;

        public ViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.tv_title);
            tvPublisher = (TextView) v.findViewById(R.id.tv_publisher);
            tvTime = (TextView) v.findViewById(R.id.tv_time);
            cvParent = v.findViewById(R.id.cv_parent);
        }
    }

}
