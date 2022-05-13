package com.bytedance.sjtu

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

fun uriToPath(mContext: Context, uri: Uri): String? {
    var path: String? = null
    val cursor = mContext.contentResolver.query(uri, null, null, null, null)
    if (cursor != null) {
        if (cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        }
        cursor.close()
    }
    return path
}