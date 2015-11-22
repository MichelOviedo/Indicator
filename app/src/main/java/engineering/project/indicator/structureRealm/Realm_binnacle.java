package engineering.project.indicator.structureRealm;

import io.realm.RealmObject;
/**
 * Created by EduardoMichel on 21/11/2015.
 */
public class Realm_binnacle extends RealmObject {
    private int idPk, idEvaluation;
    private long create, upServer;

    public long getCreate() {
        return create;
    }

    public void setCreate(long create) {
        this.create = create;
    }

    public int getIdPk() {
        return idPk;
    }

    public void setIdPk(int idPk) {
        this.idPk = idPk;
    }

    public long getUpServer() {
        return upServer;
    }

    public void setUpServer(long upServer) {
        this.upServer = upServer;
    }

    public int getIdEvaluation() {
        return idEvaluation;
    }

    public void setIdEvaluation(int idEvaluation) {
        this.idEvaluation = idEvaluation;
    }
}
