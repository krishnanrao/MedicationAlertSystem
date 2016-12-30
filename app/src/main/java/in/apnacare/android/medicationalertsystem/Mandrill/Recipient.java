package in.apnacare.android.medicationalertsystem.Mandrill;

/**
 * Created by dell on 16-11-2016.
 */

public class Recipient {

    private String email;
    private String name;
    private String type="to";

    public String getEmail() {
        return email;
    }

    public void setName(String name ){
        this.name =  name ;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
