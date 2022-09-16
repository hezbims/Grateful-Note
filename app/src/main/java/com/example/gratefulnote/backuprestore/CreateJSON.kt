package com.example.gratefulnote.backuprestore

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts

class CreateJSON : ActivityResultContracts.CreateDocument() {
    override fun createIntent(context: Context, input: String) =
        super.createIntent(context, input).setType("application/json")
}