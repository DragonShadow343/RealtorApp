package com.example.realtorapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.realtorapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SMS_Local_Agents extends Fragment {

    ListView listViewLocal, listViewAll;
    ImageButton btnBackHome; // Back Button
    ArrayList<SMS_Agent> localList, allList;
    SMS_AgentAdapter localAdapter, allAdapter;
    DatabaseReference databaseReference;

    public SMS_Local_Agents() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_s_m_s__local__agents, container, false);

        // Initialize Views
        listViewLocal = view.findViewById(R.id.listViewLocal);
        listViewAll = view.findViewById(R.id.listViewAllExperts);
        btnBackHome = view.findViewById(R.id.btnBackHome);

        localList = new ArrayList<>();
        allList = new ArrayList<>();

        localAdapter = new SMS_AgentAdapter(getActivity(), localList);
        allAdapter = new SMS_AgentAdapter(getActivity(), allList);

        listViewLocal.setAdapter(localAdapter);
        listViewAll.setAdapter(allAdapter);

        // Back Button Logic
        btnBackHome.setOnClickListener(v -> {
            // Assuming HomeActivity is the main home screen
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            // Clear flags so pressing back doesn't loop
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            // Optional: finish() current activity if this fragment is inside one that should close
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        // Click Listener for Agents
        AdapterView.OnItemClickListener listener = (parent, v, position, id) -> {
            SMS_Agent clickedAgent = (SMS_Agent) parent.getItemAtPosition(position);
            Intent intent = new Intent(getActivity(), SMS_AgentDetailsActivity.class);
            intent.putExtra("name", clickedAgent.name);
            intent.putExtra("bio", clickedAgent.bio);
            intent.putExtra("rating", clickedAgent.rating);
            intent.putExtra("image", clickedAgent.imageUrl);
            startActivity(intent);
        };

        listViewLocal.setOnItemClickListener(listener);
        listViewAll.setOnItemClickListener(listener);

        // Retrieve agents from Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Shukan").child("agents");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                localList.clear();
                allList.clear();

                int count = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    try {
                        SMS_Agent agent = data.getValue(SMS_Agent.class);
                        if (agent != null) {
                            // Top 3 agents go to Local/Top Experts section
                            if (count < 3) {
                                localList.add(agent);
                            } else if (count < 20) {
                                // Agents 4 to 20 go to All Experts section
                                allList.add(agent);
                            }
                            count++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                localAdapter.notifyDataSetChanged();
                allAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}