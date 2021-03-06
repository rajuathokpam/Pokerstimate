package com.itservz.android.pokerstimate;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.itservz.android.pokerstimate.core.Dealer;
import com.itservz.android.pokerstimate.model.CardStatus;
import com.itservz.android.pokerstimate.model.CardViewModel;
import com.itservz.android.pokerstimate.sensor.ShakeDetector;

import java.util.ArrayList;
import java.util.List;

public class CardsPagerAdapter extends FragmentStatePagerAdapter {
    private final Dealer dealer;
    private final List<CardFragment> fragmentList;
    private CardFragment.OnCardStatusChangeListener onCardFragmentStateChange;
    private Context context;

    public CardsPagerAdapter(Context context, FragmentManager fragmentManager, Dealer dealer) {
        super(fragmentManager);
        this.dealer = dealer;
        this.context = context;
        this.fragmentList = new ArrayList<>(dealer.getDeckLength());
        initializeListener();
        initializeFragmentPool();
    }

    @Override
    public int getCount() {
        return dealer.getDeckLength();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    private void initializeListener() {
        onCardFragmentStateChange = new CardFragment.OnCardStatusChangeListener() {
            @Override
            public void onCardStatusChange(Fragment fragment, CardViewModel card, CardStatus newStatus) {
                flip(newStatus);
            }
        };

        ShakeDetector.getInstance().setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count, CardStatus  cardStatus) {
                flip(cardStatus);
            }
        });
    }

    private void flip(CardStatus newStatus){
        for(int index = 0; index < fragmentList.size(); index++) {
            fragmentList.get(index).setCardStatus(newStatus);
            dealer.flipDeck();
        }
    }

    private void initializeFragmentPool() {
        int count = dealer.getDeckLength();
        for(int index = 0; index < count; index++) {
            CardViewModel cardViewModel = new CardViewModel(context, true, dealer.getCardAtPosition(index));
            cardViewModel.setStatus(dealer.getCardStatus());
            CardFragment fragment = new CardFragment();
            fragment.setCardViewModel(cardViewModel);
            fragment.setOnCardStatusChangeListener(onCardFragmentStateChange);
            fragmentList.add(fragment);
        }
    }

}
