package com.camo.kripto.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.camo.kripto.databinding.CryptocurrenciesMarketCapItemBinding
import com.camo.kripto.remote.model.CoinMarket
import com.camo.kripto.utils.AnimationUtils
import com.camo.kripto.utils.Extras
import com.camo.kripto.utils.HapticUtils
import timber.log.Timber
import java.math.BigDecimal

class CryptocurrenciesMarketCapAdapter(
    private val onCryptocurrencyListener: OnCryptocurrencyListener,
    var curr: String,
    diffCallback: DiffUtil.ItemCallback<CoinMarket.CoinMarketItem>
) :
    PagingDataAdapter<CoinMarket.CoinMarketItem, CryptocurrenciesMarketCapAdapter.ViewHolder>(
        diffCallback
    ) {

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CryptocurrenciesMarketCapItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onCryptocurrencyListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coinMarketItem = getItem(position)
        if (coinMarketItem != null) {
            Glide.with(holder.binding.root.context)
                .load(coinMarketItem.image)
                .fitCenter()
                .into(holder.binding.ivCoin)
            holder.binding.tvCoinName.text = coinMarketItem.symbol
            holder.binding.tvCurrentPrice.text = Extras.getFormattedDoubleCurr(
                coinMarketItem.current_price,
                curr,
                suffix = ""
            )
            holder.binding.tvMarketCap.text = Extras.getFormattedDoubleCurr(
                coinMarketItem.market_cap,
                curr,
                suffix = ""
            )

            val perChange = coinMarketItem.market_cap_change ?: BigDecimal(0)
            if (perChange >= BigDecimal(0)) holder.binding.tvCryprocurrenciesMarketCapItemPerChange.setTextColor(
                Color.GREEN
            )
            else {
                holder.binding.tvCryprocurrenciesMarketCapItemPerChange.setTextColor(Color.RED)
            }
            holder.binding.tvCryprocurrenciesMarketCapItemPerChange.text =
                Extras.getFormattedPerChange(
                    perChange
                )

            // Add smooth fade-in animation for new items
            if (position > lastPosition) {
                AnimationUtils.fadeIn(holder.binding.root, duration = 200)
                lastPosition = position
            }
        } else {
            Timber.d("coinItem is null")
        }
    }

    fun getCoin(position: Int): CoinMarket.CoinMarketItem? {
        return getItem(position)
    }

    class ViewHolder(
        val binding: CryptocurrenciesMarketCapItemBinding,
        private val onCryptocurrencyListener: OnCryptocurrencyListener
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            // Add pulse animation and haptic feedback on click
            AnimationUtils.pulse(binding.root, scale = 1.05f, duration = 150)
            HapticUtils.lightClick(binding.root)
            onCryptocurrencyListener.onCryptocurrencyClicked(absoluteAdapterPosition)
        }
    }

    object Comparator : DiffUtil.ItemCallback<CoinMarket.CoinMarketItem>() {
        override fun areItemsTheSame(
            oldItem: CoinMarket.CoinMarketItem,
            newItem: CoinMarket.CoinMarketItem
        ): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CoinMarket.CoinMarketItem,
            newItem: CoinMarket.CoinMarketItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface OnCryptocurrencyListener {
        fun onCryptocurrencyClicked(position: Int)
    }
}
