package `in`.gokiwi.kiwitpap.core.vpa_udp.service


import `in`.gokiwi.kiwitpap.core.vpa_udp.model.VpaValidRequest
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class VpaVerificationService {

    private val webClient = WebClient.builder()
        .baseUrl("https://api.gokiwi.in/vvs/v1")
        .build()

    fun verifyVpa(request: VpaValidRequest): Mono<String> {
        return webClient.post()
            .uri("/vpa/verify")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(String::class.java)
    }
}
