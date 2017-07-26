package com.opentmn.opentmn.screens.menu;

import android.content.Context;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.opentmn.opentmn.screens.base.BaseViewHolder;

import сom.opentmn.opentmn.R;


/**
 * Created by kost on 11.12.16.
 */

public class NavigationDrawerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final static int ITEM_TYPE_EMPTY = 0;
    private final static int ITEM_TYPE_ITEM = 1;
    private Context mContext;
    private Menu mMenu;
    private NavigationView.OnNavigationItemSelectedListener mItemSelectedListener;

    public NavigationDrawerAdapter(Context context, NavigationView.OnNavigationItemSelectedListener itemSelectedListener) {
        mContext = context;
        MenuInflater menuInflater = new MenuInflater(mContext);
        mMenu = new NavigationMenu(mContext);
        menuInflater.inflate(R.menu.activity_main_drawer, mMenu);
        mItemSelectedListener = itemSelectedListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE_EMPTY) {
            return new BaseViewHolder(new View(mContext), mContext.getResources().getDimensionPixelSize(R.dimen.nav_drawer_header_height));
        } else {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_drawer, parent, false);
            return new DrawerViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if(holder.getItemViewType() == ITEM_TYPE_ITEM) {
            DrawerViewHolder drawerViewHolder = (DrawerViewHolder) holder;
            MenuItem menuItem = mMenu.getItem(position - 1);
            drawerViewHolder.setupWithMenuItem(menuItem);
            drawerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemSelectedListener.onNavigationItemSelected(menuItem);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMenu.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return ITEM_TYPE_EMPTY;
        else
            return ITEM_TYPE_ITEM;
    }
}
