package view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import api.ImageApiImpl.Companion.BASE_URL
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.BirdImage

@Composable
fun BirdsPage(viewModel: BirdViewModel) {
    val uiState = viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val result = uiState.value) {
            is BirdState.Loading -> {
                CircularProgressIndicator()
                return
            }

            is BirdState.Error -> {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = Color.Red,
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = result.msg,
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth(.7f)
                )
                Spacer(Modifier.height(48.dp))
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    modifier = Modifier.size(80.dp)
                        .clickable {
                            viewModel.updateImages()
                        }
                )
                return
            }

            is BirdState.Success -> {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Button(
                        onClick = { viewModel.updateImages() },
                        modifier = Modifier.aspectRatio(1.0f).fillMaxSize().weight(1.0f),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            focusedElevation = 0.dp
                        )
                    ) { Text("ALL") }
                    for (category in result.categories) {
                        Button(
                            onClick = {
                                viewModel.selectCategory(category)
                            },
                            modifier = Modifier.aspectRatio(1.0f).fillMaxSize().weight(1.0f),
                            elevation = ButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                focusedElevation = 0.dp
                            )
                        ) { Text(category) }
                    }
                }
                AnimatedVisibility(result.images.isNotEmpty() && result.selectedImages.isEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp),
                        content = {
                            items(result.images) {
                                BirdCellImage(it)
                            }
                        }
                    )
                }
                AnimatedVisibility(result.selectedImages.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp),
                        content = {
                            items(result.selectedImages) {
                                BirdCellImage(it)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BirdCellImage(image: BirdImage) {
    KamelImage(
        asyncPainterResource(BASE_URL + image.path),
        "${image.category} by ${image.author}",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().aspectRatio(1.0f)
    )
}