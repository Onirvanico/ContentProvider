package com.example.contentprovider

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.BaseColumns._ID
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.RecyclerView
import com.example.contentprovider.adapter.NotesAdapter
import com.example.contentprovider.adapter.OnItemClickListener
import com.example.contentprovider.adapter.OnRemoveItem
import com.example.contentprovider.sharedata.NotesDataBaseHelp.Companion.TITLE_NOTES
import com.example.contentprovider.sharedata.NotesProvider.Companion.URI_NOTES
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    lateinit var recyclerView: RecyclerView
    lateinit var fabAdd: FloatingActionButton

    private val adapter = NotesAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()

        configAdapter()

        fabAdd.setOnClickListener {
            NoteDetailFragment().show(supportFragmentManager, "NoteDetailFragment")
        }

        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(cursor: Cursor?) {

                val id: Long = cursor?.getLong(cursor.getColumnIndexOrThrow(_ID))!!
                val noteDialog = NoteDetailFragment.instance(id)
                noteDialog.show(supportFragmentManager, "NoteDetailFragment")
            }

        })

        adapter.setRemoveItem(object : OnRemoveItem {
            override fun onRemoveItem(cursor: Cursor?) {
               val id: Long? =  cursor?.getLong(cursor.getColumnIndexOrThrow(_ID))
                contentResolver.delete(Uri.withAppendedPath(URI_NOTES, id.toString()), null, null)

            }
        })

        LoaderManager.getInstance(this).initLoader(0, null, this)
    }


    private fun configAdapter() {
        adapter.setHasStableIds(false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun bindViews() {
        recyclerView = findViewById(R.id.recycler_notes)
        fabAdd = findViewById(R.id.fab_add)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> =
        CursorLoader(this, URI_NOTES, null, null, null, TITLE_NOTES)


    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if(data != null) adapter.setCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapter.setCursor(null)
    }

}