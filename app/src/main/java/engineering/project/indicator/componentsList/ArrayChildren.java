package engineering.project.indicator.componentsList;

/**
 * Created by EduardoMichel on 17/11/2015.
 */
public class ArrayChildren {
    String title;
    int idAllocation;
    int photo;

    public ArrayChildren() {
        this("",0);
    }

    public ArrayChildren(String title, int idAllocation) {
        this.title = title;
        this.idAllocation = idAllocation;
    }

    public int getIdAllocation() {
        return idAllocation;
    }

    public void setIdAllocation(int idAllocation) {
        this.idAllocation = idAllocation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
