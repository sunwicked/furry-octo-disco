package app.com.application.main;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import app.com.application.AppConstants;
import app.com.application.OnItemClickListener;
import app.com.application.R;
import app.com.application.view.SeperatorDecoration;
import butterknife.Bind;
import butterknife.ButterKnife;

import static app.com.application.AppConstants.CATEGORY;
import static app.com.application.AppConstants.PUBLISHER;

public class FilterFragment extends BottomSheetDialogFragment {

    @Bind(R.id.rv_publisher)
    RecyclerView rvPublisher;
    @Bind(R.id.rv_category)
    RecyclerView rvCategory;
    private FilterAdapter categoryAdapter;
    private FilterAdapter publisherAdapter;
    FragmentClickListener fragmentClickListener;


    public static FilterFragment newInstance(ArrayList<String> categories, ArrayList<String> publishers) {

        Bundle args = new Bundle();
        args.putStringArrayList(AppConstants.KEY_CATEGORY, categories);
        args.putStringArrayList(AppConstants.KEY_PUBLISHER, categories);
        FilterFragment fragment = new FilterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            fragmentClickListener = (FragmentClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }


    };
    private List<String> categoryItems = new ArrayList<>();
    private List<String> publisherItems = new ArrayList<>();


    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
        ButterKnife.bind(this, contentView);
        setUpAdapter();
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }


    }

    private void setUpAdapter() {
        categoryAdapter = new FilterAdapter(categoryItems, CATEGORY, listener);
        publisherAdapter = new FilterAdapter(publisherItems, PUBLISHER, listener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        rvCategory.setLayoutManager(mLayoutManager);
        rvCategory.setItemAnimator(new DefaultItemAnimator());
        SeperatorDecoration decoration = new SeperatorDecoration(getContext(), Color.GRAY, 1.5f);
        rvCategory.addItemDecoration(decoration);
        rvCategory.setAdapter(categoryAdapter);

        RecyclerView.LayoutManager mLayoutManagerPublisher = new LinearLayoutManager(getContext());
        rvPublisher.setLayoutManager(mLayoutManagerPublisher);
        rvPublisher.setItemAnimator(new DefaultItemAnimator());
        rvPublisher.addItemDecoration(decoration);
        rvPublisher.setAdapter(publisherAdapter);
    }

    OnItemClickListener<Integer> listener = new OnItemClickListener<Integer>() {
        @Override
        public void onItemClick(@NonNull Integer item) {

        }

        @Override
        public void onItemClick(@NonNull Integer item, int type) {
            fragmentClickListener.onFilterItemClicked(item, type);
            dismiss();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void updateData(ArrayList<String> categoryItems, ArrayList<String> publisherItems) {
        this.categoryItems = categoryItems;
        this.publisherItems = publisherItems;
        categoryAdapter.notifyDataSetChanged();
        publisherAdapter.notifyDataSetChanged();
    }

   public interface FragmentClickListener {
        void onFilterItemClicked(int position, int type);
    }
}
