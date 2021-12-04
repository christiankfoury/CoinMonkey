package com.example.coinmonkey;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecurityQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecurityQuestionFragment extends Fragment {

    TextView securityQuestion;
    EditText securityAnswer;
    Button answer,cancel;
    DatabaseHelper myDB;
    String security_answer,username = "bob";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SecurityQuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecurityQuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecurityQuestionFragment newInstance(String param1, String param2) {
        SecurityQuestionFragment fragment = new SecurityQuestionFragment();
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
        securityQuestion = getActivity().findViewById(R.id.securityQuestionTextView);
        securityAnswer = getActivity().findViewById(R.id.securityAnswerEditText);
        answer = getActivity().findViewById(R.id.submitSecurityAnswerButton);
        cancel = getActivity().findViewById(R.id.cancelSecurityAnswerButton);

        setSecurityQuestion();

        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(securityAnswer.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(),"Security Answer can not be empty!",Toast.LENGTH_SHORT).show();
                }
                else if(!securityAnswer.getText().toString().equals(security_answer)){
                    Toast.makeText(getActivity(),"The Security Answer is incorrect!",Toast.LENGTH_SHORT).show();
                }
                else{
                    Bundle bundle = new Bundle();
                    String user = username;
                    bundle.putString("username",user);
                    ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
                    resetPasswordFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.forgotPasswordFrameLayout, resetPasswordFragment).commit();
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
        return inflater.inflate(R.layout.fragment_security_question, container, false);
    }

    public void setSecurityQuestion(){
        myDB = new DatabaseHelper(getActivity());
        Bundle bundle = this.getArguments();
        String username = bundle.getString("username");
        Cursor cursor = myDB.getUser(username);
        cursor.moveToFirst();
        String security_question = cursor.getString(5);
        String securityAnswer = cursor.getString(6);
        securityQuestion.setText(security_question);
        security_answer = securityAnswer;
        this.username = username;
    }
}