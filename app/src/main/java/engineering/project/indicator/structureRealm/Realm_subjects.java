package engineering.project.indicator.structureRealm;

import io.realm.RealmObject;

/**
 * Created by EduardoMichel on 07/11/2015.
 */
public class Realm_subjects extends RealmObject {
    private int id, idGrade;
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

    public int getIdGrade() {
        return idGrade;
    }

    public void setIdGrade(int idGrade) {
        this.idGrade = idGrade;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
