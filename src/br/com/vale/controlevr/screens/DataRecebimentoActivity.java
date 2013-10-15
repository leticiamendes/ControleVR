package br.com.vale.controlevr.screens;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import br.com.vale.controlevr.R;
import br.com.vale.controlevr.util.Util;

public class DataRecebimentoActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.datarecebimento);
		
		TextView textTitle = (TextView) findViewById(R.id.txtTitle);
		Typeface typeface = Typeface.createFromAsset(getAssets(), "Aller_Std_Lt.ttf");
		textTitle.setTypeface(typeface);
		
		TextView textQuestion = (TextView) findViewById(R.id.question);
		typeface = Typeface.createFromAsset(getAssets(), "QuattrocentoSans-Regular.ttf");
		textQuestion.setTypeface(typeface);
		
		final EditText edtDataRecebimento = (EditText) findViewById(R.id.edtDataRecebimento);

		final Calendar diaDeHoje = Calendar.getInstance();
		final Calendar diaDeRecebimento = Calendar.getInstance();
		
		final Button btnNext = (Button) findViewById(R.id.next);

		final DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				diaDeRecebimento.set(Calendar.YEAR, year);
				diaDeRecebimento.set(Calendar.MONTH, monthOfYear+1);
				diaDeRecebimento.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				edtDataRecebimento.setText(diaDeRecebimento.get(Calendar.DAY_OF_MONTH) + "/" + diaDeRecebimento.get(Calendar.MONTH) + "/" + diaDeRecebimento.get(Calendar.YEAR));
			}
		};
		
		final DatePickerDialog datePickerDialog = new DatePickerDialog(DataRecebimentoActivity.this, d, 
				diaDeHoje.get(Calendar.YEAR), diaDeHoje.get(Calendar.MONTH), diaDeHoje.get(Calendar.DAY_OF_MONTH)){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				super.onClick(dialog, which);
				if(which == DialogInterface.BUTTON_POSITIVE){
					btnNext.setEnabled(true);
				}
			}
		};

		edtDataRecebimento.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(!datePickerDialog.isShowing())
					datePickerDialog.show();
				return false;
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					FileOutputStream fos = openFileOutput(Util.FILEDIARECEBER, Context.MODE_PRIVATE);
					fos.write(edtDataRecebimento.getText().toString().getBytes());
					fos.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				Intent intent = new Intent(v.getContext(), DiasUsarActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}