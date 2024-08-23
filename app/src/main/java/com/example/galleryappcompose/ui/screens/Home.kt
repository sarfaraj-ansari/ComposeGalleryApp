package com.example.galleryappcompose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.galleryappcompose.R
import com.example.galleryappcompose.data.datasource.getCategoryList
import com.example.galleryappcompose.data.models.PhotoResponse
import com.example.galleryappcompose.navigation.Screens
import com.example.galleryappcompose.ui.theme.Gray
import com.example.galleryappcompose.ui.theme.SelectedCategory
import com.example.galleryappcompose.ui.theme.Violet
import com.example.galleryappcompose.ui.viewmodels.MViewmodel
import com.example.galleryappcompose.ui.viewmodels.SharedViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun Home(
    viewModel: MViewmodel,
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel
) {

    val data: LazyPagingItems<out Any> = if (sharedViewModel.selectedCategory == "All") {
        if (sharedViewModel.isPhotoSelected) {
            viewModel.allPhotos.collectAsLazyPagingItems()
        } else {
            viewModel.allVideos.collectAsLazyPagingItems()
        }
    } else {
        if (sharedViewModel.isPhotoSelected) {
            viewModel.getSearchedPhotos(sharedViewModel.selectedCategory).collectAsLazyPagingItems()
        } else {
            viewModel.getSearchedVideos(sharedViewModel.selectedCategory).collectAsLazyPagingItems()
        }
    }

    ConstraintLayout {

        val (category, imageList, type) = createRefs()

        SetupCategoryList(
            modifier = Modifier
                .padding(
                    start = 5.dp,
                    end = 5.dp,
                    top = 15.dp,
                    bottom = 10.dp
                )
                .constrainAs(category) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            selectedCategory = sharedViewModel.selectedCategory
        ) { sharedViewModel.selectedCategory = it }

        val modifier = Modifier.constrainAs(imageList) {
            top.linkTo(category.bottom, 60.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }

        SetUpPaginationData(
            modifier = modifier,
            data = data,
            isPhotoType = sharedViewModel.isPhotoSelected,
            navHostController = navHostController
        )

        val modifier2: Modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(60.dp)
            .constrainAs(type) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, 10.dp)
            }

        Type(modifier = modifier2, sharedViewModel.isPhotoSelected) { tt ->
            sharedViewModel.isPhotoSelected = tt == "p"
        }
    }

}

@Composable
fun SetUpPaginationData(
    modifier: Modifier,
    data: LazyPagingItems<out Any>,
    isPhotoType: Boolean,
    navHostController: NavHostController
) {

    when (data.loadState.refresh) {
        is LoadState.Loading -> {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

        }

        is LoadState.NotLoading -> {

            LazyVerticalGrid(
                modifier = modifier,
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 5.dp, end = 5.dp, bottom = 5.dp)
            ) {

                items(data.itemCount) { index ->

                    val photoUri = if (isPhotoType) {
                        (data[index] as PhotoResponse.Photo).src?.original
                    } else {
                        (data[index] as PhotoResponse.Video).image
                    }

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                        AsyncImage(
                            model = photoUri,
                            contentDescription = null,
                            modifier = Modifier
                                .height(250.dp)
                                .padding(10.dp)
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(15.dp))
                                .clickable {
                                    if (isPhotoType) {
                                        val encodedUrl = URLEncoder.encode(
                                            photoUri,
                                            StandardCharsets.UTF_8.toString()
                                        )
                                        navHostController.navigate("${Screens.Details.route}/$encodedUrl")
                                    } else {
                                        val videoLink: String =
                                            (data[index] as PhotoResponse.Video).video_files[2].link
                                        val encodedUrl = URLEncoder.encode(
                                            videoLink,
                                            StandardCharsets.UTF_8.toString()
                                        )
                                        navHostController.navigate("${Screens.VideoPlayer.route}/$encodedUrl")
                                    }
                                },
                            contentScale = ContentScale.Crop
                        )

                        if (!isPhotoType) {
                            Box(
                                modifier = Modifier.background(
                                    color = Color.DarkGray,
                                    shape = CircleShape
                                )
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.play),
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }

                    }
                }

                if (data.loadState.append is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

        }

        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Retry",
                    color = Color.Black,
                    modifier = Modifier.clickable { data.retry() })
            }
        }
    }
}

@Composable
fun SetupCategoryList(
    modifier: Modifier,
    selectedCategory: String,
    onCategoryChoose: (String) -> Unit
) {
    LazyRow(modifier = modifier) {
        items(getCategoryList()) { item: String ->
            CategoryItem(
                category = item,
                selected = selectedCategory,
                onCategoryChoose = onCategoryChoose
            )
        }
    }
}

@Preview
@Composable
private fun CategoryItem(
    category: String = "dddd",
    selected: String = "dddd",
    onCategoryChoose: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(150.dp)
            .padding(start = 5.dp, end = 5.dp)
            .clickable { onCategoryChoose.invoke(category) }
            .background(
                color = if (category == selected) SelectedCategory else Gray,
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = category,
            color = if (category == selected) Color.White else Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )
    }
}

@Preview
@Composable
fun Type(
    modifier: Modifier = Modifier,
    isPhotoSelected: Boolean = true,
    onclick: (type: String) -> Unit = {}
) {

    Box(
        modifier = modifier.background(Color.LightGray, shape = CircleShape)
    ) {

        Row(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        color = if (isPhotoSelected) Violet else Color.Transparent,
                        shape = CircleShape
                    )
                    //click without ripple effect
                    .pointerInput(Unit) {
                        detectTapGestures(onPress = {
                            onclick.invoke("p")
                        })
                    }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Photos",
                    color = if (isPhotoSelected) Color.White else Color.Black,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        color = if (!isPhotoSelected) Violet else Color.Transparent,
                        shape = CircleShape
                    )
                    //click without ripple effect
                    .pointerInput(Unit) {
                        detectTapGestures(onPress = {
                            onclick.invoke("v")
                        })
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Videos",
                    color = if (!isPhotoSelected) Color.White else Color.Black,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

        }

    }
}