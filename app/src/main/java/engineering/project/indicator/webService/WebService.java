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

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import engineering.project.indicator.MainActivity;
import engineering.project.indicator.R;
import engineering.project.indicator.preferences.Preferences;
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
    String ID_STUDENT;

    public WebService(Context context, String url){
        this.context = context;
        rs = context.getResources();
        p = new Preferences(context);
        this.URL += url;
    }

    public WebService(Context context, String url,String ID_STUDENT){
        this.context = context;
        rs = context.getResources();
        p = new Preferences(context);
        this.URL += url;
        this.ID_STUDENT = ID_STUDENT;
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
                                showLogError("En Exception" + e.getCause().toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                pd.dismiss();
                            }catch (Exception e){
                                showLogError("Error en progressDialog");
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

                                Realm_allocations allocations = realm.createObject(Realm_allocations.class);
                                allocations.setId(jsonObject.getInt("id"));
                                allocations.setTeacherId(json.getInt("id"));

                                JSONObject jsonSubject = new JSONObject(jsonObject.getString("subject"));
                                allocations.setSubjectId(jsonSubject.getInt("id"));
                                Realm_subjects subjects = realm.createObject(Realm_subjects.class);
                                subjects.setId(jsonSubject.getInt("id"));
                                subjects.setAbbrevition(jsonSubject.getString("abbreviation"));
                                subjects.setTitle(jsonSubject.getString("title"));

                                JSONObject jsonGroup = new JSONObject(jsonObject.getString("group"));
                                allocations.setGroupId(jsonGroup.getInt("id"));
                                Realm_school_groups school_groups = realm.createObject(Realm_school_groups.class);
                                school_groups.setId(jsonGroup.getInt("id"));
                                school_groups.setGroupName(jsonGroup.getString("group_name"));
                                school_groups.setTotalStudent(jsonGroup.getInt("total_students"));

                                JSONObject jsonGrade = new JSONObject(jsonObject.getString("grade"));
                                school_groups.setIdGrade(jsonGrade.getInt("id"));
                                subjects.setIdGrade(jsonGrade.getInt("id"));
                                Realm_grades grades = realm.createObject(Realm_grades.class);
                                allocations.setGradeId(jsonGrade.getInt("id"));
                                grades.setId(jsonGrade.getInt("id"));
                                grades.setTitle(jsonGrade.getString("title"));
                                grades.setGradeNumbre(jsonGrade.getInt("grade_number"));

                                new WebService(context, "api/students/in-group/" + jsonGroup.getInt("id") ,
                                        grades.getGradeNumbre() + "" + school_groups.getGroupName() + ", " + subjects.getTitle() + "" )
                                        .insertStudent();
                            }


                            JSONObject jsonUser = new JSONObject(json.getString("user"));
                            Realm_user user = realm.createObject(Realm_user.class);
                            user.setUsername(jsonUser.getString("username"));
                            user.setId(jsonUser.getInt("id"));
                            user.setRole(jsonUser.getString("role"));

                            realm.commitTransaction();
                            pd.dismiss();

                            loadMain();

                        }catch (Exception e){
                            showLogError("En Exception" + e.getCause().toString());
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
                            showLogError("Error al obtener el perfil");
                        }catch (Exception e){
                            showLogError("Error en progressDialog");
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

        StringRequest getRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject json = new JSONObject(response);

                            if (json.getBoolean("sucess")){
                                JSONArray jsonArray = new JSONArray(json.getString("data"));

                                for (int x = 0; x < jsonArray.length(); x++) {
                                    JSONObject jsonStudent = jsonArray.getJSONObject(x);

                                    realm.beginTransaction();
                                    Realm_students students = realm.createObject(Realm_students.class);
                                    students.setId(jsonStudent.getInt("id"));
                                    students.setFirstName(jsonStudent.getString("first_name"));
                                    students.setLastName(jsonStudent.getString("last_name"));
                                    students.setMotherName(jsonStudent.getString("mothers_name"));
                                    students.setGender("gender");
                                    students.setGruopId(jsonStudent.getInt("school_group_id"));
                                    students.setIdInformal(ID_STUDENT);

                                    Realm_students_indicator indicator = realm.createObject(Realm_students_indicator.class);
                                    indicator.setAbsences_count(-1);
                                    indicator.setFriendship_score(-1);
                                    indicator.setMath_score(-1);
                                    indicator.setParticipation_score(-1);
                                    indicator.setPerformance_score(-1);
                                    indicator.setIdStudent(jsonStudent.getInt("id"));
                                    indicator.setSubject_id(ID_STUDENT);

                                   /* showLog("1");
                                    showLog("ID: " + ID_STUDENT.substring(0, 1));
                                    String num = ID_STUDENT.substring(0,1);
                                    showLog("2");
                                    int number = Integer.parseInt(num);
                                    showLog("3");
                                    Realm_progress p = realm.createObject(Realm_progress.class);
                                    p.setGroupName(ID_STUDENT);
                                    p.setNumber(number);
                                    p.setFinish(0);*/


                                    showLogError("Este es el id de la viewTab: " + ID_STUDENT);
                                    Realm_viewTables viewTables = realm.createObject(Realm_viewTables.class);
                                    viewTables.setIdGroup(ID_STUDENT);
                                    viewTables.setAbsences_count(-1);
                                    viewTables.setFriendship_score(-1);
                                    viewTables.setMath_score(-1);
                                    viewTables.setParticipation_score(-1);
                                    viewTables.setReading_score(-1);
                                    viewTables.setPerformance_score(-1);
                                    realm.commitTransaction();
                                }

                            }
                            else{
                                showLogError("Error en: " + ID_STUDENT);
                            }

                        }catch (Exception e){
                            showLogError("En Exception" + e.getCause().toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {

                        }catch (Exception e){
                            showLogError("Error en progressDialog");
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
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
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
        Log.v("WebService",log);
    }

    private void showLogError(String error){
        Log.e("WebService",error);
    }
    //</editor-fold>
}