package com.example.administrator.onlinelearning.Page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.onlinelearning.Page.Tool.ToolMainActivity;
import com.example.administrator.onlinelearning.R;

public class PkFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_pk, null);
        getActivity().startActivity(new Intent(getActivity(), ToolMainActivity.class));
        return view;
    }
}
