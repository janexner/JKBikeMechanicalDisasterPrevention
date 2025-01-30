package com.exner.tools.kjsbikemaintenancechecker.database

import com.exner.tools.kjsbikemaintenancechecker.database.entities.Activity
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Bike
import com.exner.tools.kjsbikemaintenancechecker.database.entities.BikeActivities
import com.exner.tools.kjsbikemaintenancechecker.database.entities.Component
import com.exner.tools.kjsbikemaintenancechecker.database.entities.ComponentActivities
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import java.time.Month
import javax.inject.Provider

suspend fun populateDatabaseWithSampleData(
    provider: Provider<KJsDAO>
) {

    val taigaBuildDate = LocalDate(2020, 6, 1)
    val nowDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

    // the Taiga
    val taiga = Bike(
        name = "Taiga",
        buildDate = taigaBuildDate,
        mileage = 6395,
        lastUsedDate = nowDate,
        uid = 0,
    )
    val taigaUid = provider.get().insertBike(taiga)
    val bike1Fork = Component(
        name = "Mastodon",
        description = "Manitou Mastodon Pro Ext 120mm",
        bikeUid = taigaUid,
        parentComponentUid = null,
        acquisitionDate = taigaBuildDate,
        mileage = 6395,
        lastUsedDate = nowDate,
        uid = 0
    )
    provider.get().insertComponent(bike1Fork)
    val bike1Brakes = Component(
        name = "Dominions",
        description = "Hayes Dominion A4",
        bikeUid = taigaUid,
        parentComponentUid = null,
        acquisitionDate = taigaBuildDate,
        mileage = 6395,
        lastUsedDate = nowDate,
        uid = 0
    )
    val taigaBrakesUid = provider.get().insertComponent(bike1Brakes)
    val bike1FrontBrakes = Component(
        name = "Front brakes",
        description = "Hayes Dominion A4 - Front",
        bikeUid = taigaUid,
        parentComponentUid = taigaBrakesUid,
        acquisitionDate = taigaBuildDate,
        mileage = 6395,
        lastUsedDate = nowDate,
        uid = 0
    )
    val taigaFrontBrakesUid = provider.get().insertComponent(bike1FrontBrakes)
    val bike1FrontBrakePads = Component(
        name = "Hayes sintered",
        description = "Hayes Dominion A4 sintered pads",
        bikeUid = taigaUid,
        parentComponentUid = taigaFrontBrakesUid,
        acquisitionDate = LocalDate.parse("2024-11-01"),
        mileage = 181,
        lastUsedDate = nowDate,
        uid = 0
    )
    provider.get().insertComponent(bike1FrontBrakePads)
    val bike1RearBrakes = Component(
        name = "Rear brakes",
        description = "Hayes Dominion A4 - Rear",
        bikeUid = taigaUid,
        parentComponentUid = taigaBrakesUid,
        acquisitionDate = taigaBuildDate,
        mileage = 6395,
        lastUsedDate = nowDate,
        uid = 0
    )
    val taigaRearBrakesUid = provider.get().insertComponent(bike1RearBrakes)
    val bike1RearBrakePads = Component(
        name = "Hayes sintered",
        description = "Hayes Dominion A4 sintered pads",
        bikeUid = taigaUid,
        parentComponentUid = taigaRearBrakesUid,
        acquisitionDate = LocalDate.parse("2024-09-01"),
        mileage = 200,
        lastUsedDate = nowDate,
        uid = 0
    )
    provider.get().insertComponent(bike1RearBrakePads)

    // the Vado
    val vado = Bike(
        name = "Turbo Vado",
        buildDate = LocalDate.parse("2023-05-20"),
        mileage = 6500,
        lastUsedDate = nowDate,
        uid = 0
    )
    val vadoUid = provider.get().insertBike(vado)
    val bike2Battery = Component(
        name = "Battery",
        description = "730 Wh battery",
        bikeUid = vadoUid,
        parentComponentUid = null,
        acquisitionDate = LocalDate.parse("2023-05-20"),
        mileage = 6500,
        lastUsedDate = nowDate,
        uid = 0,
    )
    val batteryUid = provider.get().insertComponent(bike2Battery)
    val chargeBattery = Activity(
        title = "Charge Battery",
        description = "If battery is low, charge it",
        isCompleted = false,
        createdDate = nowDate,
        dueDate = nowDate,
        uid = 0,
    )
    val chargeBatteryUid = provider.get().insertActivity(chargeBattery)
    val chargeBatteryOnVadoActivity = BikeActivities(
        bikeUid = vadoUid,
        activityUid = chargeBatteryUid,
        uid = 0
    )
    provider.get().insertBikeActivities(chargeBatteryOnVadoActivity)
    val chargeBatteryComponentActivity = ComponentActivities(
        componentUid = batteryUid,
        activityUid = chargeBatteryUid,
        uid = 0
    )
    provider.get().insertComponentActivity(chargeBatteryComponentActivity)

    // the Evolink
    val bike3 = Bike(
        name = "Evolink",
        buildDate = LocalDate.parse("2022-09-09"),
        mileage = 1388,
        lastUsedDate = LocalDate.parse("2025-01-16"),
        uid = 0
    )
    provider.get().insertBike(bike3)

    // every ride
    val bottleActivity = Activity(
        title = "Take Water",
        description = "Take a bottle of fresh water",
        isCompleted = false,
        createdDate = nowDate,
        dueDate = null,
        uid = 0
    )
    provider.get().insertActivity(bottleActivity)

    // some things for the shelf
    val extraCassette1 = Component(
        name = "11psd 11-50 Cassette",
        description = "Deore 11-50 Cassette",
        bikeUid = 0,
        parentComponentUid = null,
        acquisitionDate = LocalDate(2024, 6,1),
        mileage = 0,
        lastUsedDate = null,
        uid = 0,
    )
    provider.get().insertComponent(extraCassette1)
    provider.get().insertComponent(extraCassette1) // I've got 2 of those
}

suspend fun generateTopLevelComponentsForNewBike(bikeUid: Long, repository: KJsRepository) {
    val buildDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

    // drivetrain
    val drivetrain = Component(
        name = "Drivetrain",
        description = "Drivetrain",
        bikeUid = bikeUid,
        parentComponentUid = null,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    val drivetrainUid = repository.insertComponent(drivetrain)
    val bottomBracket = Component(
        name = "Bottom bracket",
        description = "Bottom bracket",
        bikeUid = bikeUid,
        parentComponentUid = drivetrainUid,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    repository.insertComponent(bottomBracket)
    val chainring = Component(
        name = "Chainring",
        description = "Chainring",
        bikeUid = bikeUid,
        parentComponentUid = drivetrainUid,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    repository.insertComponent(chainring)
    // fork
    val fork = Component(
        name = "Fork",
        description = "Fork",
        bikeUid = bikeUid,
        parentComponentUid = null,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    repository.insertComponent(fork)
    // suspension
    val suspension = Component(
        name = "Suspension",
        description = "Suspension",
        bikeUid = bikeUid,
        parentComponentUid = null,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    repository.insertComponent(suspension)
    // frame + stuff
    val frame = Component(
        name = "Frame",
        description = "Frame",
        bikeUid = bikeUid,
        parentComponentUid = null,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    repository.insertComponent(frame)
    // cockpit
    val cockpit = Component(
        name = "Cockpit",
        description = "Cockpit",
        bikeUid = bikeUid,
        parentComponentUid = null,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    repository.insertComponent(cockpit)
    // wheels
    val wheels = Component(
        name = "Wheels",
        description = "Wheels",
        bikeUid = bikeUid,
        parentComponentUid = null,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    val wheelsUid = repository.insertComponent(wheels)
    val rearWheel = Component(
        name = "Rear wheel",
        description = "Rear wheel",
        bikeUid = bikeUid,
        parentComponentUid = wheelsUid,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    val rearWheelUid = repository.insertComponent(rearWheel)
    val rearTyre = Component(
        name = "Rear tyre",
        description = "Rear tyre",
        bikeUid = bikeUid,
        parentComponentUid = rearWheelUid,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    repository.insertComponent(rearTyre)
    val rearRim = Component(
        name = "Rear rim",
        description = "Rear rim",
        bikeUid = bikeUid,
        parentComponentUid = rearWheelUid,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    repository.insertComponent(rearRim)
    val rearHub = Component(
        name = "Rear hub",
        description = "Rear hub",
        bikeUid = bikeUid,
        parentComponentUid = rearWheelUid,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    repository.insertComponent(rearHub)
    val frontWheel = Component(
        name = "Front wheel",
        description = "Front wheel",
        bikeUid = bikeUid,
        parentComponentUid = wheelsUid,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    val frontWheelUid = repository.insertComponent(frontWheel)
    val frontTyre = Component(
        name = "Front tyre",
        description = "Front tyre",
        bikeUid = bikeUid,
        parentComponentUid = frontWheelUid,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    repository.insertComponent(frontTyre)
    val frontRim = Component(
        name = "Front rim",
        description = "Front rim",
        bikeUid = bikeUid,
        parentComponentUid = frontWheelUid,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    repository.insertComponent(frontRim)
    val frontHub = Component(
        name = "Front hub",
        description = "Front hub",
        bikeUid = bikeUid,
        parentComponentUid = frontWheelUid,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    repository.insertComponent(frontHub)
    // brakes
    val brakes = Component(
        name = "Brakes",
        description = "Brakes",
        bikeUid = bikeUid,
        parentComponentUid = null,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    val brakesUid = repository.insertComponent(brakes)
    val frontBrakes = Component(
        name = "Front brakes",
        description = "Front brakes",
        bikeUid = bikeUid,
        parentComponentUid = brakesUid,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    val frontBrakesUid = repository.insertComponent(frontBrakes)
    val frontBrakePads = Component(
        name = "Front brake pads",
        description = "Front brake pads",
        bikeUid = bikeUid,
        parentComponentUid = frontBrakesUid,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    repository.insertComponent(frontBrakePads)
    val frontRotor = Component(
        name = "Front rotor",
        description = "Front rotor",
        bikeUid = bikeUid,
        parentComponentUid = frontBrakesUid,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    repository.insertComponent(frontRotor)
    val rearBrakes = Component(
        name = "Rear brakes",
        description = "Rear brakes",
        bikeUid = bikeUid,
        parentComponentUid = brakesUid,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    val rearBrakesUid = repository.insertComponent(rearBrakes)
    val rearBrakePads = Component(
        name = "Rear brake pads",
        description = "Rear brake pads",
        bikeUid = bikeUid,
        parentComponentUid = frontBrakesUid,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    repository.insertComponent(rearBrakePads)
    val rearRotor = Component(
        name = "Rear rotor",
        description = "Rear rotor",
        bikeUid = bikeUid,
        parentComponentUid = rearBrakesUid,
        acquisitionDate = buildDate,
        mileage = 0,
        lastUsedDate = null,
        uid = 0
    )
    repository.insertComponent(rearRotor)
}
