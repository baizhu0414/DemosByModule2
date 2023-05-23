package com.example.utillibrary.forward_menu.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.utillibrary.forward_menu.logic.ForwardMenuLogic;
import com.example.utillibrary.normalutil.Utils;

public class ForwardMenuListener implements View.OnClickListener {

    Activity activity;
    ViewGroup mainFrame;

    public ForwardMenuListener(ViewGroup rootView, Activity activity) {
        this.activity = activity;
        this.mainFrame = rootView;
    }

    @Override
    public void onClick(View v) {
        ForwardMenuBuilder builder = new ForwardMenuBuilder();
        builder.addScan().addEapp(ForwardMenuLogic.getInstance().getExtArgsList());
        ForwardMenuLogic.getInstance().initForwardMenu(mainFrame, v.getHeight(),
                activity, builder.getItems(), new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (builder.getItems().get(position).itemType) {
                            case ForwardMenuBuilder.ITEM_TYPE_SCAN:
                                Utils.makeText("扫码");
                                break;
                            case ForwardMenuBuilder.ITEM_TYPE_EAPP:
                                Utils.makeText("Eapp应用");
                                break;
                            default:
                                break;
                        }
                    }
                });
    }
}
