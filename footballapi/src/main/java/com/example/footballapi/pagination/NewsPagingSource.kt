package com.example.footballapi.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.footballapi.FootballApiService
import com.example.footballapi.modelClasses.latestNews.LatestNewsResponseItem

class NewsPagingSource(
    private val api: FootballApiService
) : PagingSource<Int, LatestNewsResponseItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LatestNewsResponseItem> {
        val page = params.key ?: 1
        return try {
            Log.d("NewsPagingSource", "Loading page: $page")


            val response = api.getLatestNews(page.toString())
            val newsList = response

            LoadResult.Page(
                data = newsList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (newsList.isEmpty()) null else page + 1
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LatestNewsResponseItem>): Int? {
        return state.anchorPosition?.let { anchor ->
            val anchorPage = state.closestPageToPosition(anchor)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
