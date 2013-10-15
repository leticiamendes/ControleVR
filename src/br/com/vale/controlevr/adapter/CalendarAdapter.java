package br.com.vale.controlevr.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.vale.controlevr.R;
import br.com.vale.controlevr.classes.Dia;
import br.com.vale.controlevr.util.Util;

public class CalendarAdapter extends BaseAdapter {
	private Context mContext;

	private java.util.Calendar mesAtual;
	public GregorianCalendar mesAnterior; // calendar instance for previous month
	/**
	 * calendar instance for previous month for getting complete view
	 */
	public GregorianCalendar previousMonthMaxSet;
	private GregorianCalendar selectedDate;
	int primeiroDia;
	int numeroMaxSemanas;
	int ultimoDiaMesAnterior;
	int calMaxP;
	int lastWeekDay;
	int leftDays;
	int tamanhoMes;
	String itemvalue, curentDateString;
	DateFormat df;
	ArrayList<View> viewsAdded;

	private ArrayList<String> items;

	public CalendarAdapter(Context c, GregorianCalendar monthCalendar) {
		Locale.setDefault(Locale.US);
		mesAtual = monthCalendar;
		selectedDate = (GregorianCalendar) monthCalendar.clone();
		mContext = c;
		mesAtual.set(GregorianCalendar.DAY_OF_MONTH, 1);
		items = new ArrayList<String>();
		df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		curentDateString = df.format(selectedDate.getTime());
		viewsAdded = new ArrayList<View>();
	}

	public void setItems(ArrayList<String> items) {
		for (int i = 0; i != items.size(); i++) {
			if (items.get(i).length() == 1) {
				items.set(i, "0" + items.get(i));
			}
		}
		items = new ArrayList<String>();
		this.items = items;
	}

	public int getCount() {
		return Util.dias.size();
	}

	public Object getItem(int position) {
		return Util.dias.get(position);
	}
	
	public long getItemId(int position) {
		return 0;
	}

	// create a new view for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		TextView dayView;
		
		if(position == 0 || position == 1 || position == 2 || position == 3 || position == 4 || position == 5 || position == 6){
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.calendar_item_top, null);
			}
			
			dayView = (TextView) v.findViewById(R.id.dayOfWeek);
			dayView.setClickable(false);
			dayView.setFocusable(false);
			
			if(position == 0){
				dayView.setText("Dom");
			}else if(position == 1){
				dayView.setText("Seg");
			}else if(position == 2){
				dayView.setText("Ter");
			}else if(position == 3){
				dayView.setText("Qua");
			}else if(position == 4){
				dayView.setText("Qui");
			}else if(position == 5){
				dayView.setText("Sex");
			}else if(position == 6){
				dayView.setText("Sab");
			}
		}else{
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.calendar_item, null);
			}
				
			dayView = (TextView) v.findViewById(R.id.date);
			
			if (((Integer.parseInt(Util.dias.get(position).getDia()) > 1) && (position < primeiroDia)) || ((Integer.parseInt(Util.dias.get(position).getDia()) < 7) && (position > 35))) {
				dayView.setTextColor(Color.WHITE);
				dayView.setClickable(true);
				dayView.setFocusable(true);
				dayView.setEnabled(true);
			} else {
				dayView.setTextColor(Color.BLUE);
			}
			
			v.setBackgroundResource(R.drawable.list_item_background);
			dayView.setText(Util.dias.get(position).getDia());

			String date = Util.dias.get(position).getDiaCompleto();

			if (date.length() == 1) {
				date = "0" + date;
			}
			String monthStr = "" + (mesAtual.get(GregorianCalendar.MONTH) + 1);
			if (monthStr.length() == 1) {
				monthStr = "0" + monthStr;
			}
			
			if(estaSelecionado(Util.dias.get(position).getDiaCompleto())){
				setSelected(v, position);
			}
		}
		
		return v;
	}

	public View setSelected(View view, int position) {
		if(viewsAdded.contains(view)){
			viewsAdded.remove(view);
			view.setBackgroundResource(R.drawable.list_item_background);
			Util.dias.get(position).setSelecionado(false);
			Util.diasSelecionados.remove(Util.dias.get(position));
		}else{
			viewsAdded.add(view);
			view.setBackgroundResource(R.drawable.calendar_cel_selectl);
			Util.dias.get(position).setSelecionado(true);
			Util.diasSelecionados.add(Util.dias.get(position));
		}
		return view;
	}

	public void refreshDays() {
		// clear items
		items.clear();
		Util.dias.clear();
		mesAnterior = (GregorianCalendar) mesAtual.clone();
		// month start day. ie; sun, mon, etc
		primeiroDia = mesAtual.get(GregorianCalendar.DAY_OF_WEEK);
		primeiroDia = primeiroDia + 7;
		// finding number of weeks in current month.
		numeroMaxSemanas = mesAtual.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
		// allocating maximum row number for the gridview.
		tamanhoMes = (numeroMaxSemanas + 1) * 7;
		ultimoDiaMesAnterior = getMaxP(); // previous month maximum day 31,30....
		calMaxP = ultimoDiaMesAnterior - (primeiroDia - 1);// calendar offday starting 24,25 ...
		/**
		 * Calendar instance for getting a complete gridview including the three
		 * month's (previous,current,next) dates.
		 */
		previousMonthMaxSet = (GregorianCalendar) mesAnterior.clone();
		/**
		 * setting the start date as previous month's required date.
		 */
		previousMonthMaxSet.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

		/**
		 * filling calendar gridview.
		 */
		for (int n = 0; n < tamanhoMes; n++) {
			itemvalue = df.format(previousMonthMaxSet.getTime());
			previousMonthMaxSet.add(GregorianCalendar.DATE, 1);
			Dia day = new Dia(itemvalue, "0", false);
			Util.dias.add(day);
		}
	}

	private int getMaxP() {
		int maxP;
		if (mesAtual.get(GregorianCalendar.MONTH) == mesAtual.getActualMinimum(GregorianCalendar.MONTH)) {
			mesAnterior.set((mesAtual.get(GregorianCalendar.YEAR) - 1), mesAtual.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			mesAnterior.set(GregorianCalendar.MONTH, mesAtual.get(GregorianCalendar.MONTH) - 1);
		}
		maxP = mesAnterior.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		return maxP;
	}
	
	public int getCountDays(){
		return viewsAdded.size();
	}
	
	private boolean estaSelecionado(String dia){
		boolean estaSelecionado = false;
		for(int i = 0; i < Util.diasSelecionados.size(); i++){
			if(Util.diasSelecionados.get(i).getDiaCompleto().equals(dia)){
				estaSelecionado = true;
				break;
			}
		}
		return estaSelecionado;
	}

}