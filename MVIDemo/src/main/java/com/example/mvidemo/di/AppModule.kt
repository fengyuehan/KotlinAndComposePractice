package com.example.mvidemo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.RealmConfiguration

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    companion object{

        @Provides
        fun provideRealmConfiguration():RealmConfiguration{
            return RealmConfiguration.Builder()
                .name("mvi")
                .allowWritesOnUiThread(true)
                .allowQueriesOnUiThread(true)
                .build()
        }
    }
}