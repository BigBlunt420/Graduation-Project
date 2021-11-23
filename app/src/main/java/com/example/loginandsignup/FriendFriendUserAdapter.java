//package com.example.loginandsignup;
//
//import android.app.AlertDialog;
//import android.app.DatePickerDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//
//public class FriendFriendUserAdapter extends RecyclerView.Adapter<FriendFriendViewHolder> implements AdapterView.OnItemSelectedListener{
//    FriendFriendSchedule friendFriendSchedule;
//    List<Model> modelList;
//    Context context;
//    FirebaseFirestore firestoredb;
//    FirebaseAuth firebaseAuth;
//    private TextView inputStartTime,inputEndTime;
//    private int starthour,startminute,endhour,endminute;
//    int setYear,setMonth,setDay,month;
//    private TextView inputDate;
//    DatePickerDialog datePickerDialog;
//    private Button addDetail,cancelDetail;
//    String UserID;
//    private EditText inputTile,inputDescribe;
//    String date;
//    String dbtitle,dbstartTime,dbendTime,dblocation,dbdescription,dbdate,dbid;
//    String setStartTime,setEndTime;
//    private Spinner inputParameter;
//    String choice;
//
//    public FriendFriendUserAdapter(FriendFriendSchedule friendFriendSchedule, List<Model> modelList) {
//        this.friendFriendSchedule = friendFriendSchedule;
//        this.modelList = modelList;
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
//
//    @NonNull
//    @NotNull
//    @Override
//    public FriendFriendViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
//        //inflate layout
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_layout,parent,false);
//
//        //處理 item click 事件
//        FriendFriendViewHolder viewHolder = new FriendFriendViewHolder(itemView);
//        viewHolder.setOnClickListener(new FriendFriendViewHolder.ClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                //click
//
//                //show data
////                String dbid = modelList.get(position).getId();
//                String title = modelList.get(position).getTile();
//                String startTime = modelList.get(position).getStartTime();
//                String endTime = modelList.get(position).getEndTime();
//                String location = modelList.get(position).getLocation();
//                String description = modelList.get(position).getDescription();
//                String date = modelList.get(position).getDate();
////
////                firebaseAuth = FirebaseAuth.getInstance();
////                UserID = firebaseAuth.getCurrentUser().getUid();
////                //Toast.makeText(scheduleList, dbid,Toast.LENGTH_LONG).show();
//                Toast.makeText(friendFriendSchedule, title+"  "+date+"\n"+startTime+"-"+endTime+"  "+location+"\n"+description,Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onItemLongClick(View view, final int position) {
//                //long click
//                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//
//                String [] action = {"修改資料","刪除資料"};
//                builder.setItems(action, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == 0){
//                            //修改資料
//                            dbid = modelList.get(position).getId();
//                            dbtitle = modelList.get(position).getTile();
//                            dbstartTime = modelList.get(position).getStartTime();
//                            dbendTime = modelList.get(position).getEndTime();
//                            dblocation = modelList.get(position).getLocation();
//                            dbdescription = modelList.get(position).getDescription();
//                            dbdate = modelList.get(position).getDate();
//
//
//                            updateDetailSchedule();
//                        }
//                        if (which == 1){
//                            //刪除資料
//                            dbid = modelList.get(position).getId();
//                            deleteData(dbid);
//                        }
//                    }
//                }).create().show();
//            }
//        });
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull @NotNull FriendFriendViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//}
