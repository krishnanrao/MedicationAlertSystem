package in.apnacare.android.medicationalertsystem.adapter;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import java.util.ArrayList;

import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.activity.ActivityDoctorEdit;
import in.apnacare.android.medicationalertsystem.activity.CareTakerDetailsActivity;
import in.apnacare.android.medicationalertsystem.activity.CareTakerEditActivity;
import in.apnacare.android.medicationalertsystem.database.CareTakerCollectionModel;
import in.apnacare.android.medicationalertsystem.model.CareTakerCollection;
import in.apnacare.android.medicationalertsystem.utils.Constants;
import in.apnacare.android.medicationalertsystem.utils.MedicationUsers;

/**
 * Created by dell on 29-10-2016.
 */

public class CareTakerAdapter extends RecyclerView.Adapter<CareTakerAdapter.ViewHolder> {

    private ArrayList<CareTakerCollection> careTakerList;
    Context mContext;
    CareTakerCollectionModel cr, mcr;

    public CareTakerAdapter(Context context) {
        super();
        mContext = context;
        Log.e(Constants.TAG, "DoctorListAdapter: ");

        careTakerList = new ArrayList<CareTakerCollection>();

        cr = new CareTakerCollectionModel(MedicationUsers.getContext());
        mcr = new CareTakerCollectionModel(MedicationUsers.getContext());
        careTakerList = cr.getCareTaker();
        // pharmacyModList = mcr.getMorningMedications();
    }


    @Override
    public CareTakerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.care_taker_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        Log.e(Constants.TAG, "onCreateViewHolder: ");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CareTakerAdapter.ViewHolder viewholder, int i) {

        CareTakerCollection careTakerRecord = careTakerList.get(i);

        viewholder.lblCareTakerName.setText(careTakerRecord.getCare_taker_name());
        viewholder.lblCareTakerRelation.setText(careTakerRecord.getCare_taker_relation());
        viewholder.lblCareTakerEmail.setText(careTakerRecord.getCare_taker_email());
        viewholder.btnEditDetails.setTag(careTakerRecord.getCare_taker_id());
        viewholder.btnCallCareTaker.setTag(careTakerRecord.getCare_taker_phnumber());



        viewholder.btnDeleteDetails.setTag(careTakerRecord.getCare_taker_id());
    }

    @Override
    public int getItemCount() {
        Log.e(Constants.TAG, "getItemCount: " + careTakerList.size());
        return careTakerList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView lblCareTakerName, lblCareTakerEmail, lblCareTakerRelation;
        public Button btnCallCareTaker;
        private LinearLayout statusColorBar;
        private Button btnEditDetails, btnDeleteDetails;

        CardView cardView;
        protected String fileNo;


        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            btnDeleteDetails = (Button) itemView.findViewById(R.id.btnDelete);
            btnEditDetails = (Button) itemView.findViewById(R.id.btnEdit);
            lblCareTakerName = (TextView) itemView.findViewById(R.id.txt_ct_name);
            lblCareTakerEmail = (TextView) itemView.findViewById(R.id.txt_ct_email);
            lblCareTakerRelation = (TextView) itemView.findViewById(R.id.txt_ct_relation);
            btnCallCareTaker = (Button) itemView.findViewById(R.id.callDoctor);


            cardView = (CardView) itemView.findViewById(R.id.card_view);

            statusColorBar = (LinearLayout) itemView.findViewById(R.id.statusColorBar);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, CareTakerDetailsActivity.class);
                    i.putExtra("file_no", fileNo);
                    mContext.startActivity(i);
                }
            });

            btnEditDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CareTakerCollection careTakerCollection = new CareTakerCollection();
                    CareTakerCollectionModel careTakerCollectionModel = new CareTakerCollectionModel(MedicationUsers.getContext());
                    Log.e(Constants.TAG, "onClick Delete: " + btnEditDetails.getTag());
                    Log.e(Constants.TAG, "onClick Edit: " + btnEditDetails.getTag());


                    int id = (int) btnEditDetails.getTag();
                    Log.e(Constants.TAG, "Doctor Edit ID: " + id);
                    Intent i = new Intent(mContext, CareTakerEditActivity.class);
                    Log.e(Constants.TAG, "onClick Edit: " + i.putExtra("id", id));
                   i.putExtra("id", id);

                    mContext.startActivity(i);
                }
            });


            btnCallCareTaker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e(Constants.TAG, "Calling..... Doctor " + btnCallCareTaker.getTag());


                    try {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + btnCallCareTaker.getTag().toString()));
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mContext.startActivity(intent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(mContext, "Call permission is not enabled in your device for this app ", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            btnDeleteDetails.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View view) {
                    CareTakerCollection doctorCol = new CareTakerCollection();
                    AlertDialog diaBox = AskOption();
                    diaBox.show();

                    Log.e("delete button log", "onClick Delete: " + btnDeleteDetails.getTag() + btnDeleteDetails.getId());
                    Log.e("Edit Doctor Button", "onClick Edit: " + (Integer) btnDeleteDetails.getTag());

                }

            });


        }

        private AlertDialog AskOption()

        {

            final CareTakerCollectionModel docMod = new CareTakerCollectionModel(MedicationUsers.getContext());
            AlertDialog myQuittingDialogBox =new AlertDialog.Builder(mContext)
                    //set message, title, and icon
                    .setTitle("Delete")
                    .setMessage("Do you want to Delete")
                    .setIcon(R.drawable.ic_delete_black_24dp)

                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            //your deleting code
                            docMod.removeByID((Integer)btnDeleteDetails.getTag());
                            Intent i = new Intent(mContext, CareTakerDetailsActivity.class);
                            i.putExtra("file_no",fileNo);
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
