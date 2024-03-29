package com.example.administrator.onlinelearning.Page.Tool.DrawingView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.onlinelearning.R;

public class SaveDialog extends CustomDialog {
    private Context context;
    private View view;
    private InkPresenter inkPresenter;
    private char[] illegalCharacters = {'\\', '/', ':', '*', '?', '#', '"', '<', '>', '|', ' '};
    private String fileFormat;

    /**
     * 构造函数
     *
     * @param context
     */
    public SaveDialog(Context context) {
        dialogInit(context);

        onButtonClick();

        RadioGroup fileFormatRadioGroup = view.findViewById(R.id.file_format_radio_group);
        RadioButton radioButton = (RadioButton) fileFormatRadioGroup.getChildAt(0);
        TextView textView = view.findViewById(R.id.file_format_text_view);
        fileFormat = textView.getText().toString();
        radioButton.setChecked(true);
        fileFormatRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = view.findViewById(checkedId);
                TextView textView = view.findViewById(R.id.file_format_text_view);
                textView.setText("." + checkedRadioButton.getText());
                fileFormat = textView.getText().toString();
            }
        });
    }

    /**
     * 添加按钮点击事件
     */
    public void onButtonClick() {
        Button ok = view.findViewById(R.id.save_dialog_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = view.findViewById(R.id.file_name);
                String fileName = editText.getText().toString();
                if (fileName.length() > 0 && fileName.length() < 256) {
                    boolean judgeFlag = true;
                    if (fileName.charAt(0) == '.') {
                        judgeFlag = false;
                    } else {
                        OUT:
                        for (int i = 0, length = fileName.length(); i < length; i++) {
                            for (int j = 0, charLength = illegalCharacters.length; j < charLength; j++) {
                                if (fileName.charAt(i) == illegalCharacters[j]) {
                                    judgeFlag = false;
                                    break OUT;
                                }
                            }
                        }
                    }
                    if (judgeFlag == true) {
                        inkPresenter = MainActivity.getMainActivity().getInkPresenter();
                        inkPresenter.save(fileName, fileFormat);
                        delayAndDismiss(delayMills);
                    } else {
                        Toast.makeText(context, "文件名可能输错了呢", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "文件名可能输错了呢", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button cancel = view.findViewById(R.id.save_dialog_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delayAndDismiss(delayMills);
            }
        });
    }

    @Override
    public void dialogInit(Context context) {
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.view = LayoutInflater.from(context).inflate(R.layout.save_dialog, null);
        this.context = context;
        setOutsideTouchable(true);
        this.view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = v.findViewById(R.id.save_dialog).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        setContentView(this.view);
        setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        setBackgroundDrawable(dw);
        setAnimationStyle(R.style.BottomDialogAnimation);
    }
}
