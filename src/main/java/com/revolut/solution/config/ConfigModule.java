package com.revolut.solution.config;

import com.google.inject.AbstractModule;

import javax.sql.DataSource;

public class ConfigModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataSource.class).toProvider(H2DataSourceProvider.class);
    }
}
