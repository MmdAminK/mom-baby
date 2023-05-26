package com.app.mombaby.viewModels

import androidx.lifecycle.*
import com.app.mombaby.models.pregnancyHome.PregnancyArticleDto
import com.app.mombaby.models.pregnancyHome.PregnancyGuideDto
import com.app.mombaby.models.pregnancyHome.SingleBlog
import com.app.mombaby.models.pregnancyHome.categories.PregnancyArticleCategoryDto
import com.app.mombaby.models.pregnancyHome.categories.PregnancyGuideCategoryDto
import com.app.mombaby.repositories.PregnancyBlogsRepository
import com.app.mombaby.utils.models.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowBlogsViewModel @Inject
constructor(
    private val savedState: SavedStateHandle,
    private val pregnancyHomeRepository: PregnancyBlogsRepository
) : ViewModel() {

    private val _dataStateGuideBlogs: MutableLiveData<DataState<PregnancyGuideDto?>> =
        MutableLiveData()
    var dataStateGuideBlogs: LiveData<DataState<PregnancyGuideDto?>> = _dataStateGuideBlogs

    private val _dataStateGuideCategories: MutableLiveData<DataState<PregnancyGuideCategoryDto?>> =
        MutableLiveData()
    var dataStateGuideCategories: LiveData<DataState<PregnancyGuideCategoryDto?>> =
        _dataStateGuideCategories

    private val _dataStateArticleCategories: MutableLiveData<DataState<PregnancyArticleCategoryDto?>> =
        MutableLiveData()
    var dataStateArticleCategories: LiveData<DataState<PregnancyArticleCategoryDto?>> =
        _dataStateArticleCategories

    private val _dataStateArticleBlogs: MutableLiveData<DataState<PregnancyArticleDto?>> =
        MutableLiveData()
    var dataStateArticleBlogs: LiveData<DataState<PregnancyArticleDto?>> =
        _dataStateArticleBlogs

    private val _dataStateSingleArticle: MutableLiveData<DataState<SingleBlog?>> =
        MutableLiveData()
    var dataStateSingleArticle: LiveData<DataState<SingleBlog?>> =
        _dataStateSingleArticle

    private val _dataStateSingleGuide: MutableLiveData<DataState<SingleBlog?>> =
        MutableLiveData()
    var dataStateSingleGuide: LiveData<DataState<SingleBlog?>> =
        _dataStateSingleGuide

    fun setStateEvent(showBlogsEvent: ShowBlogsEvent) {
        viewModelScope.launch {

            when (showBlogsEvent) {
                is ShowBlogsEvent.PregnancyGuideBlogs -> {
                    pregnancyHomeRepository.pregnancyGuideList(
                        showBlogsEvent.cateId,
                        showBlogsEvent.week
                    ).onEach {
                        _dataStateGuideBlogs.value = it
                    }
                        .launchIn(viewModelScope)
                }
                is ShowBlogsEvent.PregnancyGuideCategories -> {
                    pregnancyHomeRepository.pregnancyGuideCategoryList(showBlogsEvent.week).onEach {
                        _dataStateGuideCategories.value = it
                    }
                        .launchIn(viewModelScope)
                }
                is ShowBlogsEvent.PregnancyArticleBlogs -> {
                    pregnancyHomeRepository.pregnancyArticleList(showBlogsEvent.cateId).onEach {
                        _dataStateArticleBlogs.value = it
                    }
                        .launchIn(viewModelScope)
                }
                is ShowBlogsEvent.PregnancyArticleCategories -> {
                    pregnancyHomeRepository.pregnancyArticleCategoryList().onEach {
                        _dataStateArticleCategories.value = it
                    }
                        .launchIn(viewModelScope)
                }
                is ShowBlogsEvent.SingleArticle -> {
                    pregnancyHomeRepository.article(showBlogsEvent.id).onEach {
                        _dataStateSingleArticle.value = it
                    }
                        .launchIn(viewModelScope)
                }
                is ShowBlogsEvent.SingleGuide -> {
                    pregnancyHomeRepository.guide(showBlogsEvent.id).onEach {
                        _dataStateSingleGuide.value = it
                    }
                        .launchIn(viewModelScope)
                }
            }
        }

    }

    fun clearObservers() {
        _dataStateGuideBlogs.value = null
        _dataStateGuideCategories.value = null
        _dataStateArticleCategories.value = null
        _dataStateArticleBlogs.value = null
    }

}

sealed class ShowBlogsEvent {
    class PregnancyGuideBlogs(val cateId: Int, val week: Int) : ShowBlogsEvent()
    class PregnancyGuideCategories(val week: Int) : ShowBlogsEvent()
    class PregnancyArticleBlogs(val cateId: Int) : ShowBlogsEvent()
    object PregnancyArticleCategories : ShowBlogsEvent()
    class SingleArticle(val id: Int) : ShowBlogsEvent()
    class SingleGuide(val id: Int) : ShowBlogsEvent()
}