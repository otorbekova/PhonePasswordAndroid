package com.example.a2month_navigation_lesson1.ui.onboard;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a2month_navigation_lesson1.Prefs;
import com.example.a2month_navigation_lesson1.R;

import static android.content.Context.MODE_PRIVATE;

public class BordFragment extends Fragment {


    public BordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bord, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager=view.findViewById(R.id.viewPager);
        PagerAdapter adapter=new PagerAdapter();
        viewPager.setAdapter(adapter);

        adapter.setOnStartClickListener(new PagerAdapter.OnStartClickListener() {
            @Override
            public void onClick() {
new Prefs(requireActivity()).isShown(true);
                NavController controller= Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
                controller.popBackStack();
            }
        });

        OnBackPressedCallback callback=new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),callback);
    }
}