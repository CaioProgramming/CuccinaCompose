package com.ilustris.cuccina

import com.ilustris.cuccina.feature.recipe.domain.service.RecipeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun providesRecipeService() = RecipeService()


}