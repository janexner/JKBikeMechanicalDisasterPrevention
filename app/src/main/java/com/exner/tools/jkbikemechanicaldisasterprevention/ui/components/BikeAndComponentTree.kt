package com.exner.tools.jkbikemechanicaldisasterprevention.ui.components

import com.exner.tools.jkbikemechanicaldisasterprevention.database.KJsRepository
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Bike
import com.exner.tools.jkbikemechanicaldisasterprevention.database.entities.Component

data class RootNode(
    var bikes: List<BikeNode>,
)

data class BikeNode(
    val bike: Bike,
    val attachedComponents: List<ComponentNode>,
)

data class ComponentNode(
    val component: Component,
    val attachedComponents: List<ComponentNode>,
)

suspend fun createBikeAndComponentTree(
    repository: KJsRepository
): RootNode {
    val listOfBikeNodes: MutableList<BikeNode> = mutableListOf()
    val bikes: List<Bike> = repository.getAllBikes()
    bikes.forEach { bike ->
        val componentsForBike = getComponentTreeForBike(bike, repository)
        val bikeNode = BikeNode(
            bike = bike,
            attachedComponents = componentsForBike
        )
        listOfBikeNodes.add(bikeNode)
    }
    return RootNode(listOfBikeNodes)
}

suspend fun getComponentTreeForBike(
    bike: Bike,
    repository: KJsRepository
): List<ComponentNode> {
    val componentsForBike: MutableList<ComponentNode> = mutableListOf()
    val flatComponentsForBike = repository.getComponentsForBike(bike.uid)

    flatComponentsForBike.forEach { component ->
        if (component.parentComponentUid == null) {
            // a top-level component, so let's add it to the list!
            val componentNode = ComponentNode(
                component = component,
                attachedComponents = getSubComponentTreeForComponent(
                    component,
                    flatComponentsForBike
                )
            )
            componentsForBike.add(componentNode)
        }
    }
    return componentsForBike
}

fun getSubComponentTreeForComponent(
    component: Component,
    flatListOfComponents: List<Component>
): List<ComponentNode> {
    val listOfComponentNode: MutableList<ComponentNode> = mutableListOf()

    flatListOfComponents.forEach { comp ->
        if (comp.parentComponentUid == component.uid) {
            // add this one to the list of "children"
            val componentNode = ComponentNode(
                component = comp,
                attachedComponents = getSubComponentTreeForComponent(comp, flatListOfComponents)
            )
            listOfComponentNode.add(componentNode)
        }
    }
    return listOfComponentNode
}

//

fun RootNode.bikeAndComponentTreeToListOfString(): List<String> {
    val result: MutableList<String> = mutableListOf()
    if (this.bikes.isEmpty()) {
        result.add("Empty, no bikes")
    } else {
        this.bikes.forEach { bikeNode ->
            result.add("Bike ${bikeNode.bike.name}")
            result.addAll(componentAndSubComponentsToListOfString(bikeNode.attachedComponents, 1))
        }
    }
    return result
}

fun componentAndSubComponentsToListOfString(
    componentNodes: List<ComponentNode>,
    level: Int
): List<String> {
    val result: MutableList<String> = mutableListOf()
    val spacer = " ".repeat(level * 2)
    if (componentNodes.isNotEmpty()) {
        componentNodes.forEach { componentNode ->
            result.add("$spacer- ${componentNode.component.name}")
            result.addAll(
                componentAndSubComponentsToListOfString(
                    componentNode.attachedComponents,
                    level + 1
                )
            )
        }
    }
    return result
}

//

data class BikeOrComponent(
    val bike: Bike?,
    val component: Component?,
    val level: Int,
    val hasChildren: Boolean,
    val collapseId: String,
    val collapseIdTags: List<String>,
) {
    fun isBike(): Boolean {
        return (bike != null)
    }

    fun isComponent(): Boolean {
        return (bike == null)
    }
}

fun RootNode.flattenWithIndent(): List<BikeOrComponent> {
    val result: MutableList<BikeOrComponent> = mutableListOf()
    if (this.bikes.isNotEmpty()) {
        this.bikes.forEach { bikeNode ->
            val collapseId = "bike-${bikeNode.bike.uid}"
            val collapseIdTags: List<String> = emptyList()
            result.add(
                BikeOrComponent(
                    bike = bikeNode.bike,
                    component = null,
                    level = 0,
                    hasChildren = bikeNode.attachedComponents.isNotEmpty(),
                    collapseId = collapseId,
                    collapseIdTags = collapseIdTags
                )
            )
            val newCollapseIdTags: MutableList<String> = collapseIdTags.toMutableList()
            newCollapseIdTags.add(collapseId)
            result.addAll(flattenComponentAndSubComponents(bikeNode.attachedComponents, 1, newCollapseIdTags))
        }
    }
    return result
}

fun flattenComponentAndSubComponents(
    componentNodes: List<ComponentNode>,
    level: Int,
    collapseIdTags: List<String>,
): List<BikeOrComponent> {
    val result: MutableList<BikeOrComponent> = mutableListOf()
    if (componentNodes.isNotEmpty()) {
        componentNodes.forEach { componentNode ->
            val collapseId = "component-${componentNode.component.uid}"
            result.add(BikeOrComponent(
                bike = null,
                component = componentNode.component,
                level,
                hasChildren = componentNode.attachedComponents.isNotEmpty(),
                collapseId = collapseId,
                collapseIdTags = collapseIdTags
            ))
            val newCollapseIdTags: MutableList<String> = collapseIdTags.toMutableList()
            newCollapseIdTags.add(collapseId)
            result.addAll(
                flattenComponentAndSubComponents(
                    componentNodes = componentNode.attachedComponents,
                    level = level + 1,
                    collapseIdTags = newCollapseIdTags,
                )
            )
        }
    }
    return result
}
