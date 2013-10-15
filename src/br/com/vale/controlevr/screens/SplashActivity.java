package br.com.vale.controlevr.screens;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.vale.controlevr.R;
import br.com.vale.controlevr.util.Util;

public class SplashActivity extends Activity {

	public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.splash);
	    
	    TextView textTitle = (TextView) findViewById(R.id.title);
		Typeface typeface = Typeface.createFromAsset(getAssets(), "Aller_Std_Lt.ttf");
		textTitle.setTypeface(typeface);
	    
		Handler handler = new Handler(); 
	    handler.postDelayed(new Runnable() { 
	         public void run() { 
	        	 Util.settings = getSharedPreferences(Util.PREFS_NAME, 0);
	     	    
//	     	    if (Util.settings.getBoolean("primeiroAcesso", true)) {
	     	    	Util.settings.edit().putBoolean("primeiroAcesso", false).commit();
	     	    	Intent intent = new Intent(getApplicationContext(), SaldoActivity.class);
	     			startActivity(intent);
	     			finish();
//	     		} else {
//	     			Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
//	     			startActivity(intent);
//	     			finish();
//	     		} 
	         } 
	    }, 3000); 
	    
	    StartAnimations();
	}
	
	private void StartAnimations() {
	    Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
	    anim.reset();
	    LinearLayout l =(LinearLayout) findViewById(R.id.lin_lay);
	    l.clearAnimation();
	    l.startAnimation(anim);
	
	    anim = AnimationUtils.loadAnimation(this, R.anim.translate);
	    anim.reset();
	    ImageView iv = (ImageView) findViewById(R.id.logo);
	    iv.clearAnimation();
	    iv.startAnimation(anim);
	}
}