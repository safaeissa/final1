package com.example.final1.SignInLogInForget;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final1.FirebaseServices;
import com.example.final1.MainActivity;
import com.example.final1.MainPages.HomeFragment;
import com.example.final1.R;
import com.example.final1.Users.User;
import com.example.final1.Utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddDataFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE =123 ;
     ImageView img;
    private EditText Name;
    private EditText Weight;
    private EditText height;
    private EditText Age;
    private FirebaseServices auth;
    private TextView Start;
    private Utils msg ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddDataFragment newInstance(String param1, String param2) {
        AddDataFragment fragment = new AddDataFragment();
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
        return inflater.inflate(R.layout.fragment_add_data, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
         conect();
    }
    public void conect ()
    {
        msg=Utils.getInstance();
        img=getView().findViewById(R.id.imageViewProfile);
        auth=FirebaseServices.getInstance();
        Name=getView().findViewById(R.id.textName);
        Weight=getView().findViewById(R.id.etWeight);
        height=getView().findViewById(R.id.etHeight);
        Age=getView().findViewById(R.id.etage);
        Start=getView().findViewById(R.id.btnStart);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addToFirestore();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
              openGallery();
            }
        });
        ((MainActivity)getActivity()).pushFragment(new AddDataFragment());
    }
    public void addToFirestore()
    {
        String name1, weight1, height1, age1;

        name1 = Name.getText().toString();
        weight1 = Weight.getText().toString();
        height1 = height.getText().toString();
        age1 = Age.getText().toString();


        if (name1.isEmpty() || weight1.isEmpty() || height1.isEmpty() || age1.isEmpty()) {
            Toast.makeText(getActivity(), " something is wrong", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri selectedImageURL = auth.getSelectedImageURL();
        String imgUrl = "";
        if( selectedImageURL!=null)
        imgUrl=selectedImageURL.toString();

       User user =new User(imgUrl, name1, weight1, height1, age1);
       auth.getFire().collection("user").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getActivity(), "Welcome!!", Toast.LENGTH_SHORT).show();
                gotohome();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            img.setImageURI(selectedImageUri);
            Utils.getInstance().uploadImage(getActivity(), selectedImageUri);
        }
    }
private void gotohome()
{
    FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
    transaction.replace(R.id.main ,new HomeFragment());
    transaction.commit();
    setNavigationBarVisible();
}
private void setNavigationBarVisible ()
{
    ((MainActivity)getActivity()).getBottomNavigationView().setVisibility(View.VISIBLE);
}
}