package com.example.coinmonkey;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResetPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPasswordFragment extends Fragment {

    EditText newPassword,confirmNewPassword;
    Button confirm,cancel;
    DatabaseHelper myDB;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForgotPasswordChangePassword.
     */
    // TODO: Rename and change types and number of parameters
    public static ResetPasswordFragment newInstance(String param1, String param2) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newPassword = getActivity().findViewById(R.id.newPasswordResetEditText);
        confirmNewPassword = getActivity().findViewById(R.id.newPasswordResetConfirmEditText);
        confirm = getActivity().findViewById(R.id.resetPasswordButton);
        cancel = getActivity().findViewById(R.id.cancelResetPasswordButton);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newPassword.getText().toString().trim().isEmpty() || confirmNewPassword.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), "The password values can not be left empty!", Toast.LENGTH_SHORT).show();
                }
                else if(!newPassword.getText().toString().equals(confirmNewPassword.getText().toString())){
                    Toast.makeText(getActivity(), "The passwords do not match!", Toast.LENGTH_SHORT).show();
                }
                else{
                    String username = getUsername();
                    myDB = new DatabaseHelper(getActivity());
                    myDB.updatePassword(username,newPassword.getText().toString());
                    Toast.makeText(getActivity(), "Your Password has been successfully reset!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(),MainActivity.class);
                    startActivity(i);
                    getActivity().finishAffinity();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnterUsernameFragment enterUsernameFragment = new EnterUsernameFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.forgotPasswordFrameLayout,enterUsernameFragment).commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password_change_password, container, false);
    }

    public String getUsername(){
        Bundle bundle = this.getArguments();
        String username = bundle.getString("username");
        return username;
    }
}