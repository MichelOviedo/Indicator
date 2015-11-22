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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

import engineering.project.indicator.activities.BinnacleActivity;
import engineering.project.indicator.activities.LoginActivity;
import engineering.project.indicator.componentsList.AdapterList;
import engineering.project.indicator.componentsList.ArrayChildren;
import engineering.project.indicator.componentsList.ParentList;
import engineering.project.indicator.menuDrawer.DrawerItem;
import engineering.project.indicator.menuDrawer.ListAdapterDrawer;
import engineering.project.indicator.preferences.Preferences;
import engineering.project.indicator.structureModel.ModelList;
import engineering.project.indicator.structureModel.ModelTeacher;
import engineering.project.indicator.structureRealm.Realm_allocations;
import engineering.project.indicator.structureRealm.Realm_binnacle;
import engineering.project.indicator.structureRealm.Realm_faculty_member;
import engineering.project.indicator.structureRealm.Realm_grades;
import engineering.project.indicator.structureRealm.Realm_indicator_details;
import engineering.project.indicator.structureRealm.Realm_school_groups;
import engineering.project.indicator.structureRealm.Realm_students;
import engineering.project.indicator.structureRealm.Realm_evaluation_indicator;
import engineering.project.indicator.structureRealm.Realm_subjects;
import engineering.project.indicator.structureRealm.Realm_user;
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

    ArrayList<ArrayChildren> children;
    ParentList parent;
    ArrayList<ParentList> parentList = new ArrayList<ParentList>();


    public static final String AUTHORITY = "engineering.project.indicator.sync.provider";
    public static final String ACCOUNT_TYPE = "www.evaluafacil.com.mx";
    public static final String ACCOUNT = "Evalua Facil";
    Account mAccount;
    ContentResolver mResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccount = CreateSyncAccount(this);
        mResolver = getContentResolver();
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

        RealmResults<Realm_grades> grades = realm.where(Realm_grades.class)
                .notEqualTo("id",-1)
                .findAll();

        int cont,num,i,j=0,k,z=0 ;
        ArrayList<Integer> v_aux = new ArrayList<Integer>();
        ArrayList<Integer> idGrades = new ArrayList<Integer>();

        for (i=0;i<grades.size();i++) {
            cont=0;
            num=grades.get(i).getId();
            v_aux.add(num);
            j++;
            for (k=0;k<v_aux.size();k++)
                if ( v_aux.get(k) == num )
                    cont++;

            if ( cont == 1 ) {
                idGrades.add(num);
            }
        }

        for (int x = 0; x < idGrades.size(); x++){
            parent = new ParentList();//Conter el gropu en global  1 Secundaria o 2 Secundaria
            children = new ArrayList<ArrayChildren>();//se guardan todos los gupus que pertenescan a parent

            RealmResults<Realm_grades> grade = realm.where(Realm_grades.class)
                    .equalTo("id", idGrades.get(x))
                    .findAll();

            parent.setTitle(grade.get(0).getTitle());//1 Primaria

            RealmResults<Realm_subjects> sb = realm.where(Realm_subjects.class)
                    .equalTo("idGrade", idGrades.get(x))
                    .findAll();

            for (int y = 0; y < sb.size(); y++){
                RealmResults<Realm_allocations> allocation = realm.where(Realm_allocations.class)
                        .equalTo("id", sb.get(y).getIdAllocation())
                        .findAll();
                RealmResults<Realm_school_groups> groups = realm.where(Realm_school_groups.class)
                        .equalTo("id", allocation.get(0).getGroupId())
                        .findAll();

                String title = parent.getTitle();
                StringTokenizer token = new StringTokenizer(title, " ");
                String subTitle = token.nextToken().toString() + "" + groups.get(0).getGroupName() + " " + sb.get(y).getTitle();

                ArrayChildren chi = new ArrayChildren();
                chi.setTitle(subTitle);
                chi.setPhoto(allocation.get(0).getIs_finish());
                chi.setIdAllocation(allocation.get(0).getId());
                children.add(chi);
            }

            int con = 0;
            for (int m = 0; m < children.size(); m++)
                if (children.get(m).getPhoto() == 1)
                    con++;

            parent.setPorcentaje((con * 100 / children.size()) + "");
            parent.setArrayChildren(children);
            parentList.add(parent);

        }

        listExpandable.setAdapter(new AdapterList(this, parentList));

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
        realm.clear(Realm_evaluation_indicator.class);
        realm.clear(Realm_subjects.class);
        realm.clear(Realm_user.class);
        realm.clear(Realm_binnacle.class);
        realm.clear(Realm_indicator_details.class);
        realm.commitTransaction();
        p.clear();
        loadLogin();
        this.finish();
    }

    private void dialogSesion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.contentSesion) + "\n" + getResources().getString(R.string.subCintentSesion))//Contenido
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
        tagTitles = new String[3];
        tagTitles = getResources().getStringArray(R.array.menuDrawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        ArrayList<DrawerItem> items =
                new ArrayList<DrawerItem>();
        items.add(
                new DrawerItem(tagTitles[0], R.mipmap.binnacle));
        items.add(
                new DrawerItem(tagTitles[1], R.mipmap.comentario));
        items.add(
                new DrawerItem(tagTitles[2], R.mipmap.salir) );

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
                binnacleStart();
                break;
            case 1:
                Toast.makeText(this, "Esta parte permanece en contrccion", Toast.LENGTH_LONG).show();

                break;
            case 2:
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

    private void binnacleStart(){
        Intent i = new Intent(this, BinnacleActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 14) {
            this.finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showLog(String log){
        Log.e("MainActivity",log);
    }
}