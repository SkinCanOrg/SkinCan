/*
 * Copyright (C) 2022 SkinCan Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package io.github.skincanorg.skincan.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import data.Result
import io.github.skincanorg.skincan.data.repository.ResultRepository
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(private val repository: ResultRepository) : ViewModel() {

    val results: LiveData<PagingData<Result>> = repository.fetchResults().cachedIn(viewModelScope).asLiveData()
}
