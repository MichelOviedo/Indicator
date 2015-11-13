package engineering.project.indicator.structureModel;

/**
 * Created by EduardoMichel on 09/11/2015.
 */
public class ModelStudent {

    private int id, gruopId;
    private String firstName, lastName, motherName, gender, idInformal;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getGruopId() {
        return gruopId;
    }

    public void setGruopId(int gruopId) {
        this.gruopId = gruopId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdInformal() {
        return idInformal;
    }

    public void setIdInformal(String idInformal) {
        this.idInformal = idInformal;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }
}
