package com.kylehebert.fictionfodder.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kylehebert.fictionfodder.R;
import com.kylehebert.fictionfodder.activity.NoteActivity;
import com.kylehebert.fictionfodder.model.ImageNote;
import com.kylehebert.fictionfodder.model.Note;
import com.kylehebert.fictionfodder.model.NoteList;
import com.kylehebert.fictionfodder.model.TextNote;

import java.util.List;

/**
 * Created by kylehebert on 11/9/15. The primary fragment. By default will show a list of all notes. If a user selects a tag from the navigation menu it will show that list instead
 */

public class NoteListFragment extends Fragment {

    public static NoteListFragment newInstance() {
        return new NoteListFragment();
    }

    private RecyclerView mNoteRecyclerView;
    private FloatingActionButton mAddNoteButton;
    private NoteAdapter mNoteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mNoteRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycler_view);
        mNoteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAddNoteButton = (FloatingActionButton) view.findViewById(R.id.add_note_fab);

        updateUI();

        return view;
    }

    private void updateUI() {
        NoteList noteList = NoteList.get(getActivity());
        List<Note> notes = noteList.getNotes();

        mNoteAdapter = new NoteAdapter(notes);
        mNoteRecyclerView.setAdapter(mNoteAdapter);
    }

    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Note mNote;
        private TextNote mTextNote;
        private ImageNote mImageNote;

        private TextView mTitleTextView;
        private TextView mSnippetTextView;
        private ImageView mImageView;

        public NoteHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.note_title_text_view);
            mSnippetTextView = (TextView) itemView.findViewById(R.id.text_note_snippet_text_view);
            //mImageView = (ImageView) itemView.findViewById(R.id.photo_note_thumbnail_image_view);

        }

        public void bindNote(Note note) {
            //TODO figure out what kind of note first

            //for now assume all notes are text notes
            mTextNote = (TextNote)note;
            mTitleTextView.setText(mTextNote.getTitle());
            mSnippetTextView.setText(mTextNote.getNoteBody());

        }

        @Override
        public void onClick(View view) {
            Intent intent = NoteActivity.newIntent(getActivity(), mNote.getId());
            startActivity(intent);
        }

    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {

        private List<Note> mNoteList;

        public NoteAdapter(List<Note> noteList) {
            mNoteList = noteList;
        }

        @Override
        public NoteHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_note, viewGroup, false);
            return new NoteHolder(view);
        }

        @Override
        public void onBindViewHolder(NoteHolder noteHolder, int position) {
            Note note = mNoteList.get(position);
            noteHolder.bindNote(note);

        }

        @Override
        public int getItemCount() {
            return mNoteList.size();
        }
    }



}
