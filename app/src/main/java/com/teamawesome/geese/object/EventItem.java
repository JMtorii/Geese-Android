package com.teamawesome.geese.object;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by codychung on 11/1/15.
 */
public interface EventItem {
    int getViewType();
    View getView(LayoutInflater inflater, View convertView);
}
