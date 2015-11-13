package engineering.project.indicator.structureModel;

/**
 * Created by EduardoMichel on 08/11/2015.
 */
public class ModelList {
    int gradeNumbre, allStudents;
    String titleGradeNumbre,group , matterName, matterAbbre;

    public ModelList() {
    }

    public int getAllStudents() {
        return allStudents;
    }

    public void setAllStudents(int allStudents) {
        this.allStudents = allStudents;
    }

    public int getGradeNumbre() {
        return gradeNumbre;
    }

    public void setGradeNumbre(int gradeNumbre) {
        this.gradeNumbre = gradeNumbre;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getMatterAbbre() {
        return matterAbbre;
    }

    public void setMatterAbbre(String matterAbbre) {
        this.matterAbbre = matterAbbre;
    }

    public String getMatterName() {
        return matterName;
    }

    public void setMatterName(String matterName) {
        this.matterName = matterName;
    }

    public String getTitleGradeNumbre() {
        return titleGradeNumbre;
    }

    public void setTitleGradeNumbre(String titleGradeNumbre) {
        this.titleGradeNumbre = titleGradeNumbre;
    }
}
