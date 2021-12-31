package com.example.contentprovider

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.contentprovider.sharedata.NotesDataBaseHelp.Companion.DESCRIPTION_NOTES
import com.example.contentprovider.sharedata.NotesDataBaseHelp.Companion.TITLE_NOTES
import com.example.contentprovider.sharedata.NotesProvider.Companion.URI_NOTES

class NoteDetailFragment : DialogFragment(), DialogInterface.OnClickListener {

    private lateinit var title: EditText
    private lateinit var description: EditText
    private var id: Long = 0L

    companion object {
        private const val EXTRA_ID = "id"

        fun instance(id: Long): NoteDetailFragment {
            val bundle = Bundle()
            bundle.putLong(EXTRA_ID, id)

            val notesDialog = NoteDetailFragment()
            notesDialog.arguments = bundle
            return notesDialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val  view = layoutInflater.inflate(R.layout.note_detail, null)

        title = view.findViewById(R.id.input_title)
        description = view.findViewById(R.id.input_description)

        var newNote = true

        if(arguments != null && arguments?.getLong(EXTRA_ID) != 0L) {
            id = arguments?.getLong(EXTRA_ID) as Long

            val uri = Uri.withAppendedPath(URI_NOTES, id.toString())
            val cursor = activity?.contentResolver?.query(uri, null, null, null, null)

            if(cursor?.moveToNext() as Boolean) {
                newNote = false

                with(cursor) {
                    title.setText(getString(getColumnIndexOrThrow(TITLE_NOTES)))
                    description.setText(getString(getColumnIndexOrThrow(DESCRIPTION_NOTES)))

                }
            }

            cursor.close()

        }

       return   AlertDialog.Builder(activity as Activity)
            .setTitle(if(newNote) "Nova mensagem" else "Editar mensagem")
            .setView(view)
            .setPositiveButton("Salvar", this)
            .setNegativeButton("Cancelar", this)
            .create()

    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        val values = ContentValues()
        values.put(TITLE_NOTES, title.text.toString())
        values.put(DESCRIPTION_NOTES, description.text.toString())

        if(id != 0L) {
            val uri = Uri.withAppendedPath(URI_NOTES, id.toString())
            context?.contentResolver?.update(uri, values, null, null)
            context?.contentResolver?.notifyChange(uri, null)

        } else
            context?.contentResolver?.insert(URI_NOTES, values)
            context?.contentResolver?.notifyChange(URI_NOTES, null)

    }
}