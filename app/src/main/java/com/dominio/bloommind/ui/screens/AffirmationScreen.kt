package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dominio.bloommind.R

@Composable
fun AffirmationScreen(
    affirmationText: String,
    imageIndex: Int
) {

    val affirmationImages = listOf(
        R.drawable.affirmation1,
        R.drawable.affirmation2,
        R.drawable.affirmation3,
        R.drawable.affirmation4,
        R.drawable.affirmation5,
        R.drawable.affirmation6,
        R.drawable.affirmation7,
        R.drawable.affirmation8,
        R.drawable.affirmation9,
        R.drawable.affirmation10,
        R.drawable.affirmation11,
        R.drawable.affirmation12,
        R.drawable.affirmation13,
        R.drawable.affirmation14,
        R.drawable.affirmation15,
        R.drawable.affirmation16,
        R.drawable.affirmation17,
        R.drawable.affirmation18,
        R.drawable.affirmation19,
        R.drawable.affirmation20,
        R.drawable.affirmation21,
        R.drawable.affirmation22,
        R.drawable.affirmation23,
        R.drawable.affirmation24,
        R.drawable.affirmation25,
        R.drawable.affirmation26,
        R.drawable.affirmation27,
        R.drawable.affirmation28,
        R.drawable.affirmation29,
        R.drawable.affirmation30,
        R.drawable.affirmation31,
        R.drawable.affirmation32
    )

    val imageRes = if (imageIndex in affirmationImages.indices) {
        affirmationImages[imageIndex]
    } else {
        affirmationImages.getOrElse(imageIndex % affirmationImages.size) { R.drawable.icon1 }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = stringResource(id = R.string.affirmation_image_cd),
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = affirmationText,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}
