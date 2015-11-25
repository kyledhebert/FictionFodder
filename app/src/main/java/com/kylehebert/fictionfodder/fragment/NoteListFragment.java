package com.kylehebert.fictionfodder.fragment;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kylehebert.fictionfodder.R;
import com.kylehebert.fictionfodder.activity.NoteActivity;
import com.kylehebert.fictionfodder.utility.Constants;
import com.kylehebert.fictionfodder.model.ImageNote;
import com.kylehebert.fictionfodder.model.Note;
import com.kylehebert.fictionfodder.model.NoteList;
import com.kylehebert.fictionfodder.model.TextNote;
import com.kylehebert.fictionfodder.utility.DividerItemDecoration;
import com.squareup.picasso.Picasso;

import java.io.File;
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
    private int mDeletedNotePosition;


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

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST);
        mNoteRecyclerView.addItemDecoration(itemDecoration);

        mAddNoteToolBar = (Toolbar) view.findViewById(R.id.select_note_type_toolbar);
        mAddNoteToolBar.inflateMenu(R.menu.fragment_note_bottom_menu);
        mAddNoteToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_add_text_note:
                        Note textNote = new Note();
                        textNote.setType(Constants.TYPE_TEXT_NOTE);
                        NoteList.get(getActivity()).addNote(textNote);
                        Log.i("NoteListFragment", "textNote type:" + textNote.getType());
                        Intent textIntent = NoteActivity.newIntent(getActivity(), textNote.getId());
                        startActivity(textIntent);
                        return true;
                    case R.id.menu_item_add_image_note:
                        Note imageNote = new Note();
                        imageNote.setType(Constants.TYPE_IMAGE_NOTE);
                        NoteList.get(getActivity()).addNote(imageNote);
                        Intent imageIntent = NoteActivity.newIntent(getActivity(), imageNote.getId());
                        startActivity(imageIntent);
                        return true;
                    case R.id.menu_item_close_toolbar:
                        toggleToolbarVisibility(view);
                        mAddNoteButton.setVisibility(View.VISIBLE);
                    default:
                        return false;

                }

            }
        });


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

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        NoteList noteList = NoteList.get(getActivity());
        List<Note> notes = noteList.getNotes();

        if (mNoteAdapter == null) {
            mNoteAdapter = new NoteAdapter(notes);
            mNoteRecyclerView.setAdapter(mNoteAdapter);
        } else {
            mNoteAdapter.setNotes(notes);
            mNoteAdapter.notifyItemRemoved(mDeletedNotePosition);
            mDeletedNotePosition = RecyclerView.NO_POSITION;
            mNoteAdapter.notifyItemChanged(mUpdatedNotePosition);
            mUpdatedNotePosition = RecyclerView.NO_POSITION;
        }

    }

    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Note mNote;

        private TextView mTitleTextView;
        private TextView mSnippetTextView;
        private ImageView mImageView;

        public NoteHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.note_title_text_view);
            mSnippetTextView = (TextView) itemView.findViewById(R.id.text_note_snippet_text_view);

        }

        public void bindNote(Note note) {
            mNote = note;
        }

        @Override
        public void onClick(View view) {
            mUpdatedNotePosition = getAdapterPosition();
            mDeletedNotePosition = getAdapterPosition();
            Intent intent = NoteActivity.newIntent(getActivity(), mNote.getId());
            startActivity(intent);
        }

    }

    private class TextNoteHolder extends NoteListFragment.NoteHolder {

        private TextView mTitleTextView;
        private TextView mSnippetTextView;

        private TextNote mTextNote;

        public TextNoteHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.note_title_text_view);
            mSnippetTextView = (TextView) itemView.findViewById(R.id.text_note_snippet_text_view);
        }

        public void bindTextNote(TextNote textNote) {
            mTextNote = textNote;
            mTitleTextView.setText(mTextNote.getTitle());
            mSnippetTextView.setText(mTextNote.getNoteBody());
        }

        @Override
        public void onClick(View view) {
            mDeletedNotePosition = getAdapterPosition();
            mUpdatedNotePosition = getAdapterPosition();
            Intent intent = NoteActivity.newIntent(getActivity(), mTextNote.getId());
            startActivity(intent);
        }

    }


    private class ImageNoteHolder extends NoteListFragment.NoteHolder {

        private TextView mCaptionTextView;
        private ImageView mThumbnailView;

        private ImageNote mImageNote;

        public ImageNoteHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mCaptionTextView = (TextView) itemView.findViewById(R.id.image_note_caption_text_view);
            mThumbnailView = (ImageView) itemView.findViewById(R.id.image_note_thumbnail_image_view);

        }

        public void bindImageNote(ImageNote imageNote) {
            mImageNote = imageNote;
            mCaptionTextView.setText(mImageNote.getCaption());

            File imageFile = NoteList.get(getActivity()).getPhotoFile(mImageNote);
            Uri uri = Uri.fromFile(imageFile);

            Picasso.with(getActivity()).load(uri).resize(120,120).centerCrop().into(mThumbnailView);
        }

        @Override
        public void onClick(View view) {
            mUpdatedNotePosition = getAdapterPosition();
            mDeletedNotePosition = getAdapterPosition();
            Intent intent = NoteActivity.newIntent(getActivity(), mImageNote.getId());
            startActivity(intent);
        }

    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {

        private List<Note> mNoteList;

        public NoteAdapter(List<Note> noteList) {
            mNoteList = noteList;
        }

        /*
        determine the view type - text or image - for each item in the note list
         */
        @Override
        public int getItemViewType(int position) {

            int viewType;

            if(mNoteList.get(position).getType().equals(Constants.TYPE_TEXT_NOTE)) {
                viewType = Constants.TYPE_TEXT;
            } else {
                viewType = Constants.TYPE_IMAGE;
            }

            return viewType;
        }



        @Override
        public NoteHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            switch (viewType) {
                case Constants.TYPE_TEXT:
                    View viewText = layoutInflater.inflate(R.layout.list_item_text_note, viewGroup,
                            false);
                    return new TextNoteHolder(viewText);

                case Constants.TYPE_IMAGE:
                    View viewImage = layoutInflater.inflate(R.layout.list_item_image_note,
                            viewGroup, false);
                    return new ImageNoteHolder(viewImage);

                default:
                    View view = layoutInflater.inflate(R.layout.list_item_text_note, viewGroup,
                            false);
                    return new TextNoteHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(NoteHolder noteHolder, int position) {

            switch (noteHolder.getItemViewType()) {

                case Constants.TYPE_TEXT:
                    TextNoteHolder textNoteHolder = (TextNoteHolder) noteHolder;
                    Note tNote = mNoteList.get(position);
                    TextNote textNote = NoteList.get(getActivity()).getTextNote(tNote.getId());
                    textNoteHolder.bindTextNote(textNote);
                    break;

                case Constants.TYPE_IMAGE:
                    ImageNoteHolder imageNoteHolder = (ImageNoteHolder) noteHolder;
                    Note iNote = mNoteList.get(position);
                    ImageNote imageNote = NoteList.get(getActivity()).getImageNote(iNote
                            .getId());
                    imageNoteHolder.bindImageNote(imageNote);
                    break;


            }
            Note note = mNoteList.get(position);
            noteHolder.bindNote(note);

        }

        @Override
        public int getItemCount() {
            return mNoteList.size();
        }

        /*
        gets called by updateUI to refresh the fragment's snapshot of the NoteList
         */
        public void setNotes(List<Note> noteList) {
            mNoteList = noteList;
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
