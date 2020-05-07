package com.example.githubsearch

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.githubsearch.helper.KeyboardController.hideKeyboard
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test
import org.mockito.Mockito

class KeyboardControllerTest {
    private val context = Mockito.mock(Context::class.java)
    private val inputMethodManager = Mockito.mock(InputMethodManager::class.java)
    private val view = Mockito.mock(View::class.java)
    private val binder = Mockito.mock(IBinder::class.java)

    @Test
    fun keyboardController_shouldBeHidden(){
        Mockito.`when`(context.getSystemService(Activity.INPUT_METHOD_SERVICE))
            .thenReturn(inputMethodManager)
        Mockito.`when`(view.windowToken).thenReturn(binder)
        Mockito.`when`(inputMethodManager.hideSoftInputFromWindow(binder, 0)).thenReturn(true)

        context.hideKeyboard(view)
        verify(inputMethodManager).hideSoftInputFromWindow(binder, 0)
    }
}