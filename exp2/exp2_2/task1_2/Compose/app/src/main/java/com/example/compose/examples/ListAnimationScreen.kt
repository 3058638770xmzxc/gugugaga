package com.example.compose.examples

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ListItem(
    val id: Int,
    val title: String,
    val description: String
)

@Composable
fun ListAnimationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "列表动画示例",
            style = MaterialTheme.typography.headlineMedium
        )
        
        AnimatedListExample()
        ListItemAnimationExample()
        ListModificationExample()
    }
}

@Composable
fun AnimatedListExample() {
    var items by remember {
        mutableStateOf(
            (1..10).map { index ->
                ListItem(
                    id = index,
                    title = "项目 $index",
                    description = "这是第 $index 个列表项"
                )
            }
        )
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "1. LazyColumn 项动画 - 自动动画化列表项",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        items = items + ListItem(
                            id = items.size + 1,
                            title = "新项目 ${items.size + 1}",
                            description = "新添加的项"
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("添加")
                }
                Button(
                    onClick = {
                        if (items.isNotEmpty()) {
                            items = items.dropLast(1)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("删除")
                }
            }
            
            LazyColumn(
                modifier = Modifier.height(300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = items,
                    key = { it.id }
                ) { item ->
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ) + fadeIn(),
                        exit = slideOutVertically(
                            targetOffsetY = { -it },
                            animationSpec = tween(300)
                        ) + fadeOut()
                    ) {
                        ListItemCard(item = item)
                    }
                }
            }
        }
    }
}

@Composable
fun ListItemCard(item: ListItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ListItemAnimationExample() {
    var expandedItems by remember { mutableStateOf(setOf<Int>()) }
    
    val items = remember {
        (1..5).map { index ->
            ListItem(
                id = index,
                title = "可展开项目 $index",
                description = "点击展开查看详细信息"
            )
        }
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "2. 列表项展开/收起动画",
                style = MaterialTheme.typography.titleMedium
            )
            
            LazyColumn(
                modifier = Modifier.height(400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    var isExpanded by remember { mutableStateOf(expandedItems.contains(item.id)) }
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            isExpanded = !isExpanded
                            expandedItems = if (isExpanded) {
                                expandedItems + item.id
                            } else {
                                expandedItems - item.id
                            }
                        },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                val rotation by animateFloatAsState(
                                    targetValue = if (isExpanded) 180f else 0f,
                                    label = "icon_rotation"
                                )
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    modifier = Modifier.rotate(rotation)
                                )
                            }
                            
                            AnimatedVisibility(
                                visible = isExpanded,
                                enter = expandVertically(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                ) + fadeIn(),
                                exit = shrinkVertically(
                                    animationSpec = tween(300)
                                ) + fadeOut()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                ) {
                                    Text(
                                        text = item.description,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "这是展开后的详细内容。可以包含更多信息、操作按钮等。",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListModificationExample() {
    var items by remember {
        mutableStateOf(
            (1..5).map { index ->
                ListItem(
                    id = index,
                    title = "项目 $index",
                    description = "描述 $index"
                )
            }
        )
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "3. 列表操作动画 - 添加、删除、重新排序",
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val newId = (items.maxOfOrNull { it.id } ?: 0) + 1
                        items = items + ListItem(
                            id = newId,
                            title = "新项目 $newId",
                            description = "新添加"
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("添加")
                }
                Button(
                    onClick = {
                        if (items.isNotEmpty()) {
                            items = items.shuffled()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("打乱")
                }
            }
            
            LazyColumn(
                modifier = Modifier.height(300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                state = rememberLazyListState()
            ) {
                items(
                    items = items,
                    key = { it.id }
                ) { item ->
                    var isRemoving by remember { mutableStateOf(false) }
                    
                    AnimatedVisibility(
                        visible = !isRemoving,
                        enter = slideInVertically(
                            initialOffsetY = { -it },
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ) + fadeIn(),
                        exit = slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300)
                        ) + fadeOut()
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = item.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                val scope = rememberCoroutineScope()
                                IconButton(
                                    onClick = {
                                        isRemoving = true
                                        // 延迟删除以显示动画
                                        scope.launch {
                                            delay(300)
                                            items = items.filter { it.id != item.id }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "删除"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

