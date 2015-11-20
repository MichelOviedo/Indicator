package engineering.project.indicator.structureRealm;

import io.realm.RealmObject;

/**
 * Created by EduardoMichel on 07/11/2015.
 */
public class Realm_school_groups extends RealmObject {
    private int id, totalStudent, idAllocation;
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalStudent() {
        return totalStudent;
    }

    public void setTotalStudent(int totalStudent) {
        this.totalStudent = totalStudent;
    }

    public int getIdAllocation() {
        return idAllocation;
    }

    public void setIdAllocation(int idAllocation) {
        this.idAllocation = idAllocation;
    }
}
