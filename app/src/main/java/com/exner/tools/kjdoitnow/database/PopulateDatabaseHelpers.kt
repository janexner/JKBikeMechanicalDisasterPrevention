package com.exner.tools.kjdoitnow.database

import com.exner.tools.kjdoitnow.database.entities.Bike
import com.exner.tools.kjdoitnow.database.entities.Component
import com.exner.tools.kjdoitnow.database.entities.TemplateActivity
import com.exner.tools.kjdoitnow.ui.helpers.RideLevel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
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
        isElectric = false,
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
        isElectric = true,
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
    provider.get().insertComponent(bike2Battery)

    // the Evolink
    val bike3 = Bike(
        name = "Evolink",
        buildDate = LocalDate.parse("2022-09-09"),
        mileage = 1388,
        lastUsedDate = LocalDate.parse("2025-01-16"),
        isElectric = false,
        uid = 0
    )
    provider.get().insertBike(bike3)

    // some things for the shelf
    val extraCassette1 = Component(
        name = "11psd 11-50 Cassette",
        description = "Deore 11-50 Cassette",
        bikeUid = 0,
        parentComponentUid = null,
        acquisitionDate = LocalDate(2024, 6, 1),
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

suspend fun generatePreparationTemplateActivities(provider: Provider<KJsDAO>) {
    // create activities for quick ride
    createQuickRideTemplateActivity(
        provider = provider,
        title = "Check/fix tyre pressure",
        description = "Check pressure on both tyres, and inflate if necessary.",
    )
    createQuickRideTemplateActivity(
        provider = provider,
        title = "Brake knock test",
        description = "Pull both brakes and rock bike back and forth to test for knocks",
    )
    createQuickRideTemplateActivity(
        provider = provider,
        title = "Check axles",
        description = "Check that both axles are properly installed and torqued",
    )
    createQuickRideTemplateActivity(
        provider = provider,
        title = "Check rear derailleur",
        description = "Check that the rear mech is securely attached and not bent or damaged",
    )
    createQuickRideTemplateActivity(
        provider = provider,
        title = "Check dropper post",
        description = "Check that dropper post moves up, down, and stays where it should",
    )
    createQuickRideTemplateActivity(
        provider = provider,
        title = "Chain lubed",
        description = "Check that chain is lubed, and re-apply if needed",
    )
    createQuickRideTemplateActivity(
        provider = provider,
        title = "Check gears",
        description = "Check that gears are indexed properly and all gears can be used",
    )
    createQuickRideTemplateActivity(
        provider = provider,
        title = "Charge battery",
        description = "Check battery level and charge if needed",
        isEBikeSpecific = true
    )
    createQuickRideTemplateActivity(
        provider = provider,
        title = "Battery properly installed",
        description = "Make sure that battery and cover are securely mounted and locked",
        isEBikeSpecific = true
    )
    createQuickRideTemplateActivity(
        provider = provider,
        title = "Electronics OK",
        description = "Check bike computer works, and all ride levels function",
        isEBikeSpecific = true
    )

    // create activities for day out
    createDayOutTemplateActivity(
        provider = provider,
        title = "Shifter / cable work smoothly",
        description = "Check that shifter and cable work smoothly, replace if needed",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check pedals",
        description = "Pedals secure with no play",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check chainring",
        description = "Chainring bolts or lockring torqued",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check cranks",
        description = "Crank bolts torqued",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check bottom bracket",
        description = "Turn pedals with bike on bike stand, listen and feel for grinding",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check tyre sealant",
        description = "Shake wheel and listen for liquid. If not, refill sealant",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check brake rotor securely attached",
        description = "Check that disc bolts or lockring are properly torqued",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check brake pads",
        description = "Check brake pads with wheel out, all round the pads, as they may wear unevenly and look OK from top but a disaster underneath",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check wheel bearings",
        description = "Do the wheels rotate freely, without grinding noises",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check freehub",
        description = "Check that freehub is working and does not make unusual noises",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check spokes",
        description = "Check that spoke tension is similar around the wheels",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check stem and faceplate bolts",
        description = "Make sure stem and faceplate bolts are properly torqued",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check headset bolts",
        description = "Make sure headset bolts are properly torqued",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check cockpit",
        description = "Check that all levers and shifters are secured",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check dropper lever",
        description = "Dropper lever works smoothly",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check whether brakes need bleeding",
        description = "Check brakes don't need to be ‘pumped’ to stop them pulling to the bars",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check saddle bolts",
        description = "Saddle bolts torqued",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check seatpost bolts",
        description = "Seatpost bolts torqued",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check dropper",
        description = "Seatpost works properly (up, down, stays in position)",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check suspension works",
        description = "moves freely and correctly pressured",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check suspension is clean",
        description = "Not weeping oil from anywhere",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check suspension is quiet",
        description = "No squelching sounds",
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Lubricate suspension",
        description = "Lubricate stanchions"
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "No noises from drive",
        description = "Drive is smooth with no noises or clunks or grinding",
        isEBikeSpecific = true
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Charge extra battery",
        description = "Extra battery charged and ready",
        isEBikeSpecific = true
    )

    createDayOutTemplateActivity(
        provider = provider,
        title = "Check motor bolts",
        description = "Motor bolts secure",
        isEBikeSpecific = true
    )


    // create template activities for holidays
    createHolidaysTemplateActivity(
        provider = provider,
        title = "Wheel bearings rotate smoothly",
        description = "Check whether wheel bearings are turning smoothly, replace if needed",
    )
    createHolidaysTemplateActivity(
        provider = provider,
        title = "Rims are fine",
        description = "Check rims for cracks or bad dents - if has, replace before holiday",
    )
    createHolidaysTemplateActivity(
        provider = provider,
        title = "Tyres OK",
        description = "Tyres not over worn or with knobs missing",
    )
    createHolidaysTemplateActivity(
        provider = provider,
        title = "Grips OK",
        description = "Grips no over worn",
    )
    createHolidaysTemplateActivity(
        provider = provider,
        title = "Pivot bearings smooth",
        description = "Pivot bearings smooth and travel freely, and greased",
    )
    createHolidaysTemplateActivity(
        provider = provider,
        title = "Headset bearings smooth",
        description = "Headset bearings smooth and greased",
    )
    createHolidaysTemplateActivity(
        provider = provider,
        title = "BB bearings smooth",
        description = "BB bearings smooth and greased",
    )
    createHolidaysTemplateActivity(
        provider = provider,
        title = "Chain is not too worn",
        description = "Chain not beyond .5 (if its, replace whole drivetrain before holiday - chain, cassette, chainring)",
    )
    createHolidaysTemplateActivity(
        provider = provider,
        title = "Service Suspension",
        description = "Suspension serviced (could be just a lowers/air can service, but better a full rebuild from reputable source e.g. TFT, J-tech, silverfish (fox) etc)",
    )
    createHolidaysTemplateActivity(
        provider = provider,
        title = "Bleed brakes",
        description = "Bleed brakes",
    )
    createHolidaysTemplateActivity(
        provider = provider,
        title = "Lube caliper pistons",
        description = "Lube caliper pistons",
    )

    createHolidaysTemplateActivity(
        provider = provider,
        title = "Use warranty now",
        description = "Issues worth warrantying before holiday?",
        isEBikeSpecific = true
    )

    createHolidaysTemplateActivity(
        provider = provider,
        title = "Consider rebuilding motor/bearings",
        description = "If out of warranty, consider rebuilding motor/bearings from places like ebike motor centre",
        isEBikeSpecific = true
    )

    createHolidaysTemplateActivity(
        provider = provider,
        title = "Take extra battery",
        description = "Extra battery (or does currently battery have good enough condition for big days)",
        isEBikeSpecific = true
    )

    createHolidaysTemplateActivity(
        provider = provider,
        title = "Check electric cables",
        description = "Electric cables all with smooth run and no kinks",
        isEBikeSpecific = true
    )


}

suspend fun createQuickRideTemplateActivity(
    provider: Provider<KJsDAO>,
    title: String,
    description: String,
    isEBikeSpecific: Boolean = false
) {
    val templateActivity = TemplateActivity(
        rideLevel = RideLevel.getRideLevelQuickRide(),
        title = title,
        description = description,
        isEBikeSpecific = isEBikeSpecific,
    )
    provider.get().insertTemplateActivity(templateActivity)
}

suspend fun createDayOutTemplateActivity(
    provider: Provider<KJsDAO>,
    title: String,
    description: String,
    isEBikeSpecific: Boolean = false
) {
    val templateActivity = TemplateActivity(
        rideLevel = RideLevel.getRideLevelDayOut(),
        title = title,
        description = description,
        isEBikeSpecific = isEBikeSpecific,
    )
    provider.get().insertTemplateActivity(templateActivity)
}

suspend fun createHolidaysTemplateActivity(
    provider: Provider<KJsDAO>,
    title: String,
    description: String,
    isEBikeSpecific: Boolean = false
) {
    val templateActivity = TemplateActivity(
        rideLevel = RideLevel.getRideLevelHolidays(),
        title = title,
        description = description,
        isEBikeSpecific = isEBikeSpecific,
    )
    provider.get().insertTemplateActivity(templateActivity)
}
