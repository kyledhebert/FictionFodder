package com.kylehebert.fictionfodder.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.kylehebert.fictionfodder.R;
import com.kylehebert.fictionfodder.model.Note;
import com.kylehebert.fictionfodder.model.NoteList;
import com.kylehebert.fictionfodder.model.TextNote;

import java.util.UUID;

/**
 * Created by kylehebert on 11/11/15.
 * This fragment is where the creation or editing of a new note takes place.
 */
public class NoteFragment extends Fragment {

    private static final String ARG_NOTE_ID = "note_id";

    private Note mNote;
    private TextNote mTextNote;
    private EditText mTagEditText;
    private EditText mTextNoteBodyEditText;
    private ImageView mPhotoNoteImageView;

    public static NoteFragment newInstance(UUID noteId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, noteId);

        NoteFragment noteFragment = new NoteFragment();
        noteFragment.setArguments(args);
        return noteFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID noteId = (UUID) getArguments().getSerializable(ARG_NOTE_ID);
        mNote = NoteList.get(getActivity()).getNote(noteId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        mTagEditText = (EditText) view.findViewById(R.id.item_tag_edit_text);
        mTagEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing to see here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextNote.setTag(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //nothing to see here either

            }
        });

        mTextNoteBodyEditText = (EditText) view.findViewById(R.id.item_body_edit_text);
        mTextNoteBodyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextNote.setNoteBody(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }







}
