package engineering.project.indicator.tabsIndicators;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import engineering.project.indicator.R;
import engineering.project.indicator.preferences.Preferences;
import engineering.project.indicator.structureModel.ModelStudent;
import engineering.project.indicator.structureRealm.Realm_students;
import engineering.project.indicator.structureRealm.Realm_students_indicator;
import engineering.project.indicator.structureRealm.Realm_viewTables;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by EduardoMichel on 13/11/2015.
 */
public class TabFriendship extends Fragment {

    View view;
    TableLayout table, head;
    TableRow.LayoutParams layoutFila, layoutId, layoutTexto;
    Resources rs;
    Context context;
    Button save, edit;
    Spinner indicator, extIndicator;
    TextView type, count, tableTitle, tablePorcentage, tableFileOne, tableFileOnePro, tableFileTwo, tableFileTwoPor, tableFileThree, tableFileThreePor, titleEdit;
    RelativeLayout content, subContenedor;
    Preferences p;
    Realm realm;
    ArrayList<ModelStudent> listStudent;
    ArrayList<Integer> indicators;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_tab, container, false);
        context = container.getContext();

        builder();
        userButton();

        return view;
    }

    private void builder(){

        content = (RelativeLayout) view.findViewById(R.id.rltContentViewTable);
        p = new Preferences(context);
        rs = context.getResources();
        save = (Button) view.findViewById(R.id.btnSaveTab);
        realm = Realm.getInstance(context);

        head = (TableLayout)view.findViewById(R.id.tblHead);
        table = (TableLayout)view.findViewById(R.id.tblContentTable);
        type = (TextView) view.findViewById(R.id.txvTitleTab);
        count = (TextView) view.findViewById(R.id.txvSubTitleTab);

        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
        layoutId = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        layoutTexto = new TableRow.LayoutParams(160,TableRow.LayoutParams.MATCH_PARENT);

        type.setText(rs.getString(R.string.indicatorFriend));
        count.setText(rs.getString(R.string.indicatorFriendSub));

        agregarCabecera();
        agregarFilasTabla();

        subContenedor = (RelativeLayout) view.findViewById(R.id.rltContentViewEditTable);
        edit = (Button) view.findViewById(R.id.btnEdit);
        tableTitle = (TextView) view.findViewById(R.id.txvTableTitle);
        tablePorcentage = (TextView) view.findViewById(R.id.txvTablePercentage);
        tableFileOne = (TextView) view.findViewById(R.id.txvTableFileOne);
        tableFileOnePro  = (TextView) view.findViewById(R.id.txvTableFileOnePor);
        tableFileTwo  = (TextView) view.findViewById(R.id.txvTableFileTwo);
        tableFileTwoPor  = (TextView) view.findViewById(R.id.txvTableFileTwoPor);
        tableFileThree  = (TextView) view.findViewById(R.id.txvTableFileThree);
        tableFileThreePor  = (TextView) view.findViewById(R.id.txvTableFileThreePor);
        titleEdit = (TextView) view.findViewById(R.id.txvTlitleEdit);

        titleEdit.setText(rs.getString(R.string.conviEdit));
        tableTitle.setText(rs.getString(R.string.titleTab));
        tablePorcentage.setText(rs.getString(R.string.promedio));
        tableFileOne.setText(rs.getString(R.string.tabMala));
        tableFileTwo.setText(rs.getString(R.string.tabReg));
        tableFileThree.setText(rs.getString(R.string.tabBue));
        tableFileOnePro.setText("");
        tableFileTwoPor.setText("");
        tableFileThreePor.setText("");


        RealmResults<Realm_viewTables> viewTables = realm.where(Realm_viewTables.class)
                .equalTo("idGroup",p.getIdGroup())
                .findAll();
        if (viewTables.get(0).getFriendship_score() <= 0) {
            viewLayoutList();
        }
        else{
            viewLatoutEdit();
        }



    }

    //<editor-fold desc="Tabla dinamica">
    public void agregarCabecera(){
        TableRow fila;
        TextView txtId;
        TextView txtNombre;

        fila = new TableRow(context);
        fila.setLayoutParams(layoutFila);

        txtId = new TextView(context);
        txtNombre = new TextView(context);

        txtId.setText(rs.getString(R.string.name));
        txtId.setGravity(Gravity.CENTER_HORIZONTAL);
        txtId.setBackgroundResource(R.drawable.cabecera);
        txtId.setTextColor(Color.BLACK);
        txtId.setLayoutParams(layoutId);

        txtNombre.setText(rs.getString(R.string.indicatorFri));
        txtNombre.setTextColor(Color.BLACK);
        txtNombre.setGravity(Gravity.CENTER_HORIZONTAL);
        txtNombre.setBackgroundResource(R.drawable.cabecera);
        txtNombre.setLayoutParams(layoutTexto);

        fila.addView(txtId);
        fila.addView(txtNombre);
        head.addView(fila);
    }

    public void agregarFilasTabla() {
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Buena");
        spinnerArray.add("Regular");
        spinnerArray.add("Mala");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        listStudent = new ArrayList<ModelStudent>();
        ModelStudent student;
        RealmResults<Realm_students> studentses = realm.where(Realm_students.class)
                .equalTo("idInformal", p.getIdGroup())
                .findAll();

        for (int x = 0; x < studentses.size(); x++) {
            student = new ModelStudent();
            student.setId(studentses.get(x).getId());
            student.setLastName(studentses.get(x).getLastName());
            student.setFirstName(studentses.get(x).getFirstName());
            student.setMotherName(studentses.get(x).getMotherName());
            student.setIdInformal(studentses.get(x).getIdInformal());
            student.setGender(studentses.get(x).getGender());
            student.setGruopId(studentses.get(x).getGruopId());

            listStudent.add(student);
        }

        for (int x = 0; x < listStudent.size(); x++) {
            TableRow fila;
            TextView txtId;

            fila = new TableRow(context);
            fila.setLayoutParams(layoutFila);

            txtId = new TextView(context);
            indicator = new Spinner(context);

            txtId.setText(listStudent.get(x).getFirstName() + "\n" +
                    listStudent.get(x).getLastName() + " " + listStudent.get(x).getMotherName());
            txtId.setGravity(Gravity.CENTER_HORIZONTAL);
            txtId.setBackgroundResource(R.drawable.celda);
            txtId.setLayoutParams(layoutId);
            txtId.setTextColor(Color.BLACK);

            indicator.setGravity(Gravity.CENTER_HORIZONTAL);
            indicator.setId(x);
            indicator.setBackgroundResource(R.drawable.celda);
            //indicator.setRawInputType(InputType.TYPE_CLASS_NUMBER);
           // indicator.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
            indicator.setAdapter(adapter);
            indicator.setLayoutParams(layoutTexto);

            fila.addView(txtId);
            fila.addView(indicator);
            table.addView(fila);
        }
    }
    //</editor-fold>

    private void userButton(){
        save.setOnClickListener(new View.OnClickListener() {

            //como cambiar de fragment v4
            @Override
            public void onClick(View v) {

                indicators = new ArrayList<Integer>();
                double ind = 0;
                realm.beginTransaction();

                for (int x = 0; x < listStudent.size(); x++) {
                    extIndicator = (Spinner) view.findViewById(x);
                    RealmResults<Realm_students_indicator> results = realm.where(Realm_students_indicator.class)
                            .equalTo("idStudent", listStudent.get(x).getId())
                            .findAll();

                    showLog("Student: " + results.get(0).getFriendship_score());

                    if (extIndicator.getSelectedItem().toString().equalsIgnoreCase("Buena"))
                        ind = 1;
                    if (extIndicator.getSelectedItem().toString().equalsIgnoreCase("Regular"))
                        ind = 0.5;
                    if (extIndicator.getSelectedItem().toString().equalsIgnoreCase("Mala"))
                        ind = 0;

                    results.get(0).setFriendship_score(ind);
                    //Como sincronizar la data o bien cada cuando se debe de sincronizar
                    showLog("Student Result: " + results.get(0).getFriendship_score());

                }

                RealmResults<Realm_viewTables> view = realm.where(Realm_viewTables.class)
                        .equalTo("idGroup", p.getIdGroup())
                        .findAll();

                view.get(0).setFriendship_score(1);
                realm.commitTransaction();

                viewLatoutEdit();


            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                RealmResults<Realm_viewTables> view = realm.where(Realm_viewTables.class)
                        .equalTo("idGroup", p.getIdGroup())
                        .findAll();

                view.get(0).setFriendship_score(0);
                realm.commitTransaction();

                viewLayoutList();
            }
        });

    }

    private void viewLatoutEdit(){
        content.setVisibility(View.INVISIBLE);
        subContenedor.setVisibility(View.VISIBLE);
        int total = 0, ninguna= 0, una= 0, masDeUna=0;

        /*for (int x = 0; x < listStudent.size(); x++){
            RealmResults<Realm_students_indicator> stIndi = realm.where(Realm_students_indicator.class)
                    .equalTo("idStudent",listStudent.get(x).getId())
                    .findAll();

            if (stIndi.get(0).getAbsences_count() == 0)
                ninguna++;
            else
            if (stIndi.get(0).getAbsences_count() == 1)
                una++;
            else
                masDeUna++;

        }

        total = masDeUna + una + ninguna;*/

        //una * 100 / total + rs.getString(R.string.signo)
        tableFileOnePro.setText("Construccion");
        tableFileTwoPor.setText("Construccion");
        tableFileThreePor.setText("Construccion");


    }

    private void viewLayoutList(){
        content.setVisibility(View.VISIBLE);
        subContenedor.setVisibility(View.INVISIBLE);

        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Mala");
        spinnerArray.add("Buena");
        spinnerArray.add("Regular");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        List<String> spi =  new ArrayList<String>();
        spi.add("Regular");
        spi.add("Buena");
        spi.add("Mala");
        ArrayAdapter<String> ada = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, spi);
        ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        List<String> s =  new ArrayList<String>();
        s.add("Buena");
        s.add("Regular");
        s.add("Mala");
        ArrayAdapter<String> ad = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, s);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (int x = 0; x < listStudent.size(); x++){
            RealmResults<Realm_students_indicator> stIndi = realm.where(Realm_students_indicator.class)
                    .equalTo("idStudent",listStudent.get(x).getId())
                    .findAll();

            extIndicator = (Spinner) view.findViewById(x);

            showLog("Valor de indicator: " + stIndi.get(0).getFriendship_score());

            if (stIndi.get(0).getFriendship_score() == 0)
                extIndicator.setAdapter(adapter);
            if (stIndi.get(0).getFriendship_score() == 0.5)
                extIndicator.setAdapter(ada);
            if (stIndi.get(0).getFriendship_score() == 1)
                extIndicator.setAdapter(ad);
        }
    }
    private void showLog(String log){
        Log.v("TabFriendship", log);
    }

    private void messageToast(String toast){
        Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
    }
}
