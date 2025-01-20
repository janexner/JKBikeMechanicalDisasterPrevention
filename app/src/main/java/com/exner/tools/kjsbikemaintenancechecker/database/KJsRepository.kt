package com.exner.tools.kjsbikemaintenancechecker.database

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class KJsRepository @Inject constructor(kjsDAO: KJsDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val observeActivitiesByDueDate: Flow<List<Activity>> = kjsDAO.observeActivitiesOrderedByDueDate()
}