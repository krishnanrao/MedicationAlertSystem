package in.apnacare.android.medicationalertsystem.Mandrill;

import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.net.CookieHandler;

import in.apnacare.android.medicationalertsystem.utils.Constants;

/**
 * Created by dell on 16-11-2016.
 */

public class Attachment {


    private String type ;
    private String name ;
    private String content;
    private  File attach ;
    private  Uri uri;

    public void setType(String type) {
        Log.e(Constants.TAG, "setName: "+type);
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
        Log.e(Constants.TAG, "setName: "+name);
    }

    public void setContent(String contents) {
        Log.e(Constants.TAG, "setName: "+contents);
        this.content = contents;
    }
    public  void setImage(File attach){

        this.uri = Uri.fromFile(attach);
        Log.e(Constants.TAG, "setImage: "+uri);
    }

}
