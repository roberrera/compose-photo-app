package com.roberrera.resytakehome

import androidx.compose.ui.Alignment
import com.roberrera.resytakehome.network.Photo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ResyTakeHomeUnitTest {

    @Test
    fun `image alignment is correct for landscape`() {
        // Given a landscape photo (width > height)
        val photo = Photo(id = 1, width = 1000, height = 500)

        // When we apply the alignment logic
        val alignment = if (photo.height < photo.width) Alignment.Center else Alignment.TopCenter

        // Then the alignment should be Center
        assertEquals(Alignment.Center, alignment)
    }

    @Test
    fun `image alignment is correct for portrait`() {
        // Given a portrait photo (height > width)
        val photo = Photo(id = 1, width = 500, height = 1000)

        // When we apply the alignment logic
        val alignment = if (photo.height < photo.width) Alignment.Center else Alignment.TopCenter

        // Then the alignment should be TopCenter
        assertEquals(Alignment.TopCenter, alignment)
    }

    @Test
    fun `image alignment is incorrect for landscape`() {
        // Given a landscape photo (width > height)
        val photo = Photo(id = 1, width = 500, height = 1000)

        // When we apply the alignment logic
        val alignment = if (photo.height < photo.width) Alignment.Center else Alignment.TopCenter

        // Then the alignment should be Center
        assertNotEquals(Alignment.Center, alignment)
    }

    @Test
    fun `image alignment is incorrect for portrait`() {
        // Given a portrait photo (height > width)
        val photo = Photo(id = 1, width = 1000, height = 500)

        // When we apply the alignment logic
        val alignment = if (photo.height < photo.width) Alignment.Center else Alignment.TopCenter

        // Then the alignment should be TopCenter
        assertNotEquals(Alignment.TopCenter, alignment)
    }
}
