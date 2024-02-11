package com.example.gratefulnote.runner

import com.example.gratefulnote.GratefulNoteApplication
import dagger.hilt.android.testing.CustomTestApplication

@CustomTestApplication(GratefulNoteApplication::class)
interface CustomTestApplication