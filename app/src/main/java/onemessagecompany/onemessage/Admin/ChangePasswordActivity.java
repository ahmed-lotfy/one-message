package onemessagecompany.onemessage.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import onemessagecompany.onemessage.BaseActivity;
import onemessagecompany.onemessage.Public.SendMessageActivity;
import onemessagecompany.onemessage.R;
import onemessagecompany.onemessage.model.ChangeUserPasswordRequest;
import onemessagecompany.onemessage.model.User;
import onemessagecompany.onemessage.rest.ApiClient;
import onemessagecompany.onemessage.rest.UsersApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {

    private TextInputLayout mPassword;
    private TextInputLayout mConfirmPassword;
    private User user;
    //private TextInputLayout userPassword;
    //private TextInputLayout userConfirmPassword;

    private String password;
    private String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_change_password);

        findViewById(R.id.ac_change_password).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        user = (User) getIntent().getSerializableExtra("userDetails");

        mPassword = (TextInputLayout) findViewById(R.id.edit_password);
        mConfirmPassword = (TextInputLayout) findViewById(R.id.edit_confirmPassword);



        Button mRegisterButton = (Button) findViewById(R.id.change_password_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate())
                    ChangePasswordPost();
            }
        });


    }

    public boolean validate() {
        initialize();
        boolean valid = true;
        if (password.isEmpty() || password.length() < 6) {
            mPassword.setError("Required & Should not be less than 6 characters");
            valid = false;
        }
        else {
            mPassword.setError(null);
        }
        if (confirmPassword.isEmpty() || confirmPassword.length() < 6) {
            mConfirmPassword.setError("Required & Should not be less than 6 characters");
            valid = false;
        }
        else{
            mConfirmPassword.setError(null);
        }
        if (!confirmPassword.equals(password)) {
            mConfirmPassword.setError("Confirm & Password must be the same");
            valid = false;
        }
        return valid;
    }

    public void initialize() {
        password = mPassword.getEditText().getText().toString();
        confirmPassword = mConfirmPassword.getEditText().getText().toString();
    }

    private void ChangePasswordPost() {

        password = mPassword.getEditText().getText().toString();
        confirmPassword = mConfirmPassword.getEditText().getText().toString();


        UsersApi usersApi = ApiClient.getAuthorizedClient().create(UsersApi.class);
        ChangeUserPasswordRequest changeUserPasswordRequest = new ChangeUserPasswordRequest();

        changeUserPasswordRequest.setNewPassword(password);
        changeUserPasswordRequest.setConfirmPassword(confirmPassword);
        changeUserPasswordRequest.setUserName(user.getUserName());

        usersApi.ChangePassword(changeUserPasswordRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Toast.makeText(getApplicationContext(), "Password Changed Successfully", Toast.LENGTH_LONG).show();
                    finish() ;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Invalid Login username or password", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_send_message:
                Intent intentSendMessage = new Intent(getApplicationContext(), SendMessageActivity.class);
                startActivity(intentSendMessage);
                return true;

            default:
                return true;
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("userDetails", user);
        setResult(RESULT_OK, intent);
        finish();
    }
}
