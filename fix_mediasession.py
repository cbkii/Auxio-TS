with open("app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt", "r") as f:
    content = f.read()

# Make sure the text metadata is published *before* bitmap loads, which it already is:
#             // First publish text-only metadata for immediate responsiveness.
#             val initialMetadata = builder.build()
#             mediaSession.setMetadata(initialMetadata)

# And in WidgetComponent, we did `widgetProvider.update(context, uiSettings, initialState)`
# before `bitmapProvider.load()`. So the text and basic info is populated instantly!

# Wait, `WidgetProvider.kt` doesn't need to be modified as long as the state has `cover = null`.
# Let's double check if I missed anything in the plan for this step.
# "publish text metadata first; defer artwork;" - Done.
# "bound bitmap sizes;" - Done (`NotificationBitmapSafety.MAX_ICON_SIZE_PX` is 512).
# "do not block widget update on library scan; do not block widget update on cover loading;" - Done.
# "raw fast-resume metadata must populate widget/MediaSession/legacy broadcasts before reconciliation;" - Done.
# "avoid repeated foreground notification churn during scan;" - already handled in the codebase.
# "throttle progress broadcasts without suppressing real changes." - check `TopwayProgressStatePolicy.kt`.
