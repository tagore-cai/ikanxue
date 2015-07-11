package com.mislead.ikanxue.app.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.mislead.ikanxue.app.activity.MainActivity;
import com.mislead.ikanxue.app.application.MyApplication;

/**
 * BaseFragment
 *
 * @author Mislead
 *         DATE: 2015/7/9
 *         DESC:
 **/
public class BaseFragment extends Fragment {

  private static String TAG = "BaseFragment";

  protected String title = "i看雪";

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  protected MainActivity mainActivity;

  private BroadcastReceiver logReciever = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      onLoginOrLogout();
    }
  };

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    mainActivity = (MainActivity) activity;
    IntentFilter filter = new IntentFilter(MyApplication.LOGIN_STATE_CHANGE_ACTION);

    getActivity().registerReceiver(logReciever, filter);
  }

  @Override public void onDetach() {
    super.onDetach();
    getActivity().unregisterReceiver(logReciever);
  }

  @Override public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);
    if (!hidden) {
      mainActivity.getSupportActionBar().setTitle(title);
    }
  }

  // do someting when user log state changed.
  protected void onLoginOrLogout() {
  }
}