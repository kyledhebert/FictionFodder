package com.kylehebert.fictionfodder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.kylehebert.fictionfodder.R;
import com.kylehebert.fictionfodder.utility.Constants;
import com.kylehebert.fictionfodder.model.NoteList;
import com.kylehebert.fictionfodder.model.TextNote;

import java.util.UUID;

/**
 * Created by kylehebert on 11/11/15.
 * This fragment is where the creation or editing of a new note takes place.
 */
public class TextNoteFragment extends Fragment {


    private TextNote mTextNote;
    private EditText mTagEditText;
    private EditText mTextNoteBodyEditText;
    private Toolbar mToolbar;

    public static TextNoteFragment newInstance(UUID noteId) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.ARG_NOTE_ID, noteId);

        TextNoteFragment noteFragment = new TextNoteFragment();
        noteFragment.setArguments(args);
        return noteFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID noteId = (UUID) getArguments().getSerializable(Constants.ARG_NOTE_ID);
        mTextNote = NoteList.get(getActivity()).getTextNote(noteId);

    }

    @Override
    public void onPause() {
        super.onPause();

        NoteList.get(getActivity()).updateTextNote(mTextNote);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_text_note, container, false);

        // replace the action bar with the support toolbar
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(mToolbar);


        mTagEditText = (EditText) view.findViewById(R.id.item_tag_edit_text);
        mTagEditText.setText(mTextNote.getTag());
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
        mTextNoteBodyEditText.setText(mTextNote.getNoteBody());
        mTextNoteBodyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextNote.setTitle(getTitle(s));
                mTextNote.setNoteBody(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;

    }

    /*
    gets the first line of the note to use as the title
     */
    public String getTitle(CharSequence s) {
        String noteBody = s.toString();
        String[] noteLines = noteBody.split(System.getProperty("line.separator"));
        String noteTitle = noteLines[0];

        return noteTitle;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_note, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_save_note:
                NoteList.get(getActivity()).updateTextNote(mTextNote);
                Toast.makeText(getActivity(), R.string.save_item_toast, Toast.LENGTH_SHORT).show();
                getActivity().finish();
                return true;
            case R.id.menu_item_delete_note:
                //TODO make Toast a Snackbar with undo
                NoteList.get(getActivity()).addTextNoteToTrash(mTextNote);
                NoteList.get(getActivity()).deleteNote(mTextNote);
                Toast.makeText(getActivity(), R.string.delete_item_toast, Toast.LENGTH_SHORT).show();
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);

        }
    }







}
