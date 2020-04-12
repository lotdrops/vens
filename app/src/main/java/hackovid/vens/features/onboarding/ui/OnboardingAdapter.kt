package hackovid.vens.features.onboarding.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hackovid.vens.R
import hackovid.vens.databinding.OnboardingLayoutBinding
import hackovid.vens.features.onboarding.OnboardingModel

class OnboardingAdapter(
    private val onBoardingListModel: List<OnboardingModel>,
    private val nextButtonClicked: (OnboardingModel, Int) -> Unit,
    private val discoverButtonClicked: () -> Unit,
    private val skipButtonClicked: () -> Unit
) : RecyclerView.Adapter<OnboardingAdapter.OnBoardingViewHolder>() {

    lateinit var binding: OnboardingLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = OnboardingLayoutBinding.inflate(inflater, parent, false)
        val viewHolder = OnBoardingViewHolder(binding)
        binding.nextIcon.setOnClickListener {
            nextButtonClicked(
                onBoardingListModel[viewHolder.adapterPosition],
                viewHolder.adapterPosition + 1
            )
        }
        binding.skipText.setOnClickListener {
            skipButtonClicked()
        }
        binding.discover.setOnClickListener {
            discoverButtonClicked()
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return onBoardingListModel.size
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        when (position) {
            0 -> {
                binding.topIcImageCenter.setImageResource(R.drawable.ic_on_board_market)
                binding.dotOne.setImageResource(R.drawable.ic_dot_selected_onboarding)
                binding.dotTwo.setImageResource(R.drawable.ic_dot_unselected_onboarding)
                binding.dotThree.setImageResource(R.drawable.ic_dot_unselected_onboarding)
            }
            1 -> {
                binding.topIcImageCenter.setImageResource(R.drawable.ic_on_board_schedule_store)
                binding.dotOne.setImageResource(R.drawable.ic_dot_unselected_onboarding)
                binding.dotTwo.setImageResource(R.drawable.ic_dot_selected_onboarding)
                binding.dotThree.setImageResource(R.drawable.ic_dot_unselected_onboarding)
            }
            2 -> {
                binding.topIcImageCenter.setImageResource(0)
                binding.topIcImageRight.setImageResource(R.drawable.ic_on_board_direct_chat)
                binding.dotOne.setImageResource(R.drawable.ic_dot_unselected_onboarding)
                binding.dotTwo.setImageResource(R.drawable.ic_dot_unselected_onboarding)
                binding.dotThree.setImageResource(R.drawable.ic_dot_selected_onboarding)
            }
        }
        holder.bind(onBoardingListModel[position])
    }

    inner class OnBoardingViewHolder(val binding: OnboardingLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(onboardingModel: OnboardingModel) {
            binding.onBoarding = onboardingModel
            binding.executePendingBindings()
        }
    }
}
