package com.example.mycashbook.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.jjoe64.graphview.series.DataPoint
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "CashBook"

class DatabaseHandler(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    private val TABLE_USER = "User"
    private val KEY_ID = "id"
    private val KEY_NAME = "username"
    private val KEY_PASS = "password"

    private val TABLE_CASHFLOW = "Cashflow"
    private val KEY_ID_CASHFLOW = "id"
    private val KEY_MASUK_KELUAR_CASHFLOW = "masukkeluar"
    private val KEY_TANGGAL_CASHFLOW = "tanggal"
    private val KEY_NOMINAL_CASHFLOW = "nominal"
    private val KEY_KETERANGAN_CASHFLOW = "keterangan"

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USER_TABLE =
            ("CREATE TABLE " + TABLE_USER.toString() + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                    + KEY_PASS.toString() + " TEXT" + ")")
        db!!.execSQL(CREATE_USER_TABLE)
        val CREATE_CASHFLOW_TABLE =
            ("CREATE TABLE " + TABLE_CASHFLOW.toString() + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MASUK_KELUAR_CASHFLOW + " TEXT,"
                    + KEY_TANGGAL_CASHFLOW.toString() + " DATE," + KEY_NOMINAL_CASHFLOW.toString() + " CURRENCY," + KEY_KETERANGAN_CASHFLOW.toString() + " TEXT" + ")")
        db!!.execSQL(CREATE_CASHFLOW_TABLE)
        db!!.execSQL("INSERT INTO User(username,password) VALUES('rahmat','rahmat')")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_USER)
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_CASHFLOW)
        onCreate(db)
    }

    fun addRecord(userModels: UserModel) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, userModels.username)
        values.put(KEY_PASS, userModels.password)
        db.insert(TABLE_USER, null, values)
        db.close()
    }

    fun addRecord(cashflowModel: CashflowModel) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_MASUK_KELUAR_CASHFLOW, cashflowModel.masukKeluar)
        values.put(KEY_TANGGAL_CASHFLOW, cashflowModel.tanggal)
        values.put(KEY_NOMINAL_CASHFLOW, cashflowModel.nominal)
        values.put(KEY_KETERANGAN_CASHFLOW, cashflowModel.keterangan)
        db.insert(TABLE_CASHFLOW, null, values)
        db.close()
    }

    fun updateUser(user: UserModel): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, user.username)
        values.put(KEY_PASS, user.password)

        // updating row
        return db.update(
            TABLE_USER,
            values,
            "$KEY_ID = ?",
            arrayOf(user.getIdUser().toString())
        )
    }

    fun updatePassword(id: Int, password : String): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_PASS, password)

        // updating row
        return db.update(
            TABLE_USER,
            values,
            "$KEY_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun getUser(username: String, password : String): UserModel? {
        val db = this.readableDatabase
        val cursor: Cursor? = db.query(
            TABLE_USER, arrayOf(
                KEY_ID,
                KEY_NAME, KEY_PASS
            ), "$KEY_NAME=? AND $KEY_PASS=?", arrayOf(username, password), null, null, null, null
        )
        cursor?.moveToFirst()
        // return contact
        if(cursor!!.getCount() > 0){
            return UserModel(
                cursor?.getString(0)!!.toInt(),
                cursor.getString(1),
                cursor.getString(2)
            )
        }
        else{
            return null
        }
    }

    fun getUser(id: Int): UserModel {
        val db = this.readableDatabase
        val cursor: Cursor? = db.query(
            TABLE_USER, arrayOf(
                KEY_ID,
                KEY_NAME, KEY_PASS
            ), "$KEY_ID=?", arrayOf(id.toString()), null, null, null, null
        )
        cursor?.moveToFirst()
        // return contact
        if(cursor!!.getCount() > 0){
            return UserModel(
                cursor?.getString(0)!!.toInt(),
                cursor.getString(1),
                cursor.getString(2)
            )
        }
        return UserModel()
    }

    // get All Record
    fun getAllRecord(): List<CashflowModel> {
        val cashflowList: MutableList<CashflowModel> = ArrayList<CashflowModel>()
        // Select All Query
        val selectQuery = "SELECT  * FROM $TABLE_CASHFLOW"
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val cashflowModel = CashflowModel()
                cashflowModel.masukKeluar = cursor.getString(1)
                cashflowModel.tanggal = cursor.getString(2)
                cashflowModel.nominal = cursor.getInt(3)
                cashflowModel.keterangan = cursor.getString(4)
                cashflowList.add(cashflowModel)
            } while (cursor.moveToNext())
        }

        // return contact list
        return cashflowList
    }

    fun getAllMasuk(): Int {
        val selectQuery = "SELECT  * FROM $TABLE_CASHFLOW WHERE $KEY_MASUK_KELUAR_CASHFLOW = 'masuk'"
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        var total = 0
        if (cursor.moveToFirst()) {
            do {
                total  += cursor.getInt(3)
            } while (cursor.moveToNext())
        }

        // return contact list
        return total
    }

    fun getAllKeluar(): Int {
        val selectQuery = "SELECT  * FROM $TABLE_CASHFLOW WHERE $KEY_MASUK_KELUAR_CASHFLOW = 'keluar'"
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        var total = 0
        if (cursor.moveToFirst()) {
            do {
                total  += cursor.getInt(3)
            } while (cursor.moveToNext())
        }

        // return contact list
        return total
    }

    fun getAllMasukForGraph(): List<DataPoint> {
        val cashflowList: MutableList<DataPoint> = ArrayList<DataPoint>()
        // Select All Query
        val selectQuery = "SELECT $KEY_TANGGAL_CASHFLOW, SUM(NOMINAL) FROM $TABLE_CASHFLOW WHERE $KEY_MASUK_KELUAR_CASHFLOW = 'masuk' GROUP BY $KEY_TANGGAL_CASHFLOW, $KEY_MASUK_KELUAR_CASHFLOW ORDER BY $KEY_TANGGAL_CASHFLOW ASC"
        val db = this.writableDatabase
        var dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        var kas : Int = 0
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
//                var formatDate = dateFormatter.format(dateFormatter.parse(cursor.getString(2)))
                var date : String = cursor.getString(0).subSequence(0, 2).toString()
//                var date = dateFormatter.parse(cursor.getString(2))
                var dateDouble : Double = date.toDouble()
                val dataPoint = DataPoint(dateDouble, cursor.getDouble(1))
                cashflowList.add(dataPoint)
            } while (cursor.moveToNext())
        }

        // return contact list
        return cashflowList
    }

    fun getAllKeluarForGraph(): List<DataPoint> {
        val cashflowList: MutableList<DataPoint> = ArrayList<DataPoint>()
        // Select All Query
        val selectQuery = "SELECT $KEY_TANGGAL_CASHFLOW, SUM(NOMINAL) FROM $TABLE_CASHFLOW WHERE $KEY_MASUK_KELUAR_CASHFLOW = 'keluar' GROUP BY $KEY_TANGGAL_CASHFLOW, $KEY_MASUK_KELUAR_CASHFLOW ORDER BY $KEY_TANGGAL_CASHFLOW ASC"
        val db = this.writableDatabase
        var dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        var kas : Int = 0
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
//                var formatDate = dateFormatter.format(dateFormatter.parse(cursor.getString(2)))
                var date : String = cursor.getString(0).subSequence(0, 2).toString()
//                var date = dateFormatter.parse(cursor.getString(2))
                var dateDouble : Double = date.toDouble()
                val dataPoint = DataPoint(dateDouble, cursor.getDouble(1))
                cashflowList.add(dataPoint)
            } while (cursor.moveToNext())
        }

        // return contact list
        return cashflowList
    }

    fun deleteModel() {
        val db = this.writableDatabase
        db.delete(TABLE_USER, null, null)
        db.close()
    }

}