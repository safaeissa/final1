package com.example.final1.MainPages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.final1.MainPages.RecipePage.RecipeListFragment;
import com.example.final1.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SportFragment extends Fragment {
    private ImageView BackToHome;
    private WebView webUpper;
    private WebView webCoreAbs;
    private WebView webLower;
    private WebView webCARDIO;
    private WebView WEBLOWER2;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SportFragment newInstance(String param1, String param2) {
        SportFragment fragment = new SportFragment();
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
        return inflater.inflate(R.layout.fragment_sport, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        connect();
    }

    public void connect ()
    {
        BackToHome= getView().findViewById(R.id.BackToHomeFromSport);
        webUpper= getView().findViewById(R.id.webUpper);
        webCoreAbs= getView().findViewById(R.id.webCoreAbs);
        webLower= getView().findViewById(R.id.webLower);
        WEBLOWER2= getView().findViewById(R.id.webLower2);
        webCARDIO= getView().findViewById(R.id.webCARDIO);
        BackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction= getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.main ,new HomeFragment());
                transaction.commit();
            }
        });
String videoUpper="<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/GViX8riaHX4?si=Mwln8A0z4c0C4_vp\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
webUpper.loadData(videoUpper, "text/html", "utf-8");
String videoCoreAbs="<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/1919eTCoESo?si=dIGAkapl4gkkOBKd\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
webCoreAbs.loadData(videoCoreAbs, "text/html", "utf-8");
String videoLower="<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/7I-c-yw5ZrQ?si=xK-NOjlP87n-9M28\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
webLower.loadData(videoLower, "text/html", "utf-8");
String videoLower2="<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/CacfEBsSL1Y?si=ffRUGWBxgoTslQqU\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
WEBLOWER2.loadData(videoLower2, "text/html", "utf-8");
String videoCARDIO="<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/ml6cT4AZdqI?si=Ok7RTAhbAjnnHQw-\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
webCARDIO.loadData(videoCARDIO, "text/html", "utf-8");





    }
}