package com.example.arthome.newexchangeworld.util;

import com.example.arthome.newexchangeworld.Constant;
import com.example.arthome.newexchangeworld.R;

/**
 * Created by arthome on 2016/10/30.
 */

public enum  CategoryTool {
    INSTANCE;
    public int getCategoryDrawableID(String category){
        switch (category) {
            case Constant.CATEGORY_BOOKS:
                return R.drawable.category_books;
            case Constant.CATEGORY_3C:
                return R.drawable.category_3c;
            case Constant.CATEGORY_3C_ACCESSORIES:
                return R.drawable.category_3c_accessories;
            case Constant.CATEGORY_ACCESSORIES:
                return R.drawable.category_accessories;
            case Constant.CATEGORY_CLOTHES:
                return R.drawable.category_clothes;
            case Constant.CATEGORY_COSMETIC:
                return R.drawable.category_cosmetic;
            case Constant.CATEGORY_FOOD:
                return R.drawable.category_food;
            case Constant.CATEGORY_GAMES:
                return R.drawable.category_games;
            case Constant.CATEGORY_HOUSEWARE:
                return R.drawable.category_houseware;
            case Constant.CATEGORY_OTHERS:
                return R.drawable.category_others;
            case Constant.CATEGORY_SPORTS:
                return R.drawable.category_sports;
            case Constant.CATEGORY_TEXTBOOKS:
                return R.drawable.category_textbooks;
        }
        return R.drawable.category_others;  //default
    }
}
