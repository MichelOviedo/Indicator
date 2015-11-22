package engineering.project.indicator.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import engineering.project.indicator.R;
import engineering.project.indicator.componentsList.AnimatedExpandableListView;
import engineering.project.indicator.structureRealm.Realm_binnacle;
import engineering.project.indicator.structureRealm.Realm_evaluation_indicator;
import engineering.project.indicator.structureRealm.Realm_indicator_details;
import engineering.project.indicator.structureRealm.Realm_students;
import engineering.project.indicator.webService.WebService;
import io.realm.Realm;
import io.realm.RealmResults;

public class BinnacleActivity extends Activity {

    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;
    Realm realm;
    Button btnSync;
    Context context;
    int cntSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binnacle);
        List<GroupItem> items = new ArrayList<GroupItem>();

        context = this;
        realm = Realm.getInstance(this);
        btnSync = (Button) findViewById(R.id.syncData);

        RealmResults<Realm_binnacle> sync = realm.where(Realm_binnacle.class)
                .equalTo("upServer",0)
                .findAll();
        cntSync = sync.size();

        RealmResults<Realm_binnacle> ready = realm.where(Realm_binnacle.class)
                .notEqualTo("upServer", 0)
                .findAll();

        GroupItem ok = new GroupItem();
        ok.title = getResources().getString(R.string.binnacleTitleOk);

        int interacionReady;
        if (ready.size() >=10)
            interacionReady = (ready.size() -11); // ready = 80 intetacion vale 69
        else{
            if (ready.size()< 1){
                ChildItem child = new ChildItem();
                child.title ="No tienes datos sincronizados" ;
                child.hint = ":C";
                ok.items.add(child);
            }
            interacionReady = 0; // = 0
            }

            for(int i = (ready.size()-1); interacionReady < i; i--) {

                RealmResults<Realm_evaluation_indicator> e = realm.where(Realm_evaluation_indicator.class)
                        .equalTo("idPk",ready.get(i).getIdEvaluation())
                        .findAll();
                RealmResults<Realm_students> students = realm.where(Realm_students.class)
                        .equalTo("id", e.get(0).getIdStudent())
                        .findAll();
                RealmResults<Realm_indicator_details >details = realm.where(Realm_indicator_details.class)
                        .equalTo("idPk", e.get(0).getIdIndicator())
                        .findAll();


                ChildItem child = new ChildItem();
                child.title = ready.get(i).getIdEvaluation() +": " + students.get(0).getFirstName() + " " + students.get(0).getLastName()+ " " +
                            students.get(0).getMotherName() + " " + details.get(0).getTilte();
                child.hint = secondsToDate(ready.get(i).getUpServer());

                ok.items.add(child);
            }

            items.add(ok);


        GroupItem syncData = new GroupItem();
        syncData.title = getResources().getString(R.string.binnacleTitleSync);
        int interacionSync;
        if (sync.size() >=10)
            interacionSync = 10; // ready = 80 intetacion vale 69
        else{
            if (sync.size()< 1){
                ChildItem child = new ChildItem();
                child.title ="No tienes datos para  sincronizados" ;
                child.hint = ":)";
                syncData.items.add(child);
            }
            interacionSync = 0; // = 0
        }

        for(int i = 0; i <  interacionSync; i++) {
            RealmResults<Realm_evaluation_indicator> e = realm.where(Realm_evaluation_indicator.class)
                    .equalTo("idPk",sync.get(i).getIdEvaluation())
                    .findAll();
            RealmResults<Realm_students> students = realm.where(Realm_students.class)
                    .equalTo("id", e.get(0).getIdStudent())
                    .findAll();
            RealmResults<Realm_indicator_details >details = realm.where(Realm_indicator_details.class)
                    .equalTo("idPk",e.get(0).getIdIndicator())
                    .findAll();
            ChildItem child = new ChildItem();
            child.title =sync.get(i).getIdEvaluation() +": " + students.get(0).getFirstName() + " " + students.get(0).getLastName()+ " " +
                    students.get(0).getMotherName() + " " + details.get(0).getTilte() ;
            child.hint = secondsToDate(sync.get(i).getCreate());

            syncData.items.add(child);
        }


        items.add(syncData);

        GroupItem info = new GroupItem();
        info.title = "Informacion General";
        ChildItem child = new ChildItem();
        child.title ="Sincronizados: " + ready.size();
        child.hint = "";
        info.items.add(child);
        ChildItem child2 = new ChildItem();
        child2.title ="Sin Sicronizar: " + sync.size();
        child2.hint = "";
        info.items.add(child2);

        items.add(info);


        adapter = new ExampleAdapter(this);
        adapter.setData(items);

        listView = (AnimatedExpandableListView) findViewById(R.id.listViewBinnacle);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cntSync > 0 )
                    new WebService(context).syncData();
                else
                    Toast.makeText(context, "No tienes datos para sincronizar", Toast.LENGTH_LONG).show();
            }
        });


    }

    public long secondDate() {
        return System.currentTimeMillis() / 1000;//Milisegundos actual del sismtema
    }

    public String secondsToDate(long seconds) {

        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(seconds * 1000);

        return getNameDay(calendario.get(Calendar.DAY_OF_WEEK)) + " " + calendario.get(Calendar.DAY_OF_MONTH) + "/" + getMonth(calendario.get(Calendar.MONTH))+
                "/" +  calendario.get(Calendar.YEAR) + " \n" +
                        calendario.get(Calendar.HOUR_OF_DAY)  + ":" + calendario.get(Calendar.MINUTE) + ":" +calendario.get(Calendar.SECOND)+ " ";
    }


    public String getMonth(int month){
        switch (month){
            case 0:
                return"Enero";
            case 1:
                return"Febrero";
            case 2:
                return"Marzo";
            case 3:
                return"Abril";
            case 4:
                return"Mayo";
            case 5:
                return"Junio";
            case 6:
                return"Julio";
            case 7:
                return"Agosto";
            case 8:
                return"Septiembre";
            case 9:
                return"Octubre";
            case 10:
                return"Noviembre";
            case 11:
                return"Diciembre";
            default:
                return "";
        }

    }

    public String getNameDay(int day){
        switch (day){
            case 1:
                return getResources().getString(R.string.domingo);
            case 2:
                return getResources().getString(R.string.lunes);
            case 3:
                return getResources().getString(R.string.martes);
            case 4:
                return getResources().getString(R.string.miercoles);
            case 5:
                return getResources().getString(R.string.jueves);
            case 6:
                return getResources().getString(R.string.viernes);
            case 7:
                return getResources().getString(R.string.sabado);
            case 8:
                return getResources().getString(R.string.domingo);
            default:
                return "";
        }
    }

    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String title;
        String hint;
    }

    private static class ChildHolder {
        TextView title;
        TextView hint;
    }

    private static class GroupHolder {
        TextView title;
    }

    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.binnacle_list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                holder.hint = (TextView) convertView.findViewById(R.id.textDate);
                convertView.setTag(holder);


            } else {
                holder = (ChildHolder) convertView.getTag();
            }
            holder.title.setText(item.title);
            holder.hint.setText(item.hint);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.binnacle_group_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }


}
