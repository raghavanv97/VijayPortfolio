package com.GeekyRaghavan.vijayportfolio.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.GeekyRaghavan.vijayportfolio.adapter.JobRecyclerViewAdapter;
import com.GeekyRaghavan.vijayportfolio.pojo.SingleJob;
import com.GeekyRaghavan.vijayportfolio.pojo.SingleProject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView img_coffee_stream;
    private RecyclerView recyclerView;
    private JobRecyclerViewAdapter adapter;
    Animation bottomUp;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JobFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JobFragment newInstance(String param1, String param2) {
        JobFragment fragment = new JobFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job, container, false);

        img_coffee_stream = view.findViewById(R.id.img_coffee_stream);
        bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up_with_fade_out);
//        bottomUp.setRepeatMode(Animation.RESTART);
//        bottomUp.setRepeatCount(Animation.INFINITE);


        recyclerView = view.findViewById(R.id.rv_job);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new JobRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        NetworkDetector detector = new NetworkDetector(getContext());
        if (detector.isNetworkAvailable()) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("jobs")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<SingleJob> singleJobList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.e("Vijay", document.getId() + " => " + document.getData());
                                    Map<String, Object> keyValue = document.getData();
                                    SingleJob singleJob = new SingleJob();
                                    for (String key :
                                            keyValue.keySet()) {

                                        if (key.equals("name")) {
                                            singleJob.setTitle(String.valueOf(keyValue.get(key)));
                                        } else if (key.equals("role")) {
                                            singleJob.setRole(String.valueOf(keyValue.get(key)));
                                        } else if (key.equals("date")) {
                                            singleJob.setDate(String.valueOf(keyValue.get(key)));
                                        } else if (key.equals("imageLink")) {
                                            singleJob.setImgLink(String.valueOf(keyValue.get(key)));
                                        }
                                    }
                                    singleJobList.add(singleJob);
                                }
                                adapter.setSingleJobList(singleJobList);
                            } else {
                                Log.e("Vijay", "Error getting documents.", task.getException());
                            }
                        }
                    });

        } else {
            Toast.makeText(getContext(), R.string.internet_not_available, Toast.LENGTH_LONG).show();
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        img_coffee_stream.startAnimation(bottomUp);
    }

    @Override
    public void onPause() {
        super.onPause();
        bottomUp.cancel();
    }
}
