package co.geeksters.googleplaceautocomplete.lib;

/**
 * Created by suhas on 12/8/16.
 */

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import co.geeksters.googleplaceautocomplete.R;


public class AutoCompleteAdapter extends SimpleAdapter {
    LayoutInflater inflater;
    Context mContext;
    List<HashMap<String, String>> arrayList;

    public AutoCompleteAdapter(Context context, List<HashMap<String, String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.mContext = context;
        this.arrayList = data;
        inflater.from(context);
        //Log.v("MedApp.class","autocompleteadapter: "+data);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (position != (arrayList.size() - 1))
            view = inflater.inflate(R.layout.autocomplete_list_item, null);
        else
            view = inflater.inflate(R.layout.autocomplete_google_logo, null);


        if (position != (arrayList.size() - 1)) {
            String str = arrayList.get(position).get("description").toString();
            String[] items = str.split("\\s*,\\s*",2);
            String name = items[0];
            String html = "<b>"+name+"</b><br>";
            for(int j=1;j<items.length;j++){
                html += items[j];
            }

            TextView text = (TextView) view.findViewById(R.id.autocompleteText);
            text.setText(Html.fromHtml(html));
        }
        else {
            ImageView imageView = (ImageView) view.findViewById(R.id.googleLogo);
            imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.powered_by_google_on_white));
        }

        return view;
    }
}