package engineering.project.indicator.structureRealm;

import io.realm.RealmObject;

/**
 * Created by EduardoMichel on 19/11/2015.
 */
public class Realm_indicator_details extends RealmObject{
    private int idPk;
    private String tilte;

    public int getIdPk() {
        return idPk;
    }

    public void setIdPk(int idPk) {
        this.idPk = idPk;
    }

    public String getTilte() {
        return tilte;
    }

    public void setTilte(String tilte) {
        this.tilte = tilte;
    }
}
