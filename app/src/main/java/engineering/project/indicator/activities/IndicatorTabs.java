package engineering.project.indicator.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import engineering.project.indicator.R;
import engineering.project.indicator.preferences.Preferences;
import engineering.project.indicator.structureRealm.Realm_students;
import engineering.project.indicator.tabsIndicators.TabAbsences;
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

        getStudents();
    }

    private void getStudents(){
        RealmResults<Realm_students> studentses = realm.where(Realm_students.class)
                .equalTo("idInformal", p.getIdGroup())
                .findAll();

        Toast.makeText(this, "Cantidad: " + studentses.size(), Toast.LENGTH_LONG).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new TabAbsences(), "Asistencia");
        adapter.addFrag(new TabAbsences(), "Asistencia");

        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.title_tab, null);
        tabOne.setText("Asistencia");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.salir, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.title_tab, null);
        tabTwo.setText("Paricipacion");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.comentario, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

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
        p.setIdGroup("Destroy");
        super.onDestroy();
    }
}
