package ourwallet.example.com.ourwallet;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ourwallet.example.com.ourwallet.Database.AndroidOpenDbHelper;
import ourwallet.example.com.ourwallet.Models.Contacts_Model;
import ourwallet.example.com.ourwallet.RecyclerViewAdapters.Contacts_recyclerView;
import ourwallet.example.com.ourwallet.RecyclerViewAdapters.SimpleDividerItemDecoration;

public class Contacts extends AppCompatActivity implements com.android.volley.Response.Listener<JSONObject>, com.android.volley.Response.ErrorListener{
	private Toolbar toolbar;
	private TextView toolbar_title;

	private RecyclerView uGraduateNamesListView;
	private Contacts_recyclerView uGraduateListAdapter;
	private ArrayList<Contacts_Model> contacts_list;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list);
		toolbar=(Toolbar)findViewById(R.id.toolbar);
		toolbar_title=(TextView)findViewById(R.id.title_toolbar);
        uGraduateNamesListView = (RecyclerView) findViewById(R.id.contacts_list);
        contacts_list = new ArrayList<Contacts_Model>();
	//	uGraduateListAdapter = new Contacts_recyclerView(this, populateList());
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
		uGraduateNamesListView.addItemDecoration(new SimpleDividerItemDecoration(this));
		uGraduateNamesListView.setLayoutManager(mLayoutManager);
//		uGraduateNamesListView.setAdapter(uGraduateListAdapter);
		setSupportActionBar(toolbar);
		final ActionBar ab = getSupportActionBar();
		ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
		ab.setDisplayShowTitleEnabled(false);
		toolbar_title.setText("Add Contact");
		SharedPreferences firsttime_get=getSharedPreferences("1st_time",Context.MODE_PRIVATE);
		int validate=firsttime_get.getInt("backup",0);

			if(validate==1) {
				uGraduateListAdapter = new Contacts_recyclerView(this, populateList());
				mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
				uGraduateNamesListView.addItemDecoration(new SimpleDividerItemDecoration(this));
				uGraduateNamesListView.setLayoutManager(mLayoutManager);
				uGraduateNamesListView.setAdapter(uGraduateListAdapter);

			}
		else{Log.e("2","second bar");
				try {
					List();
					Log.e("1","pehli bar");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	public List<Contacts_Model> populateList(){
		List<String> uGraduateNamesList = new ArrayList<String>();
		AndroidOpenDbHelper openHelperClass = new AndroidOpenDbHelper(this);
		SQLiteDatabase sqliteDatabase = openHelperClass.getReadableDatabase();
		Cursor cursor = sqliteDatabase.query(AndroidOpenDbHelper.TABLE_NAME_GPA, null, null, null, null, null, null);
		startManagingCursor(cursor);
		while (cursor.moveToNext()) {
			Log.e("count",cursor.toString());
			String First_name = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.Contact_Name));
			String Phone = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.Contact_number));
			String Email = cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.Contact_email));
			String Fax=cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.Contact_fax));
			String Address=cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.Contact_address));
			String Grade=cursor.getString(cursor.getColumnIndex(AndroidOpenDbHelper.Contact_grade));
			Contacts_Model ugPojoClass = new Contacts_Model();
			ugPojoClass.setFirst_name(First_name);
			ugPojoClass.setPhone(Phone);
			ugPojoClass.setEmail(Email);
			ugPojoClass.setFax(Fax);
			ugPojoClass.setAddress(Address);
			ugPojoClass.setGrade(Grade);
			contacts_list.add(ugPojoClass);
			uGraduateNamesList.add(First_name);
		}
		sqliteDatabase.close();
		return contacts_list;
	}

	private void List() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("user_id",Constants.user_id);
		JSONObject json2 = new JSONObject();
		json2.put("to","orupartners");
		json2.put("methods","fetch_contacts");
		json2.accumulate("complex",json);
		String url = "http://orupartners.com/cp/redirect_to.php";
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url,json2, this ,this){

		};
		jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
				5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
	}

	@Override
	public void onErrorResponse(VolleyError error) {

	}

	@Override
	public void onResponse(JSONObject response) {
		Log.e("response",response.toString());
		try {
			SharedPreferences firsttime_get=getSharedPreferences("1st_time",Context.MODE_PRIVATE);
			SharedPreferences.Editor editor=firsttime_get.edit();
			editor.putInt("backup",1);
			editor.commit();
			JSONArray array = response.optJSONArray("contacts");
			for (int i = 0; i < array.length(); i++) {
				Contacts_Model contacts_model=new Contacts_Model();
				JSONObject jsonObject = array.getJSONObject(i);
				contacts_model.setFirst_name(jsonObject.getString("first_name"));
				contacts_model.setLast_name(jsonObject.getString("last_name"));
				contacts_model.setFax(jsonObject.getString("fax"));
				contacts_model.setAddress(jsonObject.getString("address"));
				contacts_model.setGrade(jsonObject.getString("grade"));
				contacts_model.setEmail(jsonObject.getString("email"));
				contacts_model.setPhone(jsonObject.getString("phone"));
			//	contacts_model.setAddress(jsonObject.getString("city"));
				contacts_list.add(contacts_model);
				insert_Contacts(contacts_model);
			}
		uGraduateListAdapter = new Contacts_recyclerView(this, contacts_list);
			RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
			uGraduateNamesListView.addItemDecoration(new SimpleDividerItemDecoration(this));
			uGraduateNamesListView.setLayoutManager(mLayoutManager);
		uGraduateNamesListView.setAdapter(uGraduateListAdapter);


		}catch (Exception ex){
			ex.printStackTrace();
		}


	}
	public void insert_Contacts(Contacts_Model contacts_model){
		AndroidOpenDbHelper androidOpenDbHelperObj = new AndroidOpenDbHelper(this);
		SQLiteDatabase sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(AndroidOpenDbHelper.Contact_Name, contacts_model.getFirst_name());
	contentValues.put(AndroidOpenDbHelper.Contact_number, contacts_model.getPhone());
		contentValues.put(AndroidOpenDbHelper.Contact_email, contacts_model.getEmail());
		contentValues.put(AndroidOpenDbHelper.Contact_Lname, contacts_model.getLast_name());
		contentValues.put(AndroidOpenDbHelper.Contact_fax, contacts_model.getFax());
		contentValues.put(AndroidOpenDbHelper.Contact_address, contacts_model.getAddress());
		contentValues.put(AndroidOpenDbHelper.Contact_grade, contacts_model.getGrade());
		long affectedColumnId = sqliteDatabase.insert(AndroidOpenDbHelper.TABLE_NAME_GPA, null, contentValues);
		sqliteDatabase.close();
	Toast.makeText(this, "Values inserted column ID is :" + affectedColumnId, Toast.LENGTH_SHORT).show();

	}
}