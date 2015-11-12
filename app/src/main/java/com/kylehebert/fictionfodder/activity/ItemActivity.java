package com.kylehebert.fictionfodder.activity;

import android.support.v4.app.Fragment;

import com.kylehebert.fictionfodder.R;
import com.kylehebert.fictionfodder.fragment.ItemFragment;

/**
 * Created by kylehebert on 11/11/15.
 * Will host a single item Fragment.
 */
public class ItemActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return ItemFragment.newInstance();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_single_fragment;
    }

}
