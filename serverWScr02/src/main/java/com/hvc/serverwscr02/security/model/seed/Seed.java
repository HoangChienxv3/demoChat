package com.hvc.serverwscr02.security.model.seed;

public interface Seed {

    Seed[] seeds = new Seed[]{

    };

    void seed();

    static void execute() {
        for (Seed seed : seeds) {
            seed.seed();
        }
    }
}
