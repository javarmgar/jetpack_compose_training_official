package com.example.composetraining.ui.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/*
Creating your own CompositionLocal

- Another key signal for using CompositionLocal is when the parameter is cross-cutting
- and intermediate layers of implementation should not be aware it exists,

    For example,
        - querying for Android permissions is afforded by a CompositionLocal under the hood.
 */

/*
Deciding whether to use CompositionLocal

A CompositionLocal should have a good default value.
If there's no default value,
    - you must guarantee that it is exceedingly difficult for a developer to get into a situation where a
     value for the CompositionLocal isn't provided.
    - Not providing a default value can cause problemsand frustration
        when:
         - creating tests or
         - previewing a composable that uses that

Avoid CompositionLocal for
    - concepts that aren't thought as
        - tree-scoped or
        - sub-hierarchy scoped.
    - A CompositionLocal makes sense when it can be potentially used by any descendant, not by a few of them.


 */
data class Elevations(
    val card:Dp = 0.dp,
    val default:Dp = 0.dp
)
object StandardElevations{
    val XS = Elevations(card = 2.dp, default = 2.dp)
    val S  = Elevations(card = 4.dp, default = 4.dp)
    val M  = Elevations(card = 6.dp, default = 6.dp)
    val L  = Elevations(card = 8.dp, default = 8.dp)
    val XL = Elevations(card = 10.dp, default = 10.dp)
}
/*
There are two APIs to create a CompositionLocal:
    - compositionLocalOf:
        - Changing the value provided during recomposition invalidates only the content that reads its current value.
    - staticCompositionLocalOf:
        - Unlike compositionLocalOf, reads of a staticCompositionLocalOf are not tracked by Compose.
        - Changing the value causes the entirety of the content lambda where the CompositionLocal is provided to be recomposed,
        - instead of just the places where the current value is read in the Composition.

- If the value provided to the CompositionLocal is highly unlikely to change or will never change,
 use staticCompositionLocalOf to get performance benefits.


 */
val LocalElevations: ProvidableCompositionLocal<Elevations> = compositionLocalOf { Elevations() }