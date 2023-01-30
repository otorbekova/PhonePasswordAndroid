package com.example.a2month_navigation_lesson1.ui.auth;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2month_navigation_lesson1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class PhoneFragment extends Fragment {
    private EditText editPhone, editCode;
    private View viewPhone;
    private View viewCode;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private boolean isCodeSend;
    private String veryficationId, kol;
    private TextView timer;
    private int seconds = 0;
    private boolean isRunning = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    new CountDownTimer(60000, 1000) {
        //      @Override
        //    public void onTick(long millisUntilFinished) {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            // написать сами и подвердить но это автоматом подверждает здесь
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.e("TAG", "onVeryficationFailder:" + phoneAuthCredential.getSmsCode());
                if (isCodeSend) {
                    editCode.setText(phoneAuthCredential.getSmsCode());
                } else {
                    signIn(phoneAuthCredential);//хранит все инфор.. пользователя
                }


            }

            @Override
            // ошибка  серитификация
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("TAG", "onVeryficationFailder:" + e.getMessage());

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                //дает что смс отправлен
                isCodeSend = true;
                veryficationId = s;
                showViewCode();

                // runtimerr();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                showViewPhone();
            }
        };
        //  timer.setText("00:00:" + millisUntilFinished / 1000);
    }

    /**
     * @Override public void onFinish() {
     * showViewPhone();
     * Toast.makeText(requireActivity(), "Время вышлo попробуйте еще раз", Toast.LENGTH_LONG).show();
     * }
     * }.start();
     */


    private void signIn(PhoneAuthCredential phoneAuthCredential) { // создает пользователя и проверяет
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            NavController controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                            controller.popBackStack();
                        } else {
                            Toast.makeText(requireActivity(), "Don't correct cod", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editPhone = view.findViewById(R.id.editPhone);
        editCode = view.findViewById(R.id.editCode);
        viewPhone = view.findViewById(R.id.viewPhone);
        viewCode = view.findViewById(R.id.viewCode);
        viewCode.setVisibility(View.GONE);
        timer = view.findViewById(R.id.timer);

        view.findViewById(R.id.btnContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSms();
            }


            private void requestSms() { //otpravit
                String phone = editPhone.getText().toString().trim();
                if (!phone.isEmpty()) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phone,
                            120, //second
                            TimeUnit.SECONDS,
                            requireActivity(),
                            callbacks);//узнать наш запрос выполнился или нет
                }
            }
        });

        view.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

    }

    private void verify() {
        String code = editCode.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(veryficationId, code);
        signIn(credential);
    }

    private void showViewCode() {
        viewCode.setVisibility(View.VISIBLE);

        viewPhone.setVisibility(View.GONE);
    }

    private void showViewPhone() {
        viewCode.setVisibility(View.GONE);
        viewPhone.setVisibility(View.VISIBLE);
    }

    private void StartTimer() {
        isRunning = true;
        runtimerr();
    }

    private void runtimerr() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int sec = seconds % 60;

                String ti = String.format(Locale.getDefault(), "8d:02d:%02d", hours, minutes, sec);
                timer.setText(ti);
                if (isRunning) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });

    }

    /**  private void cgeck(){
     new CountDownTimer(60000,1000){

    @Override public void onTick(long millisUntilFinished) {
    timer.setText("00:00: "+millisUntilFinished/1000);

    }

    @Override public void onFinish() {
    showViewPhone();
    }
    }.start();
     }*/

}