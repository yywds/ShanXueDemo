package com.example.administrator.onlinelearning.Page.Tool.DrawingView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.administrator.onlinelearning.R;


public class ToolBarLayout extends LinearLayout {
    /**
     * 构造函数
     *
     * @param context
     * @param attrs
     */
    public ToolBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.tool_bar, this);
    }
}
