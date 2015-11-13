package engineering.project.indicator.componentsList;

import java.util.ArrayList;

/**
 * Created by EduardoMichel on 08/11/2015.
 */
public class ParentList {
    private String mTitle;
    private ArrayList<String> mArrayChildren;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public ArrayList<String> getArrayChildren() {
        return mArrayChildren;
    }

    public void setArrayChildren(ArrayList<String> arrayChildren) {
        mArrayChildren = arrayChildren;
    }
}
