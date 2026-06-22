package org.oxycblt.auxio.headunit.root

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RootModule {
    @Provides @Singleton fun provideRootStateHolder(): RootStateHolder = RootStateHolder()
}
