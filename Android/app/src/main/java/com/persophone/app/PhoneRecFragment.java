package com.persophone.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.persophone.persophone_bottom.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

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
    private static final String PHONE_DATA_PARAM = "PHONE_DATA";

    private OnFragmentInteractionListener mListener;

    public String name;


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
    public static PhoneRecFragment newInstance(String name, JSONObject phoneData) {
        PhoneRecFragment fragment = new PhoneRecFragment();
        Bundle args = new Bundle();
        // add arguments to the fragment
        args.putString(PHONE_NAME_PARAM,name);

        Iterator<String> keys = phoneData.keys();

        while( keys.hasNext() ) {
            String key = (String)keys.next();
            try {
                if (!(phoneData.getString(key).isEmpty())) {
                    args.putString(key,phoneData.getString(key));
                }
            }
            catch (Exception ex){

            }
        }

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
        ((TextView)inf .findViewById(R.id.rec_user_rates)).setMovementMethod(new ScrollingMovementMethod());

        setPhone(inf);
        return inf;
    }

    private void setPhone(View v){

        final  View viewForInnerFunc = v;
        JSONObject jsnImprovement = new PhonesData().CalculateImprovmentFromCurrentPhone(this.getArguments());

        Bundle args = this.getArguments();
        try {
            ((TextView)v.findViewById(R.id.phone_name)).setText(args.getString("name"));
            ((TextView)v.findViewById(R.id.screen_value)).setText(args.getString("screen_size"));
            ((TextView)v.findViewById(R.id.screen_upgrade)).setText(String.format("(+%s%%)",jsnImprovement.getString("screen_size_improvement")));

            ((TextView)v.findViewById(R.id.screen_res_value)).setText(args.getString("screen_resolution"));
            ((TextView)v.findViewById(R.id.screen_res_upgrade)).setText(" (old: " + UsersData.CurrentUserDevDetails.GetScreenResolution()+")" );
            //((TextView)v.findViewById(R.id.screen_res_upgrade)).setTextSize();

            ((TextView)v.findViewById(R.id.memory_value)).setText(args.getString("ram")+"GB");
            ((TextView)v.findViewById(R.id.memory_upgrade)).setText(String.format("(+%s%%)",jsnImprovement.getString("ram_improvement")));;

            ((TextView)v.findViewById(R.id.storage_value)).setText(args.getString("storage")+"GB");
            ((TextView)v.findViewById(R.id.storage_upgrade)).setText(String.format("(+%s%%)",jsnImprovement.getString("storage_improvement")));

            ((TextView)v.findViewById(R.id.battery_value)).setText(args.getString("battery"));
            ((TextView)v.findViewById(R.id.battery_upgrade)).setText(String.format("(+%s%%)",jsnImprovement.getString("battery_improvement")));

            ((TextView)v.findViewById(R.id.cpu_cores_value)).setText(args.getString("cpu_cores"));
            ((TextView)v.findViewById(R.id.cpu_cores_upgrade)).setText(String.format("(+%s%%)",jsnImprovement.getString("cpu_cores_improvement")));

            ((TextView)v.findViewById(R.id.camera_value)).setText(args.getString("camera"));
            ((TextView)v.findViewById(R.id.price_value)).setText(args.getString("price"));

            new PhonesData().GetPhoneRates(args.getString("name"), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        ((TextView)viewForInnerFunc .findViewById(R.id.rec_user_rates)).setText(response.getString("result"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception ex){

        }

        loadPicture(v, args.getString("image_url"));
    }

    private void loadPicture(View v,String Url) {
        final View _v = v;
        new PhonesData().GetPhonesImage(Url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null){
                    ((ImageView)_v.findViewById(R.id.phone_image)).setImageBitmap(response.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

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
