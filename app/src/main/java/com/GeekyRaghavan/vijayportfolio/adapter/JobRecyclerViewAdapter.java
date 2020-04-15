package com.GeekyRaghavan.vijayportfolio.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.GeekyRaghavan.vijayportfolio.R;
import com.GeekyRaghavan.vijayportfolio.pojo.SingleJob;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class JobRecyclerViewAdapter extends RecyclerView.Adapter<JobRecyclerViewAdapter.JobViewHolder> {

    private List<SingleJob> singleJobList;
    private FirebaseStorage firebaseStorage;

    public JobRecyclerViewAdapter(){
        firebaseStorage = FirebaseStorage.getInstance();
        singleJobList = new ArrayList<>();
    }

    public void setSingleJobList(List<SingleJob> singleJobs){
        singleJobList.clear();
        singleJobList.addAll(singleJobs);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.job_cardview, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final JobViewHolder holder, int position) {
        SingleJob singleJob = singleJobList.get(position);

        holder.title.setText(singleJob.getTitle());
        holder.description.setText(singleJob.getRole());
        holder.date.setText(singleJob.getDate());
        if (singleJob.getImgLink() != null) {
            StorageReference gsReference = firebaseStorage.getReferenceFromUrl(singleJob.getImgLink());

            final long ONE_MEGABYTE = 1024 * 1024;
            gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.img_job_src.setImageBitmap(bmp);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return singleJobList.size();
    }

    static class JobViewHolder extends RecyclerView.ViewHolder{

        private TextView title, description, date;
        private ImageView img_job_src;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_job_title);
            description = itemView.findViewById(R.id.tv_job_role);
            date = itemView.findViewById(R.id.job_date);
            img_job_src = itemView.findViewById(R.id.img_job_src);
        }
    }
}
