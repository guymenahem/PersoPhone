package com.persophone.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.persophone.collector.PhonesData;
import com.persophone.persophone_bottom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecommendationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecommendationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecommendationFragment extends Fragment{

    // Swipe bar
    ViewPager mViewPager;
    TabLayout tabLayout;
    ReccomendationPagerAdapter pagerAdapter;
    ArrayList<PhoneRecFragment> recommendations;


    private OnFragmentInteractionListener mListener;

    public RecommendationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecommendationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecommendationFragment newInstance() {
        RecommendationFragment fragment = new RecommendationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillRecommendation();

    }

    public void onCreateSwipe(){
        mViewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout)getActivity().findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void fillRecommendation(){
        this.recommendations = new ArrayList<PhoneRecFragment>();
        final RecommendationFragment _this = this;

        pagerAdapter = new ReccomendationPagerAdapter(getFragmentManager(),_this);
        new PhonesData().GetRecomendedPhones(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject phone = response.getJSONObject(i);
                        _this.recommendations.add(PhoneRecFragment.newInstance(phone.getString("name"), phone));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //pagerAdapter = new ReccomendationPagerAdapter(getFragmentManager(),_this);
                pagerAdapter.notifyDataSetChanged();
            }
        });
    }

    public PhoneRecFragment getItem(int i){
        return this.recommendations.get(i);
    }

    public int getCount(){
        return this.recommendations.size();
    }

    public CharSequence getItemText(int i){
        return this.recommendations.get(i).getItemText();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recommendation, container, false);
    }

    @Override
    public void onPause(){
        super.onPause();
        TabLayout tabLayout = (TabLayout)getActivity().findViewById(R.id.tabLayout);
        tabLayout.removeAllTabs();
    }
    @Override
    public void onStart(){
        super.onStart();
        onCreateSwipe();
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
            //throw new RuntimeException(context.toString()
              //      + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

    public static class ReccomendationPagerAdapter extends FragmentStatePagerAdapter {

        RecommendationFragment rf;

        public ReccomendationPagerAdapter(FragmentManager fm,RecommendationFragment rf) {
            super(fm);
            this.rf = rf;
        }

        @Override
        public Fragment getItem(int i) {
            return rf.getItem(i);
        }

        @Override
        public int getCount() {
            return rf.getCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return rf.getItemText(position);
        }
    }
}
