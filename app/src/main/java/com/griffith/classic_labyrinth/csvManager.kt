// Lincoln Teo Ming Kern 3122215
// GitHub repo: https://github.com/lincolnteo/Classic_Labyrinth
package com.griffith.classic_labyrinth

import android.content.Context
import java.io.File

/**
 * Manager object for reading and writing CSV files (player_setting.csv and shop.csv)
 * to keep the activity files clean and organized.
 */
object csvManager {
    // get and create the writable shop.csv file in internal storage
    fun getShopCsvFile(context: Context): File {
        val file = File(context.filesDir, "shop.csv")
        // checks id user have the file
        if (!file.exists()) {
            // save shop.csv from assets into internal storage so we can write to it
            context.assets.open("shop.csv").use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
        return file
    }
    // Load bought skin IDs from shop.csv
    fun loadBoughtSkinIds(context: Context): List<Int> {
        val ids = mutableListOf<Int>()
        val file = getShopCsvFile(context)
        for (line in file.readLines()) {
            val parts = line.split(",")// acts as the divider
            // get index 1 which is the second value
            if (parts.size >= 3 && parts[1] == "true") {
                val id = parts[0].toIntOrNull()
                if (id != null) ids.add(id)
            }
        }
        return ids
    }

    // Load item prices from shop.csv
    fun loadShopItemPrices(context: Context): Map<Int, Int> {
        val prices = mutableMapOf<Int, Int>()
        val file = getShopCsvFile(context)
        for (line in file.readLines()) {
            val parts = line.split(",")
            // index 0 is ID, index 2 is price
            if (parts.size >= 3 && parts[0] != "id") {
                val id = parts[0].toInt()
                val price = parts[2].toInt()
                prices[id] = price
            }
        }
        return prices
    }

    // Updates shop.csv to mark itemId as bought
    // IsBought = true
    fun purchaseShopItem(context: Context, itemId: Int) {
        // Read lines from shop.csv
        val file = getShopCsvFile(context)
        val lines = file.readLines()
        // Update the line for this item ID from false to true while preserving price at index 2
        val updatedLines = lines.map { line ->
            val parts = line.split(",")// splits into an array
            // get index 0 which is the first value
            if (parts.size >= 3 && parts[0] == itemId.toString()) {
                "$itemId,true,${parts[2]}"
            } else {
                line
            }
        }
        // save the updated lines back to shop.csv
        file.writeText(updatedLines.joinToString("\n"))
    }

    // get and create the writable player_settings.csv file in internal storage
    fun getPlayerSettingsFile(context: Context): File {
        val file = File(context.filesDir, "player_setting.csv")
        // checks id user have the file
        if (!file.exists()) {
            // save player_setting.csv from assets into internal storage so we can write to it
            context.assets.open("player_setting.csv").use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }
        return file
    }

    // Load data from csv
    fun loadPlayerSettings(context: Context): Map<String, String> {
        val map = mutableMapOf<String, String>()

        val file = getPlayerSettingsFile(context)
        for (line in file.readLines()) {
            val parts = line.split(",")// split to an array
            // get index 0 which is the first value
            if (parts.size >= 2 && parts[0] != "Key") {
                map[parts[0]] = parts[1]
            }
        }
        return map
    }

    // Saves Key and Value pair in player_setting.csv
    fun savePlayerSetting(context: Context, keyToUpdate: String, newValue: String) {

        val file = getPlayerSettingsFile(context)
        val lines = file.readLines()
        val updatedLines = lines.map { line ->
            val parts = line.split(",")// split to an array
            // get index 0 which is the first value
            if (parts.size >= 2 && parts[0] == keyToUpdate) {
                "$keyToUpdate,$newValue"
            } else {
                line
            }
        }
        file.writeText(updatedLines.joinToString("\n"))
    }
}
