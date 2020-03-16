package com.equipu.profeplus.controllers;

import android.util.Log;

import com.equipu.profeplus.LearnApp;
import com.equipu.profeplus.gson_models.Login;
import com.equipu.profeplus.gson_models.RegUser;
import com.equipu.profeplus.gson_models.UserData;
import com.equipu.profeplus.models.AppStateParcel;
import com.equipu.profeplus.models.UserParcel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Herbert Caller on 6/27/2016.
 * Este controlador permite hacer peticiones por HTTP al servidor
 * para obtener datos de usuario en formato JSON
 */
public class UserController extends BaseController {

    AppStateParcel appStateParcel;
    UserParcel userParcel;

    public UserController(){
        //client = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).build();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public AppStateParcel getAppStateParcel() {
        return appStateParcel;
    }

    public void setAppStateParcel(AppStateParcel appStateParcel) {
        this.appStateParcel = appStateParcel;
    }

    public UserParcel getUserParcel() {
        return userParcel;
    }

    public void setUserParcel(UserParcel userParcel) {
        this.userParcel = userParcel;
    }

    public JSONObject getJSONdata(UserParcel userParcel) {
        JSONObject mUser = new JSONObject();
        try {
            mUser.put("first_name", userParcel.getFirstame());
            mUser.put("last_name", userParcel.getLastname());
            mUser.put("nationid", String.valueOf(userParcel.getDni()));
            mUser.put("birthdate", userParcel.getBirth());
            mUser.put("country", userParcel.getCountry());
            mUser.put("gender", userParcel.getGender());
            mUser.put("city", userParcel.getCity());
            mUser.put("phone", userParcel.getPhone());
            mUser.put("studentid", userParcel.getStudentId());
            mUser.put("teacher", userParcel.getTeacher());
            mUser.put("appmode", userParcel.getAppMode());
            mUser.put("email", userParcel.getMail());
            mUser.put("password", userParcel.getPassword());
            mUser.put("speciality", userParcel.getSpeciality());
            mUser.put("institution_name", userParcel.getInstitution());
            mUser.put("concurso_group", userParcel.getConcursoGroup());
            mUser.put("concurso_institute", userParcel.getConcursoInstitute());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mUser;
    }

    public JSONObject getJSONrecoverdata(UserParcel userParcel) {
        JSONObject mUser = new JSONObject();
        try {
            mUser.put("nationid", String.valueOf(userParcel.getDni()));
            mUser.put("birthdate", userParcel.getBirth());
            mUser.put("email", userParcel.getMail());
            mUser.put("yourpass", userParcel.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mUser;
    }

    public JSONObject getJSONusermode(AppStateParcel ap) {
        JSONObject mUser = new JSONObject();
        try {
            mUser.put("appmode", ap.getAppMode());
            mUser.put("teacher", ap.getUserType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mUser;
    }

    public JSONObject getJSONuser(UserParcel userParcel) {
        JSONObject mUser = new JSONObject();
        try {
            mUser.put("first_name", userParcel.getFirstame());
            mUser.put("last_name", userParcel.getLastname());
            mUser.put("nationid", String.valueOf(userParcel.getDni()));
            mUser.put("birthdate", userParcel.getBirth());
            mUser.put("country", userParcel.getCountry());
            mUser.put("gender", userParcel.getGender());
            mUser.put("city", userParcel.getCity());
            mUser.put("phone", userParcel.getPhone());
            mUser.put("studentid", userParcel.getStudentId());
            mUser.put("teacher", userParcel.getTeacher());
            mUser.put("appmode", userParcel.getAppMode());
            mUser.put("speciality", userParcel.getSpeciality());
            mUser.put("institution_name", userParcel.getInstitution());
            mUser.put("concurso_group", userParcel.getConcursoGroup());
            mUser.put("concurso_institute", userParcel.getConcursoInstitute());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mUser;
    }


    public void loginUser(UserParcel up) {
        setStatus(FAILED);
        AppStateParcel appStateParcel = new AppStateParcel();
        //String url = LearnApp.baseURL + "/usertoken?email=" + up.getMail() + "&password=" + up.getPassword();
        String url = String.format("%s/user-auth?email=%s&password=%s",
                LearnApp.baseURL, up.getMail(), up.getPassword());
        Log.d("profeplus.url", url);
        try {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("Keep-Alive","timeout=5, max=50")
                    .build();
            response = client.newCall(request).execute();
            inputStream = response.body().byteStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line = "";
            while((line = bufferedReader.readLine()) != null) {
                jsonString.append(line);
            }
            socketOn = true;
            if (response.code() == HttpURLConnection.HTTP_OK) {
                setStatus(DONE);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                Login login = gson.fromJson(jsonString.toString(), Login.class);
                appStateParcel.setToken(login.getToken());
                appStateParcel.setUserId(String.valueOf(login.getUser_id()));
                appStateParcel.setAppMode(login.getAppmode());
                appStateParcel.setUserType(login.getTeacher());
                appStateParcel.setCountry(login.getCountry());
                appStateParcel.setUsername(login.getName());
                appStateParcel.setDocumentId(login.getDocument_id());
                appStateParcel.setEvaluations(login.getEvaluations());
                appStateParcel.setGender(login.getGender());
                setAppStateParcel(appStateParcel);
            } else {
                setStatus(FAILED);
            }
        } catch (SocketException e) {
            socketOn = false;
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            setStatus(FAILED);
        }
    }

    public void guestSchoolLogin(UserParcel up) {
        setStatus(FAILED);
        AppStateParcel appStateParcel = new AppStateParcel();
        //String url = LearnApp.baseURL + "/usertoken?email=" + up.getMail() + "&password=" + up.getPassword();
        String url = String.format("%s/guest-login?email=%s&password=%s",
                LearnApp.baseURL, up.getMail(), up.getPassword());
        Log.d("profeplus.url", url);
        try {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("Keep-Alive","timeout=5, max=50")
                    .build();
            response = client.newCall(request).execute();
            inputStream = response.body().byteStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line = "";
            while((line = bufferedReader.readLine()) != null) {
                jsonString.append(line);
            }
            socketOn = true;
            if (response.code() == HttpURLConnection.HTTP_OK) {
                setStatus(DONE);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                Login login = gson.fromJson(jsonString.toString(), Login.class);
                appStateParcel.setToken(login.getToken());
                appStateParcel.setUserId(String.valueOf(login.getUser_id()));
                appStateParcel.setAppMode(login.getAppmode());
                appStateParcel.setUserType(login.getTeacher());
                appStateParcel.setCountry(login.getCountry());
                appStateParcel.setUsername(login.getName());
                appStateParcel.setDocumentId(login.getDocument_id());
                appStateParcel.setEvaluations(login.getEvaluations());
                appStateParcel.setGender(login.getGender());
                setAppStateParcel(appStateParcel);
            } else {
                setStatus(FAILED);
            }
        } catch (SocketException e) {
            socketOn = false;
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            setStatus(FAILED);
        }
    }

    public void RegisterUser(UserParcel userParcel, AppStateParcel appStateParcel) {
        setStatus(FAILED);
        setUserParcel(userParcel);
        setAppStateParcel(appStateParcel);
        final String url = String.format("%s/user/create",LearnApp.baseURL);
        Log.d("profeplus.url", url);
        try {
            JSONObject mData = getJSONdata(userParcel);
            Log.d("profeplus.json", mData.toString());
            body = RequestBody.create(JSON, mData.toString());
            request = new Request.Builder()
                    .url(url)
                    .addHeader("Keep-Alive","timeout=5, max=50")
                    .post(body)
                    .build();
            response = client.newCall(request).execute();
            inputStream = response.body().byteStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line = "";
            while((line = bufferedReader.readLine()) != null) {
                jsonString.append(line);
            }
            if (response.code() == HttpURLConnection.HTTP_CREATED) {
                setStatus(DONE);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                RegUser login = gson.fromJson(jsonString.toString(), RegUser.class);
                appStateParcel.setToken(login.getToken());
                appStateParcel.setUserId(String.valueOf(login.getUser_id()));
                appStateParcel.setAppMode(login.getAppmode());
                appStateParcel.setUserType(login.getTeacher());
                appStateParcel.setCountry(login.getCountry());
                appStateParcel.setUsername(login.getName());
                appStateParcel.setDocumentId(login.getDocument_id());
                setAppStateParcel(appStateParcel);
            } else {
                setStatus(FAILED);
            }
        } catch (IOException e) {
            e.printStackTrace();
            setStatus(FAILED);
        }
    }


    public void changePassword(UserParcel up) {
        setStatus(FAILED);
        final String url = String.format("%s/user/recover-pass",LearnApp.baseURL);
        Log.d("profeplus.url", url);
        try {
            JSONObject mData = getJSONrecoverdata(up);
            Log.d("profeplus.json", mData.toString());
            body = RequestBody.create(JSON, mData.toString());
            request = new Request.Builder()
                    .url(url)
                    .addHeader("Keep-Alive","timeout=5, max=50")
                    .post(body)
                    .build();
            response = client.newCall(request).execute();
            response.body().string();
            if (response.code() == HttpURLConnection.HTTP_CREATED) {
                setStatus(DONE);
            } else {
                setStatus(FAILED);
                Log.d("profeplus.response", response.message());
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    public void GetUserData(String userId){
        setStatus(FAILED);
        UserParcel userParcel = new UserParcel();
        final String url = String.format("%s/user/details/%s", LearnApp.baseURL, userId);
        Log.d("profeplus.url", url);
        try {
            JSONObject jsonObject = new JSONObject();
            body = RequestBody.create(JSON, jsonObject.toString());
            request = new Request.Builder()
                    .url(url)
                    .addHeader("Keep-Alive","timeout=5, max=50")
                    .post(body)
                    .build();
            response = client.newCall(request).execute();
            inputStream = response.body().byteStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line = "";
            while((line = bufferedReader.readLine()) != null) {
                jsonString.append(line);
            }
            if (response.code() == HttpURLConnection.HTTP_OK) {
                setStatus(DONE);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                UserData userData = gson.fromJson(jsonString.toString(), UserData.class);
                userParcel.setUserId(userData.getId());
                userParcel.setLastname(userData.getLast_name());
                userParcel.setFirstame(userData.getFirst_name());
                userParcel.setMail(userData.getEmail());
                userParcel.setDni(String.valueOf(userData.getNationid()));
                userParcel.setBirth(userData.getBirthdate());
                userParcel.setCountry(userData.getCountry());
                userParcel.setCity(userData.getCity());
                userParcel.setPhone(userData.getPhone());
                userParcel.setAppMode(userData.getAppmode());
                userParcel.setGender(userData.getGender());
                userParcel.setTeacher(userData.getTeacher());
                userParcel.setStudentId(userData.getStudentid());
                userParcel.setSpeciality(userData.getSpeciality());
                userParcel.setInstitution(userData.getInstitution());
                userParcel.setConcurso(userData.getConcurso());
                setUserParcel(userParcel);
            } else {
                setStatus(FAILED);
            }
        } catch (IOException e1) {
            setStatus(CONNECTION);
            e1.printStackTrace();
        }
    }


    public void updaterUser(AppStateParcel appStateParcel, UserParcel userParcel) {
        final String url = String.format("%s/user/update/%s%s%s", LearnApp.baseURL,
                appStateParcel.getUserId(), LearnApp.tokenURL, appStateParcel.getToken());
        Log.d("profeplus.url", url);
        setStatus(FAILED);
        try {
            JSONObject mData = getJSONuser(userParcel);
            Log.d("profeplus.json", mData.toString());
            body = RequestBody.create(JSON, mData.toString());
            request = new Request.Builder()
                    .url(url)
                    .addHeader("Keep-Alive","timeout=5, max=50")
                    .put(body)
                    .build();
            response = client.newCall(request).execute();
            response.body().string();
            socketOn = true;
            if (response.code() != HttpURLConnection.HTTP_OK) {
                Log.d("profeplus.response", response.message());
                Log.d("profeplus.json", mData.toString());
            } else {
                setStatus(DONE);
            }
        } catch (SocketException e){
            socketOn = false;
            e.printStackTrace();
        } catch (IOException e1) {
            setStatus(CONNECTION);
            e1.printStackTrace();
        }
    }


    public void reloadProfile(AppStateParcel appStateParcel) {
        final String url = String.format("%s/user/mode/%s/%d/%d",LearnApp.baseURL,
                appStateParcel.getUserId(), appStateParcel.getAppMode(),
                appStateParcel.getUserType());
        Log.d("profeplus.url", url);
        setStatus(FAILED);
        try {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("Keep-Alive","timeout=5, max=50")
                    .build();
            response = client.newCall(request).execute();
            response.body().string();
            socketOn = true;
            if (response.code() == HttpURLConnection.HTTP_OK) {
                Log.d("profeplus.msg", response.message());
                setStatus(DONE);
            }
        } catch (SocketException e1) {
            socketOn = false;
            e1.printStackTrace();
        } catch (IOException e1) {
            setStatus(CONNECTION);
            e1.printStackTrace();
        }
    }

}
