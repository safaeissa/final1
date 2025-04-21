package com.example.final1.MainPages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.final1.FirebaseServices;
import com.example.final1.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthFragment extends Fragment {
    private ImageButton b;
    private TextView t1,t2,t3,t4,t5,t6;
    private FirebaseServices fbs;
    private Switch sw;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HealthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthFragment newInstance(String param1, String param2) {
        HealthFragment fragment = new HealthFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_health, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        coneect();
    }
    public void coneect() {
        fbs = new FirebaseServices();
        b=getView().findViewById(R.id.btnBackHealth);
        sw=getView().findViewById(R.id.Female);
        Boolean Famele=sw.isChecked();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main, new HomeFragment());
                transaction.commit();
            }
        });
        t1=getView().findViewById(R.id.BMI);
        t2=getView().findViewById(R.id.CalToGainWeight);
        t3=getView().findViewById(R.id.CalToLoseWeight);
        t4=getView().findViewById(R.id.PerfectWeight);
        t5=getView().findViewById(R.id.Decide);
        t6=getView().findViewById(R.id.days);
        UpDate(false);
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
           UpDate(true);
            else
                UpDate(false);
        });


    }
    public  void UpDate (Boolean isFamele)
    {
        String email = "";
        FirebaseUser user = fbs.getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String weight1 = document.getString("weight").toString();
                                String length1 = document.getString("length").toString();
                                String age1 = document.getString("age").toString();
                                Double BMR = BMR(weight1, length1, age1, isFamele);
                                t1.setText("Your BMI is " + BmiCount(weight1, length1));
                                t2.setText("To gain weight, you have to eat " + (BMR + 500) + "calories");
                                t3.setText("To lose weight, you have to eat " + (BMR - 500) + "calories");
                                double perfectWeight = perfectWeight(length1,weight1,isFamele);
                                t4.setText("Your perfect weight is " + perfectWeight);
                                if (BmiCount(weight1, length1) > 25) {
                                    t5.setText("To get perfect you need to lose weight");
                                    t6.setText("To reach your goal, you need to eat" + (BMR - 500) + "calories within " + perfectDay(weight1, length1, isFamele) + " days");
                                } else if (BmiCount(weight1, length1) < 18.5) {
                                    t5.setText("To get perfect you need to gain weight");
                                    t6.setText("To reach your goal, you need to eat" + (BMR + 500) + "calories within " + perfectDay(weight1, length1, isFamele) + " days");
                                } else {
                                    t5.setText("You are perfect");
                                    t6.setText("To save your Weight you need to eat  " + BMR + "calories");
                                }
                            }
                        }
                    });
        }
    }
    public double perfectWeight(String h, String w, Boolean isFemale) {
        double height = extractNumbersFromText(h) / 100.0;
        double idealBMI = isFemale ? 21.0 : 22.0;
        return idealBMI * height * height;
    }

    public Integer perfectDay(String w, String h, Boolean isfamele) {
        double height = extractNumbersFromText(h) / 100.0;
        double perfectWeight = perfectWeight(h, w, isfamele);
        double currentWeight = extractNumbersFromText(w);
        return (int) ((Math.abs(perfectWeight - currentWeight) * 7700) / 500);
    }



    public double BmiCount(String w, String h) {
        double weight = extractNumbersFromText(w);
        double height = extractNumbersFromText(h);
        double bmi = weight / Math.pow(height / 100.0, 2);
        return bmi;
    }


    public double BMR(String w, String h, String a, Boolean isFemale) {
        double weight = extractNumbersFromText(w);
        double height = extractNumbersFromText(h);
        double age = extractNumbersFromText(a);
        if (!isFemale)
            return 66 + (13.7 * weight) + (5 * height) - (6.8 * age);
        else
            return 665 + (9.6 * weight) + (1.8 * height) - (4.7 * age);
    }



    public Integer extractNumbersFromText(String text) {
        StringBuilder result = new StringBuilder();
        String digits = text.replaceAll("[^0-9]", "");
        for (int i = 0; i < digits.length(); i++) {
            result.append(digits.charAt(i));
        }
        // تحويل النتيجة إلى رقم من نوع Integer
        try {
            return Integer.parseInt(result.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1; // إرجاع قيمة افتراضية في حال حدوث خطأ
        }
    }
    }
