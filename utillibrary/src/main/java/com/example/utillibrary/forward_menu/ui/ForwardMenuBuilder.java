package com.example.utillibrary.forward_menu.ui;

import com.example.utillibrary.forward_menu.logic.ForwardMenuLogic;
import com.example.utillibrary.normalutil.Utils;

import java.util.ArrayList;
import java.util.List;

public class ForwardMenuBuilder {
    private final List<ForwardMenuLogic.ForwardMenuItem> list = new ArrayList<>();

    /**
     * 扫码功能
     */
    public static final int ITEM_TYPE_SCAN = 1;

    /**
     * 网络应用
     */
    public static final int ITEM_TYPE_EAPP = 2;

    public ForwardMenuBuilder addScan() {
        addItem(ITEM_TYPE_SCAN, com.example.utillibrary.R.drawable.forward_menu_scan,
                Utils.getString(com.example.utillibrary.R.string.forward_menu_item_scan));
        return this;
    }

    public ForwardMenuBuilder addEapp(List<ForwardMenuLogic.ExtArgs> extArgsList) {
        for (ForwardMenuLogic.ExtArgs args: extArgsList) {
            addItem(ITEM_TYPE_EAPP, com.example.utillibrary.R.drawable.forward_menu_eapp,
                    Utils.getString(com.example.utillibrary.R.string.forward_menu_item_eapp), args);
        }
        return this;
    }

    // Native菜单
    private void addItem(int itemType, int iconId, String itemName) {
        ForwardMenuLogic.ForwardMenuItem item = new ForwardMenuLogic.ForwardMenuItem();
        item.itemName = itemName;
        item.iconId = iconId;
        item.itemType = itemType;
        list.add(item);
    }

    // Eapp菜单
    private void addItem(int itemType, int iconId, String itemName, ForwardMenuLogic.ExtArgs extArgs) {
        ForwardMenuLogic.ForwardMenuItem item = new ForwardMenuLogic.ForwardMenuItem();
        item.itemName = itemName;
        item.iconId = iconId;
        item.itemType = itemType;
        item.extArgs = extArgs;
        list.add(item);
    }

    public List<ForwardMenuLogic.ForwardMenuItem> getItems() {
        return list;
    }
}
