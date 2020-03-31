package com.demo.winery.subscription;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.winery.R;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.my_activities.Activity_My;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class master_subscription extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    EditText et_sub_winery, et_sub_festival, et_sub_tour;
    Button btn_sub_submit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_subscription_master, container, false);
        return root;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        et_sub_winery = view.findViewById(R.id.et_sub_winery);
        et_sub_festival = view.findViewById(R.id.et_sub_festival);
        et_sub_tour = view.findViewById(R.id.et_sub_tour);

        btn_sub_submit = view.findViewById(R.id.btn_sub_submit);
        btn_sub_submit.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sub_submit:
                //Methods.waitingProgress(getContext());
                paymod_process();
        }
    }

    private void paymod_process() {
        StringRequest res_companyInfo = new StringRequest(Request.Method.POST, constant.PAYMODE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();

                        ResponseModel objRes = gson.fromJson(response, ResponseModel.class);

                        if (objRes.getStatus().equals("200")) {

                                Toast.makeText(getContext(), objRes.getMsg(), Toast.LENGTH_LONG).show();
                            Intent go_my = new Intent(getContext(), Activity_My.class);
                            startActivity(go_my);
                            //getActivity().finish();

                        }else {
                            Methods.closeProgress();
//                            Methods.showAlertDialog("There is no your Data", getActivity());
                            Toast.makeText(getContext(), "There is issues.", Toast.LENGTH_SHORT).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error_signup", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("m_p_winery", et_sub_winery.getText().toString());
                params.put("m_p_festival", et_sub_festival.getText().toString());
                params.put("m_p_tour", et_sub_tour.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(res_companyInfo);
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
