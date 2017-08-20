package com.persophone.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.Response;
import com.persophone.collector.PhonesData;
import com.persophone.collector.UsersData;
import com.persophone.persophone_bottom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PreferencesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PreferencesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreferencesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Boolean isNew = true;

    private OnFragmentInteractionListener mListener;

    public PreferencesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreferencesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreferencesFragment newInstance(String param1, String param2) {
        PreferencesFragment fragment = new PreferencesFragment();
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

    private void fetchUserPreferences(View view) {
        final View _view = view;
        try {
            new UsersData().GetUserPreferences(new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        if (response.length() == 1) {
                            JSONObject myResponse = response.getJSONObject(0);
                            isNew = false;
                            Spinner OsSpinner = ((Spinner) _view.findViewById(R.id.spinnerOS));
                            OsSpinner.setSelection(((ArrayAdapter) OsSpinner.getAdapter()).getPosition(myResponse.getString("operating_system")));

                            Spinner BrandSpinner = ((Spinner) _view.findViewById(R.id.spinnerBrand));
                            BrandSpinner.setSelection(((ArrayAdapter) BrandSpinner.getAdapter()).getPosition(myResponse.getString("brand")));

                            Spinner ScreenSpinner = ((Spinner) _view.findViewById(R.id.spinnerScreen));
                            ScreenSpinner.setSelection(((ArrayAdapter) ScreenSpinner.getAdapter()).getPosition(myResponse.getString("screen_size")));

                            Spinner PriceSpinner = ((Spinner) _view.findViewById(R.id.spinnerPrice));
                            PriceSpinner.setSelection(((ArrayAdapter) PriceSpinner.getAdapter()).getPosition(myResponse.getString("price")));
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

    private void saveUserPreferences(View view){
        Spinner mySpinner=(Spinner) view.findViewById(R.id.spinnerBrand);
        String brand_value = mySpinner.getSelectedItem().toString();

        JSONObject requestData = new JSONObject();
        mySpinner=(Spinner) view.findViewById(R.id.spinnerOS);
        String os_value = mySpinner.getSelectedItem().toString();
        mySpinner=(Spinner) view.findViewById(R.id.spinnerScreen);
        String screen_value = mySpinner.getSelectedItem().toString();
        mySpinner=(Spinner) view.findViewById(R.id.spinnerPrice);
        String price_value = mySpinner.getSelectedItem().toString();

        try {
            requestData.put("user",10);
            requestData.put("brand",brand_value.isEmpty() ? JSONObject.NULL : brand_value);
            requestData.put("os",os_value.isEmpty() ? JSONObject.NULL : os_value);
            requestData.put("screen",screen_value.isEmpty() ? JSONObject.NULL : screen_value);
            requestData.put("price",price_value.isEmpty() ? JSONObject.NULL : price_value);
            requestData.put("isNew",isNew);

            new UsersData().SaveUserPreferences(requestData, new Response.Listener<JSONObject>() {
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

        View view = inflater.inflate(R.layout.fragment_preferences,
                container, false);
        addButtonOnClick(view);
        fetchUserPreferences(view);

        // Inflate the layout for this fragment
        return view;
    }

    public void addButtonOnClick(View view){
        Button button = (Button) view.findViewById(R.id.btnSubmit);
        final View _view = view;
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveUserPreferences(_view);
            }
        });
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
