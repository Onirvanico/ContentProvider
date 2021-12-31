package com.example.contentprovider.adapter

import android.database.Cursor

interface OnRemoveItem {
    fun onRemoveItem(cursor: Cursor?)
}