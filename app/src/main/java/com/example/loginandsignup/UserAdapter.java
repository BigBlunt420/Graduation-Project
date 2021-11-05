package com.example.loginandsignup;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.service.quicksettings.Tile;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.time.Month;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class UserAdapter extends RecyclerView.Adapter<ViewHolder> {
    scheduleList scheduleList;
    List<Model> modelList;
    Context context;
    FirebaseFirestore firestoredb;
    FirebaseAuth firebaseAuth;
    private TextView inputStartTime,inputEndTime;
    private int starthour,startminute,endhour,endminute;
    int setYear,setMonth,setDay,month;
    private TextView inputDate;
    DatePickerDialog datePickerDialog;
    private Button addDetail,cancelDetail;
    String UserID;
    private EditText inputTile,inputDescribe;
    String date;
    String dbtitle,dbstartTime,dbendTime,dblocation,dbdescription,dbdate,dbid;
    String setStartTime,setEndTime;

    public UserAdapter(scheduleList scheduleList, List<Model> modelList) {
        this.scheduleList = scheduleList;
        this.modelList = modelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //inflate layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_layout,parent,false);

        //處理 item click 事件
        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //click

                //show data
//                String dbid = modelList.get(position).getId();
                String title = modelList.get(position).getTile();
                String startTime = modelList.get(position).getStartTime();
                String endTime = modelList.get(position).getEndTime();
                String location = modelList.get(position).getLocation();
                String description = modelList.get(position).getDescription();
                String date = modelList.get(position).getDate();
//
//                firebaseAuth = FirebaseAuth.getInstance();
//                UserID = firebaseAuth.getCurrentUser().getUid();
//                //Toast.makeText(scheduleList, dbid,Toast.LENGTH_LONG).show();
                Toast.makeText(scheduleList, title+"  "+date+"\n"+startTime+"-"+endTime+"  "+location+"\n"+description,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onItemLongClick(View view, final int position) {
                //long click
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                String [] action = {"修改資料","刪除資料"};
                builder.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            //修改資料
                            dbid = modelList.get(position).getId();
                            dbtitle = modelList.get(position).getTile();
                            dbstartTime = modelList.get(position).getStartTime();
                            dbendTime = modelList.get(position).getEndTime();
                            dblocation = modelList.get(position).getLocation();
                            dbdescription = modelList.get(position).getDescription();
                            dbdate = modelList.get(position).getDate();


                            updateDetailSchedule();
                        }
                        if (which == 1){
                            //刪除資料
                            dbid = modelList.get(position).getId();
                            deleteData(dbid);
                        }
                    }
                }).create().show();
            }
        });
        return viewHolder;
    }
    private void deleteData(String id){
        firestoredb = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserID = firebaseAuth.getCurrentUser().getUid();

        DocumentReference doc= firestoredb.collection("Users").document(UserID).collection("Schedule").document(id);
        doc.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        scheduleList.showScheduleList();
                        Log.d("DeleteDetailSchedule","Successful:User Profile is deleted for " + UserID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.w("DeleteDetailSchedule","Fail:"+e.getMessage());
                    }
                });
    }

    private void updateDetailSchedule() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(scheduleList);
        LayoutInflater inflater = LayoutInflater.from(scheduleList);

        View myView = inflater.inflate(R.layout.input_detail_schedule,null);
        builder.setView(myView);


        inputTile = myView.findViewById(R.id.inputTile);
        inputDescribe = myView.findViewById(R.id.inputDescribe);
        inputStartTime = myView.findViewById(R.id.inputStartTime);
        inputEndTime = myView.findViewById(R.id.inputEndTime);
        inputDate = myView.findViewById(R.id.inputDate);
        addDetail = myView.findViewById(R.id.addDetail);
        cancelDetail = myView.findViewById(R.id.cancelDetail);

        //取得firestore資料
        inputTile.setText(dbtitle);
        inputDescribe.setText(dbdescription);
        inputStartTime.setText(dbstartTime);
        inputEndTime.setText(dbendTime);
        inputDate.setText(dbdate);

        date = dbdate;
        setStartTime = dbstartTime;
        setEndTime =dbendTime;


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute =calendar.get(Calendar.MINUTE);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        dialog.show();

        //設定起始時間
        inputStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int setHour, int setMinute) {
                        setStartTime = makeTimeString(setHour,setMinute);
                        inputStartTime.setText(setStartTime);
                    }
                };
                Calendar calendar = Calendar.getInstance();
                int setStarthour = calendar.get(Calendar.HOUR_OF_DAY);
                int setStartminute =calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(scheduleList, android.app.AlertDialog.THEME_HOLO_LIGHT, onTimeSetListener,setStarthour,setStartminute,true);

                timePickerDialog.show();
            }
        });

        //設定結束時間
        inputEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int setHour, int setMinute) {
                        setEndTime = makeTimeString(setHour,setMinute);
                        inputEndTime.setText(setEndTime);
                    }
                };
                Calendar calendar = Calendar.getInstance();
                int setEndHour = calendar.get(Calendar.HOUR_OF_DAY);
                int setEndMinute =calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(scheduleList, android.app.AlertDialog.THEME_HOLO_LIGHT, onTimeSetListener,setEndHour,setEndMinute,true);

                timePickerDialog.show();
            }
        });

        //設定日期
        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        date = makeDateString(year,month,day);
                        inputDate.setText(date);
                    }
                };

                Calendar calendar = Calendar.getInstance();
                setYear = calendar.get(Calendar.YEAR);
                setMonth = calendar.get(Calendar.MONTH);
                setDay = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(scheduleList, android.app.AlertDialog.THEME_HOLO_LIGHT,dateSetListener,setYear,setMonth,setDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);

                datePickerDialog.show();
            }
        });

        addDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = inputTile.getText().toString().trim();
                String Describe = inputDescribe.getText().toString().trim();
                //檢查起始時間和結束時間
                while(year==setYear && (month+1)==setMonth&& day==setDay) {
                    if (starthour < hour || (starthour == hour && startminute < minute)) {
                        inputStartTime.requestFocus();
                        inputStartTime.setError("起始時間已過");
                        return;
                    }
                }
                if(starthour>endhour){
                    inputEndTime.requestFocus();
                    inputEndTime.setError("結束時間不得比起始時間早");
                    return;
                }
                if(starthour==endhour && startminute>endminute){
                    inputEndTime.requestFocus();
                    inputEndTime.setError("結束時間不得比起始時間早");
                    return;
                }
                //將資料加進firestore
                firestoredb = FirebaseFirestore.getInstance();
                firebaseAuth = FirebaseAuth.getInstance();
                UserID = firebaseAuth.getCurrentUser().getUid();

                DocumentReference documentReference = firestoredb.collection("Users").document(UserID).collection("Schedule").document(dbid);
                Map<String,Object> updateSchedule = new HashMap<String, Object>();
                updateSchedule.put("Title",Title);
                updateSchedule.put("Describe",Describe);
                updateSchedule.put("Date",date);
                updateSchedule.put("StartTime",setStartTime);
                updateSchedule.put("EndTime",setEndTime);

                documentReference.set(updateSchedule, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                scheduleList.showScheduleList();
                                Log.d("updateDetailSchedule","Successful:User Profile is created for " + UserID);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.w("updateDetailSchedule","Fail:"+e.getMessage());
                            }
                        });


                dialog.dismiss();
            }
        });
        cancelDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private String makeTimeString(int hour, int minute) {
        return hour+":"+minute;
    }

    private String makeDateString(int year, int month, int day) {
        return year + "年" + month + "月" + day + "日";
    }



    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //綁畫面和資料

        holder.Title.setText(modelList.get(position).getTile());
        holder.startTime.setText(modelList.get(position).getStartTime());
        holder.endTime.setText(modelList.get(position).getEndTime());
        holder.Location.setText(modelList.get(position).getLocation());
        holder.Description.setText(modelList.get(position).getDescription());
        holder.Date.setText(modelList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
