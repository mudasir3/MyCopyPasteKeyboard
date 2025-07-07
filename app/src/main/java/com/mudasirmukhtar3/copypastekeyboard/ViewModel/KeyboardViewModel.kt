package com.sharpforks.copypaste.ViewModel

class KeyboardViewModel {
    var id: Int = 0
    var text: String = ""
    var order: Int = 0

    override fun toString(): String {
        return "KeyboardViewModel(id=$id, text='$text', order=$order)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KeyboardViewModel

        if (id != other.id) return false
        if (text != other.text) return false
        if (order != other.order) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + text.hashCode()
        result = 31 * result + order
        return result
    }


}