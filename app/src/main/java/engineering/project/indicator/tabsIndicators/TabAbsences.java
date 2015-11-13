package engineering.project.indicator.tabsIndicators;

import android.content.Context;
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
import java.util.Calendar;

import engineering.project.indicator.R;
import engineering.project.indicator.preferences.Preferences;
import engineering.project.indicator.structureModel.ModelStudent;
import engineering.project.indicator.structureRealm.Realm_students;
import engineering.project.indicator.structureRealm.Realm_students_indicator;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by EduardoMichel on 09/11/2015.
 */
public class TabAbsences extends Fragment {

    View view;
    TableLayout table, head;
    TableRow.LayoutParams layoutFila, layoutId, layoutTexto;
    Resources rs;
    Context context;
    Button save, editar;
    EditText indicator, extIndicator;
    TextView type, count, tablaTitulo, tablaSubTitulo, tablaMas, tablaUno, tablaNinguno, tablaSubMas, tablaSubUno, tablaSubNinguno;
    RelativeLayout content, subContenedor;
    Preferences p;
    Realm realm;
    ArrayList<ModelStudent> listStudent;
    ArrayList<Integer> indicators;

    String  cade = "";
    public TabAbsences(){}

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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                indicators = new ArrayList<Integer>();
                int ind;
                realm.beginTransaction();
                for (int x = 0; x < listStudent.size(); x++) {
                    extIndicator = (EditText) view.findViewById(x);
                    RealmResults<Realm_students_indicator> results = realm.where(Realm_students_indicator.class)
                            .equalTo("idStudent", listStudent.get(x).getId())
                            .findAll();

                    showLog("Indicator results1: " + results.get(0).getAbsences_count());

                    if (extIndicator.getText().toString().equalsIgnoreCase(""))
                        ind = 0;
                    else
                        ind = Integer.parseInt(extIndicator.getText().toString());


                    results.get(0).setAbsences_count(ind);
                    if (results.get(0).getFriendship_score() >= 0 && results.get(0).getMath_score() >=0)
                    results.get(0).setCreateDate(date());


                    RealmResults<Realm_students_indicator> results1 = realm.where(Realm_students_indicator.class)
                            .equalTo("idStudent", listStudent.get(x).getId())
                            .findAll();

                    realm.commitTransaction();
                    showLog("Indicator results1: " + results1.get(0).getAbsences_count());
                }


            }
        });

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

        type.setText("Asistencia");
        count.setText("Sub 30");

        agregarCabecera();
        agregarFilasTabla();

        //subContenedor = (RelativeLayout) view.findViewById(R.id.rlt_subcontenedor_tabla);
        //editar = (Button) view.findViewById(R.id.btn_editar_tabla);
        //tablaTitulo = (TextView) view.findViewById(R.id.txt_asistencia_indicador);
        //tablaSubTitulo = (TextView) view.findViewById(R.id.txt_asistencia_subindicador);
        //tablaMas = (TextView) view.findViewById(R.id.txt_asistencia_masdeuna);
//        tablaUno  = (TextView) view.findViewById(R.id.txt_asistencia_una);
//        tablaNinguno  = (TextView) view.findViewById(R.id.txt_asistencia_ninguna);
//        tablaSubMas  = (TextView) view.findViewById(R.id.txt_asistencia_submasdeuna);
//        tablaSubUno  = (TextView) view.findViewById(R.id.txt_asistencia_subuna);
//        tablaSubNinguno  = (TextView) view.findViewById(R.id.txt_asistencia_subninguna);
        //
        //subContenedor.setVisibility(View.INVISIBLE);
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

        txtNombre.setText(rs.getString(R.string.indicatorAbb));
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
            indicator = new EditText(context);

            txtId.setText(listStudent.get(x).getFirstName()+"\n" +
                          listStudent.get(x).getLastName() + " " + listStudent.get(x).getMotherName());
            txtId.setGravity(Gravity.CENTER_HORIZONTAL);
            txtId.setBackgroundResource(R.drawable.celda);
            txtId.setLayoutParams(layoutId);
            txtId.setTextColor(Color.BLACK);

            indicator.setGravity(Gravity.CENTER_HORIZONTAL);
            indicator.setId(x);
            indicator.setBackgroundResource(R.drawable.celda);
            indicator.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            indicator.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
            indicator.setLayoutParams(layoutTexto);

            fila.addView(txtId);
            fila.addView(indicator);
            table.addView(fila);
        }
    }
    //</editor-fold>
    public String  date() {
        Calendar calendario = Calendar.getInstance();
        return calendario.get(Calendar.YEAR) +"-" + calendario.get(Calendar.MONTH) + "-" +  calendario.get(Calendar.DAY_OF_MONTH) + " "+
                calendario.get(Calendar.HOUR_OF_DAY) + ":" + calendario.get(Calendar.MINUTE) + ":" + calendario.get(Calendar.SECOND);
    }

    private void showLog(String log){
        Log.v("TabAbsences",log);
    }

    private void messageToast(String toast){
        Toast.makeText(context, toast, Toast.LENGTH_LONG ).show();
    }

}

