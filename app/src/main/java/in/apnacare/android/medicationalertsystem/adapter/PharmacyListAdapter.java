package in.apnacare.android.medicationalertsystem.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentNavigableMap;

import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.activity.ActivityEditPharmacy;
import in.apnacare.android.medicationalertsystem.activity.ActivityPrescription;

import in.apnacare.android.medicationalertsystem.activity.PharmacyDetailsActivity;

import in.apnacare.android.medicationalertsystem.activity.ViewMedicationActivity;
import in.apnacare.android.medicationalertsystem.database.MedicationCollectionModel;
import in.apnacare.android.medicationalertsystem.database.PharmacyCollectionModel;

import in.apnacare.android.medicationalertsystem.model.PharmacyCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;

/**
 * Created by dell on 23-10-2016.
 */

public class PharmacyListAdapter extends RecyclerView.Adapter<PharmacyListAdapter.ViewHolder> {

    private ArrayList<PharmacyCollection> pharmacyList;

    Context mContext;
   PharmacyCollectionModel cr,mcr;

    public PharmacyListAdapter(Context context) {
        super();
        mContext = context;
        Log.e(Constants.TAG, "PharmacyListAdapter: ");

        pharmacyList = new ArrayList<PharmacyCollection>();

        cr = new PharmacyCollectionModel(MedicationUsers.getContext());
        mcr = new PharmacyCollectionModel(MedicationUsers.getContext());
        pharmacyList = cr.getPharmacy();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pharma_list_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        Log.e(Constants.TAG, "onCreateViewHolder: ");
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PharmacyCollection pharmacyRecord = pharmacyList.get(i);

        viewHolder.lblPharmacyName.setText(pharmacyRecord.getPharmacy_name());
        Log.e(Constants.TAG, "PharmacyName "+pharmacyRecord.getPharmacy_name() );

        viewHolder.lblPharmacyEmail.setText(pharmacyRecord.getPharmacy_email());
        Log.e(Constants.TAG, "Pharmacy Email: "+pharmacyRecord.getPharmacy_email());

        viewHolder.lblPharmacyNumber.setText(pharmacyRecord.getPharmacyNumber());
        viewHolder.lblPharmacyLocation.setText(pharmacyRecord.getPharmacyLocation());
        viewHolder.lblPharmacyCity.setText(pharmacyRecord.getPharmacy_city());

        Log.e(Constants.TAG, "PharmacyNumber: "+pharmacyRecord.getPharmacyNumber());



        viewHolder.btnEditPharmacy.setTag(pharmacyRecord.getP_id());

        Log.e("pharmacyNamedetails", "onBindViewHolder: "+pharmacyRecord.getP_id());

        viewHolder.btnDeletePharmacy.setTag(pharmacyRecord.getP_id());
        viewHolder.btnPrescription.setTag(pharmacyRecord.getP_id());

    }
    @Override
    public int getItemCount() {
        return pharmacyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView lblPharmacyName, lblPharmacyNumber, lblPharmacyEmail,lblPharmacyLocation,lblPharmacyCity;
        public Button btnPrescription;
        private LinearLayout statusColorBar;
        private Button btnEditPharmacy, btnDeletePharmacy;
        private  int medId;
        CardView cardView;
        protected String fileNo;
        protected String phoneNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            btnDeletePharmacy = (Button)itemView.findViewById(R.id.btnDelete);
            btnEditPharmacy = (Button) itemView.findViewById(R.id.btnEdit);
            lblPharmacyName = (TextView) itemView.findViewById(R.id.txt_pharmacy_name);
            lblPharmacyNumber = (TextView) itemView.findViewById(R.id.txt_pharma_number);
            lblPharmacyEmail = (TextView) itemView.findViewById(R.id.txt_pharmacy_email);
            lblPharmacyLocation = (TextView) itemView.findViewById(R.id.txt_pharmacy_location);
            lblPharmacyCity = (TextView) itemView.findViewById(R.id.txt_pharmacy_city);

           btnPrescription = (Button) itemView.findViewById(R.id.sendPrescription);


            cardView = (CardView) itemView.findViewById(R.id.card_view);

            statusColorBar = (LinearLayout) itemView.findViewById(R.id.statusColorBar);



            btnEditPharmacy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PharmacyCollection pharmaCol = new PharmacyCollection();
                    PharmacyCollectionModel medMod = new PharmacyCollectionModel(MedicationUsers.getContext());
                    Log.e(Constants.TAG, "onClick Delete: "+btnDeletePharmacy.getTag());
                    Log.e(Constants.TAG, "onClick Edit: "+btnEditPharmacy.getTag());



                    int id = (int) btnEditPharmacy.getTag();
                    Log.e(Constants.TAG, "Edit ID: "+id);
                    Intent i = new Intent(mContext,ActivityEditPharmacy.class);
                    Log.e(Constants.TAG, "onClick Edit: "+i.putExtra("id",id));
                    i.putExtra("id",id);

                    mContext.startActivity(i);
                }
            });


            btnPrescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int id = (int) btnEditPharmacy.getTag();
                    Log.e(Constants.TAG, "Edit ID: "+id);
                    Intent i = new Intent(mContext,ActivityPrescription.class);

                    i.putExtra("id",id);
                    mContext.startActivity(i);
                }
            });


            btnDeletePharmacy.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View view) {
                    PharmacyCollection pharmacyCol = new PharmacyCollection();
                    AlertDialog diaBox = AskOption();
                    diaBox.show();

                }
            });
        }
        private AlertDialog AskOption()

        {

            final PharmacyCollectionModel pharmaMod = new PharmacyCollectionModel(MedicationUsers.getContext());
            AlertDialog myQuittingDialogBox =new AlertDialog.Builder(mContext)
                    //set message, title, and icon
                    .setTitle("Delete")
                    .setMessage("Do you want to Delete")
                    .setIcon(R.drawable.ic_delete_black_24dp)

                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            //your deleting code
                            pharmaMod.removeByID((Integer)btnDeletePharmacy.getTag());

                            Intent i = new Intent(mContext,PharmacyDetailsActivity.class);

                            mContext.startActivity(i);


                            dialog.dismiss();
                        }

                    })



                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    })
                    .create();
            return myQuittingDialogBox;

        }
    }


}
