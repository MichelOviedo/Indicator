package engineering.project.indicator.structureRealm;

import io.realm.RealmObject;

/**
 * Created by EduardoMichel on 13/11/2015.
 */
public class Realm_viewTables extends RealmObject {
    private String idGroup;
    private int absences_count, participation_score, performance_score,
    reading_score,math_score,friendship_score;

    public int getAbsences_count() {
        return absences_count;
    }

    public void setAbsences_count(int absences_count) {
        this.absences_count = absences_count;
    }

    public int getFriendship_score() {
        return friendship_score;
    }

    public void setFriendship_score(int friendship_score) {
        this.friendship_score = friendship_score;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public int getMath_score() {
        return math_score;
    }

    public void setMath_score(int math_score) {
        this.math_score = math_score;
    }

    public int getParticipation_score() {
        return participation_score;
    }

    public void setParticipation_score(int participation_score) {
        this.participation_score = participation_score;
    }

    public int getPerformance_score() {
        return performance_score;
    }

    public void setPerformance_score(int performance_score) {
        this.performance_score = performance_score;
    }

    public int getReading_score() {
        return reading_score;
    }

    public void setReading_score(int reading_score) {
        this.reading_score = reading_score;
    }
}
