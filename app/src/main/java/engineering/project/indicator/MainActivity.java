package engineering.project.indicator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import engineering.project.indicator.activities.LoginActivity;
import engineering.project.indicator.componentsList.AdapterList;
import engineering.project.indicator.componentsList.ParentList;
import engineering.project.indicator.menuDrawer.DrawerItem;
import engineering.project.indicator.menuDrawer.ListAdapterDrawer;
import engineering.project.indicator.preferences.Preferences;
import engineering.project.indicator.structureModel.ModelList;
import engineering.project.indicator.structureModel.ModelTeacher;
import engineering.project.indicator.structureRealm.Realm_allocations;
import engineering.project.indicator.structureRealm.Realm_faculty_member;
import engineering.project.indicator.structureRealm.Realm_grades;
import engineering.project.indicator.structureRealm.Realm_school_groups;
import engineering.project.indicator.structureRealm.Realm_students;
import engineering.project.indicator.structureRealm.Realm_students_indicator;
import engineering.project.indicator.structureRealm.Realm_subjects;
import engineering.project.indicator.structureRealm.Realm_user;
import engineering.project.indicator.structureRealm.Realm_viewTables;
import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends Activity {

    Preferences p;
    Realm realm;
    ModelTeacher modelTeacher;
    TextView name, user, email;
    ExpandableListView listExpandable;
    ModelList modelList;

    DrawerLayout drawerLayout;
    String[] tagTitles;
    ListView drawerList;


    public static final String AUTHORITY = "engineering.project.indicator.sync.provider";
    public static final String ACCOUNT_TYPE = "www.evaluafacil.com.mx";
    public static final String ACCOUNT = "Evalua Facil";
    Account mAccount;
    ContentResolver mResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showLog("1");
        mAccount = CreateSyncAccount(this);
        showLog("2");
        mResolver = getContentResolver();
        showLog("3");
        mResolver.setSyncAutomatically(mAccount, AUTHORITY, true);

        builder();

        if (p.getAccesToken().equalsIgnoreCase("null acces"))
            loadLogin();
        else{
            getReferencesUser();
            loadList();
            }


    }

    private void builder() {
        p = new Preferences(this);
        realm = Realm.getInstance(this);

        listExpandable = (ExpandableListView) findViewById(R.id.miListaExp);
        name = (TextView) findViewById(R.id.txvMainName);
        user = (TextView) findViewById(R.id.txvMainData);
        email = (TextView) findViewById(R.id.txvMainEmail);

        createMenu();
    }

    private void loadList(){
        ArrayList<String> numbers = new ArrayList<String>();
        boolean validate = true;
        ArrayList<ModelList> arrayModelList = new ArrayList<ModelList>();

        ArrayList<ParentList> arrayParents = new ArrayList<ParentList>();
        ArrayList<String> arrayChildren;
        ParentList parent;

        RealmResults<Realm_allocations> allocationses = realm.where(Realm_allocations .class)
                .notEqualTo("id",-1).findAll();

        for (int x = 0; x<allocationses.size(); x++ ){
            validate = true;
            modelList = new ModelList();

            RealmResults<Realm_subjects> subjectses = realm.where(Realm_subjects.class)
                    .equalTo("id",allocationses.get(x).getSubjectId())
                    .findAll();
            RealmResults<Realm_school_groups> school_groupses = realm.where(Realm_school_groups.class)
                    .equalTo("id",allocationses.get(x).getGroupId())
                    .findAll();
            RealmResults<Realm_grades> grades = realm.where(Realm_grades.class)
                    .equalTo("id", allocationses.get(x).getGradeId())
                    .findAll();

            modelList.setMatterName(subjectses.get(0).getTitle());
            modelList.setMatterAbbre(subjectses.get(0).getAbbrevition());
            modelList.setGroup(school_groupses.get(0).getGroupName());
            modelList.setAllStudents(school_groupses.get(0).getTotalStudent());
            modelList.setGradeNumbre(grades.get(0).getGradeNumbre());
            modelList.setTitleGradeNumbre(grades.get(0).getTitle());

            if (x == 0)
                numbers.add(modelList.getTitleGradeNumbre());
            else{
                for (int y = 0; y < numbers.size(); y++)
                    if (numbers.get(y).equalsIgnoreCase(modelList.getTitleGradeNumbre()))
                        validate = false;

                if (validate)
                    numbers.add(modelList.getTitleGradeNumbre());
            }

            arrayModelList.add(modelList);
        }
        for (int x = 0;  x < numbers.size(); x++){
            parent = new ParentList();
            arrayChildren = new ArrayList<String>();
            parent.setTitle(numbers.get(x));

            for (int xx = 0; xx < arrayModelList.size(); xx++)
                if (numbers.get(x).equalsIgnoreCase(arrayModelList.get(xx).getTitleGradeNumbre()))
                    arrayChildren.add(arrayModelList.get(xx).getGradeNumbre() + getResources().getString(R.string.grade) +
                            arrayModelList.get(xx).getGroup() + ", " + arrayModelList.get(xx).getMatterName()  );

            parent.setArrayChildren(arrayChildren);
            arrayParents.add(parent);
        }
        listExpandable.setAdapter(new AdapterList(this, arrayParents));
    }

    private void getReferencesUser(){
        RealmResults<Realm_faculty_member> teacher = realm.where(Realm_faculty_member.class)
        .notEqualTo("userId",0)
        .findAll();

        RealmResults<Realm_user> user = realm.where(Realm_user.class)
        .notEqualTo("id",0)
        .findAll();

        if (teacher.size() > 0 && user.size() > 0)
            modelTeacher = new ModelTeacher(teacher.get(0).getTitle(), teacher.get(0).getFisrtName(),teacher.get(0).getLastName(),
                                            user.get(0).getUsername(),teacher.get(0).getEmail(),user.get(0).getRole(),teacher.get(0).getContact_number());

        name.setText(modelTeacher.getFisrtName() + " " + modelTeacher.getLastName());
        this.user.setText("Usuario: " + modelTeacher.getUser());
        email.setText(modelTeacher.getEmail());

    }

    private void loadLogin(){
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        this.finish();
    }

    //<editor-fold desc="Cerrar sesion">
    private void closeSesion(){
        realm.beginTransaction();
        realm.clear(Realm_allocations.class);
        realm.clear(Realm_faculty_member.class);
        realm.clear(Realm_grades.class);
        realm.clear(Realm_school_groups.class);
        realm.clear(Realm_students.class);
        realm.clear(Realm_students_indicator.class);
        realm.clear(Realm_subjects.class);
        realm.clear(Realm_user.class);
        realm.clear(Realm_viewTables.class);
        realm.commitTransaction();
        p.clear();
        loadLogin();
        this.finish();
    }

    private void dialogSesion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.contentSesion))//Contenido
                .setTitle(getResources().getString(R.string.cuidado))//Titulo
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        closeSesion();
                        dialog.cancel();
                    }
                })

                .setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    //</editor-fold>

    //<editor-fold desc="Menu">
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        drawerLayout.openDrawer(drawerList);
        return false;
    }

    private void createMenu(){
        tagTitles = new String[2];
        tagTitles = getResources().getStringArray(R.array.menuDrawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        ArrayList<DrawerItem> items =
                new ArrayList<DrawerItem>();
        items.add(
                new DrawerItem(tagTitles[0], R.mipmap.comentario));
        items.add(
                new DrawerItem(tagTitles[1], R.mipmap.salir));

        drawerList.setAdapter(
                new ListAdapterDrawer(this, items));
        drawerList.setOnItemClickListener(
                new DrawerItemClickListener());
    }

    private void selectItem(int position) {
        drawerList.setItemChecked(position, true);
        setTitle(tagTitles[position]);

        switch (position){
            case 0:
                Toast.makeText(this, "Esta parte permanece en contrccion", Toast.LENGTH_LONG).show();
                break;
            case 1:
                dialogSesion();
                break;
        }

        drawerLayout.closeDrawer(drawerList);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }


    }
    //</editor-fold>

    public static Account CreateSyncAccount(Context context) {
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {}
        else {}

        return newAccount;
    }

    private void showLog(String log){
        Log.v("MainActivity",log);
    }
}
