package in.apnacare.android.medicationalertsystem.fromapi;

/**
 * Created by dell on 29-12-2016.
 */

public class UserApi {

    String name;
    String email;
    String birthdate;
    String birthMonth;
    String phonenumber;


   /* @Override
    public String toString() {
        return "Name:" + name +"Email :" +email +"BirthDate"+ birthdate +"BirthMonth : "+ birthMonth +"Phone number:"+ phonenumber;
    }*/

        public String getName(){
            return name;
        }
    public String getEmail(){
        return email;
    }
    public String getBirthdate(){
        return birthdate;
    }
    public String getBirthMonth(){
        return birthMonth;

    }


}
