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

        // the Taiga
        val bike1 = Bike(
            name = "Taiga",
            buildDate = LocalDateTime.of(2020, Month.JUNE, 1, 12, 0),
            mileage = 6395,
            lastUsedDate = LocalDateTime.now(),
            uid = 1,
        )
        provider.get().insertBike(bike1)
        val bike1Fork = Component(
            name = "Mastodon",
            description = "Manitou Mastodon Pro Ext 120mm",
            bikeUid = 1,
            parentComponentUid = null,
            acquisitionDate = LocalDateTime.of(2020, Month.APRIL, 1, 12, 0),
            mileage = 6395,
            lastUsedDate = LocalDateTime.now(),
            uid = 100
        )
        provider.get().insertComponent(bike1Fork)
        val bike1Brakes = Component(
            name = "Dominions",
            description = "Hayes Dominion A4",
            bikeUid = 1,
            parentComponentUid = null,
            acquisitionDate = LocalDateTime.of(2020, Month.JUNE, 1, 12, 0),
            mileage = 6395,
            lastUsedDate = LocalDateTime.now(),
            uid = 101
        )
        provider.get().insertComponent(bike1Brakes)
        val bike1FrontBrakes = Component(
            name = "Front brakes",
            description = "Hayes Dominion A4 - Front",
            bikeUid = 1,
            parentComponentUid = 101,
            acquisitionDate = LocalDateTime.of(2020, Month.JUNE, 1, 12, 0),
            mileage = 6395,
            lastUsedDate = LocalDateTime.now(),
            uid = 102
        )
        provider.get().insertComponent(bike1FrontBrakes)
        val bike1FrontBrakePads = Component(
            name = "Hayes sintered",
            description = "Hayes Dominion A4 sintered pads",
            bikeUid = 1,
            parentComponentUid = 102,
            acquisitionDate = LocalDateTime.of(2024, Month.NOVEMBER, 1, 12, 0),
            mileage = 181,
            lastUsedDate = LocalDateTime.now(),
            uid = 104
        )
        provider.get().insertComponent(bike1FrontBrakePads)
        val bike1RearBrakes = Component(
            name = "Rear brakes",
            description = "Hayes Dominion A4 - Rear",
            bikeUid = 1,
            parentComponentUid = 101,
            acquisitionDate = LocalDateTime.of(2020, Month.JUNE, 1, 12, 0),
            mileage = 6395,
            lastUsedDate = LocalDateTime.now(),
            uid = 103
        )
        provider.get().insertComponent(bike1RearBrakes)
        val bike1RearBrakePads = Component(
            name = "Hayes sintered",
            description = "Hayes Dominion A4 sintered pads",
            bikeUid = 1,
            parentComponentUid = 103,
            acquisitionDate = LocalDateTime.of(2024, Month.SEPTEMBER, 1, 12, 0),
            mileage = 200,
            lastUsedDate = LocalDateTime.now(),
            uid = 105
        )
        provider.get().insertComponent(bike1RearBrakePads)

        // the Vado
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
            uid = 200,
        )
        provider.get().insertComponent(bike2Battery)
        val chargeBattery = Activity(
            title = "Charge Battery",
            description = "If battery is low, charge it",
            isCompleted = false,
            createdDate = LocalDateTime.now(),
            dueDate = LocalDateTime.now(),
            uid = 200,
        )
        provider.get().insertActivity(chargeBattery)
        val chargeBatteryOnVadoActivity = BikeActivities(
            bikeUid = 2,
            activityUid = 200,
            uid = 200
        )
        provider.get().insertBikeActivities(chargeBatteryOnVadoActivity)
        val chargeBatteryComponentActivity = ComponentActivities(
            componentUid = 200,
            activityUid = 200,
            uid = 1
        )
        provider.get().insertComponentActivity(chargeBatteryComponentActivity)
        
        // the Evolink
        val bike3 = Bike(
            name = "Evolink",
            buildDate = LocalDateTime.of(2022, Month.SEPTEMBER, 9, 12, 0),
            mileage = 1388,
            lastUsedDate = LocalDateTime.of(2025, Month.JANUARY, 16, 9, 0),
            uid = 3
        )
        provider.get().insertBike(bike3)

        // every ride
        val bottleActivity = Activity(
            title = "Take Water",
            description = "Take a bottle of fresh water",
            isCompleted = false,
            createdDate = LocalDateTime.now(),
            dueDate = null,
            uid = 1
        )
        provider.get().insertActivity(bottleActivity)
    }
}