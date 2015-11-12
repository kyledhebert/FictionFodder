package com.kylehebert.fictionfodder.activity;

import android.support.v4.app.Fragment;

import com.kylehebert.fictionfodder.fragment.NoteListFragment;
import com.kylehebert.fictionfodder.R;

/**
 * Created by kylehebert on 11/9/15. The main activity that hosts the NoteListFragment
 */
public class NoteListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return NoteListFragment.newInstance();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_single_fragment;
    }




}
