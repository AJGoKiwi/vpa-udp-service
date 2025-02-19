package `in`.gokiwi.kiwitpap.core.vpa_udp.controller


import `in`.gokiwi.kiwitpap.core.vpa_udp.model.VpaValidRequest
import `in`.gokiwi.kiwitpap.core.vpa_udp.service.VpaVerificationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/vvs/v1")
class VpaUdpController(
    private val verificationService: VpaVerificationService
) {

    @PostMapping("/vpa-udp/verify")
    fun verifyVpaUdp(@RequestBody request: VpaValidRequest): ResponseEntity<String> {
        // Call the internal POST service (this example uses blocking for simplicity)
        val response = verificationService.verifyVpa(request).block()
        return ResponseEntity.ok(response ?: "Error in verification")
    }
}
