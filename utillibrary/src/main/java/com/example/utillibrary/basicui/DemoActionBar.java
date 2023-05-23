package com.example.utillibrary.basicui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.utillibrary.R;
import com.example.utillibrary.logutils.LogType;
import com.example.utillibrary.logutils.LogUtil;

/**
 * 顶部导航栏
 */
public class DemoActionBar extends RelativeLayout {

    ImageView leftImgView;
    ImageView rightImgView;
    TextView leftTextView;
    TextView rightTextView;
    TextView centerTitleView;
    /**
     * 二进制依次'按从左到右'代表leftImage,leftText,centerTitle,rightText,rightImage是否显示
     */
    public static final long L_L_C_R_R_DEFAULT = 0b10011;
    public static final long L_IMAGE = 0b10000;
    public static final long L_TEXT = 0b01000;
    public static final long C_TITLE = 0b00100;
    public static final long R_TEXT = 0b00010;
    public static final long R_IMAGE = 0b00001;

    public DemoActionBar(Context context) {
        super(context);
    }

    public DemoActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttribute(context, attrs);
    }

    public DemoActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttribute(context, attrs);
    }

    private void setAttribute(Context ctx, AttributeSet attrs) {
        String namespace = "http://schemas.android.com/apk/res-auto";
        int leftImage = attrs.getAttributeResourceValue(namespace, "left_image", 0);
        int leftText = attrs.getAttributeResourceValue(namespace, "left_text", 0);
        int centerTitle = attrs.getAttributeResourceValue(namespace, "center_title", 0);
        int rightText = attrs.getAttributeResourceValue(namespace, "right_text", 0);
        int rightImage = attrs.getAttributeResourceValue(namespace, "right_image", 0);
        int visibleCtrId = attrs.getAttributeResourceValue(namespace, "visible_ctr", 0);
        initBar(ctx, visibleCtrId);

        if (leftImage != 0) {
            leftImgView.setBackgroundResource(leftImage);
        }
        if (leftText != 0) {
            leftTextView.setText(leftText);
        }
        if (centerTitle != 0) {
            centerTitleView.setText(centerTitle);
        }
        if (rightText != 0) {
            rightTextView.setText(rightText);
        }
        if (rightImage != 0) {
            rightImgView.setBackgroundResource(rightImage);
        }
    }

    /**
     * @param ctx          创建布局用
     * @param visibleCtrId 控制可见组件,id
     */
    private void initBar(Context ctx, int visibleCtrId) {
        LayoutInflater.from(ctx).inflate(R.layout.demo_action_bar, this, true);
        leftImgView = findViewById(R.id.action_bar_back_img);
        rightImgView = findViewById(R.id.action_bar_forward_img);
        leftTextView = findViewById(R.id.action_bar_left_text);
        rightTextView = findViewById(R.id.action_bar_right_text);
        centerTitleView = findViewById(R.id.action_bar_center_title);
        long visibleConfigLong;
        if (visibleCtrId == 0) {
            visibleConfigLong = L_L_C_R_R_DEFAULT;
        } else {
            String visibleCtrStr = getResources().getString(visibleCtrId);
            // 二进制字符串转10进制数字
            visibleConfigLong = Long.parseLong(visibleCtrStr.substring(2), 2);
        }
        LogUtil.log(LogType.LEVEL_I, "DemoActionBar", "visible:" + visibleConfigLong);
        if ((visibleConfigLong & L_IMAGE) == 0) {
            leftImgView.setVisibility(INVISIBLE);
        }
        if ((visibleConfigLong & L_TEXT) == 0) {
            leftTextView.setVisibility(INVISIBLE);
        }
        if ((visibleConfigLong & C_TITLE) == 0) {
            centerTitleView.setVisibility(INVISIBLE);
        }
        if ((visibleConfigLong & R_TEXT) == 0) {
            rightTextView.setVisibility(INVISIBLE);
        }
        if ((visibleConfigLong & R_IMAGE) == 0) {
            rightImgView.setVisibility(INVISIBLE);
        }
    }

    public void setForwardListener(OnClickListener onClickListener) {
        rightImgView.setOnClickListener(onClickListener);
    }

}
