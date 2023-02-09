package com.example.mvidemo

interface CoroutineUseCase<out Output, in Parameters> {

    suspend fun invoke(parameters: Parameters? = null): Output

}