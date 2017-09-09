package app.com.application;

import android.support.annotation.NonNull;

/**
 * Created by admin on 09/09/17.
 */


public interface OnItemClickListener<T> {

    void onItemClick(@NonNull T item);
    void onItemClick(@NonNull T item,int type);
}

