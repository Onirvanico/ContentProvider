package com.example.contentprovider.sharedata

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.media.UnsupportedSchemeException
import android.net.Uri
import android.provider.BaseColumns._ID
import com.example.contentprovider.sharedata.NotesDataBaseHelp.Companion.TABLE_NOTES
import com.example.contentprovider.sharedata.NotesDataBaseHelp.Companion.TITLE_NOTES

class NotesProvider : ContentProvider() {

    private lateinit var uriMatcher: UriMatcher
    private lateinit var db: NotesDataBaseHelp
    companion object {
        const val AUTHORITY = "com.example.contentprovider.provider"
        val BASE_URI = Uri.parse("content://$AUTHORITY")
        val URI_NOTES = Uri.withAppendedPath(BASE_URI, "notes")
        const val NOTES = 1
        const val NOTE_PATH = "notes"
        const val NOTE_BY_ID = 2
    }

    override fun onCreate(): Boolean {
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        uriMatcher.addURI(AUTHORITY, NOTE_PATH, NOTES)
        uriMatcher.addURI(AUTHORITY, "$NOTE_PATH/#", NOTE_BY_ID)
        if(context != null) db = NotesDataBaseHelp(context as Context)
        return true
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        if(uriMatcher.match(uri) == NOTE_BY_ID) {
            val wrDB = db.writableDatabase
            var linesAffect = wrDB.delete(TABLE_NOTES, "$_ID = ?", arrayOf(uri.lastPathSegment))
            wrDB.close()
            context?.contentResolver?.notifyChange(uri, null)
            return linesAffect
        } else
            throw UnsupportedSchemeException("Não é possível excluir com esta Uri")
    }

    override fun getType(uri: Uri) = throw UnsupportedSchemeException("Uri não implementado")

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if(uriMatcher.match(uri) == NOTES) {
            val wrDB = db.writableDatabase
            val rawId = wrDB?.insert(TABLE_NOTES, null, values)
            val insertUri = Uri.withAppendedPath(BASE_URI, rawId.toString())
            wrDB.close()
            return  insertUri
        } else {
            throw UnsupportedSchemeException("Não foi possível inserir com esta Uri")
        }
    }


    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when {
            uriMatcher.match(uri) == NOTES -> {
                val rdDB = db.readableDatabase
                val cursor = rdDB.query(
                    TABLE_NOTES,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )
                cursor
            }
            uriMatcher.match(uri) == NOTE_BY_ID -> {
                val rdDB = db.readableDatabase
                val cursor = rdDB.query(
                    TABLE_NOTES,
                    projection,
                    "$_ID = ?",
                    arrayOf(uri.lastPathSegment),
                    null,
                    null,
                    sortOrder
                )
                cursor.setNotificationUri(context?.contentResolver, uri)
                cursor
            } else -> {
                throw UnsupportedSchemeException("Uri não implementada")
            }
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        if(uriMatcher.match(uri) == NOTE_BY_ID) {
            val wrDB = db.writableDatabase
            var linesAffect = wrDB.update(TABLE_NOTES, values, "$_ID = ?", arrayOf(uri.lastPathSegment))
            wrDB.close()
            context?.contentResolver?.notifyChange(uri, null)
            return linesAffect
        } else
            throw UnsupportedSchemeException("Não é possível excluir com esta Uri")
    }
}