package org.untitled.phoenix;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.util.ResultsUtils;
import org.jetbrains.annotations.NotNull;
import org.untitled.phoenix.component.Component;

import java.util.UUID;
import java.util.function.Supplier;

public class DynamicStep {

    public static void invokeStep(@NotNull Component component, String actionName, Runnable runnable) {
        var aStepName = String.format("%s :: %s", actionName, component);

        var uuid = UUID.randomUUID().toString();
        StepResult result = new StepResult().setName(aStepName);
        Allure.getLifecycle().startStep(uuid, result);
        try {
             runnable.run();
            Allure.getLifecycle().updateStep(uuid, s -> s.setStatus(Status.PASSED));
        } catch (Throwable e) {
            Allure.getLifecycle().updateStep(uuid, s -> s
                    .setStatus(ResultsUtils.getStatus(e).orElse(Status.BROKEN))
                    .setStatusDetails(ResultsUtils.getStatusDetails(e).orElse(null)));
            throw e;
        } finally {
            Allure.getLifecycle().stopStep(uuid);
        }
    }

    public static <T> T invokeStep(@NotNull Component component, String actionName, Supplier<T> runnable) {
        var aStepName = String.format("%s :: %s", actionName, component);

        var uuid = UUID.randomUUID().toString();
        StepResult result = new StepResult().setName(aStepName);
        Allure.getLifecycle().startStep(uuid, result);
        try {
            final var value = runnable.get();
            Allure.getLifecycle().updateStep(uuid, s -> s.setStatus(Status.PASSED));
            return value;
        } catch (Throwable e) {
            Allure.getLifecycle().updateStep(uuid, s -> s
                    .setStatus(ResultsUtils.getStatus(e).orElse(Status.BROKEN))
                    .setStatusDetails(ResultsUtils.getStatusDetails(e).orElse(null)));
            throw e;
        } finally {
            Allure.getLifecycle().stopStep(uuid);
        }
    }
}
