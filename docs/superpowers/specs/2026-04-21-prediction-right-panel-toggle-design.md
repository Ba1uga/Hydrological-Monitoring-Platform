# Prediction Right Panel Toggle Design

## Summary

Rework the prediction page's two small right-side chart cards into a single larger presentation card. The new module will default to the pollution ranking chart, expose a visible segmented toggle for switching between the two chart views, and use motion-forward transitions suited to a demo/reporting environment.

## Current State

The right panel currently renders:

- a small `污染指标排行` card with `rightChart1Container`
- a small `站点状态分布` card with `rightChart2Container`
- the radar card and warning card below

This creates two problems for a presentation-heavy workflow:

- the two charts compete for attention in a cramped vertical stack
- there is no strong focal surface for switching between complementary summary views

## Approved Direction

Use a single unified chart module with a stronger "tech showcase" feel:

- Default view: `污染指标排行`
- Secondary view: `站点状态分布`
- Visual tone: futuristic, high-contrast, presentation-oriented
- Interaction: obvious toggle control with animated chart-view switching
- Scope: only the prediction page right-side summary area; radar and warning modules remain separate

## UI Structure

Replace the current first two right-panel cards with one new top card:

1. Card header
   - Left: a single module title for the combined summary area
   - Right: a segmented toggle with two options:
     - `污染指标排行`
     - `站点状态分布`

2. Card stage
   - One larger inner chart stage styled as a focused data display surface
   - Both chart containers remain available, but only one is visually active at a time

3. Card decoration
   - Active toggle gets a brighter fill, glow edge, and subtle sweep highlight
   - The chart stage gets stronger framing than the surrounding cards so it reads like a demo spotlight region

## Motion Behavior

The interaction should feel more presentational than utilitarian:

- Toggle press animates the pill background and active label state
- Outgoing chart slightly shifts and fades
- Incoming chart slides in and fades up
- Transition duration should stay short enough for dashboard use, roughly `220-320ms`
- Motion must degrade safely for repeated rapid toggles with no stuck states

No auto-rotation is included in this phase.

## Technical Approach

Implementation should minimize data-risk by reusing existing chart logic:

- Keep the existing ECharts update flow inside `updateRightCharts(station)`
- Preserve `rightChart1` and `rightChart2` as the two chart instances
- Refactor DOM so the two chart containers live inside one unified wrapper instead of separate stacked panels
- Add a small view-state controller to switch the active chart
- After each switch, call `resize()` on the newly visible chart instance so ECharts redraws cleanly

This approach avoids rewriting data generation while still enabling the new interaction model.

## Styling Notes

The combined card should visually stand apart from standard utility panels:

- stronger inner-stage contrast than surrounding cards
- larger chart viewing area than either current mini-card
- segmented control aligned with the page's dark, blue-cyan dashboard language
- no extra explanatory copy unless it materially helps presentation readability

The result should feel like one deliberate "summary theater" module rather than two ordinary stacked dashboard cards.

## Responsive Expectations

The unified module should continue to work in current responsive breakpoints:

- desktop: one large card at the top of the right panel
- narrower layouts: toggle remains readable and wraps safely if needed
- chart stage should preserve enough height to avoid cramped labels after the merge

## Verification Expectations

Implementation should verify:

- the new module defaults to `污染指标排行`
- clicking the toggle switches views reliably in both directions
- only one chart is active/visible at a time
- revealed chart resizes correctly after switching
- existing radar and warning modules still render below without layout regression

## Out of Scope

This change does not include:

- automatic timed chart cycling
- changes to radar/warning card behavior
- new backend data fields
- redesign of the left panel or forecast chart
