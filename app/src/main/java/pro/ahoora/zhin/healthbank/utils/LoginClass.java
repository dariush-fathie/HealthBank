package pro.ahoora.zhin.healthbank.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import pro.ahoora.zhin.healthbank.activitys.LoginActivity;
import pro.ahoora.zhin.healthbank.interfaces.LoginListener;
import pro.ahoora.zhin.healthbank.models.LoginModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginClass {
    String user;
    String pass;
    String yekta;
    private String val = "";
    private Context mContaxt;
    boolean ch;
    private LoginListener loginListener;

    public LoginClass(Context context, LoginListener loginListener) {
        this.mContaxt = context;
        pass = "*";
        user = "*";
        ch = false;
        this.loginListener = loginListener ;
        callBachServer();
    }


    public LoginClass(String user, String pass, Context context) {
        this.user = user;
        this.pass = pass;
        this.mContaxt = context;
        ch = true;
    }


    public void goToLogin() {
        if (user.trim().length() < 4 || pass.trim().length() < 4) {
            Toast.makeText(mContaxt, "نام کاربری یا رمز عبور موجود نیست", Toast.LENGTH_SHORT).show();
        } else if (user.trim().equals("")) {

            Toast.makeText(mContaxt, "نام کاربری خالی است", Toast.LENGTH_SHORT).show();

        } else if (pass.trim().equals("")) {

            Toast.makeText(mContaxt, "رمز عبور خالی است", Toast.LENGTH_SHORT).show();
        } else {
            callBachServer();
        }


    }

    private void callBachServer() {
        Utils.INSTANCE.setYekta();
        yekta = VarableValues.INSTANCE.getYekta();

        try {
            KotlinApiClient.INSTANCE.getClient().create(ApiInterface.class).login(user, pass, yekta).enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(@NonNull Call<LoginModel> call, @NonNull Response<LoginModel> response) {
                    LoginModel loginModel = response.body();
                    assert loginModel != null;
                    val = loginModel.getVal();
                    Log.e("val", val + "");
                    if (val.equals("noUser") || val.equals("wPass")) {
                        if (ch) {
                            Toast.makeText(mContaxt, "نام کاربری یا رمز عبور موجود نیست", Toast.LENGTH_SHORT).show();
                        }
                    } else if (val.equals("okL")) {
                        if (ch) {
                            Toast.makeText(mContaxt, "ورود انجام شد", Toast.LENGTH_SHORT).show();
                            ((LoginActivity) mContaxt).finish();
                        } else {
                            loginListener.sessionExist();
                        }

                    } else if (val.equals("empty")) {
                        loginListener.sessionNotExist();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<LoginModel> call, @NonNull Throwable t) {

                    if (ch) {
                        Toast.makeText(mContaxt, "خطایی رخ داده است", Toast.LENGTH_SHORT).show();

                    } else {
                        loginListener.sessionNotExist();
                    }

                }
            });

        } catch (Exception e) {
            if (ch) {
                Toast.makeText(mContaxt, "خطایی رخ داده است", Toast.LENGTH_SHORT).show();

            } else {
                loginListener.sessionNotExist();
            }
        }

    }
}
