package com.bed.android.bedrock.model

import android.provider.DocumentsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
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
            Log.d(TAG,productList.toString())
            if (productList.isEmpty()) {
                Log.d(TAG, "null")
                return ArrayList<Product>()
            } else {
                val productlist_tmp = arrayListOf<Product>()
                for (e in productList) {
                    var product: Product = Product("", "", "", "", arrayListOf(), "", "")


                    val id = e.attr("id")

                    if (id == "") continue


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

        var shopList= arrayListOf<String>("쿠팡","11번가","옥션","G마켓","인터파크")
        var shopLinkList= arrayListOf<String>("http://www.coupang.com/vp/products")
        try {
            var doc = Jsoup.connect(product.product_link).get()

            var thumbnail=doc.select(".summary_left")
                .select(".photo_w")
                .select("a")
                .select("img")
                .attr("src")

            var productInfo = doc.select(".lowest_list").select(".high_list tr")

            if (productInfo.isEmpty()) {
                Log.d("detail", "null")
                return product
            } else {
                val product_price_list = arrayListOf<Store>()
                for (e in productInfo) {
                    if(e.className()=="product-pot") continue
                    var shopName=e.select(".logo_over").select("img").attr("alt").toString()
                    val imgSrc=e.select(".logo_over").select("img").attr("src")
                    val price=e.select(".price").select(".prc_t")
                    var link=e.select(".logo_over a").attr("href")
                    var shopDoc:Document
                    var productImg:String?=""

                    if(!shopList.contains(shopName))
                        continue

                    var productNumberStartIndex=link.toString().indexOf("link_pcode")+"link_pcode".length+1
                    var productNumber=if(shopName=="쿠팡"){link.substring(productNumberStartIndex,productNumberStartIndex+12)}
                    else{link.substring(productNumberStartIndex,productNumberStartIndex+10)}

                    when(shopName){

                        "G마켓"->{
                            link =
                                "http://item.gmarket.co.kr/DetailView/Item.asp?goodscode=" + productNumber
                            shopDoc = Jsoup.connect(link).get()
                            productImg=shopDoc.select(".thumb-gallery")
                                .select(".box__viewer-container")
                                .select(".viewer")
                                .select(".on a")
                                .select("img")
                                .attr("src")

                        }
                        "11번가"->{
                            link="https://www.11st.co.kr/products/"+productNumber
                            shopDoc = Jsoup.connect(link).get()
                            productImg=shopDoc.select(".l_product_side_view")
                                .select(".img_full")
                                .select("img")
                                .attr("src")



                        }

                        "옥션"->{
                            link="http://itempage3.auction.co.kr/DetailView.aspx?ItemNo="+productNumber
                            shopDoc = Jsoup.connect(link).get()
                            productImg=shopDoc.select(".thumb-gallery")
                                .select(".box__viewer-container")
                                .select(".viewer")
                                .select(".on a")
                                .select("img")
                                .attr("src")


                        }
                        "인터파크"->{
                            link="https://shopping.interpark.com/product/productInfo.do?prdNo="+productNumber
                            val lastfournumber=
                                        "/"+productNumber[productNumber.length-4]+
                                        "/"+productNumber[productNumber.length-3]+
                                        "/"+productNumber[productNumber.length-2]+
                                        "/"+productNumber[productNumber.length-1]+"/"

                            productImg="https://openimage.interpark.com/goods_image_big"+lastfournumber+productNumber+"_l.jpg"
                        }
                        else->{
                            link="www.google.com"

                        }
                    }
                    Log.d("ProductNum",productNumber)

                    product_price_list.add(Store("https:"+imgSrc,price.text(),link,"https:"+productImg))

                }
                product.img="https:"+thumbnail.toString()
                product.priceList=product_price_list

            }
        } catch (e: IOException) {
            e.printStackTrace()
            return product
        }

        return product

    }

    fun croll_keywords():List<String>{
        val keywordList = mutableListOf<String>()
        try {
            url = "http://search.danawa.com/dsearch.php?query=KONKUK"
            val doc = Jsoup.connect(url).get()

            val keywords = doc.select(".keyword_list")
                .select("ol")
                .select("li")
                .select("a")

            for ((idx, k) in keywords.withIndex()) {
                val new_keyword = (idx + 1).toString() + ". " + k.text()
                if (idx == 10) {
                    break
                }
                keywordList.add(new_keyword)
            }
            Log.d(TAG, keywordList.toString())
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return keywordList.toList()
    }


}



