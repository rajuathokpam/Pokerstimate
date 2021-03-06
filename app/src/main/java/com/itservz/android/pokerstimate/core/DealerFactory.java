package com.itservz.android.pokerstimate.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.itservz.android.pokerstimate.Preferences;
import com.itservz.android.pokerstimate.R;

import java.util.Arrays;

public class DealerFactory {

    private final Resources mResources;
    private SharedPreferences mPreferences;
    private static DealerFactory INSTANCE = null;

    private Dealer mStandardDealer;
    private Dealer mFibonacciDealer;
    private Dealer mTShirtDealer;

    public static Dealer newInstance(Context context) {
        if(INSTANCE == null){
            INSTANCE = new DealerFactory(context);
        }
        return INSTANCE.getDealer();
    }

    private DealerFactory(Context context) {
        mResources = context.getResources();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    private Dealer getDealer() {
        int deckPref = Integer.parseInt(mPreferences.getString(Preferences.DECK_PREFERENCE.name(), "0"));
        DeckType selectedType = DeckType.values()[deckPref];
        return buildDeck(selectedType);
    }

    private Dealer buildDeck(DeckType type) {
        switch (type) {
            case STANDARD:
                if(mStandardDealer == null) {
                    String[] standardCards = mResources.getStringArray(R.array.standard_cards);
                    mStandardDealer = new Dealer(Arrays.asList(standardCards));
                    mStandardDealer.setType(DeckType.STANDARD.name());
                }
                return mStandardDealer;
            case FIBONACCI:
                if(mFibonacciDealer == null) {
                    String[] fibonacciCards = mResources.getStringArray(R.array.fibonacci_cards);
                    mFibonacciDealer = new Dealer(Arrays.asList(fibonacciCards));
                    mFibonacciDealer.setType(DeckType.FIBONACCI.name());
                }
                return mFibonacciDealer;
            case TSHIRT_SIZE:
                if(mTShirtDealer == null) {
                    String[] tShirtCards = mResources.getStringArray(R.array.tshirt_size_cards);
                    mTShirtDealer = new Dealer(Arrays.asList(tShirtCards));
                    mTShirtDealer.setType(DeckType.TSHIRT_SIZE.name());
                }
                return mTShirtDealer;
            default:
                return null;
        }
    }
}
