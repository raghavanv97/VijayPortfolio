package com.GeekyRaghavan.vijayportfolio.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.GeekyRaghavan.vijayportfolio.NetworkDetector;
import com.GeekyRaghavan.vijayportfolio.R;
import com.GeekyRaghavan.vijayportfolio.pojo.SingleProject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String POSITION = "position";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int position;

    private ImageView img_hello;
    private ImageView img_big_holder, img_road, img_tree;
    private TextView tv_bio;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(int pos) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        img_hello = view.findViewById(R.id.img_hello);
        img_big_holder = view.findViewById(R.id.img_man);
        tv_bio = view.findViewById(R.id.tv_profile_bio);


        img_big_holder.setImageResource(R.drawable.ic_man);
        Animation bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
        img_hello.setAnimation(bottomUp);

        NetworkDetector detector = new NetworkDetector(getContext());
        if (detector.isNetworkAvailable()) {


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("profile")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.e("Vijay", document.getId() + " => " + document.getData());
                                    Map<String, Object> keyValue = document.getData();
                                    if (keyValue.containsKey("description"))
                                        tv_bio.setText(String.valueOf(keyValue.get("description")).replaceAll("\\\\n", System.getProperty("line.separator")));
                                }
                            } else {
                                Log.e("Vijay", "Error getting documents.", task.getException());
                            }
                        }
                    });
        } else {
            tv_bio.setText(R.string.internet_not_available);
        }


        return view;
    }


}
