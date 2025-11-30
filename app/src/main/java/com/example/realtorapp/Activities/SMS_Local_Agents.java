package com.example.realtorapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

    ListView listView;
    ArrayList<SMS_Agent> agentList;
    SMS_AgentAdapter adapter;
    DatabaseReference databaseReference;

    public SMS_Local_Agents() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_s_m_s__local__agents, container, false);

        listView = view.findViewById(R.id.listViewLocal);
        agentList = new ArrayList<>();

        adapter = new SMS_AgentAdapter(getActivity(), agentList);
        listView.setAdapter(adapter);

        // Handle clicks on agent items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SMS_Agent clickedAgent = agentList.get(position);

                // Pass details to the next screen
                Intent intent = new Intent(getActivity(), SMS_AgentDetailsActivity.class);
                intent.putExtra("name", clickedAgent.name);
                intent.putExtra("bio", clickedAgent.bio);
                intent.putExtra("rating", clickedAgent.rating);
                intent.putExtra("image", clickedAgent.imageUrl);

                startActivity(intent);
            }
        });

        // Fetch data from Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Shukan").child("agents");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                agentList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    try {
                        SMS_Agent agent = data.getValue(SMS_Agent.class);
                        if (agent != null) {
                            agentList.add(agent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
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