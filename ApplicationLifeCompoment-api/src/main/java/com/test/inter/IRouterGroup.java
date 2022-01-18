package com.test.inter;

import java.util.Map;

public interface IRouterGroup {
    Map<String,Class<? extends IRouterPath>> loadGroupMap();
}
