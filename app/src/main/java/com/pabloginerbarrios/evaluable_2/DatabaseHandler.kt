package com.pabloginerbarrios.evaluable_2

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "ColorDatabase"
        private const val TABLE_COLORS = "colors"

        private const val KEY_ID = "id"
        private const val KEY_COLOR = "color"
        private const val KEY_NAME = "name"
        private const val KEY_SAVE_DATE = "save_date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_COLORS (
                $KEY_ID INTEGER PRIMARY KEY,
                $KEY_COLOR TEXT,
                $KEY_NAME TEXT,
                $KEY_SAVE_DATE TEXT
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_COLORS")
        onCreate(db)
    }

    fun addColor(colorData: ColorData) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_COLOR, colorData.color)
        values.put(KEY_NAME, colorData.name)
        values.put(KEY_SAVE_DATE, colorData.saveDate)

        db.insert(TABLE_COLORS, null, values)
        db.close()
    }

    fun getAllColors(): List<ColorData> {
        val colorList = mutableListOf<ColorData>()
        val selectQuery = "SELECT * FROM $TABLE_COLORS"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        try {
            if (cursor.moveToFirst()) {
                do {
                    val color = ColorData(
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_COLOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_SAVE_DATE))
                    )
                    colorList.add(color)
                } while (cursor.moveToNext())
            }
        } finally {
            cursor.close()
        }

        return colorList
    }

    fun deleteColor(colorData: ColorData) {
        val db = this.writableDatabase
        db.delete(TABLE_COLORS, "$KEY_COLOR=?", arrayOf(colorData.color))
        db.close()
    }

    fun isColorNameExists(colorName: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_COLORS WHERE $KEY_NAME = ?"
        val cursor = db.rawQuery(query, arrayOf(colorName))

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}