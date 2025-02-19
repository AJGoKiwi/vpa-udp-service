package `in`.gokiwi.kiwitpap.core.vpa_udp.udp

import `in`.gokiwi.kiwitpap.core.vpa_udp.model.VpaValidRequest
import `in`.gokiwi.kiwitpap.core.vpa_udp.service.VpaVerificationService
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.net.DatagramPacket
import java.net.DatagramSocket
import kotlin.concurrent.thread

@Component
class UdpListener(
    private val verificationService: VpaVerificationService  // Autowiring the service
) {

    private val logger = LoggerFactory.getLogger(UdpListener::class.java)
    private var socket: DatagramSocket? = null
    private var running = true
    private val port = 9876  // Choose an appropriate UDP port

    @PostConstruct
    fun start() {
        thread(start = true, name = "udp-listener-thread") {
            try {
                socket = DatagramSocket(port)
                logger.info("UDP Listener started on port $port")
                while (running) {
                    val buffer = ByteArray(1024)
                    val packet = DatagramPacket(buffer, buffer.size)
                    socket?.receive(packet)
                    val data = String(packet.data, 0, packet.length)
                    logger.info("Received UDP packet: $data")

                    // Parse the UDP data assuming it's comma-separated: "vpa,traceId"
                    val parts = data.split(",")
                    if (parts.size >= 2) {
                        val vpa = parts[0].trim()
                        val traceId = parts[1].trim()
                        val request = VpaValidRequest(vpa = vpa, traceId = traceId)

                        // Call the verification service (blocking for simplicity)
                        val response = verificationService.verifyVpa(request).block()
                        logger.info("Verification response: $response")
                    } else {
                        logger.error("Invalid UDP packet format: $data")
                    }
                }
            } catch (e: Exception) {
                if (running) {
                    logger.error("Error in UDP listener", e)
                }
            }
        }
    }

    @PreDestroy
    fun shutdown() {
        running = false
        socket?.close()
        logger.info("UDP Listener stopped")
    }
}
