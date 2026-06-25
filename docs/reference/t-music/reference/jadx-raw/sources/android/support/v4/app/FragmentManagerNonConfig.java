package android.support.v4.app;

import android.arch.lifecycle.C0028q;
import java.util.List;

/* loaded from: classes3.dex */
public class FragmentManagerNonConfig {
    private final List<FragmentManagerNonConfig> mChildNonConfigs;
    private final List<Fragment> mFragments;
    private final List<C0028q> mViewModelStores;

    FragmentManagerNonConfig(List<Fragment> list, List<FragmentManagerNonConfig> list2, List<C0028q> list3) {
        this.mFragments = list;
        this.mChildNonConfigs = list2;
        this.mViewModelStores = list3;
    }

    List<FragmentManagerNonConfig> getChildNonConfigs() {
        return this.mChildNonConfigs;
    }

    List<Fragment> getFragments() {
        return this.mFragments;
    }

    List<C0028q> getViewModelStores() {
        return this.mViewModelStores;
    }
}
