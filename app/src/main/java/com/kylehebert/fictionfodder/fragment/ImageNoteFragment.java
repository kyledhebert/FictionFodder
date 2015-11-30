package com.kylehebert.fictionfodder.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kylehebert.fictionfodder.R;
import com.kylehebert.fictionfodder.activity.NoteActivity;
import com.kylehebert.fictionfodder.model.TrashNoteList;
import com.kylehebert.fictionfodder.utility.Constants;
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
    private Toolbar mToolbar;



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

        // replace the action bar with the support toolbar
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);


        mCaptionLabelTextView = (TextView) view.findViewById(R.id.caption_label_text_view);

        mEditCaptionEditText = (EditText) view.findViewById(R.id.caption_edit_text);
        mEditCaptionEditText.setText(mImageNote.getCaption());
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
            Picasso.with(getActivity()).load(uri).resize(120,120).centerCrop().into(mImageView);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_note, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_save_note:
                NoteList.get(getActivity()).updateImageNote(mImageNote);
                Toast.makeText(getActivity(), R.string.save_item_toast, Toast.LENGTH_SHORT).show();
                getActivity().finish();
                return true;
            case R.id.menu_item_delete_note:
                TrashNoteList.get(getActivity()).addNote(mImageNote);
                TrashNoteList.get(getActivity()).updateImageNote(mImageNote);
                NoteList.get(getActivity()).deleteNote(mImageNote);
                //TODO make Toast a snackbar with undo
                Toast.makeText(getActivity(), R.string.delete_item_toast, Toast.LENGTH_SHORT).show();
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);

        }
    }





}
