package com.kylehebert.fictionfodder.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.kylehebert.fictionfodder.R;
import com.kylehebert.fictionfodder.utility.Constants;
import com.kylehebert.fictionfodder.fragment.ImageNoteFragment;
import com.kylehebert.fictionfodder.fragment.TextNoteFragment;
import com.kylehebert.fictionfodder.model.Note;
import com.kylehebert.fictionfodder.model.NoteList;

import java.util.UUID;

/**
 * Created by kylehebert on 11/11/15.
 * Will host a single item Fragment.
 */
public class NoteActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context packageContext, UUID noteId) {
        Intent intent = new Intent(packageContext, NoteActivity.class);
        intent.putExtra(Constants.EXTRA_NOTE_ID, noteId);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        UUID noteId = (UUID) getIntent().getSerializableExtra(Constants.EXTRA_NOTE_ID);

        //check to see what type of note
        Note getTypeNote = NoteList.get(this).getNote(noteId);
            if (getTypeNote.getType().equals(Constants.TYPE_TEXT_NOTE)) {
            return TextNoteFragment.newInstance(noteId);
        } else {
            return ImageNoteFragment.newInstance(noteId);
        }


    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_single_fragment;
    }

}
