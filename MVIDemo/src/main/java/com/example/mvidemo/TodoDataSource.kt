package com.example.mvidemo

import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject

interface TodoDataSource {
    suspend fun getToDos():List<TodoLocal>
}

class TodoLocalDataSource @Inject constructor(
    private val realmConfiguration: RealmConfiguration
):TodoDataSource{
    private val realm:Realm
        get() = Realm.getInstance(realmConfiguration)

    override suspend fun getToDos(): List<TodoLocal> {
        return realm.use {
            realm.copyFromRealm(
                realm.where(TodoLocal::class.java)
                    .findAll()
                    .toList()
            )
        }
    }

}