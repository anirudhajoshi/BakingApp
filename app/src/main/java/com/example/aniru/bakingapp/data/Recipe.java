package com.example.aniru.bakingapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by aniru on 8/10/2017.
 */

public class Recipe implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private ArrayList<Ingredient> ingredients = null;
    @SerializedName("steps")
    @Expose
    private ArrayList<Step> steps = null;
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @SerializedName("image")
    @Expose
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    protected Recipe(Parcel in) {
        setId(in.readInt());
        setName(in.readString());

/*        setIngredients( in.createTypedArrayList(Ingredient.CREATOR) );
        setSteps( in.createTypedArrayList(Step.CREATOR) );*/
        ingredients = new ArrayList<Ingredient>() {
        };
        in.readTypedList(ingredients, Ingredient.CREATOR);
        setIngredients(ingredients);

        steps = new ArrayList<Step>();
        in.readTypedList(steps, Step.CREATOR);
        setSteps(steps);

        setServings(in.readInt());
        setImage(in.readString());
    }

    public Recipe() {
    }

    public static final Parcelable.Creator<Recipe> CREATOR =
            new Parcelable.Creator<Recipe>() {
                public Recipe createFromParcel(Parcel source) {
                    return new Recipe(source);
                }

                public Recipe[] newArray(int size) {
                    return new Recipe[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(getName());
        parcel.writeTypedList(getIngredients());
        parcel.writeTypedList(getSteps());
        parcel.writeInt(getServings());
        parcel.writeString(getImage());
    }
}
