package com.nursinglab.booking.helper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;

public interface ItemClickHelper {

    void onItemClick(int position);
    void onLongItemClick(int position);
}
