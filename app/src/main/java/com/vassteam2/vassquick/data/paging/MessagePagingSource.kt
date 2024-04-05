package com.vassteam2.vassquick.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vassteam2.vassquick.data.VassQuickService
import com.vassteam2.vassquick.data.mapper.toMessage
import com.vassteam2.vassquick.domain.model.Message

class MessagePagingSource(
    private val vassQuickService: VassQuickService,
    private val idChat: String,
    private val token: String
) : PagingSource<Int, Message>() {
    override fun getRefreshKey(state: PagingState<Int, Message>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        return try {
            val position = params.key ?: 0
            val list = mutableListOf<Message>()
            val response = vassQuickService.getMessages(
                authToken = token,
                id = idChat,
                offset = position,
                limit = params.loadSize
            )

            if (!response.isSuccessful || response.body() == null || response.body()!!.messageResponses == null) {
                throw Exception("")
            }

            response.body()!!.let { body ->
                body.messageResponses?.map { it?.let { it1 -> list.add(it1.toMessage()) } }
            }
            val nextKey = if (list.isEmpty()) null else position + params.loadSize


            LoadResult.Page(
                data = list,
                prevKey = if (position == 0) null else position - params.loadSize,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}