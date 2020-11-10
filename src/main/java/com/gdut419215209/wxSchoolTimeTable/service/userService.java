package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.demo.pojo.course;
import com.example.demo.pojo.originalCourse;
import com.example.demo.pool.webClientPoolFactory;
import com.example.demo.util.dateUtil;
import com.example.demo.util.distinguishUtil;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Service
public class userService {



    public userService(){

    }

    public List<List<originalCourse>> getOriginalCourseList(String userName, String passWord){
        String loginUrl="http://authserver.gdut.edu.cn/authserver/login?service=http%3A%2F%2Fjxfw.gdut.edu.cn%2Fnew%2FssoLogin"; //登录页面的网址
        List<originalCourse> originalCourse=null;
        List<List<originalCourse>> originalCourseList=new ArrayList<>();
        String param;
        boolean flag=true;
        try {
            WebClient webClient = webClientPoolFactory.getWebClientPool().getWebClient();
            while (flag) {
                HtmlPage page = webClient.getPage(loginUrl);//打开登录页面
                //获取账号输入框结点，然后点击账号输入框，最后输入
                HtmlTextInput inputUserName = (HtmlTextInput) page.getElementsByName("username").get(0);
                inputUserName.click();
                inputUserName.setText(userName);

                HtmlPasswordInput inputPasswd = (HtmlPasswordInput) page.getElementsByName("password").get(0);
                inputPasswd.click();
                inputPasswd.setText(passWord);

                HtmlImage codeImage = (HtmlImage) page.getElementById("captchaImg");
                String fileName = UUID.randomUUID().toString().substring(0, 5) + ".jpg";
                File codeFile = new File("D:/img/" + fileName);
                codeImage.saveAs(codeFile);

                String result = distinguishUtil.distinguishImg(fileName);

                HtmlTextInput code = (HtmlTextInput) page.getElementsByName("captchaResponse").get(0);
                code.click();
                code.setText(result);

                List<HtmlElement> byXPath = page.getByXPath("//button[@class='auth_login_btn primary full_width']");
                HtmlPage nextPage = byXPath.get(0).click();//点击登录按钮，返回值为登陆成功后跳到的页面
                String title = nextPage.getTitleText();

                if (title.equals("广东工业大学统一身份认证")) {
                    HtmlSpan htmlSpan = nextPage.getHtmlElementById("msg");
                    if (htmlSpan.asText().equals("无效的验证码")) {
                        continue;
                    } else {
                        return null;
                    }
                }
                flag = false;
            }

            if(Integer.valueOf(dateUtil.getDateMonth())>8){
                param=dateUtil.getDateYear()+"01";
            }else{
                param=dateUtil.getDateYear()+"02";
            }

            for(int i=1;i<=16;i++){
                HtmlPage coursePage = webClient.getPage("https://jxfw.gdut.edu.cn/xsgrkbcx!getKbRq.action?xnxqdm="+param+"&zc="+i);
                String originalCourseJson = coursePage.asText();
                JSONArray jsonArray = JSON.parseArray(originalCourseJson);
                List<Object> originalObjects = Arrays.asList(jsonArray.get(0));
                originalCourse = JSON.parseArray(originalObjects.get(0).toString(), originalCourse.class);
                originalCourse.sort((o1,o2)->{
                    if(o1.getJcdm().equals("05")){
                        o1.setJcdm("0500");
                    }
                    if(o2.getJcdm().equals("05")){
                        o2.setJcdm("0500");
                    }
                    int j=o1.getXq().compareTo(o2.getXq());
                    int k=Integer.valueOf(o1.getJcdm()).compareTo(Integer.valueOf(o2.getJcdm()));
                    if(j==0){
                        return k;
                    }else {
                        return j;
                    }
                });
                originalCourseList.add(originalCourse);
            }
            webClientPoolFactory.getWebClientPool().returnWebClient(webClient);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TesseractException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return originalCourseList;
    }

    public List<List<course>> getResponseCourseList(List<List<originalCourse>> originalCourseList) {
        List<List<course>> responseCourseList = new ArrayList<>();
        for (List<originalCourse> originalCourses : originalCourseList) {
            List<course> list = new ArrayList<>();
            for (originalCourse originalCourse : originalCourses) {
                list.add(new course(originalCourse.getKcmc(),originalCourse.getTeaxms(),originalCourse.getJxbmc(),originalCourse.getJcdm(),originalCourse.getZc(),originalCourse.getXq(),originalCourse.getJxcdmc(),originalCourse.getSknrjj(),Math.abs(originalCourse.getKcmc().hashCode())%20));
            }
            responseCourseList.add(list);
        }
        return responseCourseList;
    }

}
