package in.apnacare.android.medicationalertsystem.fromapi;

/**
 * Created by dell on 29-12-2016.
 */

public class MedicationApi {

    String name;
    String dosage;
    String quantity;
    String fromdate;
    String todate;
    String doctor;
    String pharmacy;
    String mtime;
    String atime;
    String etime;
    String ntime;
    String uid;

    @Override
    public String toString(){
         return "Name: "+ name + "Dosage: "+ dosage + "Quantity: "+ quantity + "Fromdate : "+ fromdate + " ToDate : "+todate +" DoctorName:  "+ doctor + " PharamacyName: " + pharmacy+ " Morning Time: " + mtime + " Noon Time: "+ atime + " Evening Time : "+etime + "Night time: " + ntime + "Usser id "+ uid ;
    }
}
