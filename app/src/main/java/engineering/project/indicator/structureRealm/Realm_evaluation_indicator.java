package engineering.project.indicator.structureRealm;

import io.realm.RealmObject;

/**
 * Created by EduardoMichel on 09/11/2015.
 */
public class Realm_evaluation_indicator extends RealmObject {

    private int idPk, idStudent, idIndicator, idAllocation, value;

    public int getIdPk() {
        return idPk;
    }

    public void setIdPk(int idPk) {
        this.idPk = idPk;
    }

    public int getIdAllocation() {
        return idAllocation;
    }

    public void setIdAllocation(int idAllocation) {
        this.idAllocation = idAllocation;
    }

    public int getIdIndicator() {
        return idIndicator;
    }

    public void setIdIndicator(int idIndicator) {
        this.idIndicator = idIndicator;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
