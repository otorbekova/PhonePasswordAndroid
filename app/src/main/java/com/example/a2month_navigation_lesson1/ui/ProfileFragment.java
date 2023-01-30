package com.example.a2month_navigation_lesson1.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a2month_navigation_lesson1.Prefs;
import com.example.a2month_navigation_lesson1.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private ImageView imageView, imageEdit;
    private TextView textName;
    private Prefs prefs;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.imageView);
        imageEdit = view.findViewById(R.id.imageViewEdit);
        textName = view.findViewById(R.id.textName);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);// progressBar
        prefs = new Prefs(requireActivity());
        showAvatar();
        setListeners();
        textName.setText(prefs.getName());

       // if (prefs.getName().isEmpty()) {
            getNameFromFB();
        //} // if not name

    }

    private void setListeners() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requesPermission();
            }
        });

        imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_profileFragment_to_editFragment);
            }
        });
       // getParentFragmentManager().setFragmentResultListener("edit",getViewLifecycleOwner(),requesPermission(););
        getParentFragmentManager().setFragmentResultListener("edit", getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Log.e("RESULT", "requestKey" + requestKey);
                String name = result.getString("name");
                prefs.saveName(name);
                saveName(name); // save FireBase
                textName.setText(name);
            }
        });
        textName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void requesPermission() {
        //have razreshenie or not
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 102) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK); //vybor
        intent.setType("image/*"); // support only image
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 101) {
            Uri uri = data.getData();
            // imageView.setImageURI(uri);
            Glide.with(this).load(uri).circleCrop().into(imageView);
            uploadToFb(uri);
        }
    }

    private void uploadToFb(Uri uri) { // dowland in storeg
        progressBar.setVisibility(View.VISIBLE);
        String userId = FirebaseAuth.getInstance().getUid();//get user Id
      final  StorageReference reference = FirebaseStorage.getInstance().getReference().child("images/" + userId + ".png");// связка
        reference.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Log.e("TAG", "uri:" + task.getResult());
                saveAvatarUrl(task.getResult().toString());
              //  if (task.isSuccessful()) {
                    Toast.makeText(requireContext(), "Success"+task.isSuccessful(), Toast.LENGTH_SHORT).show();
                //} else {
                    //Toast.makeText(requireContext(), "Not success", Toast.LENGTH_SHORT).show();
                //}
                progressBar.setVisibility(View.GONE);
            }
        });

       /* reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() { //загрузили или нет
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    Toast.makeText(requireContext(),"Success",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(requireContext(),"Not success",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
*/
    }

    private void getNameFromFB() {
        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("users").document(userId)
                /* .addSnapshotListener(new EventListener<DocumentSnapshot>() { var 1
                     @Override
                     public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                         String name=value.getString("name");
                         textName.setText(name);
                     }
                 });*/
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name = documentSnapshot.getString("name");
                        String avatar = documentSnapshot.getString("avatar");
                        textName.setText(name);
                        prefs.saveName(name);
                        prefs.saveAvatarUrl(avatar);
                        showAvatar();
                    }
                });
    }
  //  https://www.google.com/search?rlz=1C1GCEA_enKG973KG973&sxsrf=ALiCzsbBv4wntULJ5atU4zP9oAc08UIufQ:1672832084810&q=photo&tbm=isch&sa=X&ved=2ahUKEwj4_LuR6a38AhXqh4sKHVzCBGcQ0pQJegQICBAB&biw=1517&bih=694&dpr=0.9#imgrc=xGzD43MFFHxh9M

    private void showAvatar() {
        Glide.with(this)
                .load(prefs.getAvatarUrl())
                .circleCrop()
                .into(imageView);
    }

    private void saveAvatarUrl(String url) {
        prefs.saveAvatarUrl(url);
        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("users").document(userId)
                .update("avatar", url)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(requireContext(), "Result: " + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveName(String name) {
        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .update("name", name)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(requireContext(), "Result: " + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}