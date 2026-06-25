# t-music reference snapshot for Auxio-TS

This directory is a read-only reference snapshot of `cbkii/t-music` for Auxio-TS development.

Purpose:

- inspect startup, playback, service, broadcast, widget, path, and lifecycle behaviour;
- adapt normal-app-safe ideas into Auxio-TS;
- compare behaviour against TS18/DoFun launcher expectations.

Rules:

- do not edit this reference as part of Auxio-TS implementation work;
- do not copy privileged/vendor integration requirements into Auxio-TS;
- do not add platform signing, shared UID, Magisk, system-app, private Topway API, or firmware assumptions;
- use this only as evidence for simpler, faster, normal-app-safe Auxio-TS behaviour.
