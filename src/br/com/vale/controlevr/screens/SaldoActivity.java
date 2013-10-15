package br.com.vale.controlevr.screens;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import br.com.vale.controlevr.R;
import br.com.vale.controlevr.util.ActivitySwitcher;
import br.com.vale.controlevr.util.Util;

public class SaldoActivity extends FragmentActivity {
	
	private static String saldo = "R$0.00";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.saldo);
		
		TextView textTitle = (TextView) findViewById(R.id.txtTitle);
		Typeface typeface = Typeface.createFromAsset(getAssets(), "Aller_Std_Lt.ttf");
		textTitle.setTypeface(typeface);
		
		TextView textQuestion = (TextView) findViewById(R.id.question);
		typeface = Typeface.createFromAsset(getAssets(), "QuattrocentoSans-Regular.ttf");
		textQuestion.setTypeface(typeface);
		
		final Button btnNext = (Button) findViewById(R.id.next);
		
		final EditText edtSaldoAtual = (EditText) findViewById(R.id.edtSaldoAtual);
		edtSaldoAtual.setText(saldo);
		edtSaldoAtual.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					btnNext.setEnabled(false);
					return;
				}else{
					btnNext.setEnabled(true);
				}
				
				String value = s.toString().replaceAll("\\.+", "");
				value = value.toString().replace("R$", "");
				if (value.length() > 6) {
					value = value.substring(0, 6);
				}
				
				if(Util.FORMATOU){
					saldo = Util.getFormatedCurrency(value);
					edtSaldoAtual.setText(saldo);
					edtSaldoAtual.setSelection(edtSaldoAtual.length());
				}else{
					Util.FORMATOU = true;
				}
				
				if(saldo.equals("R$0.00")){
					btnNext.setEnabled(false);
				}
			}
		});
		
		if(!edtSaldoAtual.getText().toString().equals("R$0.00")){
			btnNext.setEnabled(true);
		}
		
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					FileOutputStream fos = openFileOutput(Util.FILESALDO, Context.MODE_PRIVATE);
					String value = edtSaldoAtual.getText().toString().replace("R$ ", "");
					fos.write(value.getBytes());
					fos.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				final Intent intent = new Intent(v.getContext(), DataRecebimentoActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		        ActivitySwitcher.animationOut(findViewById(R.id.saldoRelativeLayout),
		                getWindowManager(),
		                new ActivitySwitcher.AnimationFinishedListener() {
		                    @Override
		                    public void onAnimationFinished() {
//		                        startActivity(intent);
		                    }
		                });
				startActivity(intent);
				finish();
			}
		});
		
	}

}