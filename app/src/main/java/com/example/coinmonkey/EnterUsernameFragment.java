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
 * Use the {@link EnterUsernameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterUsernameFragment extends Fragment {

    EditText username;
    Button confirm,cancel;
    DatabaseHelper myDB;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EnterUsernameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnterUsernameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnterUsernameFragment newInstance(String param1, String param2) {
        EnterUsernameFragment fragment = new EnterUsernameFragment();
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

        username = getActivity().findViewById(R.id.forgotPasswordUsernameTextView);
        confirm = getActivity().findViewById(R.id.confirmForgotUsernameButton);
        cancel = getActivity().findViewById(R.id.cancelForgotPassword);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDB = new DatabaseHelper(getActivity());
                if(username.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "The username can not be empty!", Toast.LENGTH_SHORT).show();
                }
                else if(myDB.getUser(username.getText().toString()).getCount() <= 0){
                    Toast.makeText(getActivity(), "The user with the supplied username could not be found!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Cursor cursor = myDB.getUser(username.getText().toString());
                    cursor.moveToFirst();
                    String username = cursor.getString(0);
                    Bundle bundle = new Bundle();
                    bundle.putString("username",username);
                    SecurityQuestionFragment securityQuestionFragment = new SecurityQuestionFragment();
                    securityQuestionFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.forgotPasswordFrameLayout,securityQuestionFragment).commit();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),MainActivity.class);
                startActivity(i);
                getActivity().finishAffinity();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enter_username, container, false);
    }
}