/*  Copyright (C) 2022-2024 Andreas Shimokawa

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>. */
/*
    This class is a really dumb pure java port of tiny-EDCH from here
    https://github.com/kokke/tiny-ECDH-c/

    What I did:
    - remove all curves except B163 to make porting easier
    - port to java with brain switched off
    - fix the "java has no unsigned" bugs
    - add some helpers to convert int[] to byte[] and back because java has no casts

    The result is ugly, no one would write such crappy code from scratch, but I tried to
    keep it as close to the C code as possible to prevent bugs. Since I did not know what
    I was doing.
 */
package com.example.logifitappp.core.utils

object ECDH_B163 {
    const val CURVE_DEGREE: Int = 163
    const val ECC_PRV_KEY_SIZE: Int = 24
    const val ECC_PUB_KEY_SIZE: Int = 2 * ECC_PRV_KEY_SIZE

    /* margin for overhead needed in intermediate calculations */
    const val BITVEC_MARGIN: Int = 3
    const val BITVEC_NBITS: Int = (CURVE_DEGREE + BITVEC_MARGIN)
    const val BITVEC_NWORDS: Int = ((BITVEC_NBITS + 31) / 32)
    const val BITVEC_NBYTES: Int = (4 * BITVEC_NWORDS)

    /** */ /* Here the curve parameters are defined. */ /* NIST B-163 */
    val polynomial: IntArray =
        intArrayOf(0x000000c9, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000008)
    val coeff_b: IntArray =
        intArrayOf(0x4a3205fd, 0x512f7874, 0x1481eb10, -0x4736ac36, 0x0a601907, 0x00000002)
    val base_x: IntArray =
        intArrayOf(-0x17cbc1ca, -0x2b66b9c9, -0x5f66ee98, -0x795d2a82, -0xf145e9e, 0x00000003)
    val base_y: IntArray =
        intArrayOf(0x797324f1, -0x4ee3a3f4, -0x5d322abb, 0x71a0094f, -0x2ae04394, 0x00000000)
    val base_order: IntArray =
        intArrayOf(-0x5bdcb3cd, 0x77e70c12, 0x000292fe, 0x00000000, 0x00000000, 0x00000004)

    /** */ /* Private / static functions: */ /* some basic bit-manipulation routines that act on bit-vectors follow */
    fun bitvec_get_bit(x: IntArray, idx: Int): Int {
        return (((((x[idx / 32].toLong() and 0xffffffffL) shr (idx and 31)) and 1L))).toInt()
    }

    fun bitvec_clr_bit(x: IntArray, idx: Int) {
        x[idx / 32] = x[idx / 32] and (1 shl (idx and 31)).inv()
    }

    fun bitvec_copy(x: IntArray, y: IntArray) {
        var i = 0
        while (i < BITVEC_NWORDS) {
            x[i] = y[i]
            ++i
        }
    }

    fun bitvec_swap(x: IntArray, y: IntArray) {
        val tmp = IntArray(BITVEC_NWORDS)
        bitvec_copy(tmp, x)
        bitvec_copy(x, y)
        bitvec_copy(y, tmp)
    }

    /* fast version of equality test */
    fun bitvec_equal(x: IntArray, y: IntArray): Boolean {
        var i = 0
        while (i < BITVEC_NWORDS) {
            if (x[i] != y[i]) {
                return false
            }
            ++i
        }
        return true
    }

    fun bitvec_set_zero(x: IntArray) {
        var i = 0
        while (i < BITVEC_NWORDS) {
            x[i] = 0
            ++i
        }
    }

    /* fast implementation */
    fun bitvec_is_zero(x: IntArray): Boolean {
        var i = 0
        while (i < BITVEC_NWORDS) {
            if (x[i] != 0) {
                break
            }
            i += 1
        }
        return (i == BITVEC_NWORDS)
    }

    /* return the number of the highest one-bit + 1 */
    fun bitvec_degree(x: IntArray): Int {
        var i = BITVEC_NWORDS * 32

        /* Start at the back of the vector (MSB) */
        var y = BITVEC_NWORDS

        /* Skip empty / zero words */
        while ((i > 0)
            && (x[--y] == 0)
        ) {
            i -= 32
        }
        /* Run through rest if count is not multiple of bitsize of DTYPE */
        if (i != 0) {
            var u32mask = (1 shl 31)
            while (((x[y]) and u32mask) == 0) {
                u32mask = ((u32mask.toLong() and 0xffffffffL) shr 1).toInt()
                i -= 1
            }
        }
        return i
    }

    /* left-shift by 'count' digits */
    fun bitvec_lshift(x: IntArray, y: IntArray, nbits: Int) {
        var nbits = nbits
        val nwords = (nbits / 32)

        /* Shift whole words first if nwords > 0 */
        var i: Int
        i = 0
        while (i < nwords) {
            /* Zero-initialize from least-significant word until offset reached */
            x[i] = 0
            ++i
        }
        var j = 0
        /* Copy to x output */
        while (i < BITVEC_NWORDS) {
            x[i] = y[j]
            i += 1
            j += 1
        }

        /* Shift the rest if count was not multiple of bitsize of DTYPE */
        nbits = nbits and 31
        if (nbits != 0) {
            /* Left shift rest */
            i = (BITVEC_NWORDS - 1)
            while (i > 0) {
                x[i] =
                    (((x[i]).toLong() shl nbits) or ((x[i - 1].toLong() and 0xffffffffL) shr (32 - nbits))).toInt()
                --i
            }
            x[0] = ((x[0]).toLong() shl nbits).toInt()
        }
    }

    /** */ /*
     * Code that does arithmetic on bit-vectors in the Galois Field
     * GF(2^CURVE_DEGREE).
     */
    /** */
    fun gf2field_set_one(x: IntArray) {
        /* Set first word to one */
        x[0] = 1
        /* .. and the rest to zero */
        var i = 1
        while (i < BITVEC_NWORDS) {
            x[i] = 0
            ++i
        }
    }


    /* fastest check if x == 1 */
    fun gf2field_is_one(x: IntArray): Boolean {
        /* Check if first word == 1 */
        if (x[0] != 1) {
            return false
        }
        /* ...and if rest of words == 0 */
        var i = 1
        while (i < BITVEC_NWORDS) {
            if (x[i] != 0) {
                break
            }
            ++i
        }
        return (i == BITVEC_NWORDS)
    }

    /* galois field(2^m) addition is modulo 2, so XOR is used instead - 'z := a + b' */
    fun gf2field_add(z: IntArray, x: IntArray, y: IntArray) {
        var i = 0
        while (i < BITVEC_NWORDS) {
            z[i] = (x[i] xor y[i])
            ++i
        }
    }

    /* increment element */
    fun gf2field_inc(x: IntArray) {
        x[0] = x[0] xor 1
    }

    /* field multiplication 'z := (x * y)' */
    fun gf2field_mul(z: IntArray, x: IntArray, y: IntArray) {
        val tmp = IntArray(BITVEC_NWORDS)
        assert(z != y)

        bitvec_copy(tmp, x)

        /* LSB set? Then start with x */
        if (bitvec_get_bit(y, 0) != 0) {
            bitvec_copy(z, x)
        } else  /* .. or else start with zero */ {
            bitvec_set_zero(z)
        }

        /* Then add 2^i * x for the rest */
        var i = 1
        while (i < CURVE_DEGREE) {
            /* lshift 1 - doubling the value of tmp */
            bitvec_lshift(tmp, tmp, 1)

            /* Modulo reduction polynomial if degree(tmp) > CURVE_DEGREE */
            if (bitvec_get_bit(tmp, CURVE_DEGREE) != 0) {
                gf2field_add(tmp, tmp, polynomial)
            }

            /* Add 2^i * tmp if this factor in y is non-zero */
            if (bitvec_get_bit(y, i) != 0) {
                gf2field_add(z, z, tmp)
            }
            ++i
        }
    }

    /* field inversion 'z := 1/x' */
    fun gf2field_inv(z: IntArray, x: IntArray) {
        val u = IntArray(BITVEC_NWORDS)
        val v = IntArray(BITVEC_NWORDS)
        val g = IntArray(BITVEC_NWORDS)
        val h = IntArray(BITVEC_NWORDS)

        var i: Int

        bitvec_copy(u, x)
        bitvec_copy(v, polynomial)
        bitvec_set_zero(g)
        gf2field_set_one(z)

        while (!gf2field_is_one(u)) {
            i = (bitvec_degree(u) - bitvec_degree(v))

            if (i < 0) {
                bitvec_swap(u, v)
                bitvec_swap(g, z)
                i = -i
            }
            bitvec_lshift(h, v, i)
            gf2field_add(u, u, h)
            bitvec_lshift(h, g, i)
            gf2field_add(z, z, h)
        }
    }

    /** */ /*
     * The following code takes care of Galois-Field arithmetic.
     * Elliptic curve points are represented by pairs (x,y) of bitvec_t.
     * It is assumed that curve coefficient 'a' is {0,1}
     * This is the case for all NIST binary curves.
     * Coefficient 'b' is given in 'coeff_b'.
     * '(base_x, base_y)' is a point that generates a large prime order group.
     */
    /** */
    fun gf2point_copy(x1: IntArray, y1: IntArray, x2: IntArray, y2: IntArray) {
        bitvec_copy(x1, x2)
        bitvec_copy(y1, y2)
    }

    fun gf2point_set_zero(x: IntArray, y: IntArray) {
        bitvec_set_zero(x)
        bitvec_set_zero(y)
    }

    fun gf2point_is_zero(x: IntArray, y: IntArray): Boolean {
        return (bitvec_is_zero(x)
                && bitvec_is_zero(y))
    }

    /* double the point (x,y) */
    fun gf2point_double(x: IntArray, y: IntArray) {
        /* iff P = O (zero or infinity): 2 * P = P */
        if (bitvec_is_zero(x)) {
            bitvec_set_zero(y)
        } else {
            val l = IntArray(BITVEC_NWORDS)
            gf2field_inv(l, x)
            gf2field_mul(l, l, y)
            gf2field_add(l, l, x)
            gf2field_mul(y, x, x)
            gf2field_mul(x, l, l)
            gf2field_inc(l)
            gf2field_add(x, x, l)
            gf2field_mul(l, l, x)
            gf2field_add(y, y, l)
        }
    }

    /* add two points together (x1, y1) := (x1, y1) + (x2, y2) */
    fun gf2point_add(x1: IntArray, y1: IntArray, x2: IntArray, y2: IntArray) {
        if (!gf2point_is_zero(x2, y2)) {
            if (gf2point_is_zero(x1, y1)) {
                gf2point_copy(x1, y1, x2, y2)
            } else {
                if (bitvec_equal(x1, x2)) {
                    if (bitvec_equal(y1, y2)) {
                        gf2point_double(x1, y1)
                    } else {
                        gf2point_set_zero(x1, y1)
                    }
                } else {
                    /* Arithmetic with temporary variables */
                    val a = IntArray(BITVEC_NWORDS)
                    val b = IntArray(BITVEC_NWORDS)
                    val c = IntArray(BITVEC_NWORDS)
                    val d = IntArray(BITVEC_NWORDS)

                    gf2field_add(a, y1, y2)
                    gf2field_add(b, x1, x2)
                    gf2field_inv(c, b)
                    gf2field_mul(c, c, a)
                    gf2field_mul(d, c, c)
                    gf2field_add(d, d, c)
                    gf2field_add(d, d, b)
                    gf2field_inc(d)
                    gf2field_add(x1, x1, d)
                    gf2field_mul(a, x1, c)
                    gf2field_add(a, a, d)
                    gf2field_add(y1, y1, a)
                    bitvec_copy(x1, d)
                }
            }
        }
    }


    /* point multiplication via double-and-add algorithm */
    fun gf2point_mul(x: IntArray, y: IntArray, exp: IntArray) {
        val tmpx = IntArray(BITVEC_NWORDS)
        val tmpy = IntArray(BITVEC_NWORDS)

        var i: Int
        val nbits = bitvec_degree(exp)
        gf2point_set_zero(tmpx, tmpy)

        i = (nbits - 1)
        while (i >= 0) {
            gf2point_double(tmpx, tmpy)

            if (bitvec_get_bit(exp, i) != 0) {
                gf2point_add(tmpx, tmpy, x, y)
            }
            --i
        }

        gf2point_copy(x, y, tmpx, tmpy)
    }


    /* check if y^2 + x*y = x^3 + a*x^2 + coeff_b holds */
    fun gf2point_on_curve(x: IntArray, y: IntArray): Boolean {
        val a = IntArray(BITVEC_NWORDS)
        val b = IntArray(BITVEC_NWORDS)

        if (gf2point_is_zero(x, y)) {
            return false
        } else {
            gf2field_mul(a, x, x)
            gf2field_mul(b, a, x)
            gf2field_add(a, a, b)
            gf2field_add(a, a, coeff_b)
            gf2field_mul(b, y, y)
            gf2field_add(a, a, b)
            gf2field_mul(b, x, y)

            return bitvec_equal(a, b)
        }
    }

    // helper needed for C->Java conversion (Java cant cast pointers)
    fun bytes_to_int(bytes: ByteArray, offset: Int): IntArray {
        val value = IntArray(BITVEC_NWORDS)
        var byteptr = offset
        for (i in 0 until BITVEC_NWORDS) {
            value[i] =
                ((bytes[byteptr++].toInt() and 0xff)) or ((bytes[byteptr++].toInt() and 0xff) shl 8) or ((bytes[byteptr++].toInt() and 0xff) shl 16) or ((bytes[byteptr++].toInt() and 0xff) shl 24)
        }
        return value
    }

    // helper needed for C->Java conversion (Java cant cast pointers)
    fun ints_to_bytes(bytes: ByteArray, ints: IntArray, offset: Int) {
        var byteptr = offset
        for (i in 0 until BITVEC_NWORDS) {
            bytes[byteptr++] = (ints[i] and 0x000000ff).toByte()
            bytes[byteptr++] = ((ints[i] and 0x0000ff00) shr 8).toByte()
            bytes[byteptr++] = ((ints[i] and 0x00ff0000) shr 16).toByte()
            bytes[byteptr++] = ((ints[i] and -0x1000000) shr 24).toByte()
        }
    }

    /** */ /*
     * Elliptic Curve Diffie-Hellman key exchange protocol.
     */
    /** */ /* NOTE: private should contain random data a-priori! */
    fun ecdh_generate_keys(public_key: ByteArray, private_key: ByteArray): Boolean {
        val private_key_int32 = bytes_to_int(private_key, 0)
        val public_key_int32_1 = bytes_to_int(public_key, 0)
        val public_key_int32_2 = bytes_to_int(public_key, BITVEC_NBYTES)
        /* Get copy of "base" point 'G' */
        gf2point_copy(public_key_int32_1, public_key_int32_2, base_x, base_y)

        /* Abort key generation if random number is too small */
        if (bitvec_degree(private_key_int32) < (CURVE_DEGREE / 2)) {
            return false
        } else {
            /* Clear bits > CURVE_DEGREE in highest word to satisfy constraint 1 <= exp < n. */
            val nbits = bitvec_degree(base_order)

            var i = (nbits - 1)
            while (i < (BITVEC_NWORDS * 32)) {
                bitvec_clr_bit(private_key_int32, i)
                ++i
            }

            /* Multiply base-point with scalar (private-key) */
            gf2point_mul(public_key_int32_1, public_key_int32_2, private_key_int32)

            ints_to_bytes(public_key, public_key_int32_1, 0)
            ints_to_bytes(public_key, public_key_int32_2, BITVEC_NBYTES)

            return true
        }
    }

    fun ecdh_shared_secret(
        private_key: ByteArray,
        others_pub: ByteArray,
        output: ByteArray
    ): Boolean {
        val private_key_int32 = bytes_to_int(private_key, 0)
        val others_pub_int32_1 = bytes_to_int(others_pub, 0)
        val others_pub_int32_2 = bytes_to_int(others_pub, BITVEC_NBYTES)

        /* Do some basic validation of other party's public key */
        if (!gf2point_is_zero(others_pub_int32_1, others_pub_int32_2)
            && gf2point_on_curve(others_pub_int32_1, others_pub_int32_2)
        ) {
            /* Copy other side's public key to output */
            var i = 0
            while (i < (BITVEC_NBYTES * 2)) {
                output[i] = others_pub[i]
                ++i
            }

            /* Clear bits > CURVE_DEGREE in highest word to satisfy constraint 1 <= exp < n. */
            val nbits = bitvec_degree(base_order)

            i = (nbits - 1)
            while (i < (BITVEC_NWORDS * 32)) {
                bitvec_clr_bit(private_key_int32, i)
                ++i
            }

            /* Multiply other side's public key with own private key */
            val output_int32_1 = bytes_to_int(output, 0)
            val output_int32_2 = bytes_to_int(output, BITVEC_NBYTES)

            gf2point_mul(output_int32_1, output_int32_2, private_key_int32)

            ints_to_bytes(output, output_int32_1, 0)
            ints_to_bytes(output, output_int32_2, BITVEC_NBYTES)

            return true
        } else {
            return false
        }
    }

    // these are wrappers around the above C-style methods for Gadgetbridge to use
    fun ecdh_generate_public(privateEC: ByteArray): ByteArray? {
        val pubKey = ByteArray(ECC_PUB_KEY_SIZE)
        if (ecdh_generate_keys(pubKey, privateEC)) {
            return pubKey
        }
        return null
    }

    fun ecdh_generate_shared(privateEC: ByteArray, remotePublicEC: ByteArray): ByteArray? {
        val sharedKey = ByteArray(ECC_PUB_KEY_SIZE)
        if (ecdh_shared_secret(privateEC, remotePublicEC, sharedKey)) {
            return sharedKey
        }
        return null
    }
}