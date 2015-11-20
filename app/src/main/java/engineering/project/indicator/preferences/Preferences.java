package engineering.project.indicator.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by EduardoMichel on 07/11/2015.
 */
public class Preferences {
    Context context;
    SharedPreferences p;
    SharedPreferences.Editor e;
    String INDICATOR = "Project Indicator";


    public Preferences(Context context){
        this.context = context;
        p = context.getSharedPreferences(INDICATOR, context.MODE_PRIVATE);
        e = context.getSharedPreferences(INDICATOR, context.MODE_PRIVATE).edit();
    }

    //<editor-fold desc="Acces Token">
    public void setAccesToken(String accesToken){
        e.putString("AccesToken",accesToken);
        com(e);
    }

    public String getAccesToken(){
        return p.getString("AccesToken","null acces");
    }

    public void setRefreshToken(String refreshToken){
        e.putString("RefreshToken",refreshToken);
        com(e);
    }

    public String getRefreshToken(){
        return p.getString("RefreshToken","null token");
    }

    public void setExpiresIn(double expiresIn){
        e.putString("ExpiresIn", "" + expiresIn);
        com(e);
    }

    public double getExpiresIn(){
        return Double.parseDouble(p.getString("ExpiresIn","0"));
    }

    public void setTokenType(String tokenType){
        e.putString("TokenType",tokenType);
        com(e);
    }


    public void setAllocation(int allocation){
        e.putInt("Allocation", allocation);
        com(e);
    }

    public int getAllocation(){
        return p.getInt("Allocation",0);
    }

    public void clear(){
        e.clear();
        com(e);
    }
    public String getTokenType(){
        return p.getString("TokenType","null token");
    }
    //</editor-fold>

    public void setMatter(String matter) {
        e.putString("Materia", matter);
        com(e);
    }

    public String getMatter(){
        return p.getString("Materia", "Sin materia");
    }
    private void com(SharedPreferences.Editor e) {
        e.commit();
    }
}
