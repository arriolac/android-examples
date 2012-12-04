package com.commonsware.android.passwordbox;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class AuthActivity extends SherlockFragmentActivity implements 
    TextWatcher, OnCheckedChangeListener, OnClickListener {

    private EditText passphrase=null; 
    private EditText confirm=null; 
    private View ok=null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passphrase_setup); 
        
        passphrase=(EditText)findViewById(R.id.passphrase);
        confirm=(EditText)findViewById(R.id.confirm); 
        passphrase.addTextChangedListener(this);
        confirm.addTextChangedListener(this); 
        
        CompoundButton cb = (CompoundButton)findViewById(R.id.show_passphrase); 
        cb.setOnCheckedChangeListener(this);
        
        ok=findViewById(R.id.ok); 
        ok.setOnClickListener(this);
        
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        toggleShowPassphrase(passphrase, isChecked); 
        toggleShowPassphrase(confirm, isChecked);
    }

    private void toggleShowPassphrase(EditText field, boolean isChecked) { 
        int start=field.getSelectionStart(); 
        int end=field.getSelectionEnd();
        
        if (isChecked) { 
            field.setTransformationMethod(null);
        } else {
            field.setTransformationMethod(new PasswordTransformationMethod()); 
            field.setSelection(start, end);
        }
    }
    
    @Override
    public void afterTextChanged(Editable s) {
        ok.setEnabled(passphrase.getText().length() > 0
                && passphrase .getText()
                .toString().equals(confirm.getText().toString()));
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
