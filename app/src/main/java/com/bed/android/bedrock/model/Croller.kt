package com.bed.android.bedrock.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jsoup.Jsoup
import java.io.IOException

private const val TAG="Croller"


class Croller() {

    private var url: String? = null


    fun croll_list(keyword: String): ArrayList<Product> {
        val url_front = "http://search.danawa.com/dsearch.php?query="
        val url_back = "&tab=main"

        try {
            url = keyword
            val url_basic = url_front + url + url_back
            var doc = Jsoup.connect(url_basic).get()
            var productList = doc.select(".product_list").select(".prod_item")

            if (productList.isEmpty()) {
                Log.d(TAG, "null")
                return ArrayList<Product>()
            } else {
                val productlist_tmp = arrayListOf<Product>()
                for (e in productList) {
                    // Log.d(TAG,""+e.toString())
                    var product: Product = Product("", "", "", "", arrayListOf(), "", "")


                    val id = e.attr("id")

                    if (id == "") continue


                    //Log.d(TAG,id.toString())
                    val lowestPrice = e.select(".prod_pricelist")
                        .select(".rank_one")
                        .select(".price_sect")
                        .select("a")
                        .select("strong")

                    val productName = e.select(".prod_info")
                        .select(".prod_name a")

                    var imgSrc = e.select(".thumb_image")
                        .select("a")
                        .select("img")
                        .attr("src")


                    if (imgSrc.length == 0) {
                        imgSrc = e.select(".thumb_image")
                            .select("a").first()
                            .select("img")
                            .attr("data-original")

                        Log.d(TAG, "cc")
                    }

                    val desc = e.select(".prod_info")
                        .select(".prod_spec_set")
                        .select(".spec_list")

                    val productLink = e.select(".prod_info")
                        .select(".prod_name")
                        .select("a")
                        .attr("href")


                    product.lowestPrice = lowestPrice.text().toString()
                    product.img = if (imgSrc.length > 0) {
                        "https:" + imgSrc.toString()
                    } else {
                        ""
                    }
                    product.name = productName.text().toString()
                    product.id = id.toString()
                    product.des = desc.text().toString()
                    product.product_link = productLink.toString()

                    Log.d(TAG, "product-des" + productLink.toString())


                    productlist_tmp.add(product)


                }
                return productlist_tmp


            }
        } catch (e: IOException) {
            e.printStackTrace()
            return ArrayList<Product>()
        }
    }

    fun croll_detail(product: Product): Product {


        try {
            Log.d("detail",product.product_link)
            var doc = Jsoup.connect(product.product_link).get()


            var productInfo = doc.select(".lowest_list").select(".high_list")

            if (productInfo.isEmpty()) {
                Log.d("detail", "null")
                return product
            } else {
                val product_price_list = arrayListOf<Pair<String, String>>()
                for (e in productInfo) {
                    val name=e.select(".logo_over").select("img").attr("src")
                    val price=e.select(".price").select(".prc_t")
                    product_price_list.add(Pair("https:"+name,price.text()))
                }
                product.priceList=product_price_list
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return product
        }

        return product

    }


}



