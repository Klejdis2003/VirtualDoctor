package com.packages.ui.user_profile.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.packages.data.model.nutrition.NutritionValues
import com.packages.data.model.user.Stats

@Composable
fun Dashboard(stats: Stats?, nutritionValues: NutritionValues?){
    val maxLabel = "Max"
    val table =
        if(stats == null || nutritionValues == null){
        arrayOf(
            arrayOf(Pair("Calories", 0), Pair(maxLabel, 0)),
            arrayOf(Pair("Protein", 0), Pair(maxLabel, 0)),
            arrayOf(Pair("Fat", 0), Pair(maxLabel, 0)),
            arrayOf(Pair("Carbs", 0), Pair(maxLabel, 0))
        )
    } else {
        arrayOf(
            arrayOf(
                Pair("Calories", stats.calories),
                Pair(maxLabel, nutritionValues.calories)
            ),
            arrayOf(
                Pair("Protein", stats.protein),
                Pair(maxLabel, nutritionValues.protein)
            ),
            arrayOf(Pair("Fat", stats.fat), Pair(maxLabel, nutritionValues.fat)),
            arrayOf(
                Pair("Carbs", stats.carbohydrates),
                Pair(maxLabel, nutritionValues.carbohydrates)
            )
        )
    }
    Box(modifier = Modifier.fillMaxHeight(0.8f)) {
        Text(text = "Your Dashboard", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.TopCenter))
        Column(modifier = Modifier.align(Alignment.Center)) {
            table.forEach { row ->
                val pair1 = row[0]
                val pair2 = row[1]
                Row(modifier = Modifier.padding(8.dp).fillMaxWidth(0.5f), horizontalArrangement = Arrangement.SpaceBetween) {
                    row.forEach { (label, value) ->
                        Column(
                            modifier = Modifier
                                .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$value",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        val emoji = if (pair1.second > pair2.second) Emoji.SAD else Emoji.HAPPY
                        if(label == maxLabel){
                            Text(
                                text = emoji.code,
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private enum class Emoji(val code: String) {
    HAPPY("\uD83D\uDE03"),
    SAD("\uD83D\uDE1E")
}

