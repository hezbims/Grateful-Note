import android.util.Log

fun countDuration(title : String, body : () -> Unit){
    val past = System.currentTimeMillis()
    body()
    Log.e("qqq" , "$title : ${System.currentTimeMillis() - past}")
}