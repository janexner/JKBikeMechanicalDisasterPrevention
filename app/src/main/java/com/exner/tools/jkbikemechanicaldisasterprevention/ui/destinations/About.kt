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
            Text(text = "J-K Bike - Mechanical Disaster Prevention is a free, open source app that helps build positive bike maintenance behaviours, for rides quick, big or epic.")
            DefaultSpacer()
            Text(text = "We have all experienced those rides where something mechanical happens that really was preventable.")
            DefaultSpacer()
            Text(text = "As the authors, even though we take a certain pride in our own efforts in maintaining smooth running MTBs, and a disdain for our riding buddies who never give a passing thought until something goes 'CRUNCH!' in a ride-ending manner, there are still innumerate situations where something that should have been checked, or usually never needs checked after proper installation, has not been.")
            DefaultSpacer()
            Text(text = "'Where is that vibration coming from?', 'why is the rear end creaking?', 'What is that terrible knocking?' are so often result of something relatively simple being overlooked or forgotten. Recent favourites have been disc rotor bolts backing out, rear thru axles not done up properly, and headsets improperly tensioned.")
            DefaultSpacer()
            Text(text = "So, to prevent mechanical disasters happening on your rides, use this app to build better, new, habits and ensure your rides are (mechanically) glorious.")
        }
    }
}
