with open("app/src/main/java/org/oxycblt/auxio/playback/service/ExoPlaybackStateHolder.kt", "r") as f:
    content = f.read()

old_block = """    override fun shuffled(shuffled: Boolean) {
        player.setShuffleModeEnabled(shuffled)
        if (player.shuffleModeEnabled) {
            // Have to manually refresh the shuffle seed and anchor it to the new current songs
            player.setShuffleOrder(
                BetterShuffleOrder(player.mediaItemCount, player.currentMediaItemIndex)
            )
        }
        playbackManager.ack(this, StateAck.QueueReordered)
        deferSave()
    }"""

new_block = """    override fun shuffled(shuffled: Boolean) {
        val wasPlaying = player.playWhenReady || player.isPlaying
        val currentPos = player.currentPosition
        val currentIdx = player.currentMediaItemIndex
        player.setShuffleModeEnabled(shuffled)
        if (player.shuffleModeEnabled) {
            // Have to manually refresh the shuffle seed and anchor it to the new current songs
            player.setShuffleOrder(
                BetterShuffleOrder(player.mediaItemCount, currentIdx)
            )
        }
        player.seekTo(currentIdx, currentPos)
        if (wasPlaying && !player.playWhenReady) {
            player.play()
        } else if (!wasPlaying && player.playWhenReady) {
            player.pause()
        }
        playbackManager.ack(this, StateAck.QueueReordered)
        deferSave()
    }"""

content = content.replace(old_block, new_block)

with open("app/src/main/java/org/oxycblt/auxio/playback/service/ExoPlaybackStateHolder.kt", "w") as f:
    f.write(content)
