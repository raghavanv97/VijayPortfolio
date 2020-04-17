package com.GeekyRaghavan.vijayportfolio.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.GeekyRaghavan.vijayportfolio.NetworkDetector;
import com.GeekyRaghavan.vijayportfolio.R;
import com.GeekyRaghavan.vijayportfolio.adapter.ProjectsRecyclerViewAdapter;
import com.GeekyRaghavan.vijayportfolio.callbacklistener.RecyclerItemClickListener;
import com.GeekyRaghavan.vijayportfolio.pojo.SingleProject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectsFragment extends Fragment {

    ImageView img_setting_idea;
    RecyclerView recyclerView;
    List<SingleProject> singleProjects;
    ProjectsRecyclerViewAdapter adapter;
    private final String SAVE_STATE = "savestate";
    Gson gson;
    Animation bottomUp;

    public ProjectsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_projects, container, false);
        gson = new Gson();

        img_setting_idea = view.findViewById(R.id.img_setting_idea);
        recyclerView = view.findViewById(R.id.rv_projects);
        singleProjects = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProjectsRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                builder.addDefaultShareMenuItem();


                CustomTabsIntent customTabsIntent = builder.build();
                String link = singleProjects.get(position).getLink();
                if (link != null)
                    customTabsIntent.launchUrl(getContext(), Uri.parse(link));
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_along_x_axis);


        if (savedInstanceState == null) {

            NetworkDetector detector = new NetworkDetector(getContext());
            if (detector.isNetworkAvailable()) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("projects").orderBy("order")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    singleProjects.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.e("Vijay", document.getId() + " => " + document.getData());
                                        Map<String, Object> keyValue = document.getData();
                                        SingleProject singleProject = new SingleProject();
                                        for (String key :
                                                keyValue.keySet()) {

                                            if (key.equals("name")) {
                                                singleProject.setTitle(String.valueOf(keyValue.get(key)));
                                            } else if (key.equals("link")) {
                                                singleProject.setLink(String.valueOf(keyValue.get(key)));
                                            } else if (key.equals("description")) {
                                                singleProject.setDescription(String.valueOf(keyValue.get(key)));
                                            } else if (key.equals("image")) {
                                                singleProject.setImageLink(String.valueOf(keyValue.get(key)));
                                            }
                                        }
                                        singleProjects.add(singleProject);
                                    }
                                    adapter.setSingleProjectList(singleProjects);
                                } else {
                                    Log.e("Vijay", "Error getting documents.", task.getException());
                                }
                            }
                        });
            } else {
                Toast.makeText(getContext(), R.string.internet_not_available, Toast.LENGTH_LONG).show();
            }

        } else {
            singleProjects.clear();
            Type listType = new TypeToken<ArrayList<SingleProject>>() {
            }.getType();
            List<SingleProject> yourClassList = new Gson().fromJson(savedInstanceState.getString(SAVE_STATE), listType);
            singleProjects.addAll(yourClassList);
            adapter.setSingleProjectList(singleProjects);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        img_setting_idea.startAnimation(bottomUp);
    }

    @Override
    public void onPause() {
        super.onPause();
        bottomUp.cancel();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        if (singleProjects.size() > 0) {
            String value = gson.toJson(singleProjects);
            outState.putString(SAVE_STATE, value);
        }
        super.onSaveInstanceState(outState);
    }
}
