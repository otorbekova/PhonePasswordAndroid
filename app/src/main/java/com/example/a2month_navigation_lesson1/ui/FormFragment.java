package com.example.a2month_navigation_lesson1.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a2month_navigation_lesson1.App;
import com.example.a2month_navigation_lesson1.R;
import com.example.a2month_navigation_lesson1.models.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.a2month_navigation_lesson1.R.id.nav_home;

public class FormFragment extends Fragment {
    private Task task;
    private EditText editTitle, editDesk;

    public FormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTitle = view.findViewById(R.id.editTitle);
        editDesk = view.findViewById(R.id.editDesk);

            if (getArguments() != null) { //rto Activity
            task = (Task) getArguments().getSerializable("task");
            if (task != null) {
                editTitle.setText(task.getTitle());//устанавливаем его значение
                editDesk.setText(task.getDesc());
            }
        }

        view.findViewById(R.id.fabb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titl = editTitle.getText().toString().trim(); //trim()- по крайам пробелы убирает
                String des = editDesk.getText().toString().trim();
                if (task == null) {
                    task = new Task(titl, des);
                    App.getInstance().getDatabase().taskDao().insert(task);// для записи if not zapis
                    saveToFirestore(task);
                } else {
                    task.setTitle(titl);
                    task.setDesk(des);
                    App.getInstance().getDatabase().taskDao().update(task);// для записи if have zapis
                }


//Bundle bundle=new Bundle();
//bundle.putSerializable("form",task);
//getParentFragmentManager().setFragmentResult("w", bundle);//otpravka send
                close();
                // getParentFragmentManager().popBackStack();
            }
        });
    }

    private void close() {
        NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        controller.popBackStack();
    }

    private void saveToFirestore(Task task) {
        /** Map<String, String> map = new HashMap<>();
         map.put("title", task.getTitle());
         map.put("desc", task.getDesc());*/
        //zapis
        FirebaseFirestore.getInstance().collection("tasks").add(task)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            close();
                        } else {
                            Toast.makeText(requireContext(), "Erorr write", Toast.LENGTH_SHORT).show();

                        }
                    }
                });//name table
    }
}