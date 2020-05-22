package com.project.architecturestudy.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.project.architecturestudy.R
import com.project.architecturestudy.adapters.SearchAdapter
import com.project.architecturestudy.data.repository.NaverMovieRepositoryImpl
import com.project.architecturestudy.data.source.local.NaverMovieLocalDataSourceImpl
import com.project.architecturestudy.data.source.local.room.MovieRoomDataBase
import com.project.architecturestudy.data.source.remote.NaverMovieRemoteDataSourceImpl
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private val adapter: SearchAdapter = SearchAdapter()

    private val naverMovieRepositoryImpl by lazy {
        val naverMovieLocalDataSource = NaverMovieLocalDataSourceImpl(this)
        val naverMovieRemoteDataSource = NaverMovieRemoteDataSourceImpl()
        NaverMovieRepositoryImpl(naverMovieLocalDataSource, naverMovieRemoteDataSource)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setRecyclerView()
        setOnClick()

    }

    private fun setOnClick() {
        btn_search.setOnClickListener {
            if (et_search.text.toString().isEmpty()) {
                toast(getString(R.string.please_write))
                return@setOnClickListener
            }
            NaverMovieRepositoryImpl.getMovieList(et_search.text.toString(),
                Success = { items ->
                    adapter.setRemoteMovieData(items)
                    toast(getString(R.string.get_data_success))
                    NaverMovieRepositoryImpl.saveMovieListToLocal(items)
                },
                Failure = {
                    Log.d("bsjbsj", "Throwable:$it")
                    toast(getString(R.string.get_data_failure))
                })
        }

        adapter.onClick = { item ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
            startActivity(intent)
        }

    }

    private fun setRecyclerView() {
        listview_movie.adapter = adapter
        NaverMovieRepositoryImpl.getCashedMovieList(
            Success = { items ->
                adapter.setLocalMovieData(items)
            },
            Failure = {
                Log.d("bsjbsj", "Throwable:$it")
            })

    }

    override fun onDestroy() {
        MovieRoomDataBase.destroyInstance()
        NaverMovieRepositoryImpl.dispose()
        super.onDestroy()
    }
}
