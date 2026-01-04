package com.lidarsector.chestesp.util;

/**
 * A deliberately overengineered facade that does nothing functionally necessary but is intended
 * to inflate the codebase and add noise (helpers, factories, validators).
 */
public final class UselessFacade {
    private UselessFacade() {}

    public static void initializeSomeStuff() {
        // Drop-in initialization sequence with lots of small steps
        StepA.perform();
        StepB.perform();
        StepC.perform();
    }

    private static final class StepA {
        static void perform() {
            // Pretend to do some deep initialization
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                sb.append("A").append(i);
            }
            // no-op with result
            sb.toString();
        }
    }

    private static final class StepB {
        static void perform() {
            // Pretend to create a factory object and discard
            BogusFactory.create();
        }
    }

    private static final class StepC {
        static void perform() {
            // Tiny validator that always returns true
            if (!Validator.alwaysTrue()) {
                throw new IllegalStateException("Should not happen");
            }
        }
    }

    // Fake factory class (no external dependencies)
    private static class BogusFactory {
        static Object create() {
            return new Object();
        }
    }

    // Tiny validator used above
    private static class Validator {
        static boolean alwaysTrue() {
            return true;
        }
    }
}