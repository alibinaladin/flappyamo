package com.ali.amo;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


public class AndroidLauncher extends AndroidApplication {


	private RelativeLayout layout;
	private RelativeLayout.LayoutParams params;
	private AdView bannerAd;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		View gameView = initializeForView(new amo(), config);

		////////// Define the layout
		layout = new RelativeLayout(this);
		layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);

		bannerAd = new AdView(this);
		bannerAd.setAdUnitId("ca-app-pub-5402677403633185/1530084846");
//		RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("94F47E9842A94EA9F5EBD2D2673431B1");
		bannerAd.setAdSize(AdSize.SMART_BANNER);

		layout.addView(bannerAd, params);
		setContentView(layout);

		AdRequest ad = new AdRequest.Builder().build();
		bannerAd.loadAd(ad);

	}

}
