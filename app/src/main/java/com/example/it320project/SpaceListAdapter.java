package com.example.it320project;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SpaceListAdapter extends RecyclerView.Adapter<SpaceListAdapter.ViewHolder> {

    private Context mContext;
    List<Space> spacesList;
    MyDatabaseHelper dbHelper;
    RecyclerView rv;

    final View.OnClickListener onClickListener = new MyOnClickListener();

    public static class ViewHolder extends RecyclerView.ViewHolder{
        //declare variables for each row
        TextView rowName;
        TextView rowLoc;
        TextView rowCateg;
        Button viewBtn, deleteBtn;
        ImageView imageView;
        public View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            //to find each sub view
            rowName = itemView.findViewById(R.id.nameTxt);
            rowLoc = itemView.findViewById(R.id.locTxt);
            rowCateg = itemView.findViewById(R.id.categTxt);
            imageView = itemView.findViewById(R.id.imageView);
            viewBtn = itemView.findViewById(R.id.viewDetailsBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

    public SpaceListAdapter (Context context, List<Space> spacesList, RecyclerView rv){
        mContext = context;
        this.spacesList = spacesList;
        this.rv = rv;
        this.dbHelper = new MyDatabaseHelper(context);
    }

    @NonNull
    @Override
    public SpaceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.single_item, viewGroup, false);
        view.setOnClickListener(onClickListener);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SpaceListAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        Space space = spacesList.get(i);
        viewHolder.rowName.setText(space.getName());
        viewHolder.rowLoc.setText(space.getLocation());
        viewHolder.rowCateg.setText(space.getCategory());

        // Retrieve the photo data from the SQLite database for the current Space object
        byte[] photoData = dbHelper.getPhotoData(space.getId());

        // If the photo data exists, decode it as a Bitmap and display it in the ImageView
        if (photoData != null) {
            Bitmap photoBitmap = BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
            viewHolder.imageView.setImageBitmap(photoBitmap);
        } else {
            // If the photo data is null, set a default image in the ImageView
            viewHolder.imageView.setImageResource(R.drawable.home);
        }

        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the clicked space object
                int position = viewHolder.getAdapterPosition();
                Space clickedSpace = spacesList.get(position);
                showDeleteConfirmationDialog(position,clickedSpace);
            }
        });

        // Set the click listener for the view details button
        viewHolder.viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the details of the item at the current position
                showDetails(view, space);
            }
        });

        if (space.getStatus() == 1) {
            viewHolder.itemView.setVisibility(View.GONE);
            viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        } else {
            viewHolder.itemView.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = viewHolder.itemView.getLayoutParams();
            if (params instanceof RecyclerView.LayoutParams) {
                ((RecyclerView.LayoutParams)params).height = ViewGroup.LayoutParams.WRAP_CONTENT;
                ((RecyclerView.LayoutParams)params).width = ViewGroup.LayoutParams.MATCH_PARENT;
            }
        }
    }

    @Override
    public int getItemCount() {
        return spacesList != null ? spacesList.size() : 0;
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemPos = rv.getChildLayoutPosition(v);
            String item = spacesList.get(itemPos).getName();
            Toast.makeText(mContext, item, Toast.LENGTH_SHORT).show();
        }
    }

    private void showDetails(View itemView, Space item) {

        Intent intent = new Intent(itemView.getContext(), SpaceDetails.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("name", item.getName());

        itemView.getContext().startActivity(intent);
    }

    public void deleteItem(int position) {
        Space clickedSpace = spacesList.get(position);
        dbHelper.deleteOne(clickedSpace.getId());
        spacesList.remove(position);
        notifyItemRemoved(position);
        Toast.makeText(mContext, "Deleted successfully", Toast.LENGTH_SHORT).show();

    }

    private void showDeleteConfirmationDialog(final int position, Space clickedSpace) {


        int userId=dbHelper.getSpacesUserId(clickedSpace.getName());
        int currentUserId=dbHelper.getCurrentUserId();
        // Check if the space was added by the current user
        if (userId==currentUserId) {
            // If the space was added by the current user, show a confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Are you sure you want to delete this event space?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteItem(position);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            // If the space was not added by the current user, show a message
            Toast.makeText(mContext, "Only the owner can delete this event space", Toast.LENGTH_SHORT).show();
        }
    }
    public void setData(List<Space> spaces) {
        spacesList = spaces;
        notifyDataSetChanged();
    }


}