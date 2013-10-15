package br.com.vale.controlevr.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import br.com.vale.controlevr.classes.Dia;

public class Util {
	
	public static boolean FORMATOU = true;
	public static final String FILESALDO = "FILESALDO";
	public static final String FILEVALORGASTO = "FILEVALORGASTO";
	public static final String FILEDIARECEBER = "FILEDIARECEBER";
	public static final String FILEDIASUSAR = "FILEDIASUSAR";
	public static final String FILEPRIMEROACESSO = "FILEPRIMEIROACESSO";
	
	public static ArrayList<Dia> dias = new ArrayList<Dia>();
	public static ArrayList<Dia> diasSelecionados = new ArrayList<Dia>();
	
	public static final String PREFS_NAME = "ControleVRPrefsFile";

	public static SharedPreferences settings;

	public static String getFormatedCurrency(String value) {
		if(value.length() > 3){
			if(value.charAt(0) == '0'){
				value = value.substring(1);
			}
		}else{
			value = "0" + value;
		}
		
		value = "R$" + value.substring(0, value.length() - 2) + "." + value.substring(value.length() - 2);
		FORMATOU = false;
		return value;
	}
	
	public static void writeStringFile(Context context, String filename, String dados) {
		try {
			FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			fos.write(dados.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String readStringFile(Context context, String filename){
		try {
			FileInputStream fis = context.openFileInput(filename);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader buffreader = new BufferedReader(isr);
			String readString = buffreader.readLine ( );
			String datax = "";
            while(readString != null) {
                datax = datax + readString;
                readString = buffreader.readLine( );
            }
			fis.close();
			return datax;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return new String();
	}
	
	public static void saveList(Context context, String filename, List<Dia> list) {
		try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(list);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}

	public static List<String> readList(Context context, String filename) {
	    try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream in = new ObjectInputStream(fis);
            List<String> list = (List<String>) in.readObject();
            in.close();
            return list;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
	    return new ArrayList<String>();
	}
	
	public static ArrayList<Dia> readListDia(Context context, String filename) {
	    try {
            FileInputStream fis = context.openFileInput(filename);
            ObjectInputStream in = new ObjectInputStream(fis);
            ArrayList<Dia> list = (ArrayList<Dia>) in.readObject();
            in.close();
            return list;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
	    return new ArrayList<Dia>();
	}

}