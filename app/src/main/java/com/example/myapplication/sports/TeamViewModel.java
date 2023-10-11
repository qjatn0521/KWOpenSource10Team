package com.example.myapplication.sports;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.sports.data.ApiClient;
import com.example.myapplication.sports.model.Team;
import com.example.myapplication.sports.model.TeamResponse;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class TeamViewModel extends ViewModel {
    private final ApiClient apiClient = new ApiClient();
    private final CompositeDisposable disposable = new CompositeDisposable();

    private final MutableLiveData<List<Team>> teamsList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingTeamList = new MutableLiveData<>();

    public void getAllTeamsOfLeague(int leagueId) {
        loadingTeamList.setValue(true);
        Log.d("hi","thisis good");
        disposable.add(apiClient.getAllTeamsOfLeague(leagueId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<TeamResponse>() {
                    @Override
                    public void onSuccess(TeamResponse t) {
                        teamsList.setValue(t.getApi().getTeams());
                        loadingTeamList.setValue(false);
                        //Log.d("goooooooooooooooooood","thisis good");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Handle error
                        Log.d("noooooooooooooo",e.toString());
                    }
                }));
    }

    public MutableLiveData<List<Team>> getTeamsList() {
        return teamsList;
    }

    public MutableLiveData<Boolean> getLoadingTeamList() {
        return loadingTeamList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
