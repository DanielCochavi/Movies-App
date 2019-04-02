package com.academy.fundamentals.mymoviesapp.Details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.academy.fundamentals.mymoviesapp.R;
import com.academy.fundamentals.mymoviesapp.Model.MovieContent;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_POSITION = "init-position-data";

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager pager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private DetailsAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_page);

        // Instantiate a ViewPager and a PagerAdapter.
        pagerAdapter = new DetailsAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        int startPosition = getIntent().getIntExtra(EXTRA_ITEM_POSITION, 0);
        pager.setCurrentItem(startPosition, false);
    }


    public class DetailsAdapter extends FragmentPagerAdapter {

        public DetailsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MoviesDetailsFragment.newInstance(MovieContent.MOVIES.get(position));
        }

        @Override
        public int getCount() {
            return MovieContent.MOVIES.size();
        }
    }


}
