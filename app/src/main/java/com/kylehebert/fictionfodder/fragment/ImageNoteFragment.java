package com.kylehebert.fictionfodder.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kylehebert.fictionfodder.R;
import com.kylehebert.fictionfodder.config.Constants;
import com.kylehebert.fictionfodder.model.ImageNote;
import com.kylehebert.fictionfodder.model.NoteList;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

/**
 * Created by kylehebert on 11/21/15.
 * This fragment is where image notes will be created and viewed
 */
public class ImageNoteFragment extends Fragment{


    private ImageNote mImageNote;
    private File mImageFile;

    private TextView mCaptionLabelTextView;
    private EditText mEditCaptionEditText;
    private ImageView mImageView;
    private ImageButton mTakePictureButton;


    public static ImageNoteFragment newInstance(UUID noteId) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.ARG_NOTE_ID, noteId);

        ImageNoteFragment imageNoteFragment = new ImageNoteFragment();
        imageNoteFragment.setArguments(args);
        return imageNoteFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID noteId = (UUID) getArguments().getSerializable(Constants.ARG_NOTE_ID);
        mImageNote = NoteList.get(getActivity()).getImageNote(noteId);
        mImageFile = NoteList.get(getActivity()).getPhotoFile(mImageNote);
    }

    @Override
    public void onPause() {
        super.onPause();

        NoteList.get(getActivity()).updateImageNote(mImageNote);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image_note, container, false);

        mCaptionLabelTextView = (TextView) view.findViewById(R.id.caption_label_text_view);

        mEditCaptionEditText = (EditText) view.findViewById(R.id.caption_edit_text);
        mEditCaptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mImageNote.setCaption(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        PackageManager packageManager = getActivity().getPackageManager();

        mTakePictureButton = (ImageButton) view.findViewById(R.id.take_picture_button);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        /*
        check to make sure device has camera and permission to use
        if not disable the camera button
         */
        boolean canTakePicture = mImageFile != null && captureImage.resolveActivity(packageManager)
                != null;
        mTakePictureButton.setEnabled(canTakePicture);

        if (canTakePicture) {
            Uri uri = Uri.fromFile(mImageFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mTakePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, Constants.REQUEST_IMAGE);
            }
        });

        mImageView = (ImageView) view.findViewById(R.id.image_image_view);
        updateImageView();

        return view;
    }

    private void updateImageView() {
        Uri uri = Uri.fromFile(mImageFile);

        if (mImageFile == null || !mImageFile.exists()) {
            mImageView.setImageDrawable(null);
        } else {
            Picasso.with(getActivity()).load(uri).into(mImageView);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == Constants.REQUEST_IMAGE) {
            updateImageView();
        }
    }



}
