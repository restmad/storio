package com.pushtorefresh.android.bamboostorage.operation.put;

import android.content.ContentValues;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import com.pushtorefresh.android.bamboostorage.BambooStorage;
import com.pushtorefresh.android.bamboostorage.query.InsertQueryBuilder;
import com.pushtorefresh.android.bamboostorage.query.UpdateQueryBuilder;

import java.util.Collections;

public abstract class DefaultPutResolver<T> implements PutResolver<T> {

    @NonNull protected abstract String getTable();

    @Override
    @NonNull
    public PutResult performPut(@NonNull BambooStorage bambooStorage, @NonNull ContentValues contentValues) {
        final Long id = contentValues.getAsLong(BaseColumns._ID);
        final String table = getTable();

        if (id == null) {
            final long insertedId = bambooStorage.internal().insert(
                    new InsertQueryBuilder()
                            .table(table)
                            .nullColumnHack(null)
                            .build(),
                    contentValues
            );

            return PutResult.newInsertResult(insertedId, Collections.singleton(table));
        } else {
            final int numberOfUpdatedRows = bambooStorage.internal().update(
                    new UpdateQueryBuilder()
                            .table(table)
                            .where(BaseColumns._ID + "=?")
                            .whereArgs(String.valueOf(id))
                            .build(),
                    contentValues
            );

            return PutResult.newUpdateResult(numberOfUpdatedRows, Collections.singleton(table));
        }
    }
}