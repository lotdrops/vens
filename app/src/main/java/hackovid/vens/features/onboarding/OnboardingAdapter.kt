package hackovid.vens.features.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hackovid.vens.R
import hackovid.vens.databinding.OnboardingLayoutBinding

typealias nextButtonClicked = (OnboardingModel) -> Unit

class OnboardingAdapter(
    private val onBoardingListModel: List<OnboardingModel>,
    private val onClick: nextButtonClicked
) : RecyclerView.Adapter<OnboardingAdapter.OnBoardingViewHolder>() {

    lateinit var binding : OnboardingLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = OnboardingLayoutBinding.inflate(inflater, parent,false)
        val viewHolder = OnBoardingViewHolder(binding)
        binding.nextLinearLayout.setOnClickListener {
            onClick(onBoardingListModel[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return onBoardingListModel.size
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        when (position) {
            0 -> {
                binding.dotOne.setImageResource(R.drawable.ic_dot_selected_onboarding)
                binding.dotTwo.setImageResource(R.drawable.ic_dot_unselected_onboarding)
                binding.dotThree.setImageResource(R.drawable.ic_dot_unselected_onboarding)
            }
            1 -> {
                binding.dotOne.setImageResource(R.drawable.ic_dot_unselected_onboarding)
                binding.dotTwo.setImageResource(R.drawable.ic_dot_selected_onboarding)
                binding.dotThree.setImageResource(R.drawable.ic_dot_unselected_onboarding)
            }
            2 -> {
                binding.dotOne.setImageResource(R.drawable.ic_dot_unselected_onboarding)
                binding.dotTwo.setImageResource(R.drawable.ic_dot_unselected_onboarding)
                binding.dotThree.setImageResource(R.drawable.ic_dot_selected_onboarding)

            }
        }
        holder.bind(onBoardingListModel[position])
    }

    inner class OnBoardingViewHolder(val binding: OnboardingLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(onboardingModel: OnboardingModel) {
            binding.onBoarding = onboardingModel
            binding.executePendingBindings()
        }
    }

}