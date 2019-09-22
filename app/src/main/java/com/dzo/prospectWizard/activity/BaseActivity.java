package com.dzo.prospectWizard.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.dzo.prospectWizard.R;
import com.dzo.prospectWizard.ImageView.ManageLeadActivity;
import com.dzo.prospectWizard.adapters.NavigationDrawerListAdapter;
import com.dzo.prospectWizard.models.Items;

import java.util.ArrayList;


public class BaseActivity extends AppCompatActivity {


	protected FrameLayout frameLayout;



	protected ListView mDrawerList;
	SharedPreferences sharedPreferences;
	int Color1;
	RelativeLayout drawer;
	String userName;


	protected String[] listArray = { "Home" ,"Home", "Add New Prospect", "Import Contacts", "Calendar Event", "Additional Task","Tracker","Home" };
	protected ArrayList<Items> _items;


	protected static int position;

	private static boolean isLaunch = true;

	private DrawerLayout mDrawerLayout;


	private ActionBarDrawerToggle actionBarDrawerToggle;


	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation_drawer_base_layout);

		frameLayout = (FrameLayout)findViewById(R.id.content_frame);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		drawer=(RelativeLayout)findViewById(R.id.drawer);

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
		userName = preferences.getString("UserName", "");

		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Color1 = sharedPreferences.getInt("Color", -7403255);
		mDrawerList.setBackgroundColor(Color1);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color1));
		SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String Font = sharedPreferences1.getString("item", null);
		Typeface face = null;
		if (Font != null){
			face= Typeface.createFromAsset(getAssets(),Font);
		}

		_items = new ArrayList<Items>();
		_items.add(new Items("Home", "", R.drawable.home));
		_items.add(new Items("Add New Prospect", "", R.drawable.edit_profile));
		_items.add(new Items("Import Contacts", "", R.drawable.contact));
		_items.add(new Items("Calendar Event", "", R.drawable.calendar));
		_items.add(new Items("Additional Task", "", R.drawable.calendar));
		_items.add(new Items("Tracker","",R.drawable.track));
		_items.add(new Items("Log Out", "", R.drawable.logo_out));

		View header = (View)getLayoutInflater().inflate(R.layout.list_view_header_layout, null);
		mDrawerList.addHeaderView(header);

		mDrawerList.setAdapter(new NavigationDrawerListAdapter(this, _items,face));

		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				openActivity(position);
			}
		});

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		actionBarDrawerToggle = new ActionBarDrawerToggle(
				this,
				mDrawerLayout,
				R.string.open_drawer,
				R.string.close_drawer)
		{
			@Override
			public void onDrawerClosed(View drawerView) {
				getSupportActionBar().setTitle(listArray[position]);
				invalidateOptionsMenu();
				super.onDrawerClosed(drawerView);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(listArray[position]);
                invalidateOptionsMenu();
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
			}

			@Override
			public void onDrawerStateChanged(int newState) {
				super.onDrawerStateChanged(newState);
			}
		};
		mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
		actionBarDrawerToggle.syncState();


		if(isLaunch){

			isLaunch = false;
			openActivity(1);
		}
	}



	/**
	 * @param position
	 *
	 */
	protected void openActivity(int position) {


		mDrawerLayout.closeDrawer(mDrawerList);
		BaseActivity.position = position;
		switch (position) {
		case 0:

			break;
		case 1:
				Intent ints = new Intent(BaseActivity.this, HomeActivity.class);
				startActivity(ints);
				finish();
				break;
	    case 2:
				Intent intes = new Intent(BaseActivity.this, ManageLeadActivity.class);
				startActivity(intes);
				finish();

				break;
		case 3:

			   Intent intents = new Intent(BaseActivity.this, ContactActivity.class);
			   startActivity(intents);
			   finish();

			break;
		case 4:
				Intent intet = new Intent(BaseActivity.this, CalenderActivity.class);
			    startActivity(intet);
			    finish();
				break;

			case 5:
				Intent intet12 = new Intent(BaseActivity.this, AdditionalActivity.class);
				startActivity(intet12);
				finish();
				break;
			case 6:
				Intent intet94 = new Intent(BaseActivity.this, Tracker.class);
				startActivity(intet94);
				finish();
				break;
			case 7:
				Intent intet1 = new Intent(BaseActivity.this, LoginActivity.class);
				startActivity(intet1);
				finish();
				break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if(mDrawerLayout.isDrawerOpen(mDrawerList)){
			mDrawerLayout.closeDrawer(mDrawerList);
		}else {
			mDrawerLayout.openDrawer(mDrawerList);
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {


		switch (item.getItemId()) {
			case R.id.action_refresh:
				Intent inte = new Intent(BaseActivity.this, HomeActivity.class);
				startActivity(inte);
				break;

			default:
				break;
		}

		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
