package com.example.logifitappp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.logifitappp.data.models.DocumentTypeModel

@Dao
abstract class DocumentTypeDao {
    @Query("SELECT * FROM document_types")
    abstract fun all(): List<DocumentTypeModel>

    @Query("DELETE FROM document_types")
    abstract fun delete()

    @Transaction
    open suspend fun replaceAll(vararg documentTypes: DocumentTypeModel) {
        delete()
        store(*documentTypes)
    }

    @Upsert
    abstract fun store(vararg documentTypes: DocumentTypeModel)
}