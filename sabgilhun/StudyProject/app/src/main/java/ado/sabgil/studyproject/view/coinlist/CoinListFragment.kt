package ado.sabgil.studyproject.view.coinlist

import ado.sabgil.studyproject.R
import ado.sabgil.studyproject.adapter.TickerAdapter
import ado.sabgil.studyproject.data.CoinRepositoryImpl
import ado.sabgil.studyproject.databinding.FragmnetCoinListBinding
import ado.sabgil.studyproject.view.base.BaseFragment
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer

class CoinListFragment : BaseFragment<FragmnetCoinListBinding>(R.layout.fragmnet_coin_list) {

    private lateinit var coinListViewModel: CoinListViewModel

    private val baseCurrency: String? by lazy {
        arguments?.getString(ARGUMENT_BASE_CURRENCY)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.pgCoinList

        coinListViewModel = addingToContainer {
            CoinListViewModel(baseCurrency!!, CoinRepositoryImpl)
        }

        bind {
            vm = coinListViewModel
            rvTickerList.adapter = TickerAdapter()
        }

        registerEvent()
    }

    override fun onResume() {
        super.onResume()
        coinListViewModel.subscribeRemote()
    }

    override fun onPause() {
        coinListViewModel.unSubscribeRemote()
        super.onPause()
    }

    private fun registerEvent() {
        coinListViewModel.run {
            showToastEvent.observe(this@CoinListFragment, Observer(::showToastMessage))

            isLoading.observe(this@CoinListFragment, Observer {
                if (it) showProgressBar() else hideProgressBar()
            })
        }
    }

    companion object {
        const val ARGUMENT_BASE_CURRENCY = "BASE_CURRENCY"
        fun newInstance(baseCurrency: String) =
            CoinListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGUMENT_BASE_CURRENCY, baseCurrency)
                }
            }
    }
}