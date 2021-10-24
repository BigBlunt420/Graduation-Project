package com.example.loginandsignup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Schedule extends Fragment {

    List<Model> modelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseFirestore firestoredb;
    FirebaseAuth firebaseAuth;
    String UserID;
    UserAdapter adapter;
    ProgressDialog progressDialog;
    FloatingActionButton addNewSchedule;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycle_view);
        addNewSchedule = view.findViewById(R.id.addNewSchedule);


        firestoredb = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getActivity());

        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(getParentFragment().getContext());
        recyclerView.setLayoutManager(layoutManager);

        showScheduleList();

        addNewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),HomePage.class));
            }
        });

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_schedule, container, false);
        return view;

    }

    private void showScheduleList() {
        progressDialog.setTitle("資料載入中...");
        progressDialog.show();
        UserID = firebaseAuth.getCurrentUser().getUid();
        firestoredb.collection("User").document(UserID).collection("Schedule")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        modelList.clear();
                        progressDialog.dismiss();
                        //顯示資料
                        for(DocumentSnapshot documentSnapshot:task.getResult()){
                            Model model = new Model(documentSnapshot.getString("ScheduleId"),
                                    documentSnapshot.getString("Title"),
                                    documentSnapshot.getString("Describe"),
                                    documentSnapshot.getString("StartTime"),
                                    documentSnapshot.getString("EndTime"),
                                    documentSnapshot.getString("Location"),
                                    documentSnapshot.getString("Date"));
                            modelList.add(model);
                        }
                        //連接
                        adapter = new UserAdapter(Schedule.this,modelList);
                        recyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void deleteData(int position){
        firebaseAuth = FirebaseAuth.getInstance();
        UserID = firebaseAuth.getCurrentUser().getUid();
        firestoredb.collection("Users").document(UserID).collection("Schedule").document(modelList.get(position).getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()){
                            showScheduleList();
                            Log.d("DeleteDetailSchedule","Successful:User Profile is deleted for " + UserID);
                        }else {
                            Log.w("DeleteDetailSchedule","Fail:",task.getException());
                        }
                    }
                });
    }


}