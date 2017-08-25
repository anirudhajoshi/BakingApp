package com.example.aniru.bakingapp.data;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by aniru on 8/10/2017.
 */

public interface RecipesInterface {

    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipes();
}
