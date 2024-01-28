package com.hazelnuts.wim.controllers;

import com.hazelnuts.wim.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.hazelnuts.wim.models.UserProfile;
import software.amazon.awssdk.core.SdkBytes;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;

import com.hazelnuts.wim.services.EncryptDataKey;

@Controller
@RequestMapping(path="/v1")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    private final EncryptDataKey dataKey;

    public UserController(UserRepository userRepository){
        this.dataKey = new EncryptDataKey();
    }

    @PostMapping(path="/user-profile", consumes = "application/json", produces = "application/json")
    public @ResponseBody String addNewUser (@RequestBody Map<String, Object> request) throws ParseException {
        UserProfile userProfile = new UserProfile();
        userProfile.setName(request.get("name").toString());
        userProfile.setEmail(request.get("email").toString());
        userProfile.setPhoneNumber(request.get("phone_number").toString());
        userProfile.setIdNo(request.get("id_no").toString());
        byte[] dk = this.dataKey.encryptData(request.get("ssn").toString());
//        userProfile.setSsn(Base64.getEncoder().encodeToString(dk));
        userProfile.setSsn(dk);
        Date dob = new SimpleDateFormat("yyyy-mm-dd").parse(request.get("dob").toString());
        userProfile.setDob(dob);
        userRepository.save(userProfile);
        return "Saved";
    }

    @GetMapping(path="/user-profile/{id}", produces = "application/json")
    public @ResponseBody Map<String, String> getUser(@PathVariable long id){
        Optional<UserProfile> userProfile = userRepository.findById(id);

        HashMap<String, String> response = new HashMap<>();
        response.put("name", userProfile.get().getName());
        response.put("email", userProfile.get().getEmail());
        response.put("phone_number", userProfile.get().getPhoneNumber());
        response.put("id_no", userProfile.get().getIdNo());
        EncryptDataKey dataKey = new EncryptDataKey();
//        byte[] ssn = Base64.getDecoder().decode(userProfile.get().getSsn());
//        SdkBytes ssnPlainText =  dataKey.decryptData(SdkBytes.fromByteArray(ssn));
//        System.out.println(Arrays.toString(userProfile.get().getSsn()));
        SdkBytes ssnPlainText =  this.dataKey.decryptData(SdkBytes.fromByteArray(userProfile.get().getSsn()));
        response.put("ssn", new String(ssnPlainText.asByteArray()));
        response.put("dob", userProfile.get().getDob().toString());
        return response;
    }

//
//    @PutMapping(path="/user-profile/{id}", produces = "application/json")
//    public @ResponseBody Map<String, String> updateUser(@PathVariable long id, @RequestBody Map<String, Object> request){
//        Optional<UserProfile> userProfile = userRepository.findById(id);
//
//        HashMap<String, String> response = new HashMap<>();
//        response.put("name", userProfile.get().getName());
//        response.put("email", userProfile.get().getEmail());
//        response.put("phone_number", userProfile.get().getPhoneNumber());
//        response.put("id_no", userProfile.get().getIdNo());
//        EncryptDataKey dataKey = new EncryptDataKey();
////        byte[] ssn = Base64.getDecoder().decode(userProfile.get().getSsn());
////        SdkBytes ssnPlainText =  dataKey.decryptData(SdkBytes.fromByteArray(ssn));
////        System.out.println(Arrays.toString(userProfile.get().getSsn()));
//        SdkBytes ssnPlainText =  this.dataKey.decryptData(SdkBytes.fromByteArray(userProfile.get().getSsn()));
//        response.put("ssn", new String(ssnPlainText.asByteArray()));
//        response.put("dob", userProfile.get().getDob().toString());
//        return response;
//    }

}
