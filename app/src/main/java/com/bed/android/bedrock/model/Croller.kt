package com.bed.android.bedrock.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jsoup.Jsoup
import java.io.IOException

private const val TAG="Croller"


class Croller(){

    private var url:String?=null
    private val url_front="http://search.danawa.com/dsearch.php?query="
    private val url_back="&tab=main"


    fun croll(keyword:String):ArrayList<Product>{


        try {
            url=keyword
            val url_basic=url_front+url+url_back
            var doc = Jsoup.connect(url_basic).get()
            var productList=doc.select(".product_list").select(".prod_item").select(".prod_main_info")
            if(productList.isEmpty()){
                Log.d(TAG,"null")
                return ArrayList<Product>()
            }
            else{
                val productlist_tmp = arrayListOf<Product>()
                for(e in productList){
                   Log.d(TAG,""+e.toString())
                    var product: Product = Product("","","", arrayListOf(),"")

                    val lowestPrice= e
                        .select(".prod_pricelist")
                        .select(".rank_one")
                        .select(".price_sect")
                        .select("a")
                        .select("strong")

                    //val name
                    val imgSrc=e.select(".thumb_image")
                        .select("a")
                        .select("img")
                        .attr("src")

                    product.lowestPrice=lowestPrice.text().toString()
                    product.img= if(imgSrc.length>0) {
                        "https:"+imgSrc.toString()}
                    else{
                        ""
                    }

                    Log.d(TAG,"findqwdqwdqwd"+product.img)
                    productlist_tmp.add(product)

                  //  Log.d(TAG,imgSrc.toString())

                }
                return productlist_tmp


            }
        } catch (e: IOException) {
            e.printStackTrace()
            return ArrayList<Product>()
        }
    }



}