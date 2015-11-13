package engineering.project.indicator.structureModel;

/**
 * Created by EduardoMichel on 07/11/2015.
 */
public class ModelTeacher {
    String fisrtName, lastName, title, email, contact_number, user, role;

    public ModelTeacher(){
        this("","","","","","","");
    }

    public ModelTeacher(String title, String fisrtName, String lastName, String user, String email, String role, String contact_number) {
        this.title = title;
        this.fisrtName = fisrtName;
        this.lastName = lastName;
        this.user = user;
        this.email = email;
        this.role = role;
        this.contact_number = contact_number;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFisrtName() {
        return fisrtName;
    }

    public void setFisrtName(String fisrtName) {
        this.fisrtName = fisrtName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
