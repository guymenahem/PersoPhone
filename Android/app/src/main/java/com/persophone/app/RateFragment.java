package com.persophone.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import com.android.volley.Response;
import com.persophone.persophone_bottom.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Boolean isNew = true;

    private OnFragmentInteractionListener mListener;

    public RateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RateFragment newInstance(String param1, String param2) {
        RateFragment fragment = new RateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void addButtonOnClick(View view){
        Button button = (Button) view.findViewById(R.id.btnSubmit);
        final View _view = view;
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveUserRates(_view);
            }
        });
    }

    private void saveUserRates(View view){
        JSONObject requestData = new JSONObject();

        float batteryRating = ((RatingBar) view.findViewById(R.id.battery_rating)).getRating();
        float screenRating = ((RatingBar) view.findViewById(R.id.screen_rating)).getRating();
        float cameraRating = ((RatingBar) view.findViewById(R.id.camera_rating)).getRating();
        float reactivityRating = ((RatingBar) view.findViewById(R.id.reactivity_rating)).getRating();
        float overallRating = ((RatingBar) view.findViewById(R.id.overall_rating)).getRating();

        try {
            requestData.put("user",10);
            requestData.put("phone_name","Nexus 6");
            requestData.put("battery",batteryRating == 0.0 ? JSONObject.NULL : batteryRating);
            requestData.put("screen",screenRating == 0.0 ? JSONObject.NULL : screenRating);
            requestData.put("camera",cameraRating == 0.0 ? JSONObject.NULL : cameraRating);
            requestData.put("reactivity",reactivityRating == 0.0 ? JSONObject.NULL : reactivityRating);
            requestData.put("overall",overallRating == 0.0 ? JSONObject.NULL : overallRating);
            requestData.put("isNew",isNew);

            new UsersData().SaveUserRates(requestData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    isNew = false;
                }
            });
        }
        catch (Exception ex){

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate,
                container, false);
        addButtonOnClick(view);
        fetchUserRates(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void fetchUserRates(View view) {
        final View _view = view;
        try {
            new UsersData().GetUserRates(new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        if (response.length() == 1) {
                            JSONObject myResponse = response.getJSONObject(0);
                            isNew = false;

                            RatingBar screenRating = ((RatingBar) _view.findViewById(R.id.screen_rating));
                            screenRating.setRating(myResponse.isNull("screen") ? 0 : (float)myResponse.getDouble("screen"));

                            RatingBar batteryRating = ((RatingBar) _view.findViewById(R.id.battery_rating));
                            batteryRating.setRating(myResponse.isNull("battery") ? 0 : (float)myResponse.getDouble("battery"));

                            RatingBar cameraRating = ((RatingBar) _view.findViewById(R.id.camera_rating));
                            cameraRating.setRating(myResponse.isNull("camera") ? 0 : (float)myResponse.getDouble("camera"));

                            RatingBar reactivityRating = ((RatingBar) _view.findViewById(R.id.reactivity_rating));
                            reactivityRating.setRating(myResponse.isNull("reactivity") ? 0 : (float)myResponse.getDouble("reactivity"));

                            RatingBar overallRating = ((RatingBar) _view.findViewById(R.id.overall_rating));
                            overallRating.setRating(myResponse.isNull("overall") ? 0 : (float)myResponse.getDouble("overall"));
                        }
                        else{
                            isNew = true;
                        }

                    }
                    catch (Exception ex){

                    }

                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
}
