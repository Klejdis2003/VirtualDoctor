package com.packages.user_profile.dashboard

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.packages.client.user.Stats
import com.packages.main.model.user.DietaryRequirements

@Composable
fun Dashboard(stats: Stats, dietaryRequirements: DietaryRequirements){
    val maxLabel = "Max"
    val table = arrayOf(
        arrayOf(Pair("Calories", stats.calories), Pair(maxLabel, dietaryRequirements.calorieLimit)),
        arrayOf(Pair("Protein", stats.protein), Pair(maxLabel, dietaryRequirements.maxProteinContent)),
        arrayOf(Pair("Fat", stats.fat), Pair(maxLabel, dietaryRequirements.maxFatContent)),
        arrayOf(Pair("Carbs", stats.carbohydrates), Pair(maxLabel, dietaryRequirements.maxSugarContent))
    )
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
                        val emoji =
                            if (pair1.second > pair2.second) "\uD83D\uDE1E" else "\uD83D\uDE03"
                        if(label == maxLabel){
                            Text(
                                text = emoji,
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

@Composable
fun PaddedText(text: String, style: TextStyle, modifier: Modifier = Modifier) {
    Text(text = text, style = style, modifier = modifier.padding(8.dp))
}