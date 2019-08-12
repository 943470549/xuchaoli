package com.mybatis;

import com.zzy.core.corouting.KotlinUtil;
import kotlin.Function;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.GlobalScope;
import org.jetbrains.annotations.NotNull;

public class KotlinHelper {

    public static <T> T sync(Function0<T> function0) {
        return KotlinUtil.INSTANCE.sync(function0);
    }
}
