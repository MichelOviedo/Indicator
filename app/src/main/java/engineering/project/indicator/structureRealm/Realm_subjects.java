package engineering.project.indicator.structureRealm;

import io.realm.RealmObject;

/**
 * Created by EduardoMichel on 07/11/2015.
 */
public class Realm_subjects extends RealmObject {

    /**
     * TEC
     * Tecnologias
     */
    private int id, idAllocation, idGrade;
    private String abbrevition, title;

    public String getAbbrevition() {
        return abbrevition;
    }

    public void setAbbrevition(String abbrevition) {
        this.abbrevition = abbrevition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAllocation() {
        return idAllocation;
    }

    public void setIdAllocation(int idAllocation) {
        this.idAllocation = idAllocation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIdGrade() {
        return idGrade;
    }

    public void setIdGrade(int idGrade) {
        this.idGrade = idGrade;
    }
}
