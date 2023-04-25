package com.oceantech.monitorsaude;

import android.app.Application;

import org.jetbrains.annotations.NotNull;

public class MyApplication extends Application {
    @NotNull
    public final MedicamentoRepository medicamentoRepository;

    public MyApplication(@NotNull MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }
}
