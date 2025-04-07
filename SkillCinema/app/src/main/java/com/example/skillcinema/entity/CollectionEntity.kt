package com.example.skillcinema.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection_entity")
data class CollectionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "collection_id")
    val collectionId: Int?,
    @ColumnInfo(name = "name_collection")
    val nameCollection: String?
    )