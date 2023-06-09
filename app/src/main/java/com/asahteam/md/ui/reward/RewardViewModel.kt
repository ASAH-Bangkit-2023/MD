package com.asahteam.md.ui.reward

import androidx.lifecycle.ViewModel
import com.asahteam.md.repository.PointRepository

class RewardViewModel(
    private val repository: PointRepository,
) : ViewModel() {
    fun getPoint() = repository.getPoint()
    fun redeemPoint(points: Int) = repository.redeemPoint(points)
}