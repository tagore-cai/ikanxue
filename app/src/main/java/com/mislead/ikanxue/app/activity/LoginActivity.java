package com.mislead.ikanxue.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.VolleyError;
import com.mislead.ikanxue.app.R;
import com.mislead.ikanxue.app.api.Api;
import com.mislead.ikanxue.app.application.MyApplication;
import com.mislead.ikanxue.app.model.KanxueResponse;
import com.mislead.ikanxue.app.net.HttpClientUtil;
import com.mislead.ikanxue.app.util.AndroidHelper;
import com.mislead.ikanxue.app.util.LogHelper;
import com.mislead.ikanxue.app.util.ToastHelper;
import com.mislead.ikanxue.app.volley.VolleyHelper;
import org.apache.http.cookie.Cookie;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * LoginActivity
 *
 * @author Mislead
 *         DATE: 2015/7/6
 *         DESC:
 **/
public class LoginActivity extends AppCompatActivity {

  private static String TAG = "LoginActivity";

  private EditText etName;
  private EditText etPwd;

  private Button btnLogin;
  private static final int ERROR = 404;

  private Handler mHandler = new Handler() {
    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      AndroidHelper.hideProgressDialog();
      switch (msg.what) {
        case HttpClientUtil.NET_SUCCESS:
          ToastHelper.toastShort(LoginActivity.this, "登录成功！");
          LoginActivity.this.finish();
          break;
        case HttpClientUtil.NET_TIMEOUT:
          ToastHelper.toastShort(LoginActivity.this, "网络连接超时，请检查您的网络状态！");
          break;
        case HttpClientUtil.NET_FAILED:
          ToastHelper.toastShort(LoginActivity.this, "网络连接失败，请检查您的网络状态！");
          break;
        case ERROR:
          ToastHelper.toastShort(LoginActivity.this, msg.obj.toString());
          break;
        default:
          break;
      }
    }
  };

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_login);

    initActionbar(getSupportActionBar());

    etName = (EditText) findViewById(R.id.et_user_name);
    etPwd = (EditText) findViewById(R.id.et_user_pwd);

    btnLogin = (Button) findViewById(R.id.btn_login);
    btnLogin.setOnClickListener(new View.OnClickListener() {
                                  @Override public void onClick(View v) {
                                    if (check()) {
                                      AndroidHelper.showProgressDialog(LoginActivity.this);
                                      //
                                      //Api.getInstance()
                                      //    .login(etName.getText().toString(), etPwd.getText().toString(),
                                      //        new HttpClientUtil.NetClientCallback() {
                                      //          @Override
                                      //          public void execute(int status, String response, List<Cookie> cookies) {
                                      //            if (status == HttpClientUtil.NET_SUCCESS) {
                                      //              final JSONObject retObj;
                                      //              LogHelper.e(response);
                                      //              try {
                                      //                retObj = new JSONObject(response);
                                      //                final int ret = retObj.getInt("result");
                                      //                if (ret != Api.LOGIN_SUCCESS) {
                                      //                  Message message = mHandler.obtainMessage();
                                      //                  switch (ret) {
                                      //                    case Api.LOGIN_FAIL_LESS_THAN_FIVE:
                                      //                      String alertText =
                                      //                          "用户名或者密码错误,还有" + (Api.ALLOW_LOGIN_USERNAME_OR_PASSWD_ERROR_NUM
                                      //                              - retObj.getInt("strikes")) + "尝试机会";
                                      //                      message.what = ERROR;
                                      //                      message.obj = alertText;
                                      //                      mHandler.sendMessage(message);
                                      //                      break;
                                      //                    case Api.LOGIN_FAIL_MORE_THAN_FIVE:
                                      //                      message.what = ERROR;
                                      //                      message.obj =
                                      //                          getResources().getString(R.string.login_fail_more_than_five);
                                      //                      mHandler.sendMessage(message);
                                      //                      break;
                                      //                  }
                                      //                  return;
                                      //                }
                                      //                String token = retObj.getString("securitytoken");
                                      //                Api.getInstance().setToken(token);
                                      //                Api.getInstance()
                                      //                    .setLoginUserInfo(retObj.getString("username"),
                                      //                        retObj.getInt("userid"), retObj.getInt("isavatar"),
                                      //                        retObj.getString("email"));
                                      //                for (Cookie cookie : cookies) {
                                      //                  Api.getInstance()
                                      //                      .getCookieStorage()
                                      //                      .addCookie(cookie.getName(), cookie.getValue());
                                      //                }
                                      //                // get user type
                                      //                Api.getInstance()
                                      //                    .getUserInfoPage(retObj.getInt("userid"),
                                      //                        new VolleyHelper.ResponseListener<JSONObject>() {
                                      //                          @Override public void onErrorResponse(VolleyError volleyError) {
                                      //                            LogHelper.e(volleyError.toString());
                                      //                          }
                                      //
                                      //                          @Override public void onResponse(JSONObject object) {
                                      //                            try {
                                      //                              Api.getInstance()
                                      //                                  .setLoginUserType(object.getString("usertitle"));
                                      //                            } catch (JSONException e) {
                                      //                              e.printStackTrace();
                                      //                            }
                                      //                          }
                                      //                        });
                                      //
                                      //                LoginActivity.this.sendBroadcast(
                                      //                    new Intent(MyApplication.LOGIN_STATE_CHANGE_ACTION));
                                      //              } catch (JSONException e) {
                                      //                e.printStackTrace();
                                      //              }
                                      //            }
                                      //            mHandler.sendEmptyMessage(status);
                                      //          }
                                      //        });
                                      Api.getInstance()
                                          .login(etName.getText().toString(),
                                              etPwd.getText().toString(),
                                              new VolleyHelper.ResponseListener<KanxueResponse>() {
                                                @Override public void onErrorResponse(
                                                    VolleyError volleyError) {
                                                  AndroidHelper.hideProgressDialog();
                                                  Message message = mHandler.obtainMessage();
                                                  message.what = ERROR;
                                                  message.obj = volleyError.getMessage();
                                                  mHandler.sendMessage(message);
                                                }

                                                @Override public void onResponse(
                                                    KanxueResponse kanxueResponse) {
                                                  final JSONObject retObj;
                                                  LogHelper.e(kanxueResponse.getJsonString());
                                                  try {
                                                    retObj = new JSONObject(
                                                        kanxueResponse.getJsonString());
                                                    final int ret = retObj.getInt("result");
                                                    if (ret != Api.LOGIN_SUCCESS) {
                                                      Message message = mHandler.obtainMessage();
                                                      switch (ret) {
                                                        case Api.LOGIN_FAIL_LESS_THAN_FIVE:
                                                          String alertText = "用户名或者密码错误,还有"
                                                              + (Api.ALLOW_LOGIN_USERNAME_OR_PASSWD_ERROR_NUM
                                                              - retObj.getInt("strikes"))
                                                              + "尝试机会";
                                                          message.what = ERROR;
                                                          message.obj = alertText;
                                                          mHandler.sendMessage(message);
                                                          break;
                                                        case Api.LOGIN_FAIL_MORE_THAN_FIVE:
                                                          message.what = ERROR;
                                                          message.obj = getResources().getString(
                                                              R.string.login_fail_more_than_five);
                                                          mHandler.sendMessage(message);
                                                          break;
                                                      }
                                                      return;
                                                    }
                                                    String token =
                                                        retObj.getString("securitytoken");
                                                    Api.getInstance().setToken(token);
                                                    Api.getInstance()
                                                        .setLoginUserInfo(
                                                            retObj.getString("username"),
                                                            retObj.getInt("userid"),
                                                            retObj.getInt("isavatar"),
                                                            retObj.getString("email"));
                                                    for (Cookie cookie : kanxueResponse.getCookies()) {
                                                      Api.getInstance()
                                                          .getCookieStorage()
                                                          .addCookie(cookie.getName(),
                                                              cookie.getValue());
                            }
                                                    // get user type
                                                    Api.getInstance()
                                                        .getUserInfoPage(retObj.getInt("userid"),
                                                            new VolleyHelper.ResponseListener<JSONObject>() {
                                                              @Override public void onErrorResponse(
                                                                  VolleyError volleyError) {
                                                                LogHelper.e(volleyError.toString());
                                                              }

                                                              @Override public void onResponse(
                                                                  JSONObject object) {
                                                                try {
                                                                  Api.getInstance()
                                                                      .setLoginUserType(
                                                                          object.getString(
                                                                              "usertitle"));
                                                                } catch (JSONException e) {
                                                                  e.printStackTrace();
                                                                }
                                                              }
                                                            });

                                                    LoginActivity.this.sendBroadcast(new Intent(
                                                        MyApplication.LOGIN_STATE_CHANGE_ACTION));
                                                    AndroidHelper.hideProgressDialog();
                                                    mHandler.sendEmptyMessage(
                                                        HttpClientUtil.NET_SUCCESS);
                                                  } catch (JSONException e) {
                                                    e.printStackTrace();
                          }
                        }
                                              });
                                    }
                                  }
        }

    );
  }

  private void initActionbar(ActionBar actionBar) {
    if (actionBar == null) return;
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeButtonEnabled(true);
  }

  private boolean check() {
    if (etName.getText().toString().isEmpty()) {
      ToastHelper.toastShort(this, "请输入用户名！");
      return false;
    }

    if (etPwd.getText().toString().isEmpty()) {
      ToastHelper.toastShort(this, "请输入用户密码！");
      return false;
    }
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        break;
    }
    return super.onOptionsItemSelected(item);
  }
}
