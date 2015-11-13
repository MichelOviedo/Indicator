package engineering.project.indicator.structureRealm;

import io.realm.RealmObject;

/**
 * Created by EduardoMichel on 09/11/2015.
 */
public class Realm_students_indicator extends RealmObject {
    private double absences_count, participation_score, performance_score,
    reading_score,math_score,friendship_score;
    private String createDate;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    private int idStudent, subject_id;

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public double getAbsences_count() {
        return absences_count;
    }

    public void setAbsences_count(double absences_count) {
        this.absences_count = absences_count;
    }

    public double getFriendship_score() {
        return friendship_score;
    }

    public void setFriendship_score(double friendship_score) {
        this.friendship_score = friendship_score;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }

    public double getMath_score() {
        return math_score;
    }

    public void setMath_score(double math_score) {
        this.math_score = math_score;
    }

    public double getParticipation_score() {
        return participation_score;
    }

    public void setParticipation_score(double participation_score) {
        this.participation_score = participation_score;
    }

    public double getPerformance_score() {
        return performance_score;
    }

    public void setPerformance_score(double performance_score) {
        this.performance_score = performance_score;
    }

    public double getReading_score() {
        return reading_score;
    }

    public void setReading_score(double reading_score) {
        this.reading_score = reading_score;
    }
}
