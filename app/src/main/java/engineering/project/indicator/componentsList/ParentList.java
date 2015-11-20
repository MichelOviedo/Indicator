package engineering.project.indicator.componentsList;

import java.util.ArrayList;

/**
 * Created by EduardoMichel on 08/11/2015.
 */
public class ParentList {
    private String mTitle;
    private String porcentaje;
    private ArrayList<ArrayChildren> mArrayChildren;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public ArrayList<ArrayChildren> getArrayChildren() {
        return mArrayChildren;
    }

    public void setArrayChildren(ArrayList<ArrayChildren> arrayChildren) {
        mArrayChildren = arrayChildren;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }
}
