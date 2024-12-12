package com.example.logifitappp.core.utils

import android.util.Pair
import java.util.LinkedList

class LimitedQueue<K, V>(private val limit: Int) {
    private val list = LinkedList<Pair<K, V>>()

    @Synchronized
    fun add(id: K, obj: V) {
        if (list.size > limit - 1) {
            list.removeFirst()
        }
        list.add(Pair(id, obj))
    }

    @Synchronized
    fun remove(id: K) {
        val it = list.iterator()
        while (it.hasNext()) {
            val pair = it.next()
            if (id == pair.first) {
                it.remove()
            }
        }
    }

    @Synchronized
    fun lookup(id: K): V? {
        for (entry in list) {
            if (id == entry.first) {
                return entry.second
            }
        }
        return null
    }

    @Synchronized
    fun lookupByValue(value: V): K? {
        for (entry in list) {
            if (value == entry.second) {
                return entry.first
            }
        }
        return null
    }
}