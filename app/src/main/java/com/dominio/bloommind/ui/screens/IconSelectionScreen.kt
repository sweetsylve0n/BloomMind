package com.dominio.bloommind.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dominio.bloommind.R
import com.dominio.bloommind.ui.utils.IconProvider
import com.dominio.bloommind.viewmodel.IconSelectionViewModel

@Composable
fun IconSelectionScreen(
    iconSelectionViewModel: IconSelectionViewModel = viewModel(),
    onIconSelected: (String) -> Unit
) {
    val selectedIconId by iconSelectionViewModel.selectedIconId.collectAsState()
    val icons = IconProvider.iconList // Use the dynamic list from IconProvider

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.icon_selection_title), style = MaterialTheme.typography.headlineMedium)
        Text(text = stringResource(id = R.string.icon_selection_subtitle), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 8.dp, bottom = 24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Back to 2 columns
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            items(icons) { iconName ->
                val isSelected = selectedIconId == iconName
                val resourceId = IconProvider.getIconResource(iconName)

                Image(
                    painter = painterResource(id = resourceId),
                    contentDescription = stringResource(id = R.string.profile_icon_desc, iconName),
                    modifier = Modifier
                        .size(120.dp) // Increased size back
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = LocalIndication.current,
                            onClick = { iconSelectionViewModel.onIconSelected(iconName) }
                        )
                        .border(
                            width = if (isSelected) 3.dp else 0.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { selectedIconId?.let { onIconSelected(it) } },
            enabled = selectedIconId != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.next_button))
        }
    }
}
