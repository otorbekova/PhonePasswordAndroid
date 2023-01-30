package com.example.a2month_navigation_lesson1.ui.firestore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2month_navigation_lesson1.R;
import com.example.a2month_navigation_lesson1.models.Task;
import com.example.a2month_navigation_lesson1.ui.home.TaskAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FirestoreFragment extends Fragment {
    private TaskAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Task> list = new ArrayList<>();
    private ListenerRegistration listenerRegistration;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_firestore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        inistList();
    }

    private void inistList() {
        adapter = new TaskAdapter(list);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        loadData();
    }

    private void loadData() { //read
        FirebaseFirestore.getInstance().collection("tasks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null) {
                                list.clear();
                                for (QueryDocumentSnapshot snapshot : task.getResult()) { //get id
                                    String dicId = snapshot.getId();
                                    Task mTask = snapshot.toObject(Task.class);
                                    mTask.setDocId(dicId);
                                    list.add(mTask);
                                }
                                // list.addAll(task.getResult().toObjects(Task.class));
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData2();
    }

    private void loadData2() {
        listenerRegistration = FirebaseFirestore.getInstance().collection("tasks")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            list.clear();
                            list.addAll(value.toObjects(Task.class));
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
}