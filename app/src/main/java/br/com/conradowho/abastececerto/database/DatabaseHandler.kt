package br.com.conradowho.abastececerto.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.com.conradowho.abastececerto.entity.Vehicle

class DatabaseHandler private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "bdfile.sqlite"
        const val TABLE_NAME = "vehicules"

        @Volatile
        private var INSTANCE: DatabaseHandler? = null
        fun getInstance(context: Context): DatabaseHandler {
            if (INSTANCE == null) {
                INSTANCE = DatabaseHandler(context)
            }
            return INSTANCE as DatabaseHandler
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS ${TABLE_NAME} (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, alcoholConsumption DOUBLE, gasolineConsumption DOUBLE)")
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS ${TABLE_NAME}")
        onCreate(db)
    }


    fun insert(vehicule: Vehicle) {

        var item = getContentValueFromVehicle(vehicule)
        writableDatabase.insert(TABLE_NAME, null, item)

    }

    fun update(vehicule: Vehicle) {
        var item = getContentValueFromVehicle(vehicule)
        writableDatabase.update(TABLE_NAME, item, "_id = ?", arrayOf(vehicule._id.toString()))
    }


    fun delete(id: Long) {

        writableDatabase.delete(TABLE_NAME, "_id = ?", arrayOf(id.toString()))
    }

    fun getAll(): List<Vehicle> {


        val list = mutableListOf<Vehicle>()

        var cursor = writableDatabase.query(TABLE_NAME, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                var vehicle = getVehicleFromCursor(cursor)

                list.add(vehicle)

            } while (cursor.moveToNext())
        }

        cursor.close()
        return list

    }

    fun getById(id: Long): Vehicle? {

        var cursor = writableDatabase.query(
            TABLE_NAME,
            null,
            "_id=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            return getVehicleFromCursor(cursor)
        }

        return null
    }

    private fun getVehicleFromCursor(cursor: Cursor): Vehicle {
        val idIndex = cursor.getColumnIndex("_id")
        val nameIndex = cursor.getColumnIndex("name")
        val alcIndex = cursor.getColumnIndex("alcoholConsumption")
        val gasIndex = cursor.getColumnIndex("gasolineConsumption")

        val vehicle = Vehicle(
            _id = cursor.getLong(idIndex),
            name = cursor.getString(nameIndex),
            alcoholConsumption = cursor.getDouble(alcIndex),
            gasolineConsumption = cursor.getDouble(gasIndex)
        )
        return vehicle
    }

    private fun getContentValueFromVehicle(vehicule: Vehicle): ContentValues {
        var item = ContentValues();
        item.put("name", vehicule.name)
        item.put("alcoholConsumption", vehicule.alcoholConsumption)
        item.put("gasolineConsumption", vehicule.gasolineConsumption)
        return item
    }

}
