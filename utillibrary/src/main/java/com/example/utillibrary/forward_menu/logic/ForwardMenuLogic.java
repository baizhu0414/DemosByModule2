package com.example.utillibrary.forward_menu.logic;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utillibrary.R;
import com.example.utillibrary.forward_menu.ui.ForwardMenuAdapter;
import com.example.utillibrary.normalutil.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ForwardMenuLogic {
    private static volatile ForwardMenuLogic instance;
    private LinearLayout menuLayout;
    private RecyclerView menuRecyclerView;
    private PopupWindow popMenuWindow;

    private ForwardMenuLogic() {

    }

    public static ForwardMenuLogic getInstance() {
        if (instance == null) {
            synchronized (ForwardMenuLogic.class) {
                if (instance == null) {
                    instance = new ForwardMenuLogic();
                }
            }
        }
        return instance;
    }

    /**
     * @param rootView            当前页面根布局(必须是ViewGroup类型布局)
     * @param height              点击菜单按钮的高度
     * @param activity            当前页面
     * @param menuItems           需要展示的菜单项
     * @param onItemClickListener 点击事件回调
     */
    public void initForwardMenu(final ViewGroup rootView, int height, final Activity activity,
                                List<ForwardMenuItem> menuItems,
                                final AdapterView.OnItemClickListener onItemClickListener) {

        int maxTextSize = -1;
        for (ForwardMenuItem menuItem : menuItems) {
            if (!TextUtils.isEmpty(menuItem.itemName) && menuItem.itemName.length() > maxTextSize) {
                maxTextSize = menuItem.itemName.length();
            }
        }

        // 灰色透明背景
        final View view = new View(activity);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.argb(26, 0, 0, 0));
        rootView.addView(view);

        // 初始化菜单布局
        menuLayout =
                (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.forward_menu_layout, null);
        menuRecyclerView = menuLayout.findViewById(R.id.global_navibar_forward_listview);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        menuRecyclerView.setAdapter(new ForwardMenuAdapter(activity, menuItems, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClickListener.onItemClick(parent, view, position, id);
                if (popMenuWindow != null) {
                    popMenuWindow.dismiss();
                }
            }
        }));
        addDecoration(menuRecyclerView, activity);

        // 初始化弹框，并填入菜单布局
        popMenuWindow = new PopupWindow(menuLayout,
                (int) Utils.getDimensPx(com.example.utillibrary.R.dimen.forward_menu_width),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popMenuWindow.setBackgroundDrawable(new ColorDrawable());
        // 点击非PopupWindow视图区域，直接隐藏PopupWindow
        popMenuWindow.setOutsideTouchable(true);
        popMenuWindow.setFocusable(true);
        // 添加动画效果
        popMenuWindow.setAnimationStyle(com.example.utillibrary.R.style.forward_menu_anim);
        // 开始测量弹框展示位置
        int y = height + Utils.getStatusBarHeight(activity, true); // 高度
        int xOffset = activity.getResources().getDimensionPixelSize(com.example.utillibrary.R.dimen.forward_menu_offset); // 右侧边距
        // 展示菜单弹框
        popMenuWindow.showAtLocation(rootView, Gravity.END | Gravity.TOP, xOffset, y);
        // 弹出菜单点击事件
        popMenuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (menuRecyclerView != null) {
                    menuRecyclerView.setAdapter(null);
                    menuRecyclerView = null;
                }
                menuLayout = null;
                popMenuWindow = null;
                rootView.removeView(view);
            }
        });
    }

    private void addDecoration(RecyclerView recyclerView, Activity activity) {
        DividerItemDecoration decoration = new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(Utils.getDrawable(com.example.utillibrary.R.drawable.forward_menu_divider));
        recyclerView.addItemDecoration(decoration);
    }

    /**
     * 添加各类Eapp功能
     */
    public List<ExtArgs> getExtArgsList() {
        List<ExtArgs> list = new ArrayList<>();
        ForwardMenuLogic.ExtArgs extArgs = new ForwardMenuLogic.ExtArgs("https://bz.zzzmh.cn/index", "壁纸");
        list.add(extArgs);
        return list;
    }

    // forward menu 展示菜单项
    public static class ForwardMenuItem {
        public String itemName;
        public int itemType;
        public int iconId;

        public ExtArgs extArgs;
    }

    // Eapp功能
    public static class ExtArgs implements Serializable {
        public String actionUrl; // 打开方式，同应用
        public String name; // 名字

        public ExtArgs(String url, String name) {
            this.actionUrl = url;
            this.name = name;
        }
    }
}
