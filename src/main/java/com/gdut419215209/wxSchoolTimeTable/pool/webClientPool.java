package com.example.demo.pool;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class webClientPool {

    private BlockingQueue<WebClient> webClientObjectQueue;
    private volatile int generatedNumber=0;

    private webClientPool(){
        webClientObjectQueue = new ArrayBlockingQueue(10);

    }

    public WebClient getWebClient() throws InterruptedException {
        if(webClientObjectQueue.size()==0){
            if(generatedNumber<10){
                synchronized (this){
                    if(generatedNumber<10){
                        generatedNumber++;
                        WebClient webClient = new WebClient(BrowserVersion.CHROME);
                        initWebClient(webClient);
                        return webClient;
                    }
                }
            }
        }
        return webClientObjectQueue.take();
    }

    public void returnWebClient(WebClient webClient) throws InterruptedException {
        webClient.getCookieManager().clearCookies();
        webClientObjectQueue.put(webClient);
    }

    private void initWebClient(WebClient webClient){
        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setUseInsecureSSL(true);//开启SSL认证
        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS。有些网站要开启！
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
        webClient.getOptions().setTimeout(30000);
        webClient.setJavaScriptTimeout(100000);
    }

}
