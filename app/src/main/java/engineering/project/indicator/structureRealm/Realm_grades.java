package engineering.project.indicator.structureRealm;

import io.realm.RealmObject;

/**
 * Created by EduardoMichel on 07/11/2015.
 */
public class Realm_grades extends RealmObject {
    /***
     * ID del grade
     * Numero de grado
     * totle 1 Primaria
     */
    private int id, gradeNumbre;
    private String title;

    public int getGradeNumbre() {
        return gradeNumbre;
    }

    public void setGradeNumbre(int gradeNumbre) {
        this.gradeNumbre = gradeNumbre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
