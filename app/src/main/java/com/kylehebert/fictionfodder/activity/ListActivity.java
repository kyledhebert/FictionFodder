package com.kylehebert.fictionfodder.activity;

import android.support.v4.app.Fragment;

import com.kylehebert.fictionfodder.fragment.ListFragment;
import com.kylehebert.fictionfodder.R;

/**
 * Created by kylehebert on 11/9/15. The main activity that hosts the ListFragment
 */
public class ListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return ListFragment.newInstance();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_single_fragment;
    }




}
