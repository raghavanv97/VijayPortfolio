package com.GeekyRaghavan.vijayportfolio.adapter;

import android.content.Context;
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
import com.GeekyRaghavan.vijayportfolio.pojo.SingleProject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProjectsRecyclerViewAdapter extends RecyclerView.Adapter<ProjectsRecyclerViewAdapter.ProjectViewHolder> {

    private List<SingleProject> singleProjectList;
    private FirebaseStorage firebaseStorage;
    private Context context;

    public ProjectsRecyclerViewAdapter(Context context) {
        singleProjectList = new ArrayList<>();
        this.context = context;
        // Reference to an image file in Cloud Storage
        firebaseStorage = FirebaseStorage.getInstance();

    }

    public void setSingleProjectList(List<SingleProject> singleProjects) {
        singleProjectList.clear();
        singleProjectList.addAll(singleProjects);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.project_cardview, parent, false);

        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProjectViewHolder holder, int position) {
        SingleProject singleProject = singleProjectList.get(position);
        holder.title.setText(singleProject.getTitle());
        holder.description.setText(singleProject.getDescription());
        if (singleProject.getImageLink() != null) {
            StorageReference gsReference = firebaseStorage.getReferenceFromUrl(singleProject.getImageLink());

            final long ONE_MEGABYTE = 1024 * 1024;
            gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.img_project_src.setImageBitmap(bmp);
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
        return singleProjectList.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {

        private TextView title, description;
        private ImageView img_project_src;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            img_project_src = itemView.findViewById(R.id.img_project_src);
            title = itemView.findViewById(R.id.tv_project_title);
            description = itemView.findViewById(R.id.tv_project_description);
        }
    }
}
