package com.vassteam2.vassquick.presentation.chat_room.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.vassteam2.vassquick.domain.model.Message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshLazyColumn(
    items: LazyPagingItems<Message>,
    content: @Composable (index: Int) -> Unit,
    isRefresing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
    val pullToRefreshState = rememberPullToRefreshState();
    Box(modifier = modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {

        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(count = items.itemCount, key = items.itemKey { it.id }) {
                content(it)
            }
            item {
                if (items.loadState.append is LoadState.Loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        if(pullToRefreshState.isRefreshing){
            LaunchedEffect(key1 = true){
                onRefresh()
            }
        }

        LaunchedEffect(key1 = isRefresing){
            if(isRefresing){
                pullToRefreshState.startRefresh()
            }
            else {
                pullToRefreshState.endRefresh()
            }
        }

        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}