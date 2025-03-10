package com.exner.tools.jkbikemechanicaldisasterprevention.ui.destinations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.exner.tools.jkbikemechanicaldisasterprevention.R
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.KJsGlobalScaffoldViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.StravaAuthResultViewModel
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.DefaultSpacer
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.components.KJsResponsiveNavigation
import com.exner.tools.jkbikemechanicaldisasterprevention.ui.helpers.KJsAction
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.StravaAuthResultDeepLinkTargetDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>()
@Composable
fun StravaAuthResultDeepLinkTarget(
    stravaAuthResultViewModel: StravaAuthResultViewModel = hiltViewModel(),
    kJsGlobalScaffoldViewModel: KJsGlobalScaffoldViewModel,
    destinationsNavigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass,
    code: String,
    scope: String
) {
    kJsGlobalScaffoldViewModel.setDestinationTitle(stringResource(R.string.hdr_about)) // TODO

    val isAuthenticationInitiated by stravaAuthResultViewModel.isAuthenticationInitiated.collectAsStateWithLifecycle(
        initialValue = false
    )
    val isAuthenticated by stravaAuthResultViewModel.isAuthenticated.collectAsStateWithLifecycle(
        initialValue = false
    )

    // this will run in the background
    if (!isAuthenticationInitiated) {
        stravaAuthResultViewModel.authenticateWithCode(code = code)
    }

    KJsResponsiveNavigation(
        StravaAuthResultDeepLinkTargetDestination,
        destinationsNavigator,
        windowSizeClass,
        myActions = listOf(
            KJsAction(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.btn_desc_back),
                onClick = {
                    destinationsNavigator.navigateUp()
                }
            )
        ),
        headline = stringResource(R.string.hdr_about) // TODO
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = "Back from Strava!")
                DefaultSpacer()
                if (isAuthenticated) {
                    Text(text = "Authenticated!")
                    DefaultSpacer()
                    Button(onClick = {

                    }) {
                        Text(text = "Retrieve bikes from Strava")
                    }
                } else if (isAuthenticationInitiated) {
                    Text(text = "Authenticating...")
                } else {
                    Text(text = "Unknown status")
                }
            }
        }
    }
}
