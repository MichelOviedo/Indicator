package engineering.project.indicator.webService;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import engineering.project.indicator.MainActivity;
import engineering.project.indicator.R;
import engineering.project.indicator.preferences.Preferences;
import engineering.project.indicator.structureRealm.Realm_allocations;
import engineering.project.indicator.structureRealm.Realm_faculty_member;
import engineering.project.indicator.structureRealm.Realm_grades;
import engineering.project.indicator.structureRealm.Realm_indicator_details;
import engineering.project.indicator.structureRealm.Realm_school_groups;
import engineering.project.indicator.structureRealm.Realm_students;
import engineering.project.indicator.structureRealm.Realm_subjects;
import engineering.project.indicator.structureRealm.Realm_user;
import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by EduardoMichel on 07/11/2015.
 */
public class WebService implements Response.Listener<JSONObject>, Response.ErrorListener {
    String URL = "http://nkubunt.cloudapp.net:3000/";
    String BASIC_AUTHORIZATION = "Basic d2ViY2xpZW50OnBhc3N3b3Jk",  GRANT_TYPE = "password";
    Context context;
    Preferences p;
    Resources rs;
    ProgressDialog pd;
    Realm realm;

    public WebService(Context context, String url){
        this.context = context;
        rs = context.getResources();
        p = new Preferences(context);
        this.URL += url;
    }

    public void getToken(final String user, final String password){

        if (validateConexion()){

            pd = ProgressDialog.show(
                    context, rs.getString(R.string.validateUserTitle),
                    rs.getString(R.string.validateUserMessage));

            StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>(){
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject json = new JSONObject(response);
                                p.setAccesToken(json.getString("access_token"));
                                p.setRefreshToken(json.getString("refresh_token"));
                                p.setExpiresIn(json.getDouble("expires_in"));
                                p.setTokenType(json.getString("token_type"));

                                pd.dismiss();

                                new WebService(context, "api/account/me").getProfile();

                            }catch (Exception e){
                                //showLogError("En Exception" + e.getCause().toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                pd.dismiss();
                            }catch (Exception e){
                                //showLogError("Error en progressDialog");
                            }
                            messageSweet(1);
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", BASIC_AUTHORIZATION);

                    return headers;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap<String, String> post = new HashMap<String, String>();
                    post.put("username", user );
                    post.put("password", password);
                    post.put("grant_type", GRANT_TYPE);

                    return post;
                }
            };
            volleyRequest(postRequest);
        }
        else
            messageSweet(2);

    }

    public void getProfile(){
        realm = Realm.getInstance(context);
        pd = ProgressDialog.show(
                context, rs.getString(R.string.validatePorfileTitle),
                rs.getString(R.string.validatePorfileMessage));

        StringRequest getRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject json = new JSONObject(response);

                            realm.beginTransaction();
                            Realm_faculty_member faculty_member = realm.createObject(Realm_faculty_member.class);
                            faculty_member.setUserId(json.getInt("id"));
                            faculty_member.setFisrtName(json.getString("first_name"));
                            faculty_member.setLastName(json.getString("last_name"));
                            faculty_member.setTitle(json.getString("title"));
                            faculty_member.setEmail(json.getString("email"));
                            faculty_member.setContact_number(json.getString("contact_number"));

                            JSONArray array = new JSONArray(json.getString("allocations"));

                            for (int x = 0; x < array.length(); x++){
                                JSONObject jsonObject = array.getJSONObject(x);
                                int idAllocation = jsonObject.getInt("id");

                                Realm_allocations allocations = realm.createObject(Realm_allocations.class);
                                allocations.setId(idAllocation);
                                allocations.setIs_finish(0);

                                JSONObject jsonSubject = new JSONObject(jsonObject.getString("subject"));
                                    Realm_subjects subjects = realm.createObject(Realm_subjects.class);
                                    subjects.setId(jsonSubject.getInt("id"));
                                    subjects.setAbbrevition(jsonSubject.getString("abbreviation"));
                                    subjects.setTitle(jsonSubject.getString("title"));
                                    subjects.setIdAllocation(idAllocation);
                                showLogError("ID OBTENIDO; "+jsonSubject.getInt("id") + " " + idAllocation);

                                allocations.setSubjectId(jsonSubject.getInt("id"));

                                JSONObject jsonGroup = new JSONObject(jsonObject.getString("group"));
                                    Realm_school_groups groups = realm.createObject(Realm_school_groups.class);
                                    groups.setId(jsonGroup.getInt("id"));
                                    groups.setGroupName(jsonGroup.getString("group_name"));
                                    groups.setTotalStudent(jsonGroup.getInt("total_students"));
                                allocations.setGroupId(jsonGroup.getInt("id"));


                                JSONObject jsonGrade = new JSONObject(jsonObject.getString("grade"));
                                    Realm_grades grades = realm.createObject(Realm_grades.class);
                                    grades.setId(jsonGrade.getInt("id"));
                                    grades.setGradeNumbre(jsonGrade.getInt("grade_number"));
                                    grades.setTitle(jsonGrade.getString("title"));

                                    subjects.setIdGrade(jsonGrade.getInt("id"));
                            }

                            JSONObject jsonUser = new JSONObject(json.getString("user"));
                            Realm_user user = realm.createObject(Realm_user.class);
                            user.setUsername(jsonUser.getString("username"));
                            user.setId(jsonUser.getInt("id"));
                            user.setRole(jsonUser.getString("role"));

                            realm.commitTransaction();
                            pd.dismiss();

                            new WebService(context, "api/students/in-group/").insertStudent();

                        }catch (Exception e){
                            ////showLogError("En Exception" + e.getCause().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            pd.dismiss();
                            p.clear();
                            Toast.makeText(context, "Usuario incorrecto!, Vuelva a intentarlo.", Toast.LENGTH_LONG).show();
                            ////showLogError("Error al obtener el perfil");
                        }catch (Exception e){
                            ////showLogError("Error en progressDialog");
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", p.getTokenType() + " " + p.getAccesToken());

                return headers;
            }};
        volleyRequest(getRequest);
    }

    private void insertStudent(){
        realm = Realm.getInstance(context);
        pd = ProgressDialog.show(
                context, "Cargando grupos",
                "Esto puedo tomar unos segundos...");

        RealmResults<Realm_school_groups> group = realm.where(Realm_school_groups.class)
                .notEqualTo("id", -1)
                .findAll();

        int cont,num,i,j=0,k,z=0 ;
        ArrayList<Integer> v_aux = new ArrayList<Integer>();
        ArrayList<Integer> idGroups = new ArrayList<Integer>();

        for (i=0;i<group.size();i++) {
            cont=0;
            num=group.get(i).getId();
            v_aux.add(num);
            j++;
            for (k=0;k<v_aux.size();k++)
                if ( v_aux.get(k) == num )
                    cont++;

            if ( cont == 1 ) {
                idGroups.add(num);
            }
        }


        for (int x = 0; x < idGroups.size(); x++)
            insetStudentInGroup(URL + idGroups.get(x));

        loadDetailsIndicator();
        loadMain();
        pd.dismiss();
    }

    private void insetStudentInGroup(String url){
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject json = new JSONObject(response);

                            if (json.getBoolean("success")){
                                JSONArray jsonArray = new JSONArray(json.getString("data"));

                                realm.beginTransaction();
                                for (int x = 0; x < jsonArray.length(); x++) {
                                    JSONObject jsonStudent = jsonArray.getJSONObject(x);
                                        Realm_students students = realm.createObject(Realm_students.class);
                                        students.setId(jsonStudent.getInt("id"));
                                        students.setFirstName(jsonStudent.getString("first_name"));
                                        students.setLastName(jsonStudent.getString("last_name"));
                                        students.setMotherName(jsonStudent.getString("mothers_name"));
                                        students.setGruopId(jsonStudent.getInt("school_group_id"));
                                        students.setGender(jsonStudent.getString("gender"));
                                    }
                                realm.commitTransaction();
                            }


                        }catch (Exception e){
                            ////showLogError("En Exception" + e.getCause().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {

                        }catch (Exception e){
                            ////showLogError("Error en progressDialog");
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", p.getTokenType() + " " + p.getAccesToken());

                return headers;
            }};

        volleyRequest(getRequest);
    }

    private void insertEvaluatio(){
        //api/evaluations/bimester/1/student/4
    }

    private void volleyRequest(StringRequest stringRequest){
        Volley.newRequestQueue(context).add(stringRequest);
    }


    private boolean validateConexion(){
        ConnectivityManager conn = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conn.getActiveNetworkInfo() != null &&
                conn.getActiveNetworkInfo().isAvailable() &&
                conn.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }

    private void loadMain(){
        ////showLogError("LoadMain");
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    private void loadDetailsIndicator(){
        realm.beginTransaction();

        for (int x = 1; x < 7; x++){
            Realm_indicator_details id =  realm.createObject(Realm_indicator_details.class);
            id.setIdPk(x);
            switch (x){
                case 1:
                    id.setTilte(rs.getString(R.string.indAsistencia));
                    break;
                case 2:
                    id.setTilte(rs.getString(R.string.indParti));
                    break;
                case 3:
                    id.setTilte(rs.getString(R.string.indDesempe));
                    break;
                case 4:
                    id.setTilte(rs.getString(R.string.indConvi));
                    break;
                case 5:
                    id.setTilte(rs.getString(R.string.indLect));
                    break;
                case 6:
                    id.setTilte(rs.getString(R.string.indMate));
                    break;
            }
        }
        realm.commitTransaction();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    //<editor-fold desc="Mensajes">
    private void messageToast(String toast){
        Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
    }

    private void messageSweet(int exec){
        switch (exec){
            case 1:
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(rs.getString(R.string.errorMessage))
                        .setContentText(rs.getString(R.string.errorLogin))
                        .setConfirmText(rs.getString(R.string.errorConfirm))
                        .show();
                break;

            case 2:

                new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setTitleText(rs.getString(R.string.errorMessage))
                        .setContentText(rs.getString(R.string.errorConexion))
                        .setConfirmText(rs.getString(R.string.errorConfirm))
                        .setCustomImage(R.mipmap.wifi_conexion)
                        .show();


                break;
        }
    }

    private void showLog(String log){
        Log.v("WebService", log);
    }

    private void showLogError(String error){
        Log.e("WebService",error);
    }
    //</editor-fold>
}