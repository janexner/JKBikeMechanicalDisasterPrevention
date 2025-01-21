package com.exner.tools.kjsbikemaintenancechecker.ui.destinations.wrappers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.exner.tools.kjsbikemaintenancechecker.ui.IconSpacer
import com.exner.tools.kjsbikemaintenancechecker.ui.WelcomeViewModel
import com.ramcosta.composedestinations.scope.DestinationScope
import com.ramcosta.composedestinations.wrapper.DestinationWrapper

object OnboardingWrapper : DestinationWrapper {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun <T> DestinationScope<T>.Wrap(screenContent: @Composable () -> Unit) {

        val welcomeViewModel: WelcomeViewModel = hiltViewModel()
        val needsOnboarding by welcomeViewModel.needsOnboarding.collectAsState()

        val showDialog = remember { mutableStateOf(needsOnboarding) }

        Surface(modifier = Modifier.fillMaxSize()) {
            // dialog to show if onboarding is needed
            if (showDialog.value) {
                BasicAlertDialog(
                    onDismissRequest = {
                        showDialog.value = false
                    },
                ) {
                    Surface(
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight(),
                        shape = MaterialTheme.shapes.large,
                        tonalElevation = AlertDialogDefaults.TonalElevation
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Before you can see anything here, you should add your bike(s) and components.",
                            )
                            IconSpacer()
                            TextButton(
                                onClick = {
                                    showDialog.value = false
                                    welcomeViewModel.setNeedsOnboarding(false)
                                },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Confirm")
                            }
                        }
                    }
                }
            }

            // rest of the screen
            screenContent()
        }
    }

}
