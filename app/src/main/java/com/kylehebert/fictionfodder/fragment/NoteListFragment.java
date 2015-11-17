package com.kylehebert.fictionfodder.fragment;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
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
    private Toolbar mAddNoteToolBar;
    private View mAddNoteToolbarContainer;

    private int mUpdatedNotePosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                view.removeOnLayoutChangeListener(this);
                toggleToolbarVisibility(view);
            }
        });

        mNoteRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycler_view);
        mNoteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAddNoteToolBar = (Toolbar) view.findViewById(R.id.select_note_type_toolbar);
        mAddNoteToolBar.inflateMenu(R.menu.fragment_note_bottom_menu);


        mAddNoteButton = (FloatingActionButton) view.findViewById(R.id.add_note_fab);
        mAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleToolbarVisibility(view);
            }
        });


        //used to show new items in the list as they are added, or as existing items are edited
        updateUI();

        return view;
    }

    private void updateUI() {
        NoteList noteList = NoteList.get(getActivity());
        List<Note> notes = noteList.getNotes();

        if (mNoteAdapter == null) {
            mNoteAdapter = new NoteAdapter(notes);
            mNoteRecyclerView.setAdapter(mNoteAdapter);
        } else {
            mNoteAdapter.notifyItemChanged(mUpdatedNotePosition);
            mUpdatedNotePosition = RecyclerView.NO_POSITION;
        }

    }

    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Note mNote;
        private TextNote mTextNote;
        private ImageNote mImageNote;

        private int mPosition;

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
            mUpdatedNotePosition = getAdapterPosition();
            Intent intent = NoteActivity.newIntent(getActivity(), mTextNote.getId());
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

    private void toggleToolbarVisibility(View view) {

        mAddNoteToolbarContainer = view.findViewById(R.id.select_note_type_toolbar);

        //get the center for the clipping circle
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;

        float radius = Math.max(mAddNoteToolbarContainer.getWidth(), mAddNoteToolbarContainer
                .getHeight());

        if (mAddNoteToolbarContainer.getVisibility() == View.INVISIBLE) {
            mAddNoteToolbarContainer.setVisibility(View.VISIBLE);
            mAddNoteButton.setVisibility(View.INVISIBLE);

            ViewAnimationUtils.createCircularReveal(mAddNoteToolbarContainer, cx, cy, 0, radius).start();
        } else {
            Animator animator = ViewAnimationUtils.createCircularReveal(mAddNoteToolbarContainer,
                    cx,cy,radius,0);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mAddNoteToolbarContainer.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        }
    }
}
