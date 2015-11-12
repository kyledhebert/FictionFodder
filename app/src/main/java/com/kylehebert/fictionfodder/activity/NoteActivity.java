package com.kylehebert.fictionfodder.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.kylehebert.fictionfodder.R;
import com.kylehebert.fictionfodder.fragment.NoteFragment;

import java.util.UUID;

/**
 * Created by kylehebert on 11/11/15.
 * Will host a single item Fragment.
 */
public class NoteActivity extends SingleFragmentActivity {

    private static final String EXTRA_NOTE_ID = "item_id";

    public static Intent newIntent(Context packageContext, UUID noteId) {
        Intent intent = new Intent(packageContext, NoteActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        UUID noteId = (UUID) getIntent().getSerializableExtra(EXTRA_NOTE_ID);
        return NoteFragment.newInstance(noteId);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_single_fragment;
    }

}
