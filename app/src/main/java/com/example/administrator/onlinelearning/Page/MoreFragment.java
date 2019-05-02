package com.example.administrator.onlinelearning.Page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.onlinelearning.Page.More.MoreMainActivity;
import com.example.administrator.onlinelearning.R;

public class MoreFragment extends Fragment {

    private Context mContext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_me, null);
        this.mContext = getActivity();
        getActivity().startActivity(new Intent(getActivity(), MoreMainActivity.class));
        return view;
    }

}
