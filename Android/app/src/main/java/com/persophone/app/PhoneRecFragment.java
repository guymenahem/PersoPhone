package com.persophone.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.persophone.persophone_bottom.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhoneRecFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhoneRecFragment#newInstance} factory method to
 * create an instance of this fragment.
 */



public class PhoneRecFragment extends Fragment{

    private static final String PHONE_NAME_PARAM = "PHONE_NAME";


    private OnFragmentInteractionListener mListener;

    String name;


    public PhoneRecFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PhoneRecFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhoneRecFragment newInstance(String name) {
        PhoneRecFragment fragment = new PhoneRecFragment();
        Bundle args = new Bundle();
        // add arguments to the fragment
        args.putString(PHONE_NAME_PARAM,name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.name = getArguments().getString(PHONE_NAME_PARAM);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_phone_rec, container, false);
        setPhone(inf);
        return inf;
    }

    public void setPhone(View v){
        switch (this.name){
            case "Nexus 6p":
                ((TextView)v.findViewById(R.id.phone_name)).setText("nexus 6p");
                ((TextView)v.findViewById(R.id.screen_value)).setText("5.7");
                ((TextView)v.findViewById(R.id.screen_upgrade)).setText("(+15%)");
                ((TextView)v.findViewById(R.id.memory_value)).setText("3GB");
                ((TextView)v.findViewById(R.id.memory_upgrade)).setText("(+50%)");
                ((TextView)v.findViewById(R.id.storage_value)).setText("64GB");
                ((TextView)v.findViewById(R.id.storage_upgrade)).setText("(+100%)");
                ((TextView)v.findViewById(R.id.battery_value)).setText("3,450 mAh");
                ((TextView)v.findViewById(R.id.battery_upgrade)).setText("(+17%)");
                break;

            case "Iphone 7":
                ((ImageView)v.findViewById(R.id.phone_image)).setImageResource(R.drawable.iphone7);
                ((TextView)v.findViewById(R.id.phone_name)).setText("Iphone 7");
                ((TextView)v.findViewById(R.id.screen_value)).setText("4.7");
                ((TextView)v.findViewById(R.id.screen_upgrade)).setText("(+7%)");
                ((TextView)v.findViewById(R.id.memory_value)).setText("2GB");
                ((TextView)v.findViewById(R.id.memory_upgrade)).setText("(0)");
                ((TextView)v.findViewById(R.id.storage_value)).setText("128GB");
                ((TextView)v.findViewById(R.id.storage_upgrade)).setText("(+400%)");
                ((TextView)v.findViewById(R.id.battery_value)).setText("1,960 mAh");
                ((TextView)v.findViewById(R.id.battery_upgrade)).setText("(0%)");
                break;

            case "Galaxy S7":
                ((ImageView)v.findViewById(R.id.phone_image)).setImageResource(R.drawable.galaxy7s);
                ((TextView)v.findViewById(R.id.phone_name)).setText("Galaxy 7S");
                ((TextView)v.findViewById(R.id.screen_value)).setText("5.1");
                ((TextView)v.findViewById(R.id.screen_upgrade)).setText("(+12%)");
                ((TextView)v.findViewById(R.id.memory_value)).setText("4GB");
                ((TextView)v.findViewById(R.id.memory_upgrade)).setText("(+100%)");
                ((TextView)v.findViewById(R.id.storage_value)).setText("32GB");
                ((TextView)v.findViewById(R.id.storage_upgrade)).setText("(0%)");
                ((TextView)v.findViewById(R.id.battery_value)).setText("3,000 mAh");
                ((TextView)v.findViewById(R.id.battery_upgrade)).setText("(+8%)");
                break;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //hrow new RuntimeException(context.toString()
            //        + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public CharSequence getItemText(){

        if (getArguments() != null) {
            this.name = getArguments().getString(PHONE_NAME_PARAM);
        }

        return this.name;
    }
}
