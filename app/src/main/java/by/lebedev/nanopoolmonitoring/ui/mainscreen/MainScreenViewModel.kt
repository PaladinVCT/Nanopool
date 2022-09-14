package by.lebedev.nanopoolmonitoring.ui.mainscreen

import by.lebedev.nanopoolmonitoring.ui.NanopoolViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class MainScreenViewModel @AssistedInject constructor(
    @Assisted initialState: MainScreenViewState
) : NanopoolViewModel<MainScreenViewState>(initialState) {

}