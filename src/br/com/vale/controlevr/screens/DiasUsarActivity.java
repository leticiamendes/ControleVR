package br.com.vale.controlevr.screens;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import br.com.vale.controlevr.R;
import br.com.vale.controlevr.adapter.CalendarAdapter;
import br.com.vale.controlevr.util.MyGridView;
import br.com.vale.controlevr.util.Util;
import br.com.vale.controlevr.util.Utility;

public class DiasUsarActivity extends FragmentActivity {
	
	private MyGridView gridview;
	
	private GregorianCalendar month, itemmonth;// calendar instances.
	private CalendarAdapter adapter;// adapter instance
	private Handler handler;// for grabbing some event values for showing the dot marker.
	private ArrayList<String> items; // container to store calendar items which needs showing the event marker
	private LinearLayout rLayout;
	private ArrayList<String> desc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.diasusar);
		
		final ScrollView scrollview = ((ScrollView) findViewById(R.id.scrolDiasUsar)); 
		scrollview.post(new Runnable() {
			  @Override public void run() {
			    scrollview.fullScroll(ScrollView.FOCUS_UP);
			  }
			});
		
		TextView textTitle = (TextView) findViewById(R.id.txtTitle);
		Typeface typeface = Typeface.createFromAsset(getAssets(), "Aller_Std_Lt.ttf");
		textTitle.setTypeface(typeface);
		
		TextView textQuestion = (TextView) findViewById(R.id.question);
		typeface = Typeface.createFromAsset(getAssets(), "QuattrocentoSans-Regular.ttf");
		textQuestion.setTypeface(typeface);
		
		rLayout = (LinearLayout) findViewById(R.id.text);
		month = (GregorianCalendar) GregorianCalendar.getInstance();
		itemmonth = (GregorianCalendar) month.clone();
		items = new ArrayList<String>();
		adapter = new CalendarAdapter(this, month);
		
		gridview = (MyGridView) findViewById(R.id.gridview);
		gridview.setOnTouchListener(new OnTouchListener(){
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        if(event.getAction() == MotionEvent.ACTION_MOVE){
		            return true;
		        }
		        return false;
		    }
		});
		
		handler = new Handler();
		handler.post(calendarUpdater);

		TextView title = (TextView) findViewById(R.id.titleCalendar);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

		final Button btnNext = (Button) findViewById(R.id.next);
		
		boolean temDiaSelecionado = false;
		for(int i = 0; i < Util.dias.size(); i++){
			if(Util.dias.get(i).isSelecionado()){
				temDiaSelecionado = true;
				break;
			}
		}
		
		if(temDiaSelecionado)
			btnNext.setEnabled(true);
		
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				// removing the previous view if added
				if (((LinearLayout) rLayout).getChildCount() > 0) {
					((LinearLayout) rLayout).removeAllViews();
				}
				desc = new ArrayList<String>();
				String selectedGridDate = Util.dias.get(position).getDia();
				String[] separatedTime = selectedGridDate.split("-");
				String gridvalueString = separatedTime[0].replaceFirst("^0*", "");// taking last part of date. ie; 2 from 2012-12-02.
				int gridvalue = Integer.parseInt(gridvalueString);
				
				if (!((gridvalue > 10) && (position < 8)) && !((gridvalue < 7) && (position > 28))) {
					((CalendarAdapter) parent.getAdapter()).setSelected(v, position);
				}

				for (int i = 0; i < Utility.startDates.size(); i++) {
					if (Utility.startDates.get(i).equals(selectedGridDate)) {
						desc.add(Utility.nameOfEvent.get(i));
					}
				}

				if (desc.size() > 0) {
					for (int i = 0; i < desc.size(); i++) {
						TextView rowTextView = new TextView(DiasUsarActivity.this);

						// set some properties of rowTextView or something
						rowTextView.setText("Event:" + desc.get(i));
						rowTextView.setTextColor(Color.BLACK);

						// add the textview to the linearlayout
						rLayout.addView(rowTextView);
					}
				}
				desc = null;
				
				boolean temDiaSelecionado = false;
				for(int i = 0; i < Util.dias.size(); i++){
					if(Util.dias.get(i).isSelecionado()){
						temDiaSelecionado = true;
						break;
					}
				}
				
				if(temDiaSelecionado){
					btnNext.setEnabled(true);
				}else{
					btnNext.setEnabled(false);
				}
			}
		});
		
		gridview.setAdapter(adapter);
		refreshCalendar();
		
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), PrincipalActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
	}
	
	public void refreshCalendar() {
		TextView title = (TextView) findViewById(R.id.titleCalendar);

		adapter.refreshDays();
		adapter.notifyDataSetChanged();
		handler.post(calendarUpdater); // generate some calendar items
		gridview.invalidate();

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	public Runnable calendarUpdater = new Runnable() {
		@Override
		public void run() {
			items.clear();

			for (int i = 0; i < Utility.startDates.size(); i++) {
				itemmonth.add(GregorianCalendar.DATE, 1);
				items.add(Utility.startDates.get(i).toString());
			}
			adapter.setItems(items);
			adapter.notifyDataSetChanged();
			gridview.postInvalidate();
		}
	};
	
}