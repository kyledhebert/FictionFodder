package com.kylehebert.fictionfodder.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kylehebert.fictionfodder.R;
import com.kylehebert.fictionfodder.activity.TrashNoteActivity;
import com.kylehebert.fictionfodder.model.ImageNote;
import com.kylehebert.fictionfodder.model.Note;
import com.kylehebert.fictionfodder.model.TextNote;
import com.kylehebert.fictionfodder.model.TrashNoteList;
import com.kylehebert.fictionfodder.utility.Constants;
import com.kylehebert.fictionfodder.utility.DividerItemDecoration;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by kylehebert on 11/30/15.
 * A fragment that uses a recycler view to show all of the notes in the trash table
 * of the database
 */
public class TrashNoteListFragment extends Fragment {

    public static TrashNoteListFragment newInstance() {
        return new TrashNoteListFragment();
    }

    private RecyclerView mTrashNoteRecyclerView;
    private Toolbar mToolbar;
    private NoteAdapter mNoteAdapter;

    private int mUpdatedNotePosition;
    private int mDeletedNotePosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        mTrashNoteRecyclerView = (RecyclerView) view.findViewById(R.id.item_recycler_view);
        mTrashNoteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //adds a divider between list items
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST);
        mTrashNoteRecyclerView.addItemDecoration(itemDecoration);

        updateUI();

        return view;
    }


    private void updateUI() {
        TrashNoteList trashNoteList = TrashNoteList.get(getActivity());
        List<Note> notes = trashNoteList.getNotes();

        if (mNoteAdapter == null) {
            mNoteAdapter = new NoteAdapter(notes);
            mTrashNoteRecyclerView.setAdapter(mNoteAdapter);
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
            Intent intent = TrashNoteActivity.newIntent(getActivity(), mNote.getId());
            startActivity(intent);
        }

    }

    private class TextNoteHolder extends TrashNoteListFragment.NoteHolder {

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
            Intent intent = TrashNoteActivity.newIntent(getActivity(), mTextNote.getId());
            startActivity(intent);
        }

    }


    private class ImageNoteHolder extends TrashNoteListFragment.NoteHolder {

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

            File imageFile = TrashNoteList.get(getActivity()).getPhotoFile(mImageNote);
            Uri uri = Uri.fromFile(imageFile);

            Picasso.with(getActivity()).load(uri).resize(120,120).centerCrop().into(mThumbnailView);
        }

        @Override
        public void onClick(View view) {
            mUpdatedNotePosition = getAdapterPosition();
            mDeletedNotePosition = getAdapterPosition();
            Intent intent = TrashNoteActivity.newIntent(getActivity(), mImageNote.getId());
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
                    TextNote textNote = TrashNoteList.get(getActivity()).getTextNote(tNote.getId());
                    textNoteHolder.bindTextNote(textNote);
                    break;

                case Constants.TYPE_IMAGE:
                    ImageNoteHolder imageNoteHolder = (ImageNoteHolder) noteHolder;
                    Note iNote = mNoteList.get(position);
                    ImageNote imageNote = TrashNoteList.get(getActivity()).getImageNote(iNote
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

}
