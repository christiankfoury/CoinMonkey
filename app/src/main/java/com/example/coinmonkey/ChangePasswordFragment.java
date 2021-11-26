package com.example.coinmonkey;

import android.content.Context;
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
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {

    EditText currentPassword,newPassword,newPasswordConfirm;
    Button changePassword, returnButtonChangePassword;
    DatabaseHelper myDB;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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

        currentPassword = getActivity().findViewById(R.id.currentPasswordEditText);
        newPassword = getActivity().findViewById(R.id.newPasswordEditText);
        newPasswordConfirm = getActivity().findViewById(R.id.newPasswordConfirmEditText);
        changePassword = getActivity().findViewById(R.id.changePasswordButton);
        returnButtonChangePassword = getActivity().findViewById(R.id.returnButtonChangePassword);


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = (User) getActivity().getIntent().getSerializableExtra("user");

                if(!user.getPassword().equals(currentPassword.getText().toString())){
                    Toast.makeText(getActivity(),"The current password input is not correct",Toast.LENGTH_SHORT).show();
                }
                else if(!newPassword.getText().toString().equals(newPasswordConfirm.getText().toString())){
                    Toast.makeText(getActivity(),"The new password and password confirmation are not identical",Toast.LENGTH_SHORT).show();
                }
                else{
                    myDB = new DatabaseHelper(getActivity());
                    myDB.updatePassword(user.getUsername(),newPassword.getText().toString());
                    Toast.makeText(getActivity(),"Your Password has been updated",Toast.LENGTH_SHORT).show();
                }
            }
        });

        returnButtonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsActivity.fragmentManager.beginTransaction().replace(R.id.frameLayout, new SettingsFragment(), null).commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }
}