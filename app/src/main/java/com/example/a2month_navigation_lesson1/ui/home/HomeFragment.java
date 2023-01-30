package com.example.a2month_navigation_lesson1.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a2month_navigation_lesson1.App;
import com.example.a2month_navigation_lesson1.R;
import com.example.a2month_navigation_lesson1.interfacess.OnItemClickListenerr;
import com.example.a2month_navigation_lesson1.models.Task;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private TaskAdapter adapter;
    private ArrayList<Task> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        App.getInstance().getDatabase().taskDao().getAllLive() //vse slushaet
                .observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
                    @Override
                    public void onChanged(List<Task> tasks) {
                        list.clear();
                        list.addAll(tasks);
                        adapter.notifyDataSetChanged();
                    }
                });

//if(list.isEmpty()){
        // list.addAll(App.getInstance().getDatabase().taskDao().getAll());
//}

        adapter = new TaskAdapter(list);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        /**  getParentFragmentManager().setFragmentResultListener("w", getViewLifecycleOwner(), new FragmentResultListener(){
        @Override public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        Task task = (Task) result.getSerializable ("form");
        list.add(task);
        adapter.notifyDataSetChanged();
        }
        });*/

        adapter.setListener(new OnItemClickListenerr() {
            @Override
            public void onClicklElement(int position) {
                Task task = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("task", task);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.formFragment, bundle);
            }

            @Override
            public void onItemLongClick(int postion) {
                showAlert(list.get(postion));
            }
        });
    }

    private void showAlert(final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Вы хотите удвлить " + task.getTitle() + "?")
                .setNegativeButton("Отмена", null)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getInstance().getDatabase().taskDao().delete(task);
                    }
                });
        builder.show();
    }
}