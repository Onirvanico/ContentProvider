package com.example.contentprovider.adapter

import android.database.Cursor

interface OnItemClickListener {
    fun onItemClick(cursor: Cursor?)

}