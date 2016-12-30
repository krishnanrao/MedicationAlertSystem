package in.apnacare.android.medicationalertsystem.adapter;

/**
 * Created by dell on 14-10-2016.
 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.activity.ActivityDoctorDetails;
import in.apnacare.android.medicationalertsystem.activity.Alarm;
import in.apnacare.android.medicationalertsystem.activity.AlarmActivity;
import in.apnacare.android.medicationalertsystem.activity.BaseActivity;
import in.apnacare.android.medicationalertsystem.activity.EditMedicationActivity;
import in.apnacare.android.medicationalertsystem.activity.MedicationDetailsActivity;
import in.apnacare.android.medicationalertsystem.activity.ViewMedicationActivity;
import in.apnacare.android.medicationalertsystem.database.AlarmCollectionModel;
import in.apnacare.android.medicationalertsystem.database.DatabaseHandler;
import in.apnacare.android.medicationalertsystem.database.DoctorCollectionModel;
import in.apnacare.android.medicationalertsystem.database.MedicationCollectionModel;
import in.apnacare.android.medicationalertsystem.database.RefillCollectionModel;
import in.apnacare.android.medicationalertsystem.model.MedicationCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;


public class MedicationListAdapter extends RecyclerView.Adapter<MedicationListAdapter.ViewHolder> {

    private ArrayList<MedicationCollection> caseList,medModList;

    Context mContext;
    MedicationCollectionModel cr,mcr;


    public MedicationListAdapter(Context context) {
        super();
        mContext = context;
        caseList = new ArrayList<MedicationCollection>();
        medModList = new ArrayList<MedicationCollection>();
        cr = new MedicationCollectionModel(MedicationUsers.getContext());
        mcr = new MedicationCollectionModel(MedicationUsers.getContext());
        caseList = cr.getMedications();
        medModList = mcr.getMorningMedications();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.med_list_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        MedicationCollection medRecord = caseList.get(i);


        viewHolder.lblMedicationName.setText((CharSequence)medRecord.getM_name());
        Log.e("MORNINGMEDICATION", "MEd "+(CharSequence) medRecord.getM_name() );


        if(!medRecord.getMorningAlarmStatus().equals("N/A")){
            viewHolder.lblmScheduled.setText("Morning :");
            viewHolder.lblmScheduledTime.setText(medRecord.getMorningAlarmStatus());
        }
        if(!medRecord.getAfternoonAlarmStatus().equals("N/A")){
            viewHolder.lblaScheduled.setText("AfterNoon :");
            viewHolder.lblaScheduledTime.setText(medRecord.getAfternoonAlarmStatus());
        }
        if(!medRecord.getEveningAlarmStatus().equals("N/A")){
            viewHolder.lbleScheduled.setText("Evening :");
            viewHolder.lbleScheduledTime.setText(medRecord.getEveningAlarmStatus());
        }
        if(!medRecord.getNightAlarmStatus().equals("N/A")){
            viewHolder.lblnScheduled.setText("Night :");
            viewHolder.lblnScheduledTime.setText(medRecord.getNightAlarmStatus());
        }




        Log.e("pharmacyNamedetails", "onBindViewHolder: "+medRecord.getPharmacyName());
        viewHolder.btnEditMedication.setTag(medRecord.getM_id());
        viewHolder.btnDeleteMedication.setTag(medRecord.getM_id());


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date mDate = null;
        String requestDate = "";
        try {
            mDate = df.parse(medRecord.getToDate().toString());

            df = new SimpleDateFormat("dd MMM yyyy");
            requestDate = df.format(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.lblToDate.setText(medRecord.getToDate());


    }

    @Override
    public int getItemCount() {
        return caseList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView lblMedicationName, lblmScheduledTime,lblaScheduledTime,lbleScheduledTime,lblnScheduledTime, lblmScheduled,lblaScheduled,lbleScheduled,lblnScheduled, lblToDate;
        private LinearLayout statusColorBar;
        private Button btnEditMedication, btnDeleteMedication;
        private  int medId;
        CardView cardView;
        Alarm alarm;
        protected String fileNo;
        protected String phoneNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            btnDeleteMedication = (Button)itemView.findViewById(R.id.btnDelete);
            btnEditMedication = (Button) itemView.findViewById(R.id.btnEdit);
            lblMedicationName = (TextView) itemView.findViewById(R.id.txt_medication_name);

            lblmScheduled = (TextView) itemView.findViewById(R.id.mScheduled);
            lblmScheduledTime = (TextView) itemView.findViewById(R.id.mScheduledTime);
            lblaScheduled = (TextView) itemView.findViewById(R.id.aScheduled);
            lblaScheduledTime = (TextView) itemView.findViewById(R.id.aScheduledTime);
            lbleScheduled = (TextView) itemView.findViewById(R.id.eScheduled);
            lbleScheduledTime = (TextView) itemView.findViewById(R.id.eScheduledTime);
            lblnScheduled = (TextView) itemView.findViewById(R.id.nScheduled);
            lblnScheduledTime = (TextView) itemView.findViewById(R.id.nScheduledTime);


            lblToDate = (TextView) itemView.findViewById(R.id.txt_to_date);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

            statusColorBar = (LinearLayout) itemView.findViewById(R.id.statusColorBar);



            btnEditMedication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MedicationCollection medCol = new MedicationCollection();
                    MedicationCollectionModel medMod = new MedicationCollectionModel(MedicationUsers.getContext());
                    Log.e("delete button log", "onClick Delete: "+btnDeleteMedication.getTag());
                    Log.e("Edit medication Button", "onClick Edit: "+btnEditMedication.getTag());



                    int id = (int) btnEditMedication.getTag();
                    Log.e("edit med btn tag id", "Edit ID: "+id);
                    Intent i = new Intent(mContext,EditMedicationActivity.class);

                    i.putExtra("id",id);

                    mContext.startActivity(i);
                }
            });
            btnDeleteMedication.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View view) {

                    MedicationCollection medCol = new MedicationCollection();


                    AlertDialog diaBox = AskOption();
                    diaBox.show();


                    Log.e("delete button log", "onClick Delete: "+btnDeleteMedication.getTag()+btnDeleteMedication.getId());
                    Log.e("Edit medication Button", "onClick Edit: "+(Integer)btnDeleteMedication.getTag());



                                    }
            });
        }

        private AlertDialog AskOption()

        {

            final   MedicationCollectionModel medMod = new MedicationCollectionModel(MedicationUsers.getContext());
            final ArrayList<MedicationCollection> medi = new ArrayList<MedicationCollection>();
            AlertDialog myQuittingDialogBox =new AlertDialog.Builder(mContext)
                    //set message, title, and icon
                    .setTitle("Delete")
                    .setMessage("Do you want to Delete")
                    .setIcon(R.drawable.ic_delete_black_24dp)

                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            //your deleting code
                            ArrayList<MedicationCollection> medi = new ArrayList<MedicationCollection>();
                            AlarmCollectionModel alm = new AlarmCollectionModel(MedicationUsers.getContext());
                            RefillCollectionModel refill = new RefillCollectionModel(MedicationUsers.getContext());
                          medi =  medMod.getMedicationsByID((Integer)btnDeleteMedication.getTag());
                            Log.e(Constants.TAG, "Medicine list by ID: "+medi.size());
                            for(int i=0 ; i < medi.size(); i++){
                                Log.e(Constants.TAG, "onClick: "+medi.get(i).getM_id());

                                alm.deleteMedEntry(medi.get(i).getM_id());

                                refill.removeByID(medi.get(i).getM_name());
                            }
                           medMod.removeByID((Integer)btnDeleteMedication.getTag());

                            Intent i = new Intent(mContext,MedicationDetailsActivity.class);

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
