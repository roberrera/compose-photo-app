package com.roberrera.resytakehome

import androidx.compose.ui.Alignment
import com.roberrera.resytakehome.model.PhotoRepository
import com.roberrera.resytakehome.model.PhotosViewModel
import com.roberrera.resytakehome.network.Photo
import com.roberrera.resytakehome.testutils.MainDispatcherRule
import com.roberrera.resytakehome.testutils.failingApiService
import com.roberrera.resytakehome.testutils.successApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ResyTakeHomeUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `image alignment is correct for landscape`() {
        val photo = Photo(id = 1, width = 1000, height = 500)
        val alignment = if (photo.height < photo.width) Alignment.Center else Alignment.TopCenter

        assertEquals(Alignment.Center, alignment)
    }

    @Test
    fun `image alignment is correct for portrait`() {
        val photo = Photo(id = 1, width = 500, height = 1000)
        val alignment = if (photo.height < photo.width) Alignment.Center else Alignment.TopCenter

        assertEquals(Alignment.TopCenter, alignment)
    }

    @Test
    fun `image alignment is incorrect for landscape`() {
        val photo = Photo(id = 1, width = 500, height = 1000)
        val alignment = if (photo.height < photo.width) Alignment.Center else Alignment.TopCenter

        assertNotEquals(Alignment.Center, alignment)
    }

    @Test
    fun `image alignment is incorrect for portrait`() {
        val photo = Photo(id = 1, width = 1000, height = 500)
        val alignment = if (photo.height < photo.width) Alignment.Center else Alignment.TopCenter

        assertNotEquals(Alignment.TopCenter, alignment)
    }

    @Test
    fun `when fetchPhotos is successful photos state is updated`()
    = runTest(mainDispatcherRule.dispatcher) {
        val responseBody = listOf(
            Photo(
                id = 0,
                format = "jpeg",
                width = 5000,
                height = 3333,
                fileName = "0.jpeg",
                author = "Alejandro Escamilla",
                authorUrl = "https://unsplash.com/photos/yC-Yzbqy7PY",
                postUrl = "https://unsplash.com/photos/yC-Yzbqy7PY"
            ),
            Photo(
                id = 1,
                format = "jpeg",
                width = 5000,
                height = 3333,
                fileName = "1.jpeg",
                author = "Alejandro Escamilla",
                authorUrl = "https://unsplash.com/photos/LNRyGwIJr5c",
                postUrl = "https://unsplash.com/photos/LNRyGwIJr5c"
            )
        )

        // Given a fake ApiService that returns two photos
        val repository = PhotoRepository(successApiService(responseBody))
        val viewModel = PhotosViewModel(repository)

        // when fetchPhotos is called
        viewModel.fetchPhotos()

        // advance the test scheduler so the launched coroutine completes before any assertion is called
        advanceUntilIdle()

        // then verify the state was updated correctly
        assertEquals(responseBody, viewModel.photos.value)
        assertEquals(false, viewModel.isLoading.value)
        assertEquals(null, viewModel.error.value)
    }

    @Test
    fun `when fetchPhotos fails the error state is updated`()
    = runTest(mainDispatcherRule.dispatcher) {
        val repository = PhotoRepository(failingApiService())
        val viewModel = PhotosViewModel(repository)

        // when fetchPhotos is called
        viewModel.fetchPhotos()

        // advance the test scheduler
        advanceUntilIdle()

        // then verify the error state was updated correctly.
        assertEquals(emptyList<Photo>(), viewModel.photos.value)
        assertNotNull(viewModel.error.value)
        assertEquals(false, viewModel.isLoading.value)
    }
}