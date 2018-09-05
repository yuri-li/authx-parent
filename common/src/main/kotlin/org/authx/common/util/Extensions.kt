package org.authx.common.util

object Extensions {
    /**
     * 扩展Array，添加openUrl函数
     * 使用默认的浏览器打开网址
     */
    fun Array<String>.openUrl() = this.forEach { it.openUrl() }

    private inline fun String.openUrl() {
        //获取操作系统的名字
        val osName = System.getProperty("os.name")
        when {
            osName.startsWith("Mac OS") -> Class.forName("com.apple.eio.FileManager").getDeclaredMethod("openURL", *arrayOf<Class<*>>(String::class.java)).invoke(null, *arrayOf<Any>(this))
            osName.startsWith("Windows") -> Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler ${this}")
            else -> {
                var flag = true
                // Unix or Linux的打开方式
                arrayOf("firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape").forEach {
                    if (Runtime.getRuntime().exec(arrayOf("which", it)).waitFor() == 0) {
                        flag = false
                        Runtime.getRuntime().exec(arrayOf<String>(it, this))
                        return
                    }
                }
                if (flag) throw Exception("Could not find web browser")
            }
        }
    }

}
