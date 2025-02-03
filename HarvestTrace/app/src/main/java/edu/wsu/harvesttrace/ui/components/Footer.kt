package edu.wsu.weather.fst.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.wsu.harvesttrace.R

@Preview
@Composable
fun Footer() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .height(IntrinsicSize.Min)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.wsu_logo),
            contentDescription = "WSU Logo",
            Modifier
                .size(60.dp)
        )
        VerticalDivider(modifier = Modifier.padding(10.dp, 0.dp))
        Column {
            Text(
                text = "Team Pumas",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(text = "Washington State University")
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}