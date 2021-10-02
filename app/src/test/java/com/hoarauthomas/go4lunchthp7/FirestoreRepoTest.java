package com.hoarauthomas.go4lunchthp7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hoarauthomas.go4lunchthp7.api.RetrofitRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import retrofit2.Retrofit;
import retrofit2.mock.MockRetrofit;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */



@RunWith(MockitoJUnitRunner.class)
public class FirestoreRepoTest {

    private final Retrofit retrofit = new Retrofit.Builder().baseUrl("").build();


    @Mock
    RetrofitRequest myRetrofit = mock(RetrofitRequest.class);


    @Test
    public void retrofitWithNullThrows(){

    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void testNotificationAlarm(){
        //when
      //  when(myRetrofit)



    }




    //check repository

    //tester appel du repo vcers le webservice
    //tester le retour du resultat
    //verifier le cahce du repository pour éviter les appel supplémentaire







}