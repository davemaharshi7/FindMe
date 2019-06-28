package com.findme.findme.Listeners;

import android.os.Bundle;

public interface FragmentActionListener {
    String ACTION_KEY = "key";
    int ACTION_VALUE_CURRENT_LOCATION = 1;
    int ACTION_VALUE_PICK_MANUAL = 2;

    void actionPerformed(Bundle bundle);
}
