package engineering.project.indicator.tabsIndicators;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import engineering.project.indicator.MainActivity;
import engineering.project.indicator.R;
import engineering.project.indicator.preferences.Preferences;
import engineering.project.indicator.structureModel.ModelStudent;
import engineering.project.indicator.structureRealm.Realm_allocations;
import engineering.project.indicator.structureRealm.Realm_binnacle;
import engineering.project.indicator.structureRealm.Realm_evaluation_indicator;
import engineering.project.indicator.structureRealm.Realm_school_groups;
import engineering.project.indicator.structureRealm.Realm_students;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by EduardoMichel on 13/11/2015.
 */
public class TabReading extends Fragment {


    View view;
    TableLayout table, head;
    TableRow.LayoutParams layoutFila, layoutId, layoutTexto;
    Resources rs;
    Context context;
    Button save, edit;
    EditText indicator, extIndicator;
    TextView type, count, tableTitle, tablePorcentage, tableFileOne, tableFileOnePro, tableFileTwo, tableFileTwoPor, tableFileThree, tableFileThreePor, titleEdit, subEdit;
    RelativeLayout content, subContenedor;
    Preferences p;
    Realm realm;
    ArrayList<ModelStudent> listStudent;
    ArrayList<Integer> indicators;
    int isFinish;
    int idEva;

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

    private void builder() {
        content = (RelativeLayout) view.findViewById(R.id.rltContentViewTable);
        p = new Preferences(context);
        rs = context.getResources();
        save = (Button) view.findViewById(R.id.btnSaveTab);
        realm = Realm.getInstance(context);

        head = (TableLayout) view.findViewById(R.id.tblHead);
        table = (TableLayout) view.findViewById(R.id.tblContentTable);
        type = (TextView) view.findViewById(R.id.txvTitleTab);
        count = (TextView) view.findViewById(R.id.txvSubTitleTab);

        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        layoutId = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        layoutTexto = new TableRow.LayoutParams(160, TableRow.LayoutParams.MATCH_PARENT);

        type.setText(rs.getString(R.string.indicatorReading));
        count.setText(rs.getString(R.string.indicatorReadingSub));

        agregarCabecera();
        agregarFilasTabla();

        subContenedor = (RelativeLayout) view.findViewById(R.id.rltContentViewEditTable);
        edit = (Button) view.findViewById(R.id.btnEdit);
        tableTitle = (TextView) view.findViewById(R.id.txvTableTitle);
        tablePorcentage = (TextView) view.findViewById(R.id.txvTablePercentage);
        tableFileOne = (TextView) view.findViewById(R.id.txvTableFileOne);
        tableFileOnePro = (TextView) view.findViewById(R.id.txvTableFileOnePor);
        tableFileTwo = (TextView) view.findViewById(R.id.txvTableFileTwo);
        tableFileTwoPor = (TextView) view.findViewById(R.id.txvTableFileTwoPor);
        tableFileThree = (TextView) view.findViewById(R.id.txvTableFileThree);
        tableFileThreePor = (TextView) view.findViewById(R.id.txvTableFileThreePor);
        titleEdit = (TextView) view.findViewById(R.id.txvTlitleEdit);
        subEdit = (TextView) view.findViewById(R.id.txvSubEdit);

        titleEdit.setText(rs.getString(R.string.lecEdit));
        subEdit.setText(rs.getString(R.string.subEdit));
        tableTitle.setText(rs.getString(R.string.titleTab));
        tablePorcentage.setText(rs.getString(R.string.promedio));
        tableFileOne.setText(rs.getString(R.string.tabBue));
        tableFileTwo.setText(rs.getString(R.string.tabReg));
        tableFileThree.setText(rs.getString(R.string.tabMala));
        tableFileOnePro.setText("");
        tableFileTwoPor.setText("");
        tableFileThreePor.setText("");

        RealmResults<Realm_evaluation_indicator> evaluation = realm.where(Realm_evaluation_indicator.class)
                .equalTo("idIndicator", 2)
                .equalTo("idAllocation", p.getAllocation())
                .findAll();


        if (evaluation.size() > 0)
            viewLatoutEdit();
        else
            viewLayoutList();

    }

    //<editor-fold desc="Tabla dinamica">
    public void agregarCabecera() {
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

        txtNombre.setText(rs.getString(R.string.indicatorR));
        txtNombre.setTextColor(Color.BLACK);
        txtNombre.setGravity(Gravity.CENTER_HORIZONTAL);
        txtNombre.setBackgroundResource(R.drawable.cabecera);
        txtNombre.setLayoutParams(layoutTexto);

        fila.addView(txtId);
        fila.addView(txtNombre);
        head.addView(fila);

    }

    public void agregarFilasTabla() {
        listStudent = new ArrayList<ModelStudent>();
        ModelStudent student;

        /*List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Buena");
        spinnerArray.add("Regular");
        spinnerArray.add("Mala");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

        RealmResults<Realm_allocations> all = realm.where(Realm_allocations.class)
                .equalTo("id", p.getAllocation())
                .findAll();
        RealmResults<Realm_school_groups> group = realm.where(Realm_school_groups.class)
                .equalTo("id", all.get(0).getGroupId())
                .findAll();
        RealmResults<Realm_students> studentses = realm.where(Realm_students.class)
                .equalTo("gruopId", group.get(0).getId())
                .findAll();

        for (int x = 0; x < studentses.size(); x++) {
            student = new ModelStudent();
            student.setId(studentses.get(x).getId());
            student.setLastName(studentses.get(x).getLastName());
            student.setFirstName(studentses.get(x).getFirstName());
            student.setMotherName(studentses.get(x).getMotherName());
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
            indicator = new EditText(context);

            txtId.setText(listStudent.get(x).getFirstName() + "\n" +
                    listStudent.get(x).getLastName() + " " + listStudent.get(x).getMotherName());
            txtId.setGravity(Gravity.CENTER_HORIZONTAL);
            txtId.setBackgroundResource(R.drawable.celda);
            txtId.setLayoutParams(layoutId);
            txtId.setTextColor(Color.BLACK);

            indicator.setGravity(Gravity.CENTER_HORIZONTAL);
            indicator.setId(x);
            indicator.setBackgroundResource(R.drawable.celda);
            indicator.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            indicator.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
            //indicator.setAdapter(adapter);
            indicator.setLayoutParams(layoutTexto);

            fila.addView(txtId);
            fila.addView(indicator);
            table.addView(fila);
        }
    }
    //</editor-fold>

    private void userButton() {
        save.setOnClickListener(new View.OnClickListener() {

            //como cambiar de fragment v4
            @Override
            public void onClick(View v) {
                indicators = new ArrayList<Integer>();
                int ind = 0;
                realm.beginTransaction();
                for (int x = 0; x < listStudent.size(); x++) {

                    extIndicator = (EditText) view.findViewById(x);

                    if (extIndicator.getText().toString().equalsIgnoreCase(""))
                        ind = 0;
                    else
                        ind = Integer.parseInt(extIndicator.getText().toString());
                    //// asistencia paticipacion desempenio convivencia lectora matematica
                    RealmResults<Realm_evaluation_indicator> asistencia = realm.where(Realm_evaluation_indicator.class)
                            .equalTo("idAllocation", p.getAllocation())
                            .equalTo("idStudent", listStudent.get(x).getId())
                            .equalTo("idIndicator", 1)
                            .findAll();
                    RealmResults<Realm_evaluation_indicator> participacion = realm.where(Realm_evaluation_indicator.class)
                            .equalTo("idAllocation", p.getAllocation())
                            .equalTo("idStudent", listStudent.get(x).getId())
                            .equalTo("idIndicator", 2)
                            .findAll();
                    RealmResults<Realm_evaluation_indicator> despempenio = realm.where(Realm_evaluation_indicator.class)
                            .equalTo("idAllocation", p.getAllocation())
                            .equalTo("idStudent", listStudent.get(x).getId())
                            .equalTo("idIndicator", 3)
                            .findAll();
                    RealmResults<Realm_evaluation_indicator> convivencia = realm.where(Realm_evaluation_indicator.class)
                            .equalTo("idAllocation", p.getAllocation())
                            .equalTo("idStudent", listStudent.get(x).getId())
                            .equalTo("idIndicator", 4)
                            .findAll();
                    RealmResults<Realm_evaluation_indicator> lectura = realm.where(Realm_evaluation_indicator.class)
                            .equalTo("idAllocation", p.getAllocation())
                            .equalTo("idStudent", listStudent.get(x).getId())
                            .equalTo("idIndicator", 5)
                            .findAll();
                    RealmResults<Realm_evaluation_indicator> matematicas = realm.where(Realm_evaluation_indicator.class)
                            .equalTo("idAllocation", p.getAllocation())
                            .equalTo("idStudent", listStudent.get(x).getId())
                            .equalTo("idIndicator", 6)
                            .findAll();
                    RealmResults<Realm_allocations> allocations = realm.where(Realm_allocations.class)
                            .equalTo("id", p.getAllocation())
                            .findAll();

                    if (asistencia.size() > 0 && convivencia.size() > 0 && despempenio.size() > 0 && participacion.size() > 0)
                        isFinish = 1;
                    else
                        isFinish = 0;

                    allocations.get(0).setIs_finish(isFinish);

                    if (lectura.size() > 0) {
                        idEva = lectura.get(0).getIdAllocation();
                        lectura.get(0).setValue(ind);
                    }
                    else {
                        idEva = getPk();
                        Realm_evaluation_indicator evaluations = realm.createObject(Realm_evaluation_indicator.class);
                        evaluations.setIdPk(idEva);
                        evaluations.setIdStudent(listStudent.get(x).getId());
                        evaluations.setIdAllocation(p.getAllocation());
                        evaluations.setIdIndicator(5);
                        evaluations.setValue(ind);
                    }
                    saveSync(idEva);
                }
                realm.commitTransaction();
                goodJob(rs.getString(R.string.contentSave) + " '" + rs.getString(R.string.indLect) + "'" + "",
                        isFinish);
                viewLatoutEdit();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(rs.getString(R.string.seguro))
                        .setContentText(rs.getString(R.string.seguroContent))
                        .setConfirmText(rs.getString(R.string.seguroSi))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                viewLayoutList();
                            }
                        })
                        .setCancelText(rs.getString(R.string.seguroNo))
                        .show();
            }
        });

    }

    private void viewLatoutEdit() {
        content.setVisibility(View.INVISIBLE);
        subContenedor.setVisibility(View.VISIBLE);
        int total = 0, buena = 0, regular = 0, mala = 0;

        for (int x = 0; x < listStudent.size(); x++) {
            RealmResults<Realm_evaluation_indicator> indicator = realm.where(Realm_evaluation_indicator.class)
                    .equalTo("idStudent", listStudent.get(x).getId())
                    .equalTo("idAllocation", p.getAllocation())
                    .equalTo("idIndicator", 5)
                    .findAll();

            if (indicator.get(0).getValue() >=90 )
                buena++;
            else
            if (indicator.get(0).getValue() >= 70)
                regular++;
            else
                mala++;

        }

        total = buena + regular + mala;

        tableFileOnePro.setText(buena * 100 / total + rs.getString(R.string.signo));
        tableFileTwoPor.setText(regular * 100 / total + rs.getString(R.string.signo));
        tableFileThreePor.setText(mala * 100 / total + rs.getString(R.string.signo));


    }

    private void viewLayoutList() {
        content.setVisibility(View.VISIBLE);
        subContenedor.setVisibility(View.INVISIBLE);

       /* List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Mala");
        spinnerArray.add("Buena");
        spinnerArray.add("Regular");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        List<String> spi = new ArrayList<String>();
        spi.add("Regular");
        spi.add("Buena");
        spi.add("Mala");
        ArrayAdapter<String> ada = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, spi);
        ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        List<String> s = new ArrayList<String>();
        s.add("Buena");
        s.add("Regular");
        s.add("Mala");
        ArrayAdapter<String> ad = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, s);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

        for (int x = 0; x < listStudent.size(); x++) {
            RealmResults<Realm_evaluation_indicator> stIndi = realm.where(Realm_evaluation_indicator.class)
                    .equalTo("idAllocation", p.getAllocation())
                    .equalTo("idStudent", listStudent.get(x).getId())
                    .equalTo("idIndicator", 5)
                    .findAll();

            extIndicator = (EditText) view.findViewById(x);

            if (stIndi.size() > 0)
                extIndicator.setText("" + stIndi.get(0).getValue());
            else
                extIndicator.setText("100");
        }
    }

    private int getPk() {
        RealmResults<Realm_evaluation_indicator> i = realm.where(Realm_evaluation_indicator.class)
                .notEqualTo("idPk", -1)
                .findAll();

        return i.size() + 1;
    }

    private int getBinnaclePK(){
        RealmResults<Realm_binnacle> size = realm.where(Realm_binnacle.class)
                .notEqualTo("idPk", -1)
                .findAll();

        return size.size() + 1;
    }

    private void saveSync(int idEvaluation){
        Realm_binnacle binnacle = realm.createObject(Realm_binnacle.class);
        binnacle.setCreate(secondDate());
        binnacle.setUpServer(0);
        binnacle.setIdEvaluation(idEvaluation);
        binnacle.setIdPk(getBinnaclePK());
    }
    public long secondDate() {
        return System.currentTimeMillis() / 1000;//Milisegundos actual del sismtema
    }
    private void goodJob(String content, final int retorna) {

        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(rs.getString(R.string.goodJob))
                .setContentText(content)
                .setConfirmText(rs.getString(R.string.next))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        if (retorna > 0)
                            loadList();
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();

    }

    private void loadList() {
        Intent i = new Intent(context, MainActivity.class);
        // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    private void showLog(String log) {
        Log.v("TabReading", log);
    }

    private void messageToast(String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
    }
}