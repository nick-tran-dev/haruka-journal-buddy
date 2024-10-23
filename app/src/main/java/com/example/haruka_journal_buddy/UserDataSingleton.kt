package com.example.haruka_journal_buddy

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

object UserDataSingleton {
    val USER_ID = stringPreferencesKey("user_id")
    val LAST_ENTRY = intPreferencesKey("last_entry")

    suspend fun <T> changeValue(context: Context, key: Preferences.Key<T>, value: T){
        context.dataStore.edit{preferences -> preferences[key] = value}
    }

    suspend fun <T> getValue(context: Context, key: Preferences.Key<T>): T?{
        return context.dataStore.data.first()[key]
    }
}