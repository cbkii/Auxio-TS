# TS18 launcher integration

Current integration follows [Architecture](../../ARCHITECTURE.md):

1. **Track A:** direct Android/Topway compatibility inside the maintained `com.tw.media` app.
2. **Track B:** no module exists; a future DoFun-private adapter requires a separate evidence-backed architecture decision.
3. **Track C:** optional, separate LSPosed relay for genuine stock `com.tw.music` only when device evidence proves it is required.

Current references:

- [DoFun/Topway compatibility](../../DOFUN_VARIETY_COMPATIBILITY.md)
- [Topway command-service boundary](TOPWAY_COMMAND_SERVICE_BRIDGE.md)
- [Topway widget contract](TOPWAY_MUSIC_WIDGET_CONTRACT.md)
- [Optional LSPosed Track-C bridge](LSPOSED_API100_BRIDGE.md)
- [Physical validation matrix](VALIDATION_MATRIX.md)

Collect physical evidence with `scripts/evidence/collect-ts18-launcher-media-integration.sh` only for a defined test window. Old comprehensive implementation prompts/plans are non-normative and were removed once the architecture consolidated.
