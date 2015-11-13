package engineering.project.indicator.structureRealm;

import io.realm.RealmObject;

/**
 * Created by EduardoMichel on 07/11/2015.
 */
public class Realm_allocations extends RealmObject {

    private int id;
    private int idInternal;
    private int groupId;
    private int subjectId;
    private int teacherId;
    private String where;

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public int getGradeId() {
        return gradeId;
    }

    public int getIdInternal() {
        return idInternal;
    }

    public void setIdInternal(int idInternal) {
        this.idInternal = idInternal;
    }
    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    private int gradeId;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
}
