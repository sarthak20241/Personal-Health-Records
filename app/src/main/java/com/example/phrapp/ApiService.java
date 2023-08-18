package com.example.phrapp;
//
import com.example.phrapp.sampledata.Request_Model;
import com.example.phrapp.sampledata.Request_Model_Get_Otp;
import com.example.phrapp.sampledata.Request_Model_Login;
import com.example.phrapp.sampledata.Response_Model;
import com.example.phrapp.sampledata.Response_Model_Get_Otp;
import com.example.phrapp.sampledata.Response_Model_Login;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    public static  final String url = "http://192.168.0.169:3000";
    @POST("/login/aadhaar/authinit")
    Call<Response_Model_Get_Otp> abha_Login_with_aadhaar_gen_otp_createPost(@Body Request_Model_Get_Otp requestModel);
    @POST("/login/aadhaar/confirm-with-aadhaar-otp")
    Call<Response_Model_Login> abha_Login_with_aadhaar_verify_otp_createPost(@Body Request_Model_Login requestModel);
    @POST("/login/mobile/authinit")
    Call<Response_Model_Get_Otp> abha_Login_with_mobile_gen_otp_createPost(@Body Request_Model_Get_Otp requestModel);
    @POST("/login/mobile/confirm-with-mobile-otp")
    Call<Response_Model_Login> abha_Login_with_mobile_verify_otp_createPost(@Body Request_Model_Login requestModel);
    @POST("/registration/aadhaar/generateotp")
    Call<Response_Model> gen_otp_createPost(@Body Request_Model requestModel);
    @POST("/registration/aadhaar/verifyotp")
    Call<Response_Model> verify_otp_createPost(@Body Request_Model requestModel);
    @POST("/registration/aadhaar/mobileVerify/generateOtp")
    Call<Response_Model> abha_registration_via_aadhaar_verify_mobile_gen_mobile_otp_createPost(@Body Request_Model requestModel);
    @POST("/registration/aadhaar/mobileVerify/verifyOtp")
    Call<Response_Model> abha_registration_via_aadhaar_verify_mobile_verify_mobile_otp_createPost(@Body Request_Model requestModel);
    @POST("/registration/aadhaar/createHealthIdPreVerified")
    Call<Response_Model> abha_registration_via_aadhaar_create_health_id_with_pre_verified_createPost(@Body Request_Model requestModel);
    @POST("/forgotAbha/aadhaar/generateOtp")
    Call<Response_Model> abha_forgot_abha_retrieve_via_aadhaar_get_otp_createPost(@Body Request_Model requestModel);
    @POST("/forgotAbha/aadhaar/verifyOtp")
    Call<Response_Model> abha_forgot_abha_retrieve_via_aadhaar_verify_otp_createPost(@Body Request_Model requestModel);
    @POST("/forgotAbha/mobile/generateOtp")
    Call<Response_Model> abha_forgot_abha_retrieve_via_mobile_get_otp_createPost(@Body Request_Model requestModel);
    @POST("/forgotAbha/mobile/verifyOtp")
    Call<Response_Model> abha_forgot_abha_retrieve_via_mobile_verify_otp_createPost(@Body Request_Model requestModel);
    @POST("/forgotAbha/mobile/verifyOtp_and_demo")
    Call<Response_Model> abha_forgot_abha_retrieve_via_mobile_verify_otp_and_demo_createPost(@Body Request_Model requestModel);
    @POST("/getProfile")
    Call<Response_Model> abha_get_profile(@Body Request_Model requestModel);
    @POST("/get_abha_id_card")
    Call<ResponseBody> abha_get_abha_id_card(@Body Request_Model requestModel);
    @POST("/deleteAbha/aadhaar/generateOtp")
    Call<Response_Model> abha_delete_via_aadhaar_get_otp_createPost(@Body Request_Model requestModel);
    @POST("/deleteAbha/aadhaar/verifyOtp")
    Call<Response_Model> abha_delete_via_aadhaar_verify_otp_and_delete_createPost(@Body Request_Model requestModel);
    @POST("/deleteAbha/mobile/generateOtp")
    Call<Response_Model> abha_delete_via_mobile_get_otp_createPost(@Body Request_Model requestModel);
    @POST("/deleteAbha/mobile/verifyOtp")
    Call<Response_Model> abha_delete_via_mobile_verify_otp_and_delete_createPost(@Body Request_Model requestModel);
    @POST("/update/mobile/newMobile/generateOtp")
    Call<Response_Model> abha_update_mobile_new_mobile_no_get_otp_createPost(@Body Request_Model requestModel);
    @POST("/update/mobile/newMobile/verifyOtp")
    Call<Response_Model> abha_update_mobile_new_mobile_no_verify_otp_createPost(@Body Request_Model requestModel);
    @POST("/update/mobile/mobile/generateOtp")
    Call<Response_Model> abha_update_mobile_get_mobile_otp_createPost(@Body Request_Model requestModel);
    @POST("/update/mobile/aadhaar/generateOtp")
    Call<Response_Model> abha_update_mobile_get_aadhaar_otp_createPost(@Body Request_Model requestModel);
    @POST("/update/mobile/aadhaarormobile/verifyOtp")
    Call<Response_Model> abha_update_mobile_aadhaar_or_mobile_verify_otp_createPost(@Body Request_Model requestModel);
    @POST("/changeEmail/generateOtp")
    Call<Response_Model> abha_update_email_get_otp_createPost(@Body Request_Model requestModel);
    @POST("/changeEmail/verifyOtp")
    Call<Response_Model> abha_update_email_verify_otp_createPost(@Body Request_Model requestModel);
    @POST("/refresh_token")
    Call<Response_Model> refresh_X_Token(@Body Request_Model requestModel);


}


