package com.kylehebert.fictionfodder.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kylehebert.fictionfodder.R;
import com.kylehebert.fictionfodder.model.Note;
import com.kylehebert.fictionfodder.model.NoteList;

import java.util.List;

/**
 * Created by kylehebert on 11/9/15. The primary fragment. By default will show a list of all notes. If a user selects a tag from the navigation menu it will show that list instead
 */

public class ListFragment extends Fragment {

    public static ListFragment newInstance() {
        return new ListFragment();
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

    private class NoteHolder extends RecyclerView.ViewHolder {
        public TextView mSnippetTextView;

        public NoteHolder(View itemView) {
            super(itemView);

            mSnippetTextView = (TextView) itemView;
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
                    .inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            return new NoteHolder(view);
        }

        @Override
        public void onBindViewHolder(NoteHolder noteHolder, int position) {
            Note note = mNoteList.get(position);
            noteHolder.mSnippetTextView.setText(note.getBodyText());
        }

        @Override
        public int getItemCount() {
            return mNoteList.size();
        }
    }


}
