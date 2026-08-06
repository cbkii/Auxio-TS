package org.oxycblt.auxio.ts18bridge;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import android.media.session.PlaybackState;

public class MediaMirrorCommandTest {
    @Test
    public void testRequiredActionFor() {
        assertEquals(PlaybackState.ACTION_SKIP_TO_PREVIOUS, MediaMirror.requiredActionFor(BridgeCommand.PREVIOUS, false));
    }
}
