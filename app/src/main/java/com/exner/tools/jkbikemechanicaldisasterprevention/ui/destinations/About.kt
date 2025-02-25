package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.exner.tools.jkbikemechanicaldisasterprevention.BuildConfig
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.PageHeaderTextWithSpacer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.AboutDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun About(
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass
) {
    KJsResponsiveNavigation(
        AboutDestination,
        destinationsNavigator,
        windowSizeClass
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(8.dp)
        ) {
            PageHeaderTextWithSpacer(stringResource(R.string.about))
            Text(text = "Version " + BuildConfig.VERSION_NAME)
            DefaultSpacer()
            Text(text = stringResource(R.string.about1))
            DefaultSpacer()
            Text(text = stringResource(R.string.about2))
            DefaultSpacer()
            Text(text = stringResource(R.string.about3))
            DefaultSpacer()
            Text(text = stringResource(R.string.about4))
            DefaultSpacer()
            Text(text = stringResource(R.string.about5))
        }
    }
}
