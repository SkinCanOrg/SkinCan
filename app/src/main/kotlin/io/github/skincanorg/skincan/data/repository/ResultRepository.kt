/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.squareup.sqldelight.android.paging3.QueryPagingSource
import data.Result
import io.github.skincanorg.skincan.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class ResultRepository(private val database: Database) {
    private val _query = database.resultsQueries
    fun fetchResults(): Flow<PagingData<Result>> = Pager(
        config = PagingConfig(
            pageSize = 15,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            QueryPagingSource(
                countQuery = _query.countResults(),
                transacter = _query,
                dispatcher = Dispatchers.IO,
                queryProvider = _query::results,
            )
        },
    ).flow
}
