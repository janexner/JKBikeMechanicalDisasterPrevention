package com.exner.tools.kjdoitnow.database

import com.exner.tools.kjdoitnow.database.entities.Bike
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
    provider.get().insertBike(taiga)

    // the Vado
    val vado = Bike(
        name = "Turbo Vado",
        buildDate = LocalDate.parse("2023-05-20"),
        mileage = 6500,
        lastUsedDate = nowDate,
        isElectric = true,
        uid = 0
    )
    provider.get().insertBike(vado)

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
