package by.lebedev.nanopoolmonitoring.di

import by.lebedev.nanopoolmonitoring.databinding.FragmentWidgetWalletsBinding
import by.lebedev.nanopoolmonitoring.di.mvrx.AssistedViewModelFactory
import by.lebedev.nanopoolmonitoring.di.mvrx.MavericksViewModelComponent
import by.lebedev.nanopoolmonitoring.di.mvrx.ViewModelKey
import by.lebedev.nanopoolmonitoring.ui.addwallet.AddWalletViewModel
import by.lebedev.nanopoolmonitoring.ui.coins.CoinsViewModel
import by.lebedev.nanopoolmonitoring.ui.dashboard.DashboardViewModel
import by.lebedev.nanopoolmonitoring.ui.earnings.EarningsViewModel
import by.lebedev.nanopoolmonitoring.ui.editwallet.EditWalletViewModel
import by.lebedev.nanopoolmonitoring.ui.options.OptionsViewModel
import by.lebedev.nanopoolmonitoring.ui.startscreen.StartScreenViewModel
import by.lebedev.nanopoolmonitoring.ui.widget.configure.ConfigureWidgetViewModel
import by.lebedev.nanopoolmonitoring.ui.workers.WorkersViewModel
import by.lebedev.nanopoolmonitoring.ui.workers.details.WorkerDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

@Module
@InstallIn(MavericksViewModelComponent::class)
interface ViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(AddWalletViewModel::class)
    fun addViewModelFactory(factory: AddWalletViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(EditWalletViewModel::class)
    fun editViewModelFactory(factory: EditWalletViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(StartScreenViewModel::class)
    fun startScreenViewModelFactory(factory: StartScreenViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    fun dashboardViewModelFactory(factory: DashboardViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(WorkersViewModel::class)
    fun workersViewModelFactory(factory: WorkersViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(WorkerDetailsViewModel::class)
    fun workerDetailsViewModelFactory(factory: WorkerDetailsViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(EarningsViewModel::class)
    fun earningsViewModelFactory(factory: EarningsViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(CoinsViewModel::class)
    fun coinsViewModelFactory(factory: CoinsViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(OptionsViewModel::class)
    fun optionsViewModelFactory(factory: OptionsViewModel.Factory): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(ConfigureWidgetViewModel::class)
    fun configureWidgetViewModelFactory(factory: ConfigureWidgetViewModel.Factory): AssistedViewModelFactory<*, *>


}