package com.asahteam.md.ui.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asahteam.md.injection.Injection
import com.asahteam.md.repository.PointRepository
import com.asahteam.md.ui.reward.RewardViewModel

class RewardViewModelFactory private constructor(
    private val repository: PointRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RewardViewModel::class.java)) {
            return RewardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: RewardViewModelFactory? = null
        fun getInstance(context: Context): RewardViewModelFactory =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: RewardViewModelFactory(
                    Injection.getPointRepository(context),
                )
            }.also { INSTANCE = it }
    }
}