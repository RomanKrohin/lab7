package WorkModuls

import java.util.*

class Token(val value: String, var expirationDate: Date) {
    fun isValid(): Boolean {
        return expirationDate.after(Date())
    }
}