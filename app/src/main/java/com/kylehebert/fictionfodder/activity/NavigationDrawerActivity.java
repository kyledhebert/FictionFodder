package com.kylehebert.fictionfodder.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.kylehebert.fictionfodder.R;
import com.kylehebert.fictionfodder.fragment.NoteListFragment;
import com.kylehebert.fictionfodder.fragment.TrashNoteListFragment;
import com.kylehebert.fictionfodder.utility.Constants;

/**
 * Created by kylehebert on 11/28/15.
 * The main activity, a navigation drawer for choosing and displaying note list
 * fragments
 */

/**
 * Many of the ideas for this activity came from:
 * https://github.com/codepath/android_guides/wiki/Fragment-Navigation-Drawer
 */


public class NavigationDrawerActivity extends AppCompatActivity {

    private DrawerLayout mNavigationDrawer;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        // set Toolbar to replace the ActionBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        mDrawerToggle = setUpDrawerToggle();

        // ties drawer events to the action bar toggle
        mNavigationDrawer.setDrawerListener(mDrawerToggle);


        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        showDrawerContent(mNavigationView);


        // loads an all notes list by default if the fragment container is empty
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            NoteListFragment noteListFragment = NoteListFragment.newInstance(Constants
                    .QUERY_ALL_NOTES);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, noteListFragment).commit();
        }
    }

    private ActionBarDrawerToggle setUpDrawerToggle() {
        return new ActionBarDrawerToggle(this, mNavigationDrawer, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
    }

    private void showDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem item) {
        // create a new NoteList Fragment based on the item selected

        Fragment fragment;

        switch (item.getItemId()) {
            case R.id.navigation_all_notes:
                fragment = NoteListFragment.newInstance(Constants.QUERY_ALL_NOTES);
                break;
            case R.id.navigation_text_notes:
                fragment = NoteListFragment.newInstance(Constants.QUERY_TEXT_NOTES);
                break;
            case R.id.navigation_image_notes:
                fragment = NoteListFragment.newInstance(Constants.QUERY_IMAGE_NOTES);
                break;
            case R.id.navigation_trash_notes:
                fragment = TrashNoteListFragment.newInstance();
                break;
            default:
                fragment = NoteListFragment.newInstance(Constants.QUERY_ALL_NOTES);

        }

        // insert the new fragment and replace any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

        // highlight the selected item, update the title, and close the drawer
        item.setChecked(true);
        setTitle(item.getTitle());
        mNavigationDrawer.closeDrawers();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // sync the drawer toggle state
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // pass any config change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
