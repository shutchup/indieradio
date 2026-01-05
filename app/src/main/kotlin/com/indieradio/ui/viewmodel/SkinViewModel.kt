package com.indieradio.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indieradio.data.repository.SkinRepository
import com.indieradio.ui.theme.skin.SkinTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing skin theme selection
 */
@HiltViewModel
class SkinViewModel @Inject constructor(
    private val skinRepository: SkinRepository
) : ViewModel() {

    /**
     * Current selected skin theme
     */
    val currentSkin: StateFlow<SkinTheme> = skinRepository.currentSkin
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = SkinTheme.ModernMinimal
        )

    /**
     * Available skins - computed on demand to avoid initialization issues
     */
    val availableSkins: List<SkinTheme>
        get() = listOf(SkinTheme.ModernMinimal, SkinTheme.Vintage80s)

    /**
     * Select a new skin theme
     */
    fun selectSkin(skinTheme: SkinTheme) {
        viewModelScope.launch {
            skinRepository.setSkin(skinTheme)
        }
    }
}
