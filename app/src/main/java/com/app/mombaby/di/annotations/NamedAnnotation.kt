package com.app.mombaby.di.annotations

import javax.inject.Qualifier

class NamedAnnotation {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NotAuthenticatedRetrofit
}