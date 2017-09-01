package com.persophone.app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.persophone.persophone_bottom.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UsageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UsageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final double CRITICAL_BATTERY_PARAM = 0.7;
    public static final double MEDIUM_BATTERY_PARAM = 0.3;
    public static final double CRITICAL_CPU_PARAM = 0.7;
    public static final double MEDIUM_CPU_PARAM = 0.3;
    public static final double CRITICAL_MEMORY_PARAM = 0.7;
    public static final double MEDIUM_MEMORY_PARAM = 0.3;
    public static final double CRITICAL_STORAGE_PARAM = 0.7;
    public static final double MEDIUM_STORAGE_PARAM = 0.3;
    public static final double CRITICAL_CAMERA_PARAM = 0.7;
    public static final double MEDIUM_CAMERA_PARAM = 0.3;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UsageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsageFragment newInstance(String param1, String param2) {
        UsageFragment fragment = new UsageFragment();
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
        final View view = inflater.inflate(R.layout.fragment_usage, container, false);
        this.fillGraphExample(view);
        try {
            new UsersData().GetAllGrades(new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        double dBattery = response.getDouble("batteryGrade");
                        double dCpu = response.getDouble("cpuGrade");
                        double dMemory = response.getDouble("ramGrade");
                        double dStorage = response.getDouble("storageGrade");
                        double dCamera = response.getDouble("cameraGrade");

                        String usageText = "Good battery usage";
                        String badgeText = "Low";
                        Drawable badge = getContext().getDrawable(R.drawable.ok_badge);

                        // logic
                        // Battery
                        if (dBattery >= CRITICAL_BATTERY_PARAM) {
                            usageText = "Critical battery usage";
                            badgeText = "High";
                            badge = getContext().getDrawable(R.drawable.danger_badge);
                        } else if (dBattery >= MEDIUM_BATTERY_PARAM) {
                            usageText = "Average battery usage";
                            badgeText = "Medium";
                            badge = getContext().getDrawable(R.drawable.medium_badge);
                        }
                        ((TextView) view.findViewById(R.id.battery_usage)).setText(usageText);
                        ((TextView) view.findViewById(R.id.battery_notification)).setText(badgeText);
                        ((TextView) view.findViewById(R.id.battery_notification)).setBackground(badge);

                        // Memory
                        usageText = "Good memory usage";
                        badgeText = "Low";
                        badge = getContext().getDrawable(R.drawable.ok_badge);
                        if (dMemory >= CRITICAL_MEMORY_PARAM) {
                            usageText = "Critical memory usage";
                            badgeText = "High";
                            badge = getContext().getDrawable(R.drawable.danger_badge);
                        } else if (dMemory >= MEDIUM_MEMORY_PARAM) {
                            usageText = "Average memory usage";
                            badgeText = "Medium";
                            badge = getContext().getDrawable(R.drawable.medium_badge);
                        }
                        ((TextView) view.findViewById(R.id.memory_usage)).setText(usageText);
                        ((TextView) view.findViewById(R.id.memory_notification)).setText(badgeText);
                        ((TextView) view.findViewById(R.id.memory_notification)).setBackground(badge);

                        // Storage
                        usageText = "Low storage usage";
                        badgeText = "Low";
                        badge = getContext().getDrawable(R.drawable.ok_badge);
                        if (dStorage >= CRITICAL_STORAGE_PARAM) {
                            usageText = "Critical storage";
                            badgeText = "High";
                            badge = getContext().getDrawable(R.drawable.danger_badge);
                        } else if (dStorage >= MEDIUM_STORAGE_PARAM) {
                            usageText = "Average storage usage";
                            badgeText = "Medium";
                            badge = getContext().getDrawable(R.drawable.medium_badge);
                        }
                        ((TextView) view.findViewById(R.id.storage_usage)).setText(usageText);
                        ((TextView) view.findViewById(R.id.storage_notification)).setText(badgeText);
                        ((TextView) view.findViewById(R.id.storage_notification)).setBackground(badge);

                        // Cpu
                        usageText = "Low cpu usage";
                        badgeText = "Low";
                        badge = getContext().getDrawable(R.drawable.ok_badge);
                        if (dCpu >= CRITICAL_CPU_PARAM) {
                            usageText = "Critical cpu";
                            badgeText = "High";
                            badge = getContext().getDrawable(R.drawable.danger_badge);
                        } else if (dCpu >= MEDIUM_CPU_PARAM) {
                            usageText = "Average cpu usage";
                            badgeText = "Medium";
                            badge = getContext().getDrawable(R.drawable.medium_badge);
                        }
                        ((TextView) view.findViewById(R.id.cpu_usage)).setText(usageText);
                        ((TextView) view.findViewById(R.id.cpu_notification)).setText(badgeText);
                        ((TextView) view.findViewById(R.id.cpu_notification)).setBackground(badge);

                        // Camera
                        usageText = "Low camera usage";
                        badgeText = "Low";
                        badge = getContext().getDrawable(R.drawable.ok_badge);
                        if (dCamera >= CRITICAL_CAMERA_PARAM) {
                            usageText = "High camera usage";
                            badgeText = "High";
                            badge = getContext().getDrawable(R.drawable.danger_badge);
                        } else if (dCamera >= MEDIUM_CAMERA_PARAM) {
                            usageText = "Average camera usage";
                            badgeText = "Medium";
                            badge = getContext().getDrawable(R.drawable.medium_badge);
                        }
                        ((TextView) view.findViewById(R.id.camera_usage)).setText(usageText);
                        ((TextView) view.findViewById(R.id.camera_notification)).setText(badgeText);
                        ((TextView) view.findViewById(R.id.camera_notification)).setBackground(badge);

                        // Populate battery usage graph
                        /*
                        LineGraphSeries<DataPoint> series;
                        series = new LineGraphSeries<DataPoint>();
                        Object[] points = (Object[]) response.get("points");

                        for (int i = 0; i < points.length; i++){
                            series.appendData(new DataPoint(points[i].x,points[i].y),true,points.length);
                        }
                        GraphView graph = (GraphView) view.findViewById(R.id.usage_graph);
                        if (!series.isEmpty()) {
                            graph.removeAllSeries();
                            graph.addSeries(series);
                        }*/


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
/*            new UsersData().GetUserBatteryGrade(new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        double dGrade = response.getDouble("batteryGrade");
                        String usageText = "Good battery usage";
                        String badgeText = "Good";
                        Drawable badge = getContext().getDrawable(R.drawable.ok_badge);

                        // logic
                        if (dGrade >= 0  && dGrade < 0.3){
                            usageText = "Replace your battery";
                            badgeText = "Critical";
                            badge = getContext().getDrawable(R.drawable.danger_badge);
                        }
                        else if(dGrade < 0.7)
                        {
                            usageText = "Average battery usage";
                            badgeText = "Medium";
                            badge = getContext().getDrawable(R.drawable.medium_badge);
                        }
                        ((TextView)view.findViewById(R.id.battery_usage)).setText(usageText);
                        ((TextView)view.findViewById(R.id.battery_notification)).setText(badgeText);
                        ((TextView)view.findViewById(R.id.battery_notification)).setBackground(badge);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });*/
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            new UsersData().GetBatteryUsageGraph(new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        // Populate battery usage graph

                        LineGraphSeries<DataPoint> series;
                        series = new LineGraphSeries<DataPoint>();
                        JSONArray batteryUsageArray = new JSONArray((response.get("batteryUsageGraph")));

                        for (int i = 0; i < batteryUsageArray.length(); i++){
                            JSONObject point = batteryUsageArray.getJSONObject(i);
                            series.appendData(new DataPoint(i,point.getDouble("y")),true,batteryUsageArray.length());
                        }
                        GraphView graph = (GraphView) view.findViewById(R.id.usage_graph);
                        if (!series.isEmpty()) {
                            graph.removeAllSeries();
                            graph.addSeries(series);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void fillGraphExample(View v){
        GraphView gw = (GraphView)v.findViewById(R.id.usage_graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 100),
                new DataPoint(1, 95),
                new DataPoint(2, 92),
                new DataPoint(3, 80),
                new DataPoint(4, 55),
                new DataPoint(5, 20),
                new DataPoint(6, 5),
                new DataPoint(7, 40),
                new DataPoint(8, 95)
        });
        gw.getViewport().setYAxisBoundsManual(true);
        gw.getViewport().setMaxY(100);
        gw.addSeries(series);
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
