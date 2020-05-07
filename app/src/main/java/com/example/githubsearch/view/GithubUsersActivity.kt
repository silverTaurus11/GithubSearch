package com.example.githubsearch.view

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubsearch.R
import com.example.githubsearch.helper.AbstractTextWatcher
import com.example.githubsearch.helper.ErrorConst
import com.example.githubsearch.helper.KeyboardController.hideKeyboard
import com.example.githubsearch.model.users.Owner
import com.example.githubsearch.presenter.GithubUsersPresenterFactory
import com.example.githubsearch.view.adapter.GithubUsersRecyclerViewAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.input_query_layout.*


class GithubUsersActivity: AppCompatActivity(), GithubUsersView {
    private val cacheRepositoryList = "cacheRepositoryList"
    private val cacheLastPosition = "lastPosition"
    private val cacheRecyclerviewVisibilityLastState = "recyclerviewVisibilityLastState"
    private val cacheLastKeyword = "cacheLastKeyword"
    private val cacheLastPage= "cacheLastPage"
    private val cacheLastInfoMessage = "cacheLastInfoMessage"
    private val cacheLoadMoreStatus = "cacheLoadMoreStatus"

    private val githubUsersRecycleviewAdapter by lazy { GithubUsersRecyclerViewAdapter() }
    private val recyclerViewLayoutManager by lazy { LinearLayoutManager(this) }
    private val githubUsersPresenter by lazy { GithubUsersPresenterFactory.createPresenter(this, this) }
    private val gson by lazy { Gson() }
    private val loadingInfoMessageString by lazy { resources.getString(R.string.wait_a_moment) }

    private val viewThreshold = 20
    private var pageNumber = 1
    private var isLoadMoreState = false
    private var previousTotal = 0
    private var isNeedResetLayout = false
    private var lastKeyword = ""
    private var lastInfoMessage = ""
    private var isHasReachBottomList = false
    private var isLoadMoreLoading  = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
        initSearchEditor()
        defaultLayout()
    }

    private fun initRecyclerView(){
        githubUsersRecyclerView.layoutManager = recyclerViewLayoutManager
        githubUsersRecyclerView.adapter = githubUsersRecycleviewAdapter
        githubUsersRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        githubUsersRecyclerView.setHasFixedSize(true)
        githubUsersRecyclerView.setItemViewCacheSize(20)
        githubUsersRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = recyclerViewLayoutManager.childCount
                val totalItemCount = recyclerViewLayoutManager.itemCount
                val pastVisibleItem = recyclerViewLayoutManager.findFirstVisibleItemPosition()

                if(!recyclerView.canScrollVertically(1) && dy > 0
                    && !isHasReachBottomList && !isLoadMoreLoading){
                    if(isLoadMoreState){
                        if(totalItemCount >= previousTotal){
                            isLoadMoreState = false
                            previousTotal = totalItemCount
                        }
                    }

                    if(!isLoadMoreState && ((totalItemCount-visibleItemCount) <= (pastVisibleItem + viewThreshold))){
                        pageNumber++
                        loadMoreData()
                        isLoadMoreState = true
                    }
                }
            }
        })
    }

    private fun initSearchEditor(){
        searchEditor.clearFocus()
        searchEditor.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                isHasReachBottomList = false
                val keyword = searchEditor.text.toString()
                if(keyword.isNotEmpty()){
                    requestUsersData(keyword)
                    lastKeyword = keyword
                } else{
                    defaultLayout()
                }
                this.hideKeyboard(searchEditor)
                true
            } else false
        }
        searchEditor.addTextChangedListener(object: AbstractTextWatcher() {
            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    if(it.isEmpty() && isNeedResetLayout){
                        defaultLayout()
                        isNeedResetLayout = false
                    }
                }
            }
        })
    }

    private fun requestUsersData(keyword: String){
        loadingLayout()
        resetPreviousRequestData()
        githubUsersPresenter.requestUsersData(keyword, pageNumber, viewThreshold)
    }

    private fun resetPreviousRequestData(){
        previousTotal = 0
        pageNumber = 1
    }

    private fun loadMoreData(){
        showLoadMoreLoading()
        githubUsersPresenter.loadMoreData(lastKeyword, pageNumber, viewThreshold)
        this.hideKeyboard(searchEditor)
    }

    private fun defaultLayout(){
        githubUsersRecycleviewAdapter.clearDataUsers()
        hideLoadMoreLoading()
        infoMessageView.visibility = View.GONE
        githubUsersRecyclerView.visibility = View.GONE
    }

    override fun showUsersData(itemList: List<Owner>, isLoadMore: Boolean) {
        if(isLoadMore){
            githubUsersRecycleviewAdapter.updateDataUsers(itemList)
            hideLoadMoreLoading()
        } else{
            githubUsersRecycleviewAdapter.insertNewDataUsers(itemList)
            if(itemList.isNotEmpty()){
                githubUsersRecyclerView.smoothScrollToPosition(0)
            }
        }
        infoMessageView.visibility = View.GONE
        githubUsersRecyclerView.visibility = View.VISIBLE
    }

    private fun loadingLayout(){
        showInfoMessageLayout(loadingInfoMessageString)
    }

    override fun requestLimitLayout(isLoadMore: Boolean){
        if(isLoadMore){
            pageNumber--
            hideLoadMoreLoading()
            Toast.makeText(this, resources.getString(R.string.error_request_limit),
                Toast.LENGTH_LONG).show()
        } else{
            isNeedResetLayout = true
            showInfoMessageLayout(resources.getString(R.string.error_request_limit))
        }
    }

    override fun emptyUserLayout(isLoadMore: Boolean){
        if(isLoadMore){
            isHasReachBottomList = true
            hideLoadMoreLoading()
            Toast.makeText(this, getErrorMessage(ErrorConst.GithubError.HAS_REACHED_BOTTOM_LIST)
                , Toast.LENGTH_SHORT).show()
        } else{
            showInfoMessageLayout(resources.getString(R.string.error_data_not_found))
            isNeedResetLayout = true
        }
    }

    override fun showErrorToast(error: ErrorConst.GithubError, isLoadMore: Boolean){
        Toast.makeText(this, getErrorMessage(error), Toast.LENGTH_LONG).show()
        if(isLoadMore){
            pageNumber--
            hideLoadMoreLoading()
        } else{
            defaultLayout()
        }
    }

    override fun invalidQueryLayout() {
        isNeedResetLayout = true
        showInfoMessageLayout(resources.getString(R.string.invalid_keyword))
    }

    private fun showLoadMoreLoading(){
        githubUsersRecyclerView.post{
            githubUsersRecycleviewAdapter.showProgressBar()
            isLoadMoreLoading = true
        }
    }

    private fun hideLoadMoreLoading(){
        githubUsersRecyclerView.post{
            Handler().postDelayed({
                githubUsersRecycleviewAdapter.hideProgressBar()
                isLoadMoreLoading = false
            }, 500)
        }
    }

    private fun showInfoMessageLayout(message: String){
        infoMessageView.text = message
        infoMessageView.visibility = View.VISIBLE
        githubUsersRecyclerView.visibility = View.GONE
        hideLoadMoreLoading()

        lastInfoMessage = message
    }

    private fun getErrorMessage(error: ErrorConst.GithubError): String{
        return when(error){
            ErrorConst.GithubError.NO_INTERNET_CONNECTION ->
                resources.getString(R.string.error_no_internet_connection_message)
            ErrorConst.GithubError.HAS_REACHED_BOTTOM_LIST ->
                resources.getString(R.string.has_reached_bottom_list)
            else-> resources.getString(R.string.error_cant_get_information)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(cacheRepositoryList, gson.toJson(githubUsersRecycleviewAdapter.getItemList()))
        outState.putInt(cacheLastPosition, recyclerViewLayoutManager.findFirstVisibleItemPosition())
        outState.putInt(cacheRecyclerviewVisibilityLastState, githubUsersRecyclerView.visibility)
        outState.putString(cacheLastKeyword, lastKeyword)
        outState.putInt(cacheLastPage, pageNumber)
        outState.putString(cacheLastInfoMessage, lastInfoMessage)
        outState.putBoolean(cacheLoadMoreStatus, isLoadMoreLoading)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setRecyclerViewDataWhenRestoreInstanceState(savedInstanceState)
    }

    private fun setRecyclerViewDataWhenRestoreInstanceState(savedInstanceState: Bundle){
        val usersRepositorys = savedInstanceState.getString(cacheRepositoryList, "")
        val position = savedInstanceState.getInt(cacheLastPosition, 0)
        val recyclerViewVisibility = savedInstanceState.getInt(cacheRecyclerviewVisibilityLastState, View.GONE)
        val lastLoadMoreLoading = savedInstanceState.getBoolean(cacheLoadMoreStatus, false)
        lastKeyword = savedInstanceState.getString(cacheLastKeyword, "")
        lastInfoMessage = savedInstanceState.getString(cacheLastInfoMessage, "")
        pageNumber = savedInstanceState.getInt(cacheLastPage, 1)
        if(lastLoadMoreLoading){
            pageNumber--
        }

        usersRepositorys?.let{
            if(it.isNotEmpty()){
                if(recyclerViewVisibility == View.VISIBLE){
                    val usersList: List<Owner>
                            = gson.fromJson(usersRepositorys, object : TypeToken<List<Owner>>() {}.type)
                    showUsersData(usersList, false)
                    if(position >= usersList.size ){
                        githubUsersRecyclerView.smoothScrollToPosition(position)
                    }
                } else{
                    if(lastInfoMessage.isNotEmpty()){
                        if(loadingInfoMessageString.equals(lastInfoMessage)){
                            requestUsersData(lastKeyword)
                        } else{
                            showInfoMessageLayout(lastInfoMessage)
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        githubUsersPresenter.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        githubUsersPresenter.onDestroy()
        super.onDestroy()
    }
}