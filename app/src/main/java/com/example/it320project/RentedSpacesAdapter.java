package com.example.it320project;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RentedSpacesAdapter extends RecyclerView.Adapter<RentedSpacesAdapter.ViewHolder> {

    private Context mContext;
    private RecyclerView.Adapter adapter;
    private List<Space> spaces;
    private MyDatabaseHelper dbHelper;

    public RentedSpacesAdapter(Activity activity, Context context, List<Space> spaces) {
        mContext = context;
        this.spaces = spaces;
        mContext = activity;
        dbHelper = new MyDatabaseHelper(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_rented_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Space space = spaces.get(position);

        holder.rowName.setText(space.getName());
        holder.rowLoc.setText(space.getLocation());

        holder.itemView.setTag(space);

        byte[] photoData = dbHelper.getPhotoData(space.getId());

        if (photoData != null) {
            Bitmap photoBitmap = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
            holder.imageView.setImageBitmap(photoBitmap);
        } else {
            holder.imageView.setImageResource(R.drawable.home);
        }
    }

    @Override
    public int getItemCount() {
        return spaces.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        Button btnReturn;
        private TextView rowName;
        TextView rowLoc;
        TextView rowCateg;
        public View itemView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.imageView);
            rowName = itemView.findViewById(R.id.nameTxt);
            rowLoc = itemView.findViewById(R.id.locTxt);
            rowCateg = itemView.findViewById(R.id.categTxt);
            // Initialize other views
            btnReturn = itemView.findViewById(R.id.ReturnBtn);

            btnReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Space space = (Space) itemView.getTag();
                    if(space.getId() != 0) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("Confirm Return");
                        builder.setMessage("Are you sure you want to return " + space.getName() + "?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.returnSpace(space.getId());

                                // Reload data from database
                                spaces = dbHelper.getAvailableSpaces();

                                //Update adapter with new data
                                notifyDataSetChanged();

                                Toast.makeText(mContext, "Space " + space.getName() + " has been returned successfully", Toast.LENGTH_SHORT).show();
                                Log.d("Spaces", spaces.toString());
                                Intent intent = new Intent(mContext, home.class);
                                mContext.startActivity(intent);
                                ((RentedSpacesActivity)mContext).finish();
                            }
                        });
                        builder.setNegativeButton("No", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            });

        }

    }

}