package com.luma.core.helpers;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.Getter;

import static io.restassured.RestAssured.given;

public class EmailFactory {
    @Getter
    private String email;
    private String sessionId;

    public EmailFactory() {
        Response response = RestAssured.get("https://api.guerrillamail.com/ajax.php?f=get_email_address");
        this.email = response.path("email_addr");
        this.sessionId = response.getCookie("PHPSESSID");
    }
//    public void setResetPasswordCredentials() {
//        Response response;
//        Map<String, String> map = null;
//
//        long startTime = System.currentTimeMillis();
//        long timeout = 25000;
//        boolean success = false;
//
//        while(!success && System.currentTimeMillis() - startTime < timeout) {
//            try {
//                response = given()
//                        .when()
//                        .cookie("PHPSESSID", sessionId)
//                        .get("https://api.guerrillamail.com/ajax.php?f=check_email&seq=1");
//
//                map = (Map<String, String>) response.body().jsonPath().getList("list").get(0);
//                if(!map.isEmpty()) {
//                    success = true;
//                }
//            } catch (Exception e) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ex) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//        }
//
//        Response emailContentResponse = given()
//                .cookie("PHPSESSID", sessionId)
//                .when()
//                .get("https://api.guerrillamail.com/ajax.php?f=fetch_email&email_id=" + map.get("mail_id"));
//
//        resetPasswordToken = emailContentResponse.body().jsonPath().get("mail_body").toString()
//                .split("password/")[2].split("<")[0];
//
//        resetPasswordLink = emailContentResponse.body().jsonPath().get("mail_body").toString()
//                .split("link:")[1].split("href=\"")[1].split("\"")[0];
//    }
}
