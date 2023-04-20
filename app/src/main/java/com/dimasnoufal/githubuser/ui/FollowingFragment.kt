package com.dimasnoufal.githubuser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimasnoufal.githubuser.adapter.FollowAdapter
import com.dimasnoufal.githubuser.data.network.handler.NetworkResult
import com.dimasnoufal.githubuser.databinding.FragmentFollowingBinding
import com.dimasnoufal.githubuser.model.ItemsItem
import com.dimasnoufal.githubuser.utils.AppConstant
import com.dimasnoufal.githubuser.viewmodels.FollowingViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FollowingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var binding: FragmentFollowingBinding? = null
    private val followAdapter by lazy { FollowAdapter() }
    private val followingViewModel by viewModels<FollowingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = requireActivity().intent.getParcelableExtra<ItemsItem>(AppConstant.EXTRA_USER)
        followingViewModel.getFollowingList(user!!.login!!)

        followingViewModel.followingList.observe(viewLifecycleOwner) { res ->
            when (res) {
                is NetworkResult.Loading -> {
                    handleUi(
                        recylerview = false,
                        progressbar = true,
                        tverror = false,
                    )
                }
                is NetworkResult.Error -> {
                    binding!!.tvError.text = res.errorMessage
                    handleUi(
                        recylerview = false,
                        progressbar = false,
                        tverror = true,
                    )
                    Toast.makeText(requireContext(), res.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Success -> {
                    binding!!.rvUser.apply {
                        adapter = followAdapter
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(requireContext())
                        followAdapter.setData(res.data)

                    }
                    handleUi(
                        recylerview = true,
                        progressbar = false,
                        tverror = false,
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun handleUi(
        recylerview: Boolean,
        progressbar: Boolean,
        tverror: Boolean,
    ) {
        binding?.apply {
            rvUser.isVisible = recylerview
            progressBar.isVisible = progressbar
            tvError.isVisible = tverror
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FollowingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}