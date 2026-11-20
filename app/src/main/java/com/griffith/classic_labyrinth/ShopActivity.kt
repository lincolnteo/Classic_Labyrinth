// Lincoln Teo Ming Kern - 3122215
// GitHub repo: https://github.com/lincolnteo/Classic_Labyrinth
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

    // resume music when app is maximized
    override fun onResume() {
        super.onResume()
        SoundManager.isSwitchingScreens = false
        SoundManager.resumeMusic()
    }

    // pause music when app is minimized, but keep playing if switching between activities
    override fun onPause() {
        super.onPause()
        if (!SoundManager.isSwitchingScreens) {
            SoundManager.pauseMusic()
        }
    }
}

// constructor storing skins
data class ShopItem(
    val id: Int, val idName: String, val price: Int, val imageRes: Int, val category: String
)

// data of shop items (ItemID, Name, Price, Image, Category)
val allShopItems = listOf(
    // Animals
    ShopItem(1, "Bear", 100, R.drawable.shop_animals_bear, "Animals"),
    ShopItem(2, "Bull", 100, R.drawable.shop_animals_bull, "Animals"),
    ShopItem(3, "Eagle", 100, R.drawable.shop_animals_eagle, "Animals"),
    ShopItem(4, "Fox", 100, R.drawable.shop_animals_fox, "Animals"),
    ShopItem(5, "Gorilla", 100, R.drawable.shop_animals_gorrila, "Animals"),
    ShopItem(6, "Lion", 100, R.drawable.shop_animals_lion, "Animals"),
    ShopItem(7, "Panda", 100, R.drawable.shop_animals_panda, "Animals"),
    ShopItem(8, "Shark", 100, R.drawable.shop_animals_shark, "Animals"),
    ShopItem(9, "Tiger", 100, R.drawable.shop_animals_tiger, "Animals"),
    ShopItem(10, "Wolf", 100, R.drawable.shop_animals_wolf, "Animals"),
    // Woods
    ShopItem(11, "Brown Oak", 100, R.drawable.shop_wood_brownoak, "Woods"),
    ShopItem(12, "Coastal Drift", 100, R.drawable.shop_wood_coastaldrift, "Woods"),
    ShopItem(13, "Dark Oak", 100, R.drawable.shop_wood_darkoak, "Woods"),
    ShopItem(14, "Ebony", 100, R.drawable.shop_wood_ebony, "Woods"),
    ShopItem(15, "Golden Maple", 100, R.drawable.shop_wood_goldenmaple, "Woods"),
    ShopItem(16, "Grey Oak", 100, R.drawable.shop_wood_greyoak, "Woods"),
    ShopItem(17, "Natural Pine", 100, R.drawable.shop_wood_naturalpine, "Woods"),
    ShopItem(18, "Red Cedar", 100, R.drawable.shop_wood_redcedar, "Woods"),
    ShopItem(19, "Rich Mahogany", 100, R.drawable.shop_wood_richmahogany, "Woods"),
    ShopItem(20, "Rustic Walnut", 100, R.drawable.shop_wood_rusticwalnut, "Woods"),
    ShopItem(21, "Spiced Cherry", 100, R.drawable.shop_wood_spicedcherry, "Woods"),
    ShopItem(22, "Weathered Grey", 100, R.drawable.shop_wood_weatheredgrey, "Woods"),
    // Metals
    ShopItem(23, "Age Bronze", 100, R.drawable.shop_metals_agebronze, "Metals"),
    ShopItem(24, "Beaten Brass", 100, R.drawable.shop_metals_beatenbrass, "Metals"),
    ShopItem(25, "Brushed Steel", 100, R.drawable.shop_metals_brushedsteel, "Metals"),
    ShopItem(26, "Chrome", 100, R.drawable.shop_metals_chrome, "Metals"),
    ShopItem(27, "Copper", 100, R.drawable.shop_metals_copper, "Metals"),
    ShopItem(28, "Forged Iron", 100, R.drawable.shop_metals_forgediron, "Metals"),
    ShopItem(29, "Galvanized Zinc", 100, R.drawable.shop_metals_galvanizedzinc, "Metals"),
    ShopItem(30, "Metallic Blue", 100, R.drawable.shop_metals_metallicblue, "Metals"),
    ShopItem(31, "Polished Gold", 100, R.drawable.shop_metals_polishedgold, "Metals"),
    ShopItem(32, "Rusty Iron", 100, R.drawable.shop_metals_rustyiron, "Metals")
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
    // Stores the item index to auto-scroll to when a category is clicked ,-1 means idle
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

    // Load bought skin IDs directly from shop.csv
    val boughtIds = remember {
        val ids = mutableListOf<Int>()
        val lines = context.assets.open("shop.csv").bufferedReader().readLines()
        for (line in lines) {
            val parts = line.split(",")
            if (parts.size >= 2 && parts[1].trim() == "true") {
                val id = parts[0].trim().toIntOrNull()
                if (id != null) ids.add(id)
            }
        }
        ids
    }

    // Main container
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_shop),
            contentDescription = "Shop Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Ensures the image stretches to fill screen edges
        )
        // container for the top right the exit nad coin bar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top Right Coin and Exit Buttons
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.TopEnd) {

                    // Top Right Coin Button
                    Image(
                        painter = painterResource(id = R.drawable.button_coin),
                        contentDescription = "Coins",
                        modifier = Modifier
                            .height(60.dp)
                            .padding(end = 80.dp)
                    )
                    // text in the coinbar
                    Text(
                        text = "XXXX",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(end = 120.dp, top = 10.dp)
                            .align(Alignment.CenterEnd)
                    )

                    // Top Right Exit Button
                    Image(
                        painter = painterResource(id = R.drawable.button_exit),
                        contentDescription = "Close Shop",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(end = 10.dp)
                            .clickable {
                                SoundManager.playClick()
                                SoundManager.isSwitchingScreens = true
                                activity?.finish()
                            } // Triggers the Android activity finish loop
                    )
                }
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
                                SoundManager.playClick()
                                selectedCategory = category
                                targetScrollIndex = // scans the list of ShopItem then finds the very first category that match what the user clicks
                                    allShopItems.indexOfFirst { it.category == category }
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
                    )
                    .fillMaxSize()
            ) {
                // grid for storing skins
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 100.dp),
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 70.dp)
                ) {
                    //
                    items(allShopItems) { item ->
                        // check if this items is currently selected by the user
                        val isSelectedItem = selectedItem == item
                        // Checks if skin ID is in the bought list
                        val isUnlocked = item.id in boughtIds
                        val alpha =
                            if (isUnlocked) 1.0f else 0.5f// if item is locked alpha will be 0.5 while unlocked would be 1
                        val isEquipped =
                            EquippedManager.equippedItem == item // Checks if this skin is the one currently equipped

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                // equippin fucntion
                                if (isUnlocked) {
                                    SoundManager.playClick()
                                    selectedItem = item
                                    // if item is already equip then remove it, set to null
                                    // else equip the item clicked
                                    EquippedManager.equippedItem =
                                        if (EquippedManager.equippedItem == item) null else item
                                }
                            }) {
                            // skin image
                            Image(
                                painter = painterResource(id = item.imageRes),
                                contentDescription = item.idName,
                                modifier = Modifier.size(100.dp),
                                contentScale = ContentScale.Fit,
                                alpha = if (isSelectedItem) blinkAlpha else alpha
                            )
                            // the skin name
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.idName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = alpha)
                            )
                            // The cost
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${item.price} Coins",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Yellow.copy(alpha = alpha)
                            )
                            // Equip Status
                            if (isEquipped) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "EQUIPPED",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Green
                                )
                            }
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
                // function is not implemented yet
                Image(
                    painter = painterResource(id = R.drawable.button_purchase),
                    contentDescription = "Purchase",
                    modifier = Modifier
                        .height(50.dp)
                        .width(160.dp)
                        .clickable { SoundManager.playClick() })
            }
        }
    }
}
// Equipment manager used to track if an item is equipped or not
object EquippedManager {
    var equippedItem: ShopItem? = null
}