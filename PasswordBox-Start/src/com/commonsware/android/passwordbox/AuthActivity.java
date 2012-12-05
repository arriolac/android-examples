
package com.commonsware.android.passwordbox;

import java.io.IOException;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.commonsware.cwac.loaderex.SQLCipherUtils.State;

public class AuthActivity extends SherlockFragmentActivity implements
        TextWatcher, OnCheckedChangeListener, OnClickListener {

    private EditText passphrase = null;
    private EditText confirm = null;
    private View ok = null;

    private State dbState = State.UNKNOWN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passphrase_setup);

        SQLiteDatabase.loadLibs(this);
        dbState = DatabaseHelper.getDatabaseState(AuthActivity.this);

        passphrase = (EditText) findViewById(R.id.passphrase);
        passphrase.addTextChangedListener(this);
        
        confirm = (EditText) findViewById(R.id.confirm);
        confirm.addTextChangedListener(this);
        if (dbState == State.ENCRYPTED) {
            confirm.setVisibility(View.GONE);
        }

        CompoundButton cb = (CompoundButton) findViewById(R.id.show_passphrase);
        cb.setOnCheckedChangeListener(this);

        ok = findViewById(R.id.ok);
        ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        v.setEnabled(false);
        if (dbState == State.UNENCRYPTED) {
            try {
                DatabaseHelper.encrypt(this, passphrase.getText().toString());
            } catch (IOException e) {
                Toast.makeText(this, getString(R.string.problem_encrypting_database)
                        + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        
        DatabaseHelper.initDatabase(this, passphrase.getText().toString());
        
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        toggleShowPassphrase(passphrase, isChecked);
        toggleShowPassphrase(confirm, isChecked);
    }

    private void toggleShowPassphrase(EditText field, boolean isChecked) {
        int start = field.getSelectionStart();
        int end = field.getSelectionEnd();

        if (isChecked) {
            field.setTransformationMethod(null);
        } else {
            field.setTransformationMethod(new PasswordTransformationMethod());
            field.setSelection(start, end);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        boolean needsNoConfirm=confirm.getVisibility() == View.GONE;
        
        ok.setEnabled(dbState != State.UNKNOWN && 
                passphrase.getText().length() > 0 && 
                (needsNoConfirm || passphrase.getText()
                                             .toString()
                                             .equals(confirm.getText()
                                                     .toString())));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Unused
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Unused
    }
}
