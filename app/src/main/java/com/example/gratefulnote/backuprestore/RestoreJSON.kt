package com.example.gratefulnote.backuprestore

import android.content.Context
import androidx.activity.result.contract.ActivityResultContracts

class RestoreJSON : ActivityResultContracts.GetContent() {
    override fun createIntent(context: Context, input: String) =
        super.createIntent(context, input).setType("application/json")
}