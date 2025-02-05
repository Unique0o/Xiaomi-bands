package com.example.logifitappp.core.utils

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.zip.CRC32
import java.util.logging.Logger
import java.util.logging.Level


object CheckSumUtils {
    private val logger = Logger.getLogger(CheckSumUtils::class.java.name)

    fun getCRC8(seq: ByteArray): Int {
        var len = seq.size
        var i = 0
        var crc: Byte = 0x00

        while (len-- > 0) {
            var extract = seq[i++]
            var tempI: Byte = 8
            while (tempI.toInt() != 0) {
                val sum = ((crc.toInt() and 0xff) xor (extract.toInt() and 0xff)).toByte()
                val sumBit = ((sum.toInt() and 0xff) and 0x01).toByte()
                crc = ((crc.toInt() and 0xff) ushr 1).toByte()
                if (sumBit.toInt() != 0) {
                    crc = ((crc.toInt() and 0xff) xor 0x8c).toByte()
                }
                extract = ((extract.toInt() and 0xff) ushr 1).toByte()
                tempI--
            }
        }
        return crc.toInt() and 0xff
    }

    fun getCRC16(seq: ByteArray): Int {
        return getCRC16(seq, 0xFFFF)
    }

    fun getCRC16(seq: ByteArray, crc: Int): Int {
        var currentCrc = crc
        for (b in seq) {
            currentCrc = ((currentCrc ushr 8) or (currentCrc shl 8)) and 0xffff
            currentCrc = currentCrc xor (b.toInt() and 0xff)
            currentCrc = currentCrc xor ((currentCrc and 0xff) ushr 4)
            currentCrc = currentCrc xor ((currentCrc shl 12) and 0xffff)
            currentCrc = currentCrc xor (((currentCrc and 0xFF) shl 5) and 0xffff)
        }
        return currentCrc and 0xffff
    }

    fun getCRC16ansi(seq: ByteArray): Int {
        var crc = 0xffff
        val polynomial = 0xA001

        for (b in seq) {
            crc = crc xor (b.toInt() and 0xFF)
            for (j in 0..7) {
                if ((crc and 1) != 0) {
                    crc = (crc ushr 1) xor polynomial
                } else {
                    crc = crc ushr 1
                }
            }
        }

        return crc and 0xFFFF
    }

    fun getCRC32(seq: ByteArray): Int {
        val crc = CRC32()
        crc.update(seq)
        return crc.value.toInt()
    }

    fun getCRC32(seq: ByteArray, offset: Int, length: Int): Int {
        val crc = CRC32()
        crc.update(seq, offset, length)
        return crc.value.toInt()
    }

    private val Crc16Tab = intArrayOf(
        0, 4129, 8258, 12387, 16516, 20645, 24774, 28903, 33032, 37161, 41290,
        45419, 49548, 53677, 57806, 61935, 4657, 528, 12915, 8786, 21173, 17044, 29431, 25302,
        37689, 33560, 45947, 41818, 54205, 50076, 62463, 58334, 9314, 13379, 1056, 5121, 25830,
        29895, 17572, 21637, 42346, 46411, 34088, 38153, 58862, 62927, 50604, 54669, 13907,
        9842, 5649, 1584, 30423, 26358, 22165, 18100, 46939, 42874, 38681, 34616, 63455, 59390,
        55197, 51132, 18628, 22757, 26758, 30887, 2112, 6241, 10242, 14371, 51660, 55789,
        59790, 63919, 35144, 39273, 43274, 47403, 23285, 19156, 31415, 27286, 6769, 2640,
        14899, 10770, 56317, 52188, 64447, 60318, 39801, 35672, 47931, 43802, 27814, 31879,
        19684, 23749, 11298, 15363, 3168, 7233, 60846, 64911, 52716, 56781, 44330, 48395,
        36200, 40265, 32407, 28342, 24277, 20212, 15891, 11826, 7761, 3696, 65439, 61374,
        57309, 53244, 48923, 44858, 40793, 36728, 37256, 33193, 45514, 41451, 53516, 49453,
        61774, 57711, 4224, 161, 12482, 8419, 20484, 16421, 28742, 24679, 33721, 37784, 41979,
        46042, 49981, 54044, 58239, 62302, 689, 4752, 8947, 13010, 16949, 21012, 25207, 29270,
        46570, 42443, 38312, 34185, 62830, 58703, 54572, 50445, 13538, 9411, 5280, 1153, 29798,
        25671, 21540, 17413, 42971, 47098, 34713, 38840, 59231, 63358, 50973, 55100, 9939,
        14066, 1681, 5808, 26199, 30326, 17941, 22068, 55628, 51565, 63758, 59695, 39368,
        35305, 47498, 43435, 22596, 18533, 30726, 26663, 6336, 2273, 14466, 10403, 52093,
        56156, 60223, 64286, 35833, 39896, 43963, 48026, 19061, 23124, 27191, 31254, 2801,
        6864, 10931, 14994, 64814, 60687, 56684, 52557, 48554, 44427, 40424, 36297, 31782,
        27655, 23652, 19525, 15522, 11395, 7392, 3265, 61215, 65342, 53085, 57212, 44955,
        49082, 36825, 40952, 28183, 32310, 20053, 24180, 11923, 16050, 3793, 7920
    )

    fun crc16_ccitt(data: ByteArray): Int {
        var i2 = 0
        for (i3 in data.indices) {
            i2 = Crc16Tab[((i2 shr 8) xor data[i3].toInt()) and 255] xor (i2 shl 8)
        }
        return 65535 and i2
    }

    fun md5(data: ByteArray): ByteArray? {
        return try {
            val md = MessageDigest.getInstance("MD5")
            md.update(data)
            md.digest()
        } catch (e: NoSuchAlgorithmException) {
            logger.log(Level.SEVERE, "Failed to get md5 digest", e)
            null
        }
    }

    fun md5(str: String): String? {
        return try {
            val md = MessageDigest.getInstance("MD5")
            md.update(str.toByteArray(StandardCharsets.UTF_8))
            hexdump(md.digest()).lowercase(Locale.ROOT)
        } catch (e: NoSuchAlgorithmException) {
            logger.log(Level.SEVERE, "Failed to get md5 digest", e)
            null
        }
    }

    private fun hexdump(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (b in bytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }

    @Throws(IOException::class)
    private fun readAll(inputStream: InputStream, maxLen: Long): ByteArray {
        val out = ByteArrayOutputStream(maxOf(8192, inputStream.available()))
        val buf = ByteArray(8192)
        var totalRead: Long = 0
        var read: Int

        while (inputStream.read(buf).also { read = it } > 0) {
            out.write(buf, 0, read)
            totalRead += read.toLong()
            if (totalRead > maxLen) {
                throw IOException("Too much data to read into memory. Got already $totalRead")
            }
        }
        return out.toByteArray()
    }
}