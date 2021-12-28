package com.example.contentprovider.sharedata

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID

class NotesDataBaseHelp(context: Context) : SQLiteOpenHelper(
    context, NAME_DB, null, 1) {

    companion object {
        const val NAME_DB = "notes_db"
        const val TABLE_NOTES = "Notes"
        const val TITLE_NOTES = "title"
        const val DESCRIPTION_NOTES = "description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE $TABLE_NOTES (" +
                "$_ID INTEGER NOT NULL PRIMARY KEY, " +
                "$TITLE_NOTES TEXT NOT NULL, " +
                "$DESCRIPTION_NOTES TEXT NOT NULL" +
                ")"

        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}