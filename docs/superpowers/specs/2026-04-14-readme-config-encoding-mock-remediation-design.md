# README, Config Security, Encoding, and Mock Data Remediation Design

## Goal

Bring the repository closer to normal production standards by aligning documentation with actual code, removing committed secrets from tracked config, fixing critical user-facing encoding issues, and eliminating silent mock-data fallbacks that can misrepresent real system state.

## Scope

This remediation covers four areas only:

1. `README` accuracy and readability
2. Sensitive configuration handling
3. Critical encoding cleanup in touched, user-visible files
4. Removal of silent mock-data fallback behavior from production-facing flows

This remediation does not attempt a full architectural cleanup, a complete repository-wide encoding migration, or a full frontend rewrite.

## Current Problems

### 1. Documentation drift

The repository documentation does not match runtime reality:

- `README` content includes visible mojibake
- documented Java version and runtime port do not match the actual project files
- runtime entry behavior is not clearly described
- the distinction between real data flows and demo-oriented pages is unclear

### 2. Sensitive configuration committed to source

Tracked configuration currently includes real-looking credentials or keys in `application.yml`, including:

- MySQL URL/user/password
- AMap key-related values

Even if these values are no longer valid, keeping secrets in tracked config is below normal security expectations.

### 3. Encoding inconsistency

The codebase shows mixed or corrupted Chinese text in multiple places. Some of it appears in comments only, but some appears in user-visible content such as:

- page titles
- alerts
- inline labels
- README text

The immediate risk is user-facing unreadable text and harder maintenance.

### 4. Mock-data fallback hides real failures

Several frontend and backend paths silently substitute simulated data when APIs fail. This is useful for demos, but it is unsafe for a repository positioned as a monitoring platform because:

- users cannot distinguish real data from fake data
- integration bugs get masked
- empty-state and error-state handling never gets exercised
- operators may trust fabricated data

## Design Principles

### Real data over graceful deception

The system must prefer truth over continuity. If real data is unavailable, the UI should show an empty state or a clear error state rather than synthetic data that looks real.

### Explicit degradation

Fallback behavior should be visible and intentional. If a page cannot load real data, it should state that directly through a toast, inline message, or empty panel state.

### Minimal, bounded remediation

We will only touch files necessary to solve the four requested priorities. We will not use this task to opportunistically refactor unrelated modules.

### Safe coexistence with existing user edits

The worktree is already dirty. Changes must be layered on top of current edits without reverting or overwriting unrelated user modifications.

## Proposed Changes

### A. Rewrite and normalize `README`

Rewrite `README.md` as UTF-8 text and make it reflect actual repository behavior:

- actual Java version from `pom.xml`
- actual port/config defaults from `application.yml`
- backend module overview
- static page entry points
- note that some pages still contain demo-oriented frontend logic today, and that this remediation is reducing misleading fallback behavior
- basic local startup instructions using environment variables for secrets

The README should be concise, accurate, and usable by a new maintainer.

### B. Externalize sensitive values from tracked config

Replace committed credential values in `src/main/resources/application.yml` with environment-backed placeholders and safe defaults where appropriate.

Proposed pattern:

- database host/name may remain as local defaults if they are not sensitive
- username/password should come from env vars
- external keys should come from env vars

Also add a sample config artifact for local development guidance, without embedding real credentials.

The runtime goal is:

- app still boots when env vars are provided
- tracked source no longer exposes sensitive values

### C. Fix critical user-visible encoding in touched files

Target only files we are actively improving and files users directly read:

- `README.md`
- `application.yml` comments if touched
- key user-visible titles/messages in `waterlevel.html`
- key user-visible titles/messages in `prediction.html`
- any remediation-related frontend strings we modify

We will not attempt a full sweep across every historical comment or long static page. That larger migration should be a separate task.

### D. Remove silent mock-data behavior from production-facing flows

#### `waterlevel`

Backend controllers currently return simulated data on exception. That behavior will be removed.

Expected replacement behavior:

- return normal success only for real service results
- return explicit error response on failure
- frontend handles empty/error states instead of auto-filling simulated values

Frontend changes:

- remove or disable automatic simulated replacement for station/warning/water level API failure
- show a visible warning/toast when data cannot be loaded
- keep charts and panels empty or partially empty rather than fabricating values

#### `prediction`

Frontend currently falls back to generated station/warning/history/prediction data when API calls fail.

Expected replacement behavior:

- failed loads produce explicit warnings
- no mock stations or mock predictions are injected into the page
- chart areas render empty state or no-data state when required

We will preserve presentational chart setup but not synthetic business data.

### E. Preserve pages that are intentionally static-only if they are not pretending to be live APIs

The `index` page still contains some demo chart generation. Since the requested priority is mock-data mixing, we will distinguish between:

- pages that directly call backend APIs and then silently fabricate fallback data
- pages that are currently static showcase screens without pretending a failed API returned real values

This remediation will prioritize removing deceptive fallback in API-backed pages first, especially `waterlevel` and `prediction`.

If `index` contains an API-backed section with silent fallback that looks real, that specific section may be cleaned up, but we will not convert the whole page into a fully data-driven dashboard in this task.

## File-Level Intent

### Documentation and config

- `README.md`
  Rewrite with accurate, UTF-8 documentation.

- `src/main/resources/application.yml`
  Replace committed secrets with env-backed placeholders and clean up touched comments if needed.

- `src/main/resources/application-example.yml` or similar sample file
  Provide a safe reference for local setup.

### Water level module

- `src/main/java/com/baluga/module/waterlevel/controller/StationController.java`
- `src/main/java/com/baluga/module/waterlevel/controller/WaterLevelController.java`
- `src/main/java/com/baluga/module/waterlevel/controller/WarningController.java`

Remove simulated success fallback behavior from backend endpoints.

- `src/main/resources/static/waterlevel/waterlevel.html`
  Remove misleading simulated data fallback in API-backed flows, add explicit no-data/error handling, and fix touched user-facing text.

### Prediction module

- `src/main/resources/static/prediction/prediction.html`
  Remove mock fallback behavior in API-backed flows, add explicit error/no-data handling, and fix touched user-facing text.

## Error Handling Design

### Backend

For endpoints that currently swallow exceptions and return fake success:

- keep response shape stable where possible
- return error responses using the module’s existing `Result` wrapper
- do not manufacture substitute entities

### Frontend

When API calls fail:

- show user-visible warning text
- retain empty arrays or empty objects
- render empty charts or empty lists safely
- never synthesize replacement stations, alerts, or predictions

## Testing and Verification Strategy

Verification should focus on evidence, not assumption:

1. Spring Boot test suite must still load the application context
2. the project should compile after config/documentation changes
3. pages with removed mock fallback should still load without JavaScript crashes when APIs fail or return empty data
4. touched config should no longer contain real secret literals

Given the repository shape, automated coverage will likely be limited. Manual verification of changed frontend failure paths may still be necessary.

## Risks

### Risk 1: Some pages may appear emptier after remediation

This is expected and acceptable. It is a more truthful representation than fake operational data.

### Risk 2: Existing demos may rely on silent fallback

Removing silent mock behavior can expose missing backend data or API mismatches. This is valuable because it reveals integration gaps instead of hiding them.

### Risk 3: Encoding cleanup can create noisy diffs

To control this, the remediation is intentionally limited to touched and user-visible files rather than broad repository-wide normalization.

## Out of Scope

- full repository-wide Chinese text repair
- response model unification across modules
- replacing all inline HTML scripts with modular frontend code
- broad CORS/config refactoring
- redesigning `index` into a fully live data dashboard

## Recommended Implementation Order

1. secure tracked config and add sample config
2. rewrite README to match reality
3. remove backend mock-success fallbacks in `waterlevel`
4. remove frontend mock fallbacks in `waterlevel`
5. remove frontend mock fallbacks in `prediction`
6. fix touched user-facing encoding while editing those files
7. run verification and document any remaining known limitations
