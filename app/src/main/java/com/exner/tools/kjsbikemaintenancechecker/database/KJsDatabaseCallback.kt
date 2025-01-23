package com.exner.tools.kjsbikemaintenancechecker.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.BikeActivities
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import com.exner.tools.kjsbikemaintenancechecker.database.entities.ComponentActivities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Month
import javax.inject.Provider

class KJsDatabaseCallback(
    private val provider: Provider<KJsDAO>
) : RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) {
            populateDatabaseWithSampleData()
        }
    }

    private suspend fun populateDatabaseWithSampleData() {
        val bike1 = Bike(
            name = "Taiga",
            buildDate = LocalDateTime.of(2020, Month.JUNE, 1, 12, 0),
            mileage = 6000,
            lastUsedDate = LocalDateTime.now(),
            uid = 1,
        )
        provider.get().insertBike(bike1)
        val bike2 = Bike(
            name = "Turbo Vado",
            buildDate = LocalDateTime.of(2023, Month.MAY, 20, 12, 0),
            mileage = 6500,
            lastUsedDate = LocalDateTime.now(),
            uid = 2
        )
        provider.get().insertBike(bike2)
        val bike2Battery = Component(
            name = "Battery",
            description = "730 Wh battery",
            bikeUid = 2,
            parentComponentUid = null,
            acquisitionDate = LocalDateTime.of(2023, Month.MAY, 20, 12, 0),
            mileage = 6500,
            lastUsedDate = LocalDateTime.now(),
            uid = 1,
        )
        provider.get().insertComponent(bike2Battery)
        val chargeBattery = Activity(
            title = "Charge Battery",
            description = "If battery is low, charge it",
            isCompleted = false,
            createdDate = LocalDateTime.now(),
            dueDate = LocalDateTime.now(),
            uid = 1,
        )
        provider.get().insertActivity(chargeBattery)
        val chargeBatteryOnVadoActivity = BikeActivities(
            bikeUid = 2,
            activityUid = 1,
            uid = 1
        )
        provider.get().insertBikeActivities(chargeBatteryOnVadoActivity)
        val chargeBatteryComponentActivity = ComponentActivities(
            componentUid = 1,
            activityUid = 1,
            uid = 1
        )
        provider.get().insertComponentActivity(chargeBatteryComponentActivity)
        val bottleActivity = Activity(
            title = "Take Water",
            description = "Take a bottle of fresh water",
            isCompleted = false,
            createdDate = LocalDateTime.now(),
            dueDate = null,
            uid = 2
        )
        provider.get().insertActivity(bottleActivity)
    }
}