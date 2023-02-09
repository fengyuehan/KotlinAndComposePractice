package com.example.mvidemo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import java.util.UUID
import javax.inject.Inject

@HiltAndroidApp
class App :Application(){
    @Inject
    lateinit var realmConfiguration: RealmConfiguration

    override fun onCreate() {
        Realm.init(this)
        super.onCreate()

        initWithPreconditionData()
    }

    private fun initWithPreconditionData() {
        val realm = Realm.getInstance(realmConfiguration)
        val count = realm.where(TodoLocal::class.java).count()
        if (count == 0L){
            realm.executeTransaction {
                realm.insertOrUpdate(
                    RealmList(
                        TodoLocal().apply {
                            id = UUID.randomUUID().toString()
                            text = "Make a breakfast"
                            isChecked = false
                        },

                        TodoLocal().apply {
                            id = UUID.randomUUID().toString()
                            text = "Clean the room"
                            isChecked = false
                        },
                        TodoLocal().apply {
                            id = UUID.randomUUID().toString()
                            text = "Create the MVI sample"
                            isChecked = true
                        },
                        TodoLocal().apply {
                            id = UUID.randomUUID().toString()
                            text = "Upload it to medium"
                            isChecked = true
                        }
                    )
                )
            }
        }
    }
}