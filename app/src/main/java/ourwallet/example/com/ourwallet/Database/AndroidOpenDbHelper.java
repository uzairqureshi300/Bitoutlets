package ourwallet.example.com.ourwallet.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

// A helper class to manage database creation and version management. 
public class AndroidOpenDbHelper extends SQLiteOpenHelper {
	// Database attributes
	public static final String DB_NAME = "undergraduate_gpa_db";
	public static final int DB_VERSION = 1;

	// Table attributes
	public static final String TABLE_NAME_GPA = "undergraduate_details_table";
	public static final String Contact_Name = "undergraduate_name_column";
	public static final String Contact_number = "undergraduate_uni_id_column";
	public static final String Contact_email = "undergraduate_gpa_column";
	public static final String Contact_Lname = "contact_Lname_column";
	public static final String Contact_fax = "contact_fax_column";
	public static final String Contact_address = "contact_address_column";
	public static final String Contact_portal = "contact_portal_column";
	public static final String Contact_wallet_address = "contact_wallet_column";
	public static final String Contact_createdby = "contact_createdby_column";
	public static final String Contact_grade = "contact_grade_column";

	public AndroidOpenDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	// Called when the database is created for the first time. 
	//This is where the creation of tables and the initial population of the tables should happen.
	@Override
	public void onCreate(SQLiteDatabase db) {
		// We need to check whether table that we are going to create is already exists.
		//Because this method get executed every time we created an object of this class. 
		//"create table if not exists TABLE_NAME ( BaseColumns._ID integer primary key autoincrement, FIRST_COLUMN_NAME text not null, SECOND_COLUMN_NAME integer not null);"
		String sqlQueryToCreateUndergraduateDetailsTable = "create table if not exists " + TABLE_NAME_GPA + " ( " + BaseColumns._ID + " integer primary key autoincrement, "
				+ Contact_Name + " text not null, "
				+ Contact_number + " text not null, " +
				Contact_Lname + " text not null, " +
				Contact_fax + " text not null, " +
				Contact_address + " text not null, " +
				Contact_grade + " text not null, "
				+ Contact_email + " real not null);";
		// Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
		db.execSQL(sqlQueryToCreateUndergraduateDetailsTable);
	}

	// onUpgrade method is use when we need to upgrade the database in to a new version
	//As an example, the first release of the app contains DB_VERSION = 1
	//Then with the second release of the same app contains DB_VERSION = 2
	//where you may have add some new tables or alter the existing ones
	//Then we need check and do relevant action to keep our pass data and move with the next structure
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion == 1 && newVersion == 2) {
			// Upgrade the database
		}
	}
}
