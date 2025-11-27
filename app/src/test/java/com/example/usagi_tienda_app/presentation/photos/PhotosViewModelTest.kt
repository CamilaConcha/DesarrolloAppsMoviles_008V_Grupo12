package com.example.usagi_tienda_app.presentation.photos

import com.example.usagi_tienda_app.data.PhotoRepository
import com.example.usagi_tienda_app.data.model.Photo
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class PhotosViewModelTest : BehaviorSpec({
    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    Given("PhotoRepository devuelve datos correctamente") {
        val repository = mockk<PhotoRepository>()
        val fakePhotos = listOf(
            Photo(
                albumId = 1,
                id = 1,
                title = "Foto de prueba",
                url = "https://example.com/image.jpg",
                thumbnailUrl = "https://example.com/thumb.jpg"
            )
        )
        coEvery { repository.getPhotos() } returns fakePhotos

        val vm = PhotosViewModel(repository)

        When("se ejecuta load()") {
            Then("el estado pasa a cargando y luego contiene las fotos") {
                // estado inicial
                vm.state.value.isLoading shouldBe false

                // al invocar load, debe quedar en loading=true inmediatamente
                vm.load()
                vm.state.value.isLoading shouldBe true
                vm.state.value.error.shouldBeNull()

                // avanzamos el dispatcher para completar la corutina
                testDispatcher.scheduler.advanceUntilIdle()

                val finalState = vm.state.value
                finalState.isLoading shouldBe false
                finalState.error.shouldBeNull()
                finalState.photos.size shouldBe fakePhotos.size
                finalState.photos.first().title shouldBe "Foto de prueba"
            }
        }
    }

    Given("PhotoRepository lanza una excepción") {
        val repository = mockk<PhotoRepository>()
        coEvery { repository.getPhotos() } throws RuntimeException("Network error")

        val vm = PhotosViewModel(repository)

        When("se ejecuta load()") {
            Then("el estado final refleja error y sin fotos") {
                vm.load()
                testDispatcher.scheduler.advanceUntilIdle()

                val finalState = vm.state.value
                finalState.isLoading shouldBe false
                finalState.photos.size shouldBe 0
                // el mensaje puede variar, solo verificamos que exista
                (finalState.error != null) shouldBe true
            }
        }
    }
})