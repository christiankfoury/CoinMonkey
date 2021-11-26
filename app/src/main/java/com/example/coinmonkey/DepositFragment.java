package com.example.coinmonkey;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DepositFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DepositFragment extends Fragment {

    Button depositButton,returnToSettings;
    EditText input;
    TextView depositLabel;
    Context context = getActivity();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DepositFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DepositFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DepositFragment newInstance(String param1, String param2) {
        DepositFragment fragment = new DepositFragment();
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

        depositButton = getActivity().findViewById(R.id.depositButton);
        returnToSettings = getActivity().findViewById(R.id.returnButtonDeposit);
        input = getActivity().findViewById(R.id.depositEditText);
        depositLabel = getActivity().findViewById(R.id.depositLabel);

        User user = (User) getActivity().getIntent().getSerializableExtra("user");
        updateBalance(user);

        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    DatabaseHelper myDB = new DatabaseHelper(context);
                    // Saving user Deposit
                    double userDeposit = Double.parseDouble(input.getText().toString().trim());
                    //Making db object
                    myDB = new DatabaseHelper(context);
                    //getting the current Balance
                    double updatedBalance = user.getBalance();
                    //Updating the balance
                    updatedBalance += userDeposit;
                    //Updating the database balance
                    myDB.updateBalance(user.getUsername(),updatedBalance);
                    updateBalance(user);
                    Toast.makeText(context,"$" + userDeposit + " has been deposited into your balance",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(context,"The Deposit Amount has to be in the following format -> (0.00)",Toast.LENGTH_SHORT).show();
                }
            }
        });

//        returnToSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SettingsActivity.fragmentManager.beginTransaction().replace(R.id.frameLayout, new SettingsFragment(), null).commit();
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        returnToSettings.setOnClickListener(new View.OnClickListener() {
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
        View view = inflater.inflate(R.layout.fragment_deposit, container, false);
        return view;
    }

    public void updateBalance(User user){
        DatabaseHelper myDB = new DatabaseHelper(getActivity());
        Cursor cursor = myDB.getUser(user.getUsername());
        while(cursor.moveToNext()){
            user.setBalance(cursor.getDouble(4));
        }
        depositLabel.setText("$" + user.getBalance());
    }
}