package in.apnacare.android.medicationalertsystem.model;

/**
 * Created by dell on 25-10-2016.
 */

public class SpinnerModel {

    private  String PharmacyName="";
    private  String Image="";
    private  String Url="";

    /*********** Set Methods ******************/
    SpinnerModel(String pname,String image,String url){
        this.PharmacyName = pname;
        this.Image = image;
        this.Url = url;
    }
    public void setPharmacyName(String PharmacyName)
    {
        this.PharmacyName = PharmacyName;
    }

    public void setImage(String Image)
    {
        this.Image = Image;
    }

    public void setUrl(String Url)
    {
        this.Url = Url;
    }

    /*********** Get Methods ****************/
    public String getPharmacyName()
    {
        return this.PharmacyName;
    }

    public String getImage()
    {
        return this.Image;
    }

    public String getUrl()
    {
        return this.Url;
    }
}
