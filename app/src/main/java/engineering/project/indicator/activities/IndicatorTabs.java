package engineering.project.indicator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import engineering.project.indicator.MainActivity;
import engineering.project.indicator.R;
import engineering.project.indicator.preferences.Preferences;
import engineering.project.indicator.structureRealm.Realm_allocations;
import engineering.project.indicator.structureRealm.Realm_subjects;
import engineering.project.indicator.tabsIndicators.TabAbsences;
import engineering.project.indicator.tabsIndicators.TabFriendship;
import engineering.project.indicator.tabsIndicators.TabMath;
import engineering.project.indicator.tabsIndicators.TabParticipation;
import engineering.project.indicator.tabsIndicators.TabPerformance;
import engineering.project.indicator.tabsIndicators.TabReading;
import io.realm.Realm;
import io.realm.RealmResults;

public class IndicatorTabs extends AppCompatActivity {

    Preferences p;
    Realm realm;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicator_tabs);
        builder();


       toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }

    private void builder(){
        realm = Realm.getInstance(this);
        p = new Preferences(this);

        getMatter();
    }

    private void getMatter(){
        RealmResults<Realm_allocations> allocation = realm.where(Realm_allocations.class)
                .equalTo("id",p.getAllocation())
                .findAll();
        RealmResults<Realm_subjects> sub = realm.where(Realm_subjects.class)
                .equalTo("id", allocation.get(0).getSubjectId())
                .findAll();


        p.setMatter(sub.get(0).getTitle());
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        adapter.addFrag(new TabAbsences(), getResources().getString(R.string.titleAsis));
        adapter.addFrag(new TabFriendship(), getResources().getString(R.string.titleCon));
        adapter.addFrag(new TabPerformance(), getResources().getString(R.string.titleDesemp));
        adapter.addFrag(new TabParticipation(), getResources().getString(R.string.titlePart));

        if (p.getMatter().equalsIgnoreCase(getResources().getString(R.string.mat)))
            adapter.addFrag(new TabMath(), getResources().getString(R.string.titleMath));

        if (p.getMatter().equalsIgnoreCase(getResources().getString(R.string.espa)))
            adapter.addFrag(new TabReading(), getResources().getString(R.string.titleEspa));

        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.title_tab, null);
        tabOne.setText(getResources().getString(R.string.titleAsis));
        tabOne.setTextSize(9);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ind_abb, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.title_tab, null);
        tabTwo.setText(getResources().getString(R.string.titleCon));
        tabTwo.setTextSize(9);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ind_friend, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.title_tab, null);
        tabThree.setText(getResources().getString(R.string.titleDesemp));
        tabThree.setTextSize(9);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ind_performa, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tab = (TextView) LayoutInflater.from(this).inflate(R.layout.title_tab, null);
        tab.setText(getResources().getString(R.string.titlePart));
        tab.setTextSize(9);
        tab.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ind_participation, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tab);


        if (p.getMatter().equalsIgnoreCase(getResources().getString(R.string.mat))){
            TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.title_tab, null);
            tabFour.setText(getResources().getString(R.string.titleMath));
            tabFour.setTextSize(9);
            tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ind_mat, 0, 0);
            tabLayout.getTabAt(4).setCustomView(tabFour);
        }

        if (p.getMatter().equalsIgnoreCase(getResources().getString(R.string.espa))){
            TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.title_tab, null);
            tabFour.setText(getResources().getString(R.string.titleEspa));
            tabFour.setTextSize(9);
            tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ind_reading, 0, 0);
            tabLayout.getTabAt(4).setCustomView(tabFour);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){

            Intent i = new Intent(this, MainActivity.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

            return false;
        }else
        return super.onKeyDown(keyCode, event);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    protected void onDestroy() {
        p.setAllocation(0);
        super.onDestroy();
    }
}
