package xyz.nokturnal.barka.privateeye;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    DatabaseReference activityListReference = fbDatabase.getReference("reports");

    List<ReportsJO> reportsJOList;
    //ReportListAdapter reportListAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reportsJOList = new ArrayList<>();
        recyclerView = findViewById(R.id.lvList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadReports();
    }

    public void loadReports()
    {
        activityListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reportsJOList.clear();

                for(DataSnapshot postSnap : dataSnapshot.getChildren())
                {

                    ReportsJO reportsJO = dataSnapshot.getChildren().iterator().next().getValue(ReportsJO.class);
                    reportsJOList.add(reportsJO);

                    ReportListAdapter reportListAdapter = new ReportListAdapter(reportsJOList, getBaseContext());
                    recyclerView.setAdapter(reportListAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
