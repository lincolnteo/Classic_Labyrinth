// Lincoln Teo Ming Kern - 3122215
// github repo: https://github.com/lincolnteo/Classic_Labyrinth
package com.griffith.classic_labyrinth

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState

class ShopActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopScreen()
            enableEdgeToEdge()
        }
    }
}

data class ShopItem(val id: String, val imageRes: Int, val category: String)

val allShopItems = listOf(
    // Animals
    ShopItem("Bear", R.drawable.shop_animals_bear, "Animals"),
    ShopItem("Bull", R.drawable.shop_animals_bull, "Animals"),
    ShopItem("Eagle", R.drawable.shop_animals_eagle, "Animals"),
    ShopItem("Fox", R.drawable.shop_animals_fox, "Animals"),
    ShopItem("Gorilla", R.drawable.shop_animals_gorrila, "Animals"),
    ShopItem("Lion", R.drawable.shop_animals_lion, "Animals"),
    ShopItem("Panda", R.drawable.shop_animals_panda, "Animals"),
    ShopItem("Shark", R.drawable.shop_animals_shark, "Animals"),
    ShopItem("Tiger", R.drawable.shop_animals_tiger, "Animals"),
    ShopItem("Wolf", R.drawable.shop_animals_wolf, "Animals"),
    // Woods
    ShopItem("Brown Oak", R.drawable.shop_wood_brownoak, "Woods"),
    ShopItem("Coastal Drift", R.drawable.shop_wood_coastaldrift, "Woods"),
    ShopItem("Dark Oak", R.drawable.shop_wood_darkoak, "Woods"),
    ShopItem("Ebony", R.drawable.shop_wood_ebony, "Woods"),
    ShopItem("Golden Maple", R.drawable.shop_wood_goldenmaple, "Woods"),
    ShopItem("Grey Oak", R.drawable.shop_wood_greyoak, "Woods"),
    ShopItem("Natural Pine", R.drawable.shop_wood_naturalpine, "Woods"),
    ShopItem("Red Cedar", R.drawable.shop_wood_redcedar, "Woods"),
    ShopItem("Rich Mahogany", R.drawable.shop_wood_richmahogany, "Woods"),
    ShopItem("Rustic Walnut", R.drawable.shop_wood_rusticwalnut, "Woods"),
    ShopItem("Spiced Cherry", R.drawable.shop_wood_spicedcherry, "Woods"),
    ShopItem("Weathered Grey", R.drawable.shop_wood_weatheredgrey, "Woods"),
    // Metals
    ShopItem("Age Bronze", R.drawable.shop_metals_agebronze, "Metals"),
    ShopItem("Beaten Brass", R.drawable.shop_metals_beatenbrass, "Metals"),
    ShopItem("Brushed Steel", R.drawable.shop_metals_brushedsteel, "Metals"),
    ShopItem("Chrome", R.drawable.shop_metals_chrome, "Metals"),
    ShopItem("Copper", R.drawable.shop_metals_copper, "Metals"),
    ShopItem("Forged Iron", R.drawable.shop_metals_forgediron, "Metals"),
    ShopItem("Galvanized Zinc", R.drawable.shop_metals_galvanizedzinc, "Metals"),
    ShopItem("Metallic Blue", R.drawable.shop_metals_metallicblue, "Metals"),
    ShopItem("Polished Gold", R.drawable.shop_metals_polishedgold, "Metals"),
    ShopItem("Rusty Iron", R.drawable.shop_metals_rustyiron, "Metals")
)

@Preview
@Composable
fun ShopScreen() {
    // Get context and cast to Activity so we can use activity?.finish() to close the screen
    val context = LocalContext.current
    val activity = context as? Activity

    // Tracks which shop category the user currently has selected
    var selectedCategory by remember { mutableStateOf("Animals") }
    // Tracks the currently selected item skin
    var selectedItem by remember { mutableStateOf<ShopItem?>(null) }
    var targetScrollIndex by remember { mutableIntStateOf(-1) }

    val listState = rememberLazyGridState()

    // Handle programmatic scrolling to category
    LaunchedEffect(targetScrollIndex) {
        if (targetScrollIndex >= 0) {
            listState.animateScrollToItem(targetScrollIndex)
            targetScrollIndex = -1 // Reset state after scrolling is done
        }
    }

    // Blinking effect for selected item
    val infiniteTransition = rememberInfiniteTransition(label = "blinkTransition")
    val blinkAlpha by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 0.3f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500), repeatMode = RepeatMode.Reverse
        ), label = "blinkAlpha"
    )

    // Sync selected category with scroll position
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }.collect { index ->
            val firstVisibleItem = allShopItems.getOrNull(index)
            if (firstVisibleItem != null) {
                selectedCategory = firstVisibleItem.category
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_shop),
            contentDescription = "Shop Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Ensures the image stretches to fill screen edges
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top Right Coin and Exit Buttons
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp), // Padding to avoid blocking
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Top Right Coin Button
                Image(
                    painter = painterResource(id = R.drawable.button_coin),
                    contentDescription = "Coins",
                    modifier = Modifier.height(60.dp)
                )

                // Top Right Exit Button
                Image(
                    painter = painterResource(id = R.drawable.button_exit),
                    contentDescription = "Close Shop",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 10.dp)
                        .clickable { activity?.finish() } // Triggers the Android activity finish loop
                )
            }

            // Animals, Woods, Metals Categories
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(180.dp)
                    .padding(top = 40.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Categories text
                Image(
                    painter = painterResource(id = R.drawable.shop_categories),
                    contentDescription = "Categories",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, start = 12.dp),
                    contentScale = ContentScale.Fit
                )

                // Define the available categories and their image assets
                val categories = listOf(
                    "Animals" to R.drawable.shop_animals,
                    "Woods" to R.drawable.shop_wood,
                    "Metals" to R.drawable.shop_metals
                )

                // Loop through the categories list to generate a clickable image button for each category
                for ((category, imageRes) in categories) {
                    val isSelected = selectedCategory == category // Check if this is the active tab
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = category,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .clickable {
                                selectedCategory = category
                                targetScrollIndex = allShopItems.indexOfFirst { it.category == category }
                            }, // update status when clicked
                        contentScale = ContentScale.Fit,
                        // Unselected buttons are semi transparent
                        alpha = if (isSelected) 1.0f else 0.5f
                    )
                }
            }

            // Items grid (Animals, Woods, Metals)
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(
                        start = 200.dp, top = 80.dp, end = 20.dp, bottom = 20.dp
                    ) // Increased top padding to 80.dp to clip grid below coin count
                    .fillMaxSize()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 100.dp),
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 70.dp)
                ) {
                    items(allShopItems) { item ->
                        val isSelectedItem = selectedItem == item

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { selectedItem = item }
                        ) {
                            Image(
                                painter = painterResource(id = item.imageRes),
                                contentDescription = item.id,
                                modifier = Modifier.size(100.dp),
                                contentScale = ContentScale.Fit,
                                alpha = if (isSelectedItem) blinkAlpha else 1f
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.id,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            //Purchase Button
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.button_purchase),
                    contentDescription = "Purchase",
                    modifier = Modifier
                        .height(50.dp)
                        .width(160.dp)
                        .clickable {})
            }
        }
    }
}