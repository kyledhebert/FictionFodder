package com.kylehebert.fictionfodder.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.kylehebert.fictionfodder.R;
import com.kylehebert.fictionfodder.fragment.ImageNoteFragment;
import com.kylehebert.fictionfodder.fragment.TextNoteFragment;
import com.kylehebert.fictionfodder.model.Note;
import com.kylehebert.fictionfodder.model.TrashNoteList;
import com.kylehebert.fictionfodder.utility.Constants;

import java.util.UUID;

/**
 * Created by kylehebert on 11/30/15.
 * Used when viewing a note from the TrashNoteListFragment
 * TODO figure out how to check for calling Fragment in NoteActivity
 * Ideally this class would be eliminated
 *
 */
public class TrashNoteActivity  extends SingleFragmentActivity{

    public static Intent newIntent(Context packageContext, UUID noteId) {
        Intent intent = new Intent(packageContext, TrashNoteActivity.class);
        intent.putExtra(Constants.EXTRA_NOTE_ID, noteId);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        UUID noteId = (UUID) getIntent().getSerializableExtra(Constants.EXTRA_NOTE_ID);

        //check to see what type of note
        Note getTypeNote = TrashNoteList.get(this).getNote(noteId);
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
